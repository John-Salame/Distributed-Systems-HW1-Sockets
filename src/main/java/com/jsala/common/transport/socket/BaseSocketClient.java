/**
 * Class BaseSocketClient
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Base class for socket clients providing lots of abstraction
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package com.jsala.common.transport.socket;
import java.net.*;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

public class BaseSocketClient {
	private short apiVer;
	private int api;
	private int errorApi;
	private PacketPrefix packetPrefix; // use this to add important metadata to messages over the socket
	private Socket socket = null;
	private String serverIp;
	private int serverPort;
	private DataOutputStream out; // use this to write to the socket
	private DataInputStream in; // use this to read from the socket

	// CONSTRUCTORS
	public BaseSocketClient() {
		this.apiVer = 1;
		this.api = APIEnumV1.ERROR.ordinal();
		this.errorApi = APIEnumV1.ERROR.ordinal();
	}
	public BaseSocketClient(String serverIp, int serverPort, short apiVer, int api) /*throws SocketException*/ {
		this.apiVer = apiVer;
		this.api = api;
		this.errorApi = APIEnumV1.ERROR.ordinal();
		this.packetPrefix = new PacketPrefix(this.apiVer, this.api);
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		// If I do not call setup() here, I can lazily start sockets upon the first send()
		// this.setup(serverIp, serverPort); // start the socket connection
	}
	
	protected void cleanup() {
		try {
			this.in.close();
			this.out.close();
			this.socket.close();
		} catch (IOException i) {
			System.out.println(i);
		}
		this.socket = null;
	}
	public void setup(String serverIp, int serverPort) throws SocketException {
		if(this.socket != null) {
			return;
		}
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		// try to connect
		boolean setupComplete = false;
		int numFailures = 0;
		System.out.println("Attempting to connect to host " + serverIp + ", port " + serverPort);
		while(!setupComplete && numFailures < 5) {
			try {
				this.socket = new Socket(serverIp, serverPort);
				System.out.println("Connected to host " + serverIp + ", port " + serverPort);
				this.in = new DataInputStream(socket.getInputStream());
				this.out = new DataOutputStream(socket.getOutputStream());
				setupComplete = true;
				return;
			}
			catch (UnknownHostException u) {
				System.out.println(u);
			}
			catch (IOException i) {
				System.out.println(i);
			}
			numFailures++;
			// https://stackoverflow.com/questions/24104313/how-do-i-make-a-delay-in-java
			// sleep for a second in between connection attempts
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println(e);
				cleanup();
				return;
			}
		}
		throw new SocketException("Client failed to connect.");
	}

	// based on the funcId (which is an ordinal in ErrorEnum) and the exception message, throw an exception
	private void throwExceptionFromNetwork(int funcId, byte[] msg) throws IOException, IllegalArgumentException, NoSuchElementException, UnsupportedOperationException {
		ErrorEnum[] errKeys = ErrorEnum.values();
		// extract the message from msg as a String
		String message;
		ByteArrayInputStream buf = new ByteArrayInputStream(msg);
		DataInputStream reader = new DataInputStream(buf);
		message = reader.readUTF();
		reader.close();
		buf.close();
		ErrorEnum errorType;
		try {
			errorType = errKeys[funcId];
		} catch (RuntimeException e) {
			throw new RuntimeException("Socket received unknown Exception type with message: " + message);
		}
		switch(errorType) {
			case IO_EXCEPTION:
				throw new IOException(message);
			case ILLEGAL_ARGUMENT_EXCEPTION:
				throw new IllegalArgumentException(message);
			case NO_SUCH_ELEMENT_EXCEPTION:
				throw new NoSuchElementException(message);
			case UNSUPPORTED_OPERATION_EXCEPTION:
				throw new UnsupportedOperationException(message);
			default:
				throw new RuntimeException("Socket received unknown Exception type with message: " + message);
		}
	}

	protected void send(byte[] b, int funcId) throws IOException {
		// currently it will do no retries as long as the socket connection is established
		try {
			// fault tolerance -- let the client make new requests again after logging out
			if(socket == null) {
				this.setup(this.serverIp, this.serverPort);
			}
			byte[] msg = packetPrefix.prependPrefix(b, funcId); // prepare the message
			// System.out.println("Sending " + new PacketPrefix((short) b.length, this.apiVer, this.api, funcId));
			this.out.write(msg); // send the message over the socket
		} catch (SocketException se) {
			// catch what is hopefully a "Connection reset""
			System.out.println("Error socket client send(): " + se);
			this.socket = null; // change it to null so we can retry the connection later
			throw new IOException(se.getMessage());
		} catch (IOException i) {
			System.out.println("Error in socket client send(): " + i);
			throw i;
		}
	}

	// send the message and return the response
	// this method is called by subclasses
	protected byte[] sendAndReceive(byte[] msg, int funcId) throws IOException, IllegalArgumentException, NoSuchElementException, UnsupportedOperationException {
		byte[] response = null;
		// Try to send to peer and then receive from peer. If either operation fails, throw the SocketException as an IOException
		// First, try to send. If we notice a "connection reset" due to the other end failing, try to connect again in case it's back up
		//   This also has the effect of trying to send twice if we experience an IOException.
		try {
			this.send(msg, funcId);
		} catch (IOException i) {
			// catch an IOException or a SocketException since it's a subclass
			this.send(msg, funcId);
		}
		try {
			// wait for response and parse response
			SocketMessage inMsg = SocketMessage.readAndSplit(this.in);
			PacketPrefix prefix = inMsg.getPrefix();
			response = inMsg.getMsg();
			// if the message is marked as an exception, throw the exception
			if(prefix.getApi() == this.errorApi) {
				this.throwExceptionFromNetwork(prefix.getFuncId(), response);
			}
		} catch (SocketException se) {
			// catch what is hopefully a "Connection reset""
			System.out.println("Error socket client sendAndReceive(): " + se);
			this.socket = null; // change it to null so we can retry the connection later
			throw new IOException(se.getMessage());
		}
		return response; // if no error occurred or no exception was transmitted over the socket, return the expected result
	}
}

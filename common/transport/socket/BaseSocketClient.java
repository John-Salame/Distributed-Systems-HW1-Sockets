/**
 * Class BaseSocketClient
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Base class for socket clients providing lots of abstraction
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package common.transport.socket;
import common.transport.socket.APIEnumV1;
import common.transport.socket.PacketPrefix;
import common.transport.socket.SocketMessage;
import java.net.*;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BaseSocketClient {
	private short apiVer;
	private int api;
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
	}
	public BaseSocketClient(String serverIp, int serverPort, short apiVer, int api) {
		this.apiVer = apiVer;
		this.api = api;
		this.packetPrefix = new PacketPrefix(this.apiVer, this.api);
		this.setup(serverIp, serverPort);
	}
	
	protected byte[] send(byte[] b, int funcId) {
		// fault tolerance -- let the client make new requests again after logging out
		if(socket == null) {
			this.setup(this.serverIp, this.serverPort);
		}
		// currently no resiliency for sending message once socket connection has been created
		try {
			byte[] msg = packetPrefix.prependPrefix(b, funcId); // prepare the message
			System.out.println("Sending " + new PacketPrefix((short) b.length, this.apiVer, this.api, funcId));
			this.out.write(msg); // send the message over the socket
		} catch (IOException i) {
			System.out.println(i);
		}
		// wait for a response
		return new byte[0];
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
	public void setup(String serverIp, int serverPort) {
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
		throw new RuntimeException("Client failed to connect.");
	}
	// send the message and return the response
	protected byte[] sendAndReceive(byte[] msg, int funcId) throws IOException {
		this.send(msg, funcId);
		// wait for response and parse response
		SocketMessage inMsg = SocketMessage.readAndSplit(this.in);
		return inMsg.getMsg();
	}
}

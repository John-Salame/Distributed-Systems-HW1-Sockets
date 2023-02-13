/**
 * Class BaseSocketServerThread
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Base class for server-style sockets which accept requests and send responses
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package common.transport.socket;
import common.transport.socket.PacketPrefix;
import common.transport.socket.SocketMessage;
import java.net.*;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.EOFException; // happens when writing to a closed socket

public class BaseSocketServerThread implements Runnable {
	private Socket socket = null;
	private boolean stop = false; // set to true upon logout to stop the loop of reading and responding to messages
	private DataOutputStream out; // use this to write to the socket
	private DataInputStream in; // use this to read from the socket

	// CONSTRUCTORS
	// Use this Constructor for threads that have an active connection
	public BaseSocketServerThread(Socket socket) {
		this.socket = socket;
	}

	// called by logout() to make the socket stop listening and close itself in run()
	public void stopServer() {
		this.stop = true;
	}

	// Function that will run on a thread (one thread per accepted connection)
	// more info on threads: https://stackoverflow.com/questions/877096/how-can-i-pass-a-parameter-to-a-java-thread
	public void run() {
		// assume the server is listening already
		try {
			this.in = new DataInputStream(this.socket.getInputStream());
			this.out = new DataOutputStream(this.socket.getOutputStream());
		}
		catch (IOException i) {
			System.out.println(i);
		}
		// repeatedly read and respond to messages until the client closes the connection
		// I have many functions that can result in IOException but I do not do any retries
		while(!this.stop) {
			try {
				SocketMessage inMsg = SocketMessage.readAndSplit(this.in);
				// now, pass the message to the functions that will figure out who the handler is
				if (inMsg != null) {
					byte[] buf = inMsg.getMsg();
					PacketPrefix prefix = inMsg.getPrefix();
					short apiVer = prefix.getApiVer();
					int api = prefix.getApi();
					int funcId = prefix.getFuncId();
					// call the demux function
					byte[] response = this.demux(apiVer, api, funcId, buf);
					this.sendResponse(apiVer, api, funcId, response);
				}
			}
			catch (EOFException e) {
				System.out.println("SellerSocketServerThreadV1 receive loop: " + e);
				this.stop = true;
			}
			// return immediately if the socket experiences a connectoin error such as "Connection reset"
			catch (SocketException s) {
				System.out.println(s);
				this.stop = true;
			}
			catch (IOException i) {
				System.out.println(i);
			}
		}
		this.cleanup(); // if we fail to read a message correctly, then clean up (close the connection) and end the thread
		System.out.println("Exiting thread");
	}

	// b is the response we want to send, which does not yet have the packet prefix
	private void sendResponse(short apiVer, int api, int funcId, byte[] b) {
		// currently no resiliency for sending message once socket connection has been created
		try {
			byte[] msg = new PacketPrefix(apiVer, api).prependPrefix(b, funcId); // prepare the message
			this.out.write(msg); // send the message over the socket
		} catch (EOFException e) {
			System.out.println("SellerSocketServerThreadV1 sendResponse(): " + e);
		} catch (IOException i) {
			System.out.println(i);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	private void cleanup() {
		System.out.println("Cleaning up");
		try {
			this.in.close();
			this.out.close();
			this.socket.close();
			this.socket = null;
		} catch (IOException i) {
			System.out.println("SellerSocketServerThreadV1 cleanup(): " + i);
		}
	}

	// use this method to send the message to the correct handler and then return the response as a byte array which we can send over the socket later
	protected byte[] demux(short apiVer, int api, int funcId, byte[] msg) throws IOException {
		switch (apiVer) {
			default:
				throw new RuntimeException("Err BaseSocketServerThreadV1: Base socket cannot run demux.");
		}
	}
}

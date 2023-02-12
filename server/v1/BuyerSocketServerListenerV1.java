/**
 * Class BuyerSocketServerListenerV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of buyer client-server IPC on server side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package server.v1;
import common.BuyerInterface;
import java.net.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BuyerSocketServerListenerV1 {
	private BuyerInterface buyerInterfaceV1;
	private ServerSocket server = null;

	// CONSTRUCTORS
	// starting the server - use this constructor and then call startServer()
	public BuyerSocketServerListenerV1(BuyerInterface buyerInterfaceV1) {
		this.buyerInterfaceV1 = buyerInterfaceV1;
	}

	// create the listener and enter the server loop
	public void startServer(int serverPort, int maxConnections) {
		if(this.server != null) {
			return;
		}
		try {
			this.server = new ServerSocket(serverPort, maxConnections);
			System.out.println("Buyer Socket Server listening on port " + serverPort + " with max connections " + maxConnections);
		} catch (IOException i) {
			System.out.println("Error binding server socket: " + i);
			return;
		}
		// server loop
		while(true) {
			try {
				Socket socket = server.accept();
				Thread t = new Thread((Runnable) new BuyerSocketServerThreadV1(this.buyerInterfaceV1, socket));
				t.start(); // run the run() function on a new thread
				// TO-DO: Figure out how to join threads and maybe do something with the thread interrupts
				// I could potentially check the socket.getRemoteSocketAddress() for each thread to check if it's ready to join
			} catch (IOException i) {
				System.out.println("Error server.accept(): " + i);
			}
		}
	}
}

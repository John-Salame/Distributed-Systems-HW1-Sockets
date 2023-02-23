/**
 * Class BuyerSocketServerListenerV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of buyer client-server IPC on server side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package com.jsala.server.v1.socket;
import com.jsala.common.interfaces.factory.UserInterfaceFactory;
import com.jsala.common.interfaces.BuyerInterface;
import java.net.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class BuyerSocketServerListenerV1 {
	private UserInterfaceFactory buyerInterfaceFactoryV1;
	private ServerSocket server = null;

	// CONSTRUCTORS
	// starting the server - use this constructor and then call startServer()
	public BuyerSocketServerListenerV1(UserInterfaceFactory buyerInterfaceFactoryV1) {
		this.buyerInterfaceFactoryV1 = buyerInterfaceFactoryV1;
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
				Socket socket = server.accept(); // the socket which was created upon receiving a TCP connection request
				System.out.println("Received connection from " + socket.getRemoteSocketAddress());
				Thread t = new Thread((Runnable) new BuyerSocketServerThreadV1(this.buyerInterfaceFactoryV1, socket)); // I would create a base class if I knew how to abstract this line away
				t.start(); // run the run() function on a new thread
				// TO-DO: Figure out how to join threads and maybe do something with the thread interrupts
				// I could potentially check the socket.getRemoteSocketAddress() for each thread to check if it's ready to join
				//    No, this doesn't work. It doesn't change after the socket closes.
			} catch (IOException i) {
				System.out.println("Error server.accept(): " + i);
			}
		}
	}
}

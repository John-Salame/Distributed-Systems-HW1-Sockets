/**
 * Class SellerSocketServerListenerV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of seller client-server IPC on server side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package server.v1.socket;
import common.interfaces.factory.UserInterfaceFactory;
import common.interfaces.SellerInterface;
import java.net.*;
import java.io.IOException;

public class SellerSocketServerListenerV1 {
	private UserInterfaceFactory sellerInterfaceFactoryV1;
	private ServerSocket server = null;

	// CONSTRUCTORS
	// starting the server - use this constructor and then call startServer()
	public SellerSocketServerListenerV1(UserInterfaceFactory sellerInterfaceFactoryV1) {
		this.sellerInterfaceFactoryV1 = sellerInterfaceFactoryV1;
	}

	// create the listener and enter the server loop
	public void startServer(int serverPort, int maxConnections) {
		if(this.server != null) {
			return;
		}
		try {
			this.server = new ServerSocket(serverPort, maxConnections);
			System.out.println("Seller Socket Server listening on port " + serverPort + " with max connections " + maxConnections);
		} catch (IOException i) {
			System.out.println("Error binding server socket: " + i);
			return;
		}
		// server loop
		while(true) {
			try {
				Socket socket = server.accept();
				System.out.println("Received connection from " + socket.getRemoteSocketAddress());
				Thread t = new Thread((Runnable) new SellerSocketServerThreadV1(this.sellerInterfaceFactoryV1, socket));
				t.start(); // run the run() function on a new thread
				// TO-DO: Figure out how to join threads and maybe do something with the thread interrupts
				// I could potentially check the socket.getRemoteSocketAddress() for each thread to check if it's ready to join
			} catch (IOException i) {
				System.out.println("Error server.accept(): " + i);
			}
		}
	}
}

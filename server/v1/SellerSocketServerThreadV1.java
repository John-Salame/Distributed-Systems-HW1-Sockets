/**
 * Class SellerSocketServerThreadV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of seller client-server IPC on server side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package server.v1;
import common.SellerInterface;
import common.transport.serialize.*;
import common.transport.socket.SellerEnumV1;
import common.transport.socket.PacketPrefix;
import common.transport.socket.SocketMessage;
import common.Item;
import java.net.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.EOFException; // happens when writing to a closed socket

public class SellerSocketServerThreadV1 implements SellerInterface, Runnable {
	private SellerInterface sellerInterfaceV1;
	private SellerEnumV1[] sellerEnumV1Values; // for translating function ID to enum value
	private Socket socket = null;
	private boolean stop; // set to true upon logout to stop the loop of reading and responding to messages
	private DataOutputStream out; // use this to write to the socket
	private DataInputStream in; // use this to read from the socket

	// CONSTRUCTORS
	// Use this Constructor for threads that have an active connection
	public SellerSocketServerThreadV1(SellerInterface sellerInterfaceV1, Socket socket) {
		this.sellerInterfaceV1 = sellerInterfaceV1;
		this.sellerEnumV1Values = SellerEnumV1.values();
		this.socket = socket;
		this.stop = false;
	}


	// New Methods

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
					short apiVer = inMsg.getPrefix().getApiVer();
					int funcId = inMsg.getPrefix().getFuncId();
					// call the demux function
					byte[] response = this.demux(apiVer, funcId, buf);
					this.sendResponse(response, funcId, apiVer);
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
	private void sendResponse(byte[] b, int funcId, short apiVer) {
		// currently no resiliency for sending message once socket connection has been created
		try {
			byte[] msg = new PacketPrefix(apiVer).prependPrefix(b, funcId); // prepare the message
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

	private byte[] demux(short apiVer, int funcId, byte[] msg) throws IOException {
		switch (apiVer) {
			case 1:
				return this.demuxV1(funcId, msg);
			default:
				throw new RuntimeException("Err SellerSocketServerThreadV1: Received message with invalid API version.");
		}
	}
	private byte[] demuxV1(int funcId, byte[] msg) throws IOException {
		// TO-DO: Error handling if funcId is an invalid index
		SellerEnumV1 functionName = this.sellerEnumV1Values[funcId];
		switch (functionName) {
			case CREATE_USER:
				return this.bytesCreateUser(msg);
			case LOGIN:
				return this.bytesLogin(msg);
			case LOGOUT:
				return this.bytesLogout(msg);
			case GET_SELLER_RATING:
				return this.bytesGetSellerRating(msg);
			case PUT_ON_SALE:
				return this.bytesPutOnSale(msg);
			default:
				throw new RuntimeException("Err SellerSocketServerThreadV1: Unsupported method triggered by enum.");
		}
	}

	// Seller interface methods and their new counterparts
	public int createUser(String username, String password) {
		return this.sellerInterfaceV1.createUser(username, password);
	}
	private byte[] bytesCreateUser(byte[] msg) throws IOException {
		SerializeLogin serLog = SerializeLogin.deserialize(msg);
		int userId = this.createUser(serLog.getUsername(), serLog.getPassword());
		return SerializeInt.serialize(userId);
	}

	public String login(String username, String password) {
		return this.sellerInterfaceV1.login(username, password);
	}
	private byte[] bytesLogin(byte[] msg) throws IOException {
		SerializeLogin serLog = SerializeLogin.deserialize(msg);
		String sessionToken = this.login(serLog.getUsername(), serLog.getPassword());
		return SerializeString.serialize(sessionToken);
	}
	public void logout(String sessionToken) {
		this.sellerInterfaceV1.logout(sessionToken);
		this.stop = true;
		System.out.println("Logging out");
	}
	private byte[] bytesLogout(byte[] msg) throws IOException {
		String sessionToken = SerializeString.deserialize(msg);
		this.logout(sessionToken);
		byte[] output = new byte[0];
		return output;
	}
	public int[] getSellerRating(int sellerId) {
		return this.sellerInterfaceV1.getSellerRating(sellerId);
	}
	private byte[] bytesGetSellerRating(byte[] msg) throws IOException {
		int sellerId = SerializeInt.deserialize(msg);
		int[] rating = this.getSellerRating(sellerId);
		return SerializeIntArray.serialize(rating);
	}
	public void putOnSale(String sessionToken, Item item, int quantity) {
		this.sellerInterfaceV1.putOnSale(sessionToken, item, quantity);
	}
	private byte[] bytesPutOnSale(byte[] msg) throws IOException {
		throw new RuntimeException("SellerSocketServerThreadV1: putOnSale() Not implemented");
	}
	/*
	public void changePriceOfItem(String sessionToken, ItemId itemId, float newPrice);
	public void removeItemFromSale(String sessionToken, ItemId itemId, int quantity);
	public String displayItemsOnSale(String sessionToken);
	*/
}

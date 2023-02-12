/**
 * Class BuyerSocketServerThreadV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of buyer client-server IPC on server side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package server.v1;
import common.BuyerInterface;
import common.transport.serialize.*;
import common.transport.socket.BuyerEnumV1;
import common.transport.socket.PacketPrefix;
import common.transport.socket.SocketMessage;
import common.Item;
import java.net.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BuyerSocketServerThreadV1 implements BuyerInterface, Runnable {
	private BuyerInterface buyerInterfaceV1;
	private BuyerEnumV1[] buyerEnumV1Values; // for translating function ID to enum value
	private Socket socket = null;
	private DataOutputStream out; // use this to write to the socket
	private DataInputStream in; // use this to read from the socket

	// CONSTRUCTORS
	// Use this Constructor for threads that have an active connection
	public BuyerSocketServerThreadV1(BuyerInterface buyerInterfaceV1, Socket socket) {
		this.buyerInterfaceV1 = buyerInterfaceV1;
		this.buyerEnumV1Values = BuyerEnumV1.values();
		this.socket = socket;
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
		/*
		// Pull out the message metadata (packet prefix)
		PacketPrefix prefix = null;
		try {
			prefix = PacketPrefix.getPrefixFromMessage(in);
			System.out.println(prefix);
		} catch (IOException i) {
			System.out.println(i);
			this.cleanup();
			return;
		}
		short msgSize = prefix.getMsgSize();
		short apiVer = prefix.getApiVer();
		int funcId = prefix.getFuncId();
		// read the actual message
		short bytesLeft = msgSize;
		byte[] buf = new byte[msgSize];
		boolean msgOk = true;
		while(bytesLeft > 0) {
			try {
				short bytesRead = (short) in.read(buf, 0, bytesLeft);
				bytesLeft -= bytesRead;
			} catch (IOException i) {
				System.out.println(i);
				msgOk = false;
			}
		}
		*/
		// I have many functions that can result in IOException but I do not do any retries
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
		catch (IOException i) {
			System.out.println(i);
		}
		this.cleanup(); // if we fail to read a message correctly, then clean up (close the connection) and end the thread
	}

	// b is the response we want to send, which does not yet have the packet prefix
	private void sendResponse(byte[] b, int funcId, short apiVer) {
		// currently no resiliency for sending message once socket connection has been created
		try {
			byte[] msg = new PacketPrefix(apiVer).prependPrefix(b, funcId); // prepare the message
			this.out.write(msg); // send the message over the socket
		} catch (IOException i) {
			System.out.println(i);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	private void cleanup() {
		try {
			this.in.close();
			this.out.close();
			this.socket.close();
		} catch (IOException i) {
			System.out.println(i);
		}
		this.socket = null;
	}

	private byte[] demux(short apiVer, int funcId, byte[] msg) throws IOException {
		switch (apiVer) {
			case 1:
				return this.demuxV1(funcId, msg);
			default:
				throw new RuntimeException("Err BuyerSocketServerThreadV1: Received message with invalid API version.");
		}
	}
	private byte[] demuxV1(int funcId, byte[] msg) throws IOException {
		// TO-DO: Error handling if funcId is an invalid index
		BuyerEnumV1 functionName = this.buyerEnumV1Values[funcId];
		switch (functionName) {
			case CREATE_USER:
				return this.bytesCreateUser(msg);
			case LOGIN:
				return this.bytesLogin(msg);
			default:
				throw new RuntimeException("Err BuyerSocketServerThreadV1: Unsupported method triggered by enum.");
		}
	}

	// Buyer interface methods and their new counterparts
	public int createUser(String username, String password) {
		return buyerInterfaceV1.createUser(username, password);
	}
	private byte[] bytesCreateUser(byte[] msg) throws IOException {
		SerializeLogin serLog = SerializeLogin.deserialize(msg);
		int userId = this.createUser(serLog.getUsername(), serLog.getPassword());
		return SerializeInt.serialize(userId);
	}

	public String login(String username, String password) {
		int funcId = BuyerEnumV1.LOGIN.ordinal();
		System.out.println(funcId);
		throw new RuntimeException("Method not implemented BuyerSocketServerV1: login()");
	}
	private byte[] bytesLogin(byte[] msg) throws IOException {
		SerializeLogin serLog = SerializeLogin.deserialize(msg);
		String sessionToken = this.login(serLog.getUsername(), serLog.getPassword());
		System.out.println(sessionToken);
		return SerializeString.serialize(sessionToken);
	}
	// TO-DO: Determine if I need to clean up here or if the socket will close fine on its own
	public void logout(String sessionToken) {
		int funcId = BuyerEnumV1.LOGOUT.ordinal();
		System.out.println(funcId);
		this.cleanup();
		throw new RuntimeException("Method not implemented BuyerSocketServerV1: logout()");
	}
	public int[] getSellerRating(int sellerId) {
		int funcId = BuyerEnumV1.GET_SELLER_RATING.ordinal();
		System.out.println(funcId);
		throw new RuntimeException("Method not implemented BuyerSocketServerV1: getSellerRating()");
	}
	public Item[] searchItem(String sessionToken, int category, String[] keywords) {
		int funcId = BuyerEnumV1.SEARCH_ITEM.ordinal();
		System.out.println(funcId);
		throw new RuntimeException("Method not implemented BuyerSocketServerV1: searchItem()");
	}
	// public abstract void addToCart(String sessionToken, ItemId itemId, int quantity);
	// public abstract void removeFromCart(String sessionToken, ItemId itemId, int quantity);
	// public abstract void clearCart(String sessionToken);
	// public abstract String displayCart(String sessionToken); // return a string that you can display later
	/**
	 * For feedback, rating=1 is thumbs up and rating=-1 is thumbs down.
	 * A user should only be able to provide feedback for an item once.
	 * Maybe return a String so I can give a proper message if the user attempts to provide feedback more than once.
	 */
	// public abstract void provideFeedback(String sessionToken, ItemId itemId, int rating); // TO-DO: Figure out how to limit to one vote
	// public abstract Purchase[] getPurchaseHistory(String sessionToken);
}

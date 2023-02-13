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
import common.transport.socket.APIEnumV1;
import common.transport.socket.BaseSocketServerThread;
import common.transport.socket.BuyerEnumV1;
import common.Item;
import java.net.*;
import java.io.IOException;
import java.io.EOFException; // happens when writing to a closed socket

public class BuyerSocketServerThreadV1 extends BaseSocketServerThread implements BuyerInterface {
	private BuyerInterface buyerInterfaceV1;
	private BuyerEnumV1[] buyerEnumV1Values; // for translating function ID to enum value
	private APIEnumV1[] apiEnumV1Values;
	private Socket socket = null;

	// CONSTRUCTORS
	// Use this Constructor for threads that have an active connection
	public BuyerSocketServerThreadV1(BuyerInterface buyerInterfaceV1, Socket socket) {
		super(socket);
		this.buyerInterfaceV1 = buyerInterfaceV1;
		this.buyerEnumV1Values = BuyerEnumV1.values();
		this.apiEnumV1Values = APIEnumV1.values();
	}


	protected byte[] demux(short apiVer, int api, int funcId, byte[] msg) throws IOException {
		switch (apiVer) {
			case 1:
				return this.demuxV1(api, funcId, msg);
			default:
				throw new RuntimeException("Err BuyerSocketServerThreadV1: Received message with invalid API version.");
		}
	}
	private byte[] demuxV1(int api, int funcId, byte[] msg) throws IOException {
		APIEnumV1 apiName = this.apiEnumV1Values[api];
		switch (apiName) {
			case BUYER:
				return this.demuxV1Buyer(funcId, msg);
			default:
				throw new RuntimeException("Err BuyerSocketServerThreadV1: Received message with invalid API identifier.");
		}
	}
	private byte[] demuxV1Buyer(int funcId, byte[] msg) throws IOException {
		BuyerEnumV1 functionName = this.buyerEnumV1Values[funcId];
		switch (functionName) {
			case CREATE_USER:
				return this.bytesCreateUser(msg);
			case LOGIN:
				return this.bytesLogin(msg);
			case LOGOUT:
				return this.bytesLogout(msg);
			case GET_SELLER_RATING:
				return this.bytesGetSellerRating(msg);
			default:
				throw new RuntimeException("Err BuyerSocketServerThreadV1: Unsupported method triggered by enum.");
		}
	}

	// Buyer interface methods and their new counterparts
	public int createUser(String username, String password) {
		return this.buyerInterfaceV1.createUser(username, password);
	}
	private byte[] bytesCreateUser(byte[] msg) throws IOException {
		SerializeLogin serLog = SerializeLogin.deserialize(msg);
		int userId = this.createUser(serLog.getUsername(), serLog.getPassword());
		return SerializeInt.serialize(userId);
	}

	public String login(String username, String password) {
		return this.buyerInterfaceV1.login(username, password);
	}
	private byte[] bytesLogin(byte[] msg) throws IOException {
		SerializeLogin serLog = SerializeLogin.deserialize(msg);
		String sessionToken = this.login(serLog.getUsername(), serLog.getPassword());
		return SerializeString.serialize(sessionToken);
	}
	public void logout(String sessionToken) {
		this.buyerInterfaceV1.logout(sessionToken);
		this.stopServer();
		System.out.println("Logging out");
	}
	private byte[] bytesLogout(byte[] msg) throws IOException {
		String sessionToken = SerializeString.deserialize(msg);
		this.logout(sessionToken);
		byte[] output = new byte[0];
		return output;
	}
	public int[] getSellerRating(int sellerId) {
		return this.buyerInterfaceV1.getSellerRating(sellerId);
	}
	private byte[] bytesGetSellerRating(byte[] msg) throws IOException {
		int sellerId = SerializeInt.deserialize(msg);
		int[] rating = this.getSellerRating(sellerId);
		return SerializeIntArray.serialize(rating);
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

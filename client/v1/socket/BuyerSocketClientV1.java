/**
 * Class BuyerSocketClientV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of buyer client-server IPC on client side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package client.v1.socket;
import common.interfaces.BuyerInterface;
import common.transport.serialize.*;
import common.transport.socket.APIEnumV1;
import common.transport.socket.BaseSocketClient;
import common.transport.socket.BuyerEnumV1;
import common.Item;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.net.SocketException;

public class BuyerSocketClientV1 extends BaseSocketClient implements BuyerInterface {

	// CONSTRUCTORS
	// recommend serverIp = localhost
	public BuyerSocketClientV1(String serverIp, int serverPort) throws SocketException {
		super(serverIp, serverPort, (short) 1, APIEnumV1.BUYER.ordinal());
	}

	// Inherited Methods
	public int createUser(String username, String password) throws IOException, IllegalArgumentException {
		int funcId = BuyerEnumV1.CREATE_USER.ordinal();
		int userId = 0;
		byte[] msg = SerializeLogin.serialize(username, password);
		byte[] buf = this.sendAndReceive(msg, funcId);
		userId = SerializeInt.deserialize(buf);
		// stop the Socket connection. Only do persistent connection while logged in. Should not be called while logged in, or the session token may not be deleted.
		// this.cleanup();
		return userId;
	}
	public String login(String username, String password) throws IOException, NoSuchElementException {
		int funcId = BuyerEnumV1.LOGIN.ordinal();
		String sessionToken = null;
		byte[] msg = SerializeLogin.serialize(username, password);
		byte[] buf = this.sendAndReceive(msg, funcId);
		sessionToken = SerializeString.deserialize(buf);
		return sessionToken;
	}
	// clean up (close buffers and close the connection) when finished
	// not sure if I should clean up if I get an IOException from the database logout operation
	public void logout(String sessionToken) throws IOException, NoSuchElementException {
		int funcId = BuyerEnumV1.LOGOUT.ordinal();
		try {
			byte[] msg = SerializeString.serialize(sessionToken);
			byte[] buf = this.sendAndReceive(msg, funcId);
			assert buf.length == 0; // server sending empty response after packet prefix
		} catch (Exception e) {
			System.out.println(e);
		}
		this.cleanup();
	}
	public int[] getSellerRating(int sellerId) throws IOException, NoSuchElementException {
		int funcId = BuyerEnumV1.GET_SELLER_RATING.ordinal();
		int[] rating = null;
		byte[] msg = SerializeInt.serialize(sellerId);
		byte[] buf = this.sendAndReceive(msg, funcId);
		rating = SerializeIntArray.deserialize(buf);
		return rating;
	}
	public Item[] searchItem(String sessionToken, int category, String[] keywords) throws IOException {
		int funcId = BuyerEnumV1.SEARCH_ITEM.ordinal();
		Item[] results = null;
		byte[] msg = SerializeSearchArg.serialize(sessionToken, category, keywords);
		byte[] buf = this.sendAndReceive(msg, funcId);
		results = Item.deserializeArray(buf); // could potentially throw an IllegalArgumentException if the packet source is malicious
		return results;
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

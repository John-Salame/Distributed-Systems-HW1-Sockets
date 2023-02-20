/**
 * Class SellerSocketClientV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of seller client-server IPC on client side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package client.v1;
import common.SellerInterface;
import common.transport.serialize.*;
import common.transport.socket.APIEnumV1;
import common.transport.socket.BaseSocketClient;
import common.transport.socket.SellerEnumV1;
import common.Item;
import common.ItemId;
import common.SaleListingId;
import java.io.IOException;
import java.util.NoSuchElementException;

public class SellerSocketClientV1 extends BaseSocketClient implements SellerInterface {
	
	// CONSTRUCTORS
	// recommend serverIp = localhost
	public SellerSocketClientV1(String serverIp, int serverPort) {
		super(serverIp, serverPort, (short) 1, APIEnumV1.SELLER.ordinal());
	}

	// Inherited Methods
	public int createUser(String username, String password) {
		int funcId = SellerEnumV1.CREATE_USER.ordinal();
		int userId = 0;
		try {
			byte[] msg = SerializeLogin.serialize(username, password);
			byte[] buf = this.sendAndReceive(msg, funcId);
			userId = SerializeInt.deserialize(buf);
		}
		catch (IOException i) {
			System.out.println(i);
			return 0; // user id 0 indicates error
		}
		return userId;
	}
	public String login(String username, String password) {
		int funcId = SellerEnumV1.LOGIN.ordinal();
		String sessionToken = null;
		try {
			byte[] msg = SerializeLogin.serialize(username, password);
			byte[] buf = this.sendAndReceive(msg, funcId);
			sessionToken = SerializeString.deserialize(buf);
		}
		catch (IOException i) {
			System.out.println(i);
		}
		return sessionToken;
	}
	// clean up (close buffers and close the connection) when finished
	public void logout(String sessionToken) {
		int funcId = SellerEnumV1.LOGOUT.ordinal();
		try {
			byte[] msg = SerializeString.serialize(sessionToken);
			byte[] buf = this.sendAndReceive(msg, funcId);
			assert buf.length == 0; // server sending empty response after packet prefix
		}
		catch (IOException i) {
			System.out.println(i);
		}
		this.cleanup();
	}
	public int[] getSellerRating(int sellerId) {
		int funcId = SellerEnumV1.GET_SELLER_RATING.ordinal();
		int[] rating = null;
		try {
			byte[] msg = SerializeInt.serialize(sellerId);
			byte[] buf = this.sendAndReceive(msg, funcId);
			rating = SerializeIntArray.deserialize(buf);
		}
		catch (IOException i) {
			System.out.println(i);
		}
		return rating;
	}
	public SaleListingId putOnSale(String sessionToken, Item item, int quantity) {
		throw new RuntimeException("SellerSocketClientV1: putOnSale() Not implemented");
	}
	public void changePriceOfItem(String sessionToken, ItemId itemId, float newPrice) throws NoSuchElementException, IllegalArgumentException, UnsupportedOperationException {
		int funcId = SellerEnumV1.CHANGE_PRICE_OF_ITEM.ordinal();
		try {
			byte[] msg = SerializePriceArgClientServer.serialize(sessionToken, itemId, newPrice);
			byte[] buf = this.sendAndReceive(msg, funcId);
			assert buf.length == 0;
		}
		catch (IOException i) {
			System.out.println(i);
		}
	}
	/*
	public void removeItemFromSale(String sessionToken, ItemId itemId, int quantity);
	public String displayItemsOnSale(String sessionToken);
	*/
}

/**
 * Class SellerSocketClientV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of seller client-server IPC on client side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package com.jsala.client.v1.socket;
import com.jsala.common.interfaces.SellerInterface;
import com.jsala.common.transport.serialize.*;
import com.jsala.common.transport.socket.APIEnumV1;
import com.jsala.common.transport.socket.BaseSocketClient;
import com.jsala.common.transport.socket.SellerEnumV1;
import com.jsala.common.Item;
import com.jsala.common.ItemId;
import com.jsala.common.SaleListingId;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.net.SocketException;

public class SellerSocketClientV1 extends BaseSocketClient implements SellerInterface {
	
	// CONSTRUCTORS
	// recommend serverIp = localhost
	public SellerSocketClientV1(String serverIp, int serverPort) throws SocketException {
		super(serverIp, serverPort, (short) 1, APIEnumV1.SELLER.ordinal());
	}

	// Inherited Methods
	public int createUser(String username, String password) throws IOException, IllegalArgumentException {
		int funcId = SellerEnumV1.CREATE_USER.ordinal();
		int userId = 0;
		byte[] msg = SerializeLogin.serialize(username, password);
		byte[] buf = this.sendAndReceive(msg, funcId);
		userId = SerializeInt.deserialize(buf);
		// stop the Socket connection. Only do persistent connection while logged in. Should not be called while logged in, or the session token may not be deleted.
		// this.cleanup();
		return userId;
	}
	public String login(String username, String password) throws IOException, NoSuchElementException {
		int funcId = SellerEnumV1.LOGIN.ordinal();
		String sessionToken = null;
		byte[] msg = SerializeLogin.serialize(username, password);
		byte[] buf = this.sendAndReceive(msg, funcId);
		sessionToken = SerializeString.deserialize(buf);
		return sessionToken;
	}
	// clean up (close buffers and close the connection) when finished
	// not sure if I should clean up if I get an IOException from the database logout operation
	public void logout(String sessionToken) throws IOException, NoSuchElementException {
		int funcId = SellerEnumV1.LOGOUT.ordinal();
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
		int funcId = SellerEnumV1.GET_SELLER_RATING.ordinal();
		int[] rating = null;
		byte[] msg = SerializeInt.serialize(sellerId);
		byte[] buf = this.sendAndReceive(msg, funcId);
		rating = SerializeIntArray.deserialize(buf);
		return rating;
	}
	public SaleListingId putOnSale(String sessionToken, Item item, int quantity) throws IOException, IllegalArgumentException {
		throw new RuntimeException("SellerSocketClientV1: putOnSale() Not implemented");
	}
	public void changePriceOfItem(String sessionToken, ItemId itemId, float newPrice) throws IOException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException {
		int funcId = SellerEnumV1.CHANGE_PRICE_OF_ITEM.ordinal();
		byte[] msg = SerializePriceArgClientServer.serialize(sessionToken, itemId, newPrice);
		byte[] buf = this.sendAndReceive(msg, funcId);
		assert buf.length == 0;
	}
	/*
	public void removeItemFromSale(String sessionToken, ItemId itemId, int quantity);
	public String displayItemsOnSale(String sessionToken);
	*/
}

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
import common.transport.socket.APIEnumV1;
import common.transport.socket.BaseSocketServerThread;
import common.transport.socket.SellerEnumV1;
import common.Item;
import java.net.*;
import java.io.IOException;

public class SellerSocketServerThreadV1 extends BaseSocketServerThread implements SellerInterface {
	private SellerInterface sellerInterfaceV1;
	private SellerEnumV1[] sellerEnumV1Values; // for translating function ID to enum value
	private APIEnumV1[] apiEnumV1Values;

	// CONSTRUCTORS
	// Use this Constructor for threads that have an active connection
	public SellerSocketServerThreadV1(SellerInterface sellerInterfaceV1, Socket socket) {
		super(socket);
		this.sellerInterfaceV1 = sellerInterfaceV1;
		this.sellerEnumV1Values = SellerEnumV1.values();
		this.apiEnumV1Values = APIEnumV1.values();
	}

	@Override
	protected byte[] demux(short apiVer, int api, int funcId, byte[] msg) throws IOException {
		switch (apiVer) {
			case 1:
				return this.demuxV1(api, funcId, msg);
			default:
				throw new RuntimeException("Err SellerSocketServerThreadV1: Received message with invalid API version.");
		}
	}
	private byte[] demuxV1(int api, int funcId, byte[] msg) throws IOException {
		APIEnumV1 apiName = this.apiEnumV1Values[api];
		switch (apiName) {
			case SELLER:
				return this.demuxV1Seller(funcId, msg);
			default:
				throw new RuntimeException("Err SellerSocketServerThreadV1: Received message with invalid API identifier.");
		}
	}
	private byte[] demuxV1Seller(int funcId, byte[] msg) throws IOException {
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

/**
 * Class DBCustomerSellerSocketClientV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of seller server-database IPC on server side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package com.jsala.server.v1.socket;
import com.jsala.common.transport.serialize.*;
import com.jsala.common.transport.socket.APIEnumV1;
import com.jsala.common.transport.socket.BaseSocketClient;
import com.jsala.common.transport.socket.DBSellerEnumV1;
import com.jsala.common.Seller;
import com.jsala.dao.SellerDAO;
import java.util.NoSuchElementException;
import java.io.IOException;
import java.net.SocketException;

public class DBCustomerSellerSocketClientV1 extends BaseSocketClient implements SellerDAO {

	// CONSTRUCTORS
	// recommend serverIp = localhost
	public DBCustomerSellerSocketClientV1(String serverIp, int serverPort) /*throws SocketException*/ {
		super(serverIp, serverPort, (short) 1, APIEnumV1.DB_SELLER.ordinal());
	}

	// SellerDAO Methods
	public int createUser(String username, String password) throws IOException, IllegalArgumentException {
		int funcId = DBSellerEnumV1.CREATE_USER.ordinal();
		int userId = 0;
		// all these functions can throw an exception. My functions are at-most-once at all interfaces and will not retry on failure, they will simply throw an Exception.
		byte[] msg = SerializeLogin.serialize(username, password);
		byte[] buf = this.sendAndReceive(msg, funcId);
		userId = SerializeInt.deserialize(buf);
		return userId;
	}
	public int getUserId(String username, String password) throws IOException, NoSuchElementException {
		int funcId = DBSellerEnumV1.GET_USER_ID.ordinal();
		int userId = 0;
		// all these functions can throw an exception. My functions are at-most-once at all interfaces and will not retry on failure, they will simply throw an Exception.
		byte[] msg = SerializeLogin.serialize(username, password);
		byte[] buf = this.sendAndReceive(msg, funcId);
		userId = SerializeInt.deserialize(buf);
		return userId;
	}
	public Seller getSellerById(int sellerId) throws IOException, NoSuchElementException {
		int funcId = DBSellerEnumV1.GET_SELLER_BY_ID.ordinal();
		Seller seller = null;
		// all these functions can throw an exception. My functions are at-most-once at all interfaces and will not retry on failure, they will simply throw an Exception.
		byte[] msg = SerializeInt.serialize(sellerId);
		byte[] buf = this.sendAndReceive(msg, funcId);
		seller = Seller.deserialize(buf); // could potentially throw IllegalArgumentException if the sender is malicious
		return seller;
	}
	public void commitSeller(Seller seller) throws IOException, IllegalArgumentException {
		int funcId = DBSellerEnumV1.COMMIT_SELLER.ordinal();
		// all these functions can throw an exception. My functions are at-most-once at all interfaces and will not retry on failure, they will simply throw an Exception.
		byte[] msg = Seller.serialize(seller);
		byte[] buf = this.sendAndReceive(msg, funcId);
		assert buf.length == 0;
	}
	public void closeConnection() throws IOException {
		this.cleanup();
	}
}

/**
 * Class DBCustomerBuyerSocketClientV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of buyer server-database IPC on server side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package server.v1.socket;
import dao.BuyerDAO;
import common.transport.serialize.*;
import common.transport.socket.APIEnumV1;
import common.transport.socket.BaseSocketClient;
import common.transport.socket.DBBuyerEnumV1;
import common.Buyer;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.net.SocketException;

public class DBCustomerBuyerSocketClientV1 extends BaseSocketClient implements BuyerDAO {

	// CONSTRUCTORS
	// recommend serverIp = localhost
	public DBCustomerBuyerSocketClientV1(String serverIp, int serverPort) /*throws SocketException*/ {
		super(serverIp, serverPort, (short) 1, APIEnumV1.DB_BUYER.ordinal());
	}

	// BuyerDAO Methods
	public int createUser(String username, String password) throws IOException, IllegalArgumentException {
		int funcId = DBBuyerEnumV1.CREATE_USER.ordinal();
		int userId = 0; // user id 0 indicates error
		// all these functions can throw an exception. My functions are at-most-once at all interfaces and will not retry on failure, they will simply throw an Exception.
		byte[] msg = SerializeLogin.serialize(username, password);
		byte[] buf = this.sendAndReceive(msg, funcId);
		userId = SerializeInt.deserialize(buf);
		return userId;
	}
	public int getUserId(String username, String password) throws IOException, NoSuchElementException {
		int funcId = DBBuyerEnumV1.GET_USER_ID.ordinal();
		int userId = 0; // user id 0 indicates error
		// all these functions can throw an exception. My functions are at-most-once at all interfaces and will not retry on failure, they will simply throw an Exception.
		byte[] msg = SerializeLogin.serialize(username, password);
		byte[] buf = this.sendAndReceive(msg, funcId);
		userId = SerializeInt.deserialize(buf);
		return userId;
	}
	public Buyer getBuyerById(int buyerId) throws IOException, NoSuchElementException {
		int funcId = DBBuyerEnumV1.GET_BUYER_BY_ID.ordinal();
		Buyer buyer = null;
		// all these functions can throw an exception. My functions are at-most-once at all interfaces and will not retry on failure, they will simply throw an Exception.
		byte[] msg = SerializeInt.serialize(buyerId);
		byte[] buf = this.sendAndReceive(msg, funcId);
		buyer = Buyer.deserialize(buf);
		return buyer;
	}
	public void closeConnection() throws IOException {
		this.cleanup();
	}
}

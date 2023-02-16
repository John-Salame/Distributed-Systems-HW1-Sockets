/**
 * Class DBCustomerBuyerSocketClientV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of buyer server-database IPC on server side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package server.v1;
import dao.BuyerDAO;
import common.transport.serialize.*;
import common.transport.socket.APIEnumV1;
import common.transport.socket.BaseSocketClient;
import common.transport.socket.DBBuyerEnumV1;
import common.Buyer;
import java.io.IOException;

public class DBCustomerBuyerSocketClientV1 extends BaseSocketClient implements BuyerDAO {

	// CONSTRUCTORS
	// recommend serverIp = localhost
	public DBCustomerBuyerSocketClientV1(String serverIp, int serverPort) {
		super(serverIp, serverPort, (short) 1, APIEnumV1.DB_BUYER.ordinal());
	}

	// BuyerDAO Methods
	public int createUser(String username, String password) {
		int funcId = DBBuyerEnumV1.CREATE_USER.ordinal();
		int userId = 0; // user id 0 indicates error
		try {
			byte[] msg = SerializeLogin.serialize(username, password);
			byte[] buf = this.sendAndReceive(msg, funcId);
			userId = SerializeInt.deserialize(buf);
		}
		catch (IOException i) {
			System.out.println(i);
		}
		return userId;
	}
	public int getUserId(String username, String password) {
		int funcId = DBBuyerEnumV1.GET_USER_ID.ordinal();
		int userId = 0; // user id 0 indicates error
		try {
			byte[] msg = SerializeLogin.serialize(username, password);
			byte[] buf = this.sendAndReceive(msg, funcId);
			userId = SerializeInt.deserialize(buf);
		}
		catch (IOException i) {
			System.out.println(i);
		}
		return userId;
	}
	public Buyer getBuyerById(int buyerId) {
		int funcId = DBBuyerEnumV1.GET_BUYER_BY_ID.ordinal();
		Buyer buyer = null;
		try {
			byte[] msg = SerializeInt.serialize(buyerId);
			byte[] buf = this.sendAndReceive(msg, funcId);
			buyer = Buyer.deserialize(buf);
		}
		catch (IOException i) {
			System.out.println(i);
		}
		return buyer;
	}
}

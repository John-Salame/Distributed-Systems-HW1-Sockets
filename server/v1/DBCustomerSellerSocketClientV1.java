/**
 * Class DBCustomerSellerSocketClientV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of seller server-database IPC on server side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package server.v1;
import dao.SellerDAO;
import common.transport.serialize.*;
import common.transport.socket.APIEnumV1;
import common.transport.socket.BaseSocketClient;
import common.transport.socket.DBSellerEnumV1;
import common.Seller;
import java.io.IOException;

public class DBCustomerSellerSocketClientV1 extends BaseSocketClient implements SellerDAO {

	// CONSTRUCTORS
	// recommend serverIp = localhost
	public DBCustomerSellerSocketClientV1(String serverIp, int serverPort) {
		super(serverIp, serverPort, (short) 1, APIEnumV1.DB_SELLER.ordinal());
	}

	// SellerDAO Methods
	public int createUser(String username, String password) {
		int funcId = DBSellerEnumV1.CREATE_USER.ordinal();
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
		int funcId = DBSellerEnumV1.GET_USER_ID.ordinal();
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
	public Seller getSellerById(int sellerId) {
		int funcId = DBSellerEnumV1.GET_SELLER_BY_ID.ordinal();
		Seller seller = null;
		try {
			byte[] msg = SerializeInt.serialize(sellerId);
			byte[] buf = this.sendAndReceive(msg, funcId);
			seller = Seller.deserialize(buf);
		}
		catch (IOException i) {
			System.out.println(i);
		}
		return seller;
	}
	public void commitSeller(Seller seller) {
		int funcId = DBSellerEnumV1.COMMIT_SELLER.ordinal();
		try {
			byte[] msg = Seller.serialize(seller);
			byte[] buf = this.sendAndReceive(msg, funcId);
			assert buf.length == 0;
		}
		catch (IOException i) {
			System.out.println(i);
		}
	}
}

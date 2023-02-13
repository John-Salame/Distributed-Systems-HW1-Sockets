/**
 * Class DBCustomerSocketServerThreadV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of seller server-database IPC on database side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package db.customer.v1;
import dao.SellerDAO;
import common.transport.serialize.*;
import common.transport.socket.APIEnumV1;
import common.transport.socket.BaseSocketServerThread;
import common.transport.socket.DBSellerEnumV1;
import common.Item;
import common.Seller;
import java.net.*;
import java.io.IOException;

public class DBCustomerSocketServerThreadV1 extends BaseSocketServerThread implements SellerDAO {
	private SellerDAO sellerDaoV1;
	private DBSellerEnumV1[] dbSellerEnumV1Values; // for translating function ID to enum value
	private APIEnumV1[] apiEnumV1Values;

	// CONSTRUCTORS
	// Use this Constructor for threads that have an active connection
	public DBCustomerSocketServerThreadV1(SellerDAO sellerDaoV1, Socket socket) {
		super(socket);
		this.sellerDaoV1 = sellerDaoV1;
		this.dbSellerEnumV1Values = DBSellerEnumV1.values();
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
			case DB_SELLER:
				return this.demuxV1DBSeller(funcId, msg);
			default:
				throw new RuntimeException("Err SellerSocketServerThreadV1: Received message with invalid API identifier.");
		}
	}
	private byte[] demuxV1DBSeller(int funcId, byte[] msg) throws IOException {
		// TO-DO: Error handling if funcId is an invalid index
		DBSellerEnumV1 functionName = this.dbSellerEnumV1Values[funcId];
		switch (functionName) {
			case CREATE_USER:
				return this.bytesCreateUser(msg);
			case GET_USER_ID:
				return this.bytesGetUserId(msg);
			case GET_SELLER_BY_ID:
				return this.bytesGetSellerById(msg);
			case COMMIT_SELLER:
				return this.bytesCommitSeller(msg);
			default:
				throw new RuntimeException("Err SellerSocketServerThreadV1: Unsupported method triggered by enum.");
		}
	}

	// Seller interface methods and their new counterparts
	public int createUser(String username, String password) {
		return this.sellerDaoV1.createUser(username, password);
	}
	private byte[] bytesCreateUser(byte[] msg) throws IOException {
		SerializeLogin serLog = SerializeLogin.deserialize(msg);
		int userId = this.createUser(serLog.getUsername(), serLog.getPassword());
		return SerializeInt.serialize(userId);
	}
	public int getUserId(String username, String password) {
		return this.sellerDaoV1.getUserId(username, password);
	}
	private byte[] bytesGetUserId(byte[] msg) throws IOException {
		SerializeLogin serLog = SerializeLogin.deserialize(msg);
		int userId = this.getUserId(serLog.getUsername(), serLog.getPassword());
		return SerializeInt.serialize(userId);
	}
	public Seller getSellerById(int sellerId) {
		return this.sellerDaoV1.getSellerById(sellerId);
	}
	private byte[] bytesGetSellerById(byte[] msg) throws IOException {
		int sellerId = SerializeInt.deserialize(msg);
		Seller seller = this.getSellerById(sellerId);
		return Seller.serialize(seller);
	}
	public void commitSeller(Seller seller) {
		this.sellerDaoV1.commitSeller(seller);
	}
	private byte[] bytesCommitSeller(byte[] msg) throws IOException {
		Seller seller = Seller.deserialize(msg);
		this.commitSeller(seller);
		return new byte[0]; // empty response for void functions
	}
}

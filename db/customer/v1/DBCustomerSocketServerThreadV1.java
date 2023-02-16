/**
 * Class DBCustomerSocketServerThreadV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of customer server-database IPC on database side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package db.customer.v1;
import dao.BuyerDAO;
import dao.SellerDAO;
import common.transport.serialize.*;
import common.transport.socket.APIEnumV1;
import common.transport.socket.BaseSocketServerThread;
import common.transport.socket.DBBuyerEnumV1;
import common.transport.socket.DBSellerEnumV1;
import common.Buyer;
import common.Item;
import common.Seller;
import java.net.*;
import java.io.IOException;

public class DBCustomerSocketServerThreadV1 extends BaseSocketServerThread implements BuyerDAO, SellerDAO {
	private BuyerDAO buyerDaoV1;
	private SellerDAO sellerDaoV1;
	private DBBuyerEnumV1[] dbBuyerEnumV1Values; // for translating function ID to enum value
	private DBSellerEnumV1[] dbSellerEnumV1Values; // for translating function ID to enum value
	private APIEnumV1[] apiEnumV1Values;

	// CONSTRUCTORS
	// Use this Constructor for threads that have an active connection
	public DBCustomerSocketServerThreadV1(BuyerDAO buyerDaoV1, SellerDAO sellerDaoV1, Socket socket) {
		super(socket);
		this.buyerDaoV1 = buyerDaoV1;
		this.sellerDaoV1 = sellerDaoV1;
		this.dbBuyerEnumV1Values = DBBuyerEnumV1.values();
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
			case DB_BUYER:
				return this.demuxV1DBBuyer(funcId, msg);
			case DB_SELLER:
				return this.demuxV1DBSeller(funcId, msg);
			default:
				throw new RuntimeException("Err SellerSocketServerThreadV1: Received message with invalid API identifier.");
		}
	}
	private byte[] demuxV1DBBuyer(int funcId, byte[] msg) throws IOException {
		DBBuyerEnumV1 functionName = this.dbBuyerEnumV1Values[funcId];
		switch (functionName) {
			case CREATE_USER:
				return this.bytesCreateBuyer(msg);
			case GET_USER_ID:
				return this.bytesGetBuyerId(msg);
			case GET_BUYER_BY_ID:
				return this.bytesGetBuyerById(msg);
			default:
				throw new RuntimeException("Err SellerSocketServerThreadV1: Unsupported method triggered by enum.");
		}
	}
	private byte[] demuxV1DBSeller(int funcId, byte[] msg) throws IOException {
		// TO-DO: Error handling if funcId is an invalid index
		DBSellerEnumV1 functionName = this.dbSellerEnumV1Values[funcId];
		switch (functionName) {
			case CREATE_USER:
				return this.bytesCreateSeller(msg);
			case GET_USER_ID:
				return this.bytesGetSellerId(msg);
			case GET_SELLER_BY_ID:
				return this.bytesGetSellerById(msg);
			case COMMIT_SELLER:
				return this.bytesCommitSeller(msg);
			default:
				throw new RuntimeException("Err SellerSocketServerThreadV1: Unsupported method triggered by enum.");
		}
	}

	// Defunct functions
	public int createUser(String username, String password) {
		throw new RuntimeException("Err SellerSocketServerThreadV1: createUser() not implemented");
	}
	public int getUserId(String username, String password) {
		throw new RuntimeException("Err SellerSocketServerThreadV1: getUserId() not implemented");
	}

	// Buyer functions
	public int createBuyer(String username, String password) {
		return this.buyerDaoV1.createUser(username, password);
	}
	private byte[] bytesCreateBuyer(byte[] msg) throws IOException {
		SerializeLogin serLog = SerializeLogin.deserialize(msg);
		int userId = this.createBuyer(serLog.getUsername(), serLog.getPassword());
		return SerializeInt.serialize(userId);
	}
	public int getBuyerId(String username, String password) {
		return this.buyerDaoV1.getUserId(username, password);
	}
	private byte[] bytesGetBuyerId(byte[] msg) throws IOException {
		SerializeLogin serLog = SerializeLogin.deserialize(msg);
		int userId = this.getBuyerId(serLog.getUsername(), serLog.getPassword());
		return SerializeInt.serialize(userId);
	}
	public Buyer getBuyerById(int sellerId) {
		return this.buyerDaoV1.getBuyerById(sellerId);
	}
	private byte[] bytesGetBuyerById(byte[] msg) throws IOException {
		int sellerId = SerializeInt.deserialize(msg);
		Buyer buyer = this.getBuyerById(sellerId);
		return Buyer.serialize(buyer);
	}

	// Seller functions
	public int createSeller(String username, String password) {
		return this.sellerDaoV1.createUser(username, password);
	}
	private byte[] bytesCreateSeller(byte[] msg) throws IOException {
		SerializeLogin serLog = SerializeLogin.deserialize(msg);
		int userId = this.createSeller(serLog.getUsername(), serLog.getPassword());
		return SerializeInt.serialize(userId);
	}
	public int getSellerId(String username, String password) {
		return this.sellerDaoV1.getUserId(username, password);
	}
	private byte[] bytesGetSellerId(byte[] msg) throws IOException {
		SerializeLogin serLog = SerializeLogin.deserialize(msg);
		int userId = this.getSellerId(serLog.getUsername(), serLog.getPassword());
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

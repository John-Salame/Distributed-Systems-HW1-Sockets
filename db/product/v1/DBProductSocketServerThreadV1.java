/**
 * Class DBProductSocketServerThreadV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of customer server-database IPC on database side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package db.product.v1;
import common.transport.serialize.*;
import common.transport.socket.APIEnumV1;
import common.transport.socket.BaseSocketServerThread;
import common.transport.socket.DBItemEnumV1;
import common.Item;
import common.ItemId;
import dao.ItemDAO;
import java.net.*;
import java.io.IOException;
import java.util.NoSuchElementException;

public class DBProductSocketServerThreadV1 extends BaseSocketServerThread implements ItemDAO {
	private ItemDAO itemDaoV1;
	private DBItemEnumV1[] dbItemEnumV1Values; // for translating function ID to enum value
	private APIEnumV1[] apiEnumV1Values;

	// CONSTRUCTORS
	// Use this Constructor for threads that have an active connection
	public DBProductSocketServerThreadV1(ItemDAO itemDaoV1, Socket socket) {
		super(socket);
		this.itemDaoV1 = itemDaoV1;
		this.dbItemEnumV1Values = DBItemEnumV1.values();
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
			case DB_ITEM:
				return this.demuxV1DBItem(funcId, msg);
			default:
				throw new RuntimeException("Err SellerSocketServerThreadV1: Received message with invalid API identifier.");
		}
	}
	private byte[] demuxV1DBItem(int funcId, byte[] msg) throws IOException {
		DBItemEnumV1 functionName = this.dbItemEnumV1Values[funcId];
		switch (functionName) {
			case CREATE_ITEM:
				return this.bytesCreateItem(msg);
			case GET_ITEM_BY_ID:
				return this.bytesGetItemById(msg);
			case CHANGE_PRICE:
				return this.bytesChangePrice(msg);
			case GET_ITEMS_BY_SELLER:
				return this.bytesGetItemsBySeller(msg);
			case GET_ITEMS_IN_CATEGORY:
				return this.bytesGetItemsInCategory(msg);
			default:
				throw new RuntimeException("Err SellerSocketServerThreadV1: Unsupported method triggered by enum.");
		}
	}
	

	// ITEM METHODS
	// take in an item with an incomplete item id and update the id.
	public ItemId createItem(Item item) throws IOException, IllegalArgumentException {
		return itemDaoV1.createItem(item);
	}
	private byte[] bytesCreateItem(byte[] msg) throws IOException, IllegalArgumentException {
		Item item = Item.deserialize(msg);
		ItemId itemId = this.createItem(item);
		return ItemId.serialize(itemId);
	}
	public Item getItemById(ItemId itemId) throws IOException, NoSuchElementException {
		return itemDaoV1.getItemById(itemId);
	}
	private byte[] bytesGetItemById(byte[] msg) throws IOException, NoSuchElementException {
		ItemId itemId = ItemId.deserialize(msg);
		Item item = this.getItemById(itemId);
		return Item.serialize(item);
	}
	// use sellerId to verify that you are the correct seller to change the price
	public void changePrice(ItemId itemId, int sellerId, float newPrice) throws IOException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException {
		itemDaoV1.changePrice(itemId, sellerId, newPrice);
	}
	private byte[] bytesChangePrice(byte[] msg) throws IOException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException {
		SerializePriceArgDB priceArg = SerializePriceArgDB.deserialize(msg);
		this.changePrice(priceArg.getItemId(), priceArg.getSellerId(), priceArg.getPrice());
		return new byte[0];
	}
	public Item[] getItemsBySeller(int sellerId) throws IOException {
		return itemDaoV1.getItemsBySeller(sellerId);
	}
	private byte[] bytesGetItemsBySeller(byte[] msg) throws IOException {
		int sellerId = SerializeInt.deserialize(msg);
		Item[] sellerItems = this.getItemsBySeller(sellerId);
		return Item.serializeArray(sellerItems);
	}
	public Item[] getItemsInCategory(int category) throws IOException {
		return itemDaoV1.getItemsInCategory(category);
	}
	private byte[] bytesGetItemsInCategory(byte[] msg) throws IOException {
		int category = SerializeInt.deserialize(msg);
		Item[] catItems = this.getItemsInCategory(category);
		return Item.serializeArray(catItems);
	}


	public void closeConnection() {
		// do nothing
	}
}

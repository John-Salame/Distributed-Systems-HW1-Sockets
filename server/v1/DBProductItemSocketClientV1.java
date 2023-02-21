/**
 * Class DBProductItemSocketClientV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of session server-database IPC on server side
 */

package server.v1;
import dao.ItemDAO;
import common.transport.serialize.*;
import common.transport.socket.APIEnumV1;
import common.transport.socket.BaseSocketClient;
import common.transport.socket.DBItemEnumV1;
import common.Item;
import common.ItemId;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.net.SocketException;

public class DBProductItemSocketClientV1 extends BaseSocketClient implements ItemDAO {

	// CONSTRUCTORS
	// recommend serverIp = localhost
	public DBProductItemSocketClientV1(String serverIp, int serverPort) throws SocketException {
		super(serverIp, serverPort, (short) 1, APIEnumV1.DB_ITEM.ordinal());
	}

	// ItemDAO Methods
	public ItemId createItem(Item item) throws IOException, IllegalArgumentException {
		int funcId = DBItemEnumV1.CREATE_ITEM.ordinal();
		ItemId itemId = null;
		// all these functions can throw an exception. My functions are at-most-once at all interfaces and will not retry on failure, they will simply throw an Exception.
		byte[] msg = Item.serialize(item);
		byte[] buf = this.sendAndReceive(msg, funcId);
		itemId = ItemId.deserialize(buf);
		return itemId;
	}
	public Item getItemById(ItemId itemId) throws IOException, NoSuchElementException {
		int funcId = DBItemEnumV1.GET_ITEM_BY_ID.ordinal();
		Item item = null;
		// all these functions can throw an exception. My functions are at-most-once at all interfaces and will not retry on failure, they will simply throw an Exception.
		byte[] msg = ItemId.serialize(itemId);
		byte[] buf = this.sendAndReceive(msg, funcId);
		item = Item.deserialize(buf);
		return item;
	}
	public void changePrice(ItemId itemId, int sellerId, float newPrice) throws IOException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException {
		int funcId = DBItemEnumV1.CHANGE_PRICE.ordinal();
		// all these functions can throw an exception. My functions are at-most-once at all interfaces and will not retry on failure, they will simply throw an Exception.
		byte[] msg = SerializePriceArgDB.serialize(itemId, sellerId, newPrice);
		byte[] buf = this.sendAndReceive(msg, funcId);
		assert buf.length == 0;
	}
	public Item[] getItemsBySeller(int sellerId) throws IOException {
		int funcId = DBItemEnumV1.GET_ITEMS_BY_SELLER.ordinal();
		Item[] sellerItems = null;
		// all these functions can throw an exception. My functions are at-most-once at all interfaces and will not retry on failure, they will simply throw an Exception.
		byte[] msg = SerializeInt.serialize(sellerId);
		byte[] buf = this.sendAndReceive(msg, funcId);
		sellerItems = Item.deserializeArray(buf);
		return sellerItems;
	}
	public Item[] getItemsInCategory(int category) throws IOException {
		int funcId = DBItemEnumV1.GET_ITEMS_IN_CATEGORY.ordinal();
		Item[] catItems = null;
		// all these functions can throw an exception. My functions are at-most-once at all interfaces and will not retry on failure, they will simply throw an Exception.
		byte[] msg = SerializeInt.serialize(category);
		byte[] buf = this.sendAndReceive(msg, funcId);
		catItems = Item.deserializeArray(buf);
		return catItems;
	}
}

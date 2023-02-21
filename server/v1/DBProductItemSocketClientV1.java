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
	public ItemId createItem(Item item) throws IllegalArgumentException {
		int funcId = DBItemEnumV1.CREATE_ITEM.ordinal();
		ItemId itemId = null;
		try {
			byte[] msg = Item.serialize(item);
			byte[] buf = this.sendAndReceive(msg, funcId);
			itemId = ItemId.deserialize(buf);
		}
		catch (IOException i) {
			System.out.println(i);
		}
		// TO-DO: Maybe throw an exception instead of returning a null itemId
		return itemId;
	}
	public Item getItemById(ItemId itemId) throws NoSuchElementException {
		int funcId = DBItemEnumV1.GET_ITEM_BY_ID.ordinal();
		Item item = null;
		try {
			byte[] msg = ItemId.serialize(itemId);
			byte[] buf = this.sendAndReceive(msg, funcId);
			item = Item.deserialize(buf);
		}
		catch (IOException i) {
			System.out.println(i);
		}
		// TO-DO: Maybe throw an exception instead of returning a null item
		return item;
	}
	public void changePrice(ItemId itemId, int sellerId, float newPrice) throws NoSuchElementException, IllegalArgumentException {
		int funcId = DBItemEnumV1.CHANGE_PRICE.ordinal();
		try {
			byte[] msg = SerializePriceArgDB.serialize(itemId, sellerId, newPrice);
			byte[] buf = this.sendAndReceive(msg, funcId);
		}
		catch (IOException i) {
			System.out.println(i);
		}
	}
	public Item[] getItemsBySeller(int sellerId) {
		int funcId = DBItemEnumV1.GET_ITEMS_BY_SELLER.ordinal();
		Item[] sellerItems = null;
		try {
			byte[] msg = SerializeInt.serialize(sellerId);
			byte[] buf = this.sendAndReceive(msg, funcId);
			sellerItems = Item.deserializeArray(buf);
		}
		catch (IOException i) {
			System.out.println(i);
		}
		return sellerItems;
	}
	// TO-DO: Figure out if I want to include sold out / removed items in the output
	public Item[] getItemsInCategory(int category) {
		int funcId = DBItemEnumV1.GET_ITEMS_BY_CATEGORY.ordinal();
		Item[] catItems = null;
		try {
			byte[] msg = SerializeInt.serialize(category);
			byte[] buf = this.sendAndReceive(msg, funcId);
			catItems = Item.deserializeArray(buf);
		}
		catch (IOException i) {
			System.out.println(i);
		}
		return catItems;
	}
}

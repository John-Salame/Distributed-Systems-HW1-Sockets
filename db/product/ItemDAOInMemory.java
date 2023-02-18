/**
 * Class ItemDAOInMemory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Provide access to the items database.
 */

package db.product;
import common.Item;
import common.ItemId;
import dao.ItemDAO;
import java.util.List;
import java.util.ArrayList;

public class ItemDAOInMemory implements ItemDAO {
	private int[] nextId = null;
	private static final int NUM_CATEGORIES = 10;
	private List<Item> items = null;

	// CONSTRUCTORS
	public ItemDAOInMemory() {
		if(this.nextId == null) {
			this.nextId = new int[NUM_CATEGORIES];
			for(int i = 0; i < NUM_CATEGORIES; i++) {
				this.nextId[i] = 1;
			}
		}
		if (this.items == null) {
			this.items = new ArrayList<Item>();
		}
	}

	/**
	 * Generate choose the next unique user id
	 * Currently not thread-safe.
	 */
	private int generateUniqueId(int category) {
		int currId = this.nextId[category];
		this.nextId[category]++;
		return currId;
	}
	private boolean itemExists(ItemId itemId) {
		return this.getItemById(itemId) != null;
	}
	// later on, I could turn this into an O(n) insertion sorted list. I hate that there is no binary search tree Collection built into Java.
	private void insertItem(Item item) {
		this.items.add(item);
	}

	// take in an item with an incomplete item id and update the id.
	public ItemId createItem(Item item) {
		int serial = item.getSerial();
		// if the serial number has already been initialized and the item id exists in the database, skip item creation and return the existing itemId
		// if the user already exists, return the id of the existing user
		if(serial != -1 && this.itemExists(item.getId())) {
			return item.getId();
		}
		// the item does not exist; give it a serial number
		serial = this.generateUniqueId(item.getCategory());
		item.setSerial(serial);
		this.insertItem(item);
		return item.getId();
	}
	public Item getItemById(ItemId itemId) {
		for (Item item : this.items) {
			if(item.getId().equals(itemId)) {
				return item;
			}
		}
		return null;
	}
	// use sellerId to verify that you are the correct seller to change the price
	public void changePrice(ItemId itemId, int sellerId, float newPrice) {
		Item item = this.getItemById(itemId);
		if(item != null) {
			if(item.getSellerId() == sellerId) {
				item.setPrice(newPrice);
			}
		}
	}
	public Item[] getItemsBySeller(int sellerId) {
		List<Item> sellerItems = new ArrayList<Item>();
		for (Item item : this.items) {
			if(item.getSellerId() == sellerId) {
				sellerItems.add(item);
			}
		}
		Item[] ret = new Item[0];
		ret = sellerItems.toArray(ret);
		return ret;
	}
	// TO-DO: Figure out if I want to include sold out / removed items in the output
	public Item[] getItemsInCategory(int category) {
		List<Item> catItems = new ArrayList<Item>();
		for (Item item : this.items) {
			if(item.getCategory() == category) {
				catItems.add(item);
			}
		}
		Item[] ret = new Item[0];
		ret = catItems.toArray(ret);
		return ret;
	}
}

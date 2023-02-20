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
import java.util.NoSuchElementException;

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
	private int generateUniqueId(int category) throws IllegalArgumentException {
		ItemId.validateCategory(category); // throw IllegalArgumentException
		int currId = this.nextId[category];
		this.nextId[category]++;
		return currId;
	}
	private boolean itemExists(ItemId itemId) {
		try {
			this.getItemById(itemId);
		}
		catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}
	// later on, I could turn this into an O(n) insertion sorted list. I hate that there is no binary search tree Collection built into Java.
	private void insertItem(Item item) {
		this.items.add(item);
	}

	// take in an item with an incomplete item id and update the id.
	// throw an IllegalArgumentException if the item already has a serial number and it's invalid
	// throw an IllegalArgumentException if the user tries to insert a null Item
	public ItemId createItem(Item item) throws IllegalArgumentException {
		if(item == null) {
			throw new IllegalArgumentException("Cannot insert null item");
		}
		int serial = item.getSerial();
		// if the serial number has already been initialized and the item id exists in the database, skip item creation and return the existing itemId
		if(serial > 0 && this.itemExists(item.getId())) {
			return item.getId();
		}
		// the item does not exist; give it a serial number
		else if(serial == -1) {
			serial = this.generateUniqueId(item.getCategory());
		}
		else { // invalid item ID
			throw new IllegalArgumentException("Cannot insert item with negative serial number");
		}
		item.setSerial(serial);
		this.insertItem(item); // not sure if this can throw an exception
		return item.getId();
	}
	public Item getItemById(ItemId itemId) throws NoSuchElementException  {
		if (itemId == null) {
			throw new NoSuchElementException("Cannot query for a null item");
		}
		for (Item item : this.items) {
			if(item.getId().equals(itemId)) {
				return item;
			}
		}
		throw new NoSuchElementException("Item with ID " + itemId.toString() + " does not exist in database"); // if not found, throw the exception
	}
	// use sellerId to verify that you are the correct seller to change the price
	public void changePrice(ItemId itemId, int sellerId, float newPrice) throws NoSuchElementException, IllegalArgumentException, UnsupportedOperationException {
		Item item = this.getItemById(itemId); // throws NoSuchElementException
		if(item.getSellerId() == sellerId) {
			item.setPrice(newPrice); // IllegalArgumentException
		}
		else {
			throw new UnsupportedOperationException("Wrong user trying to change the price");
		}
	}
	// can return an empty array
	public Item[] getItemsBySeller(int sellerId) {
		List<Item> sellerItems = new ArrayList<Item>();
		// validation should speed up obviously invalid inputs if database is large
		try {
			Item.validateSellerId(sellerId);
		} catch (IllegalArgumentException e) {
			return new Item[0];
		}
		// search
		for (Item item : this.items) {
			if(item.getSellerId() == sellerId) {
				sellerItems.add(item);
			}
		}
		Item[] ret = new Item[0];
		ret = sellerItems.toArray(ret);
		return ret;
	}
	// can return an empty array
	public Item[] getItemsInCategory(int category) {
		List<Item> catItems = new ArrayList<Item>();
		// validation should speed up obviously invalid inputs if database is large
		try {
			Item.validateCategory(category);
		} catch (IllegalArgumentException e) {
			return new Item[0];
		}
		// search
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

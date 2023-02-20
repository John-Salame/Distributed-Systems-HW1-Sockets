/**
 * Interface ItemDAO
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: An interface to access the database of items (in Product database).
 */

package dao;
import common.Item;
import common.ItemId;
import java.util.NoSuchElementException;

public interface ItemDAO {
	public abstract ItemId createItem(Item item) throws IllegalArgumentException; // take in an item with an incomplete item id and update the id.
	public abstract Item getItemById(ItemId itemId) throws NoSuchElementException;
	public abstract void changePrice(ItemId itemId, int sellerId, float newPrice) throws NoSuchElementException, IllegalArgumentException, UnsupportedOperationException; // use sellerId to verify that you are the correct seller to change the price
	public abstract Item[] getItemsBySeller(int sellerId);
	public abstract Item[] getItemsInCategory(int category); // TO-DO: Figure out if I want to include sold out / removed items in the output
}

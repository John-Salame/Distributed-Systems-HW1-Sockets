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
import java.io.IOException;
import java.util.NoSuchElementException;

public interface ItemDAO {
	public abstract ItemId createItem(Item item) throws IOException, IllegalArgumentException; // take in an item with an incomplete item id and update the id.
	public abstract Item getItemById(ItemId itemId) throws IOException, NoSuchElementException;
	public abstract void changePrice(ItemId itemId, int sellerId, float newPrice) throws IOException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException; // use sellerId to verify that you are the correct seller to change the price
	public abstract Item[] getItemsBySeller(int sellerId) throws IOException;
	public abstract Item[] getItemsInCategory(int category) throws IOException;
	public abstract void closeConnection() throws IOException;
}

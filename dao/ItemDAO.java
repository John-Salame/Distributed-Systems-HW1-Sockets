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

public interface ItemDAO {
	public abstract Item[] getItemsInCategory(int category);
}

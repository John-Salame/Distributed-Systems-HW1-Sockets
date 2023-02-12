/**
 * Interface SaleListingDAO
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: An interface to access the sales listings (in Product database).
 */

package.dao;
import common.ItemId;
import common.SaleListing;

public interface SaleListingDAO {
	// choose a sale matching these requirements and remove it from sale; return true if you remove an item from sale.
	public abstract boolean removeItemFromSale(ItemId itemId, int quantity);
}
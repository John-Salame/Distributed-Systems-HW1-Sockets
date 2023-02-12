/**
 * Interface SaleListingDAO
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: An interface to access the sales listings (in Product database).
 */

package dao;
import common.ItemId;
import common.SaleListing;

public interface SaleListingDAO {
	// NOTE: sellerId is used for authentication purposes; assume that we have already authenticated the seller's session token on the client-facing API
	public abstract void putItemOnSale(int sellerId, ItemId itemId, int quantity);
	// choose a sale matching these requirements and remove it from sale; return true if you remove an item from sale.
	public abstract boolean removeItemFromSale(int sellerId, ItemId itemId, int quantity);
}
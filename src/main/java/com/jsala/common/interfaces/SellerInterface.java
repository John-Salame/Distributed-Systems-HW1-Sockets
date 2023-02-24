/**
 * Interface SellerInterface
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Seller interface common to client and server
 */

package com.jsala.common.interfaces;
import com.jsala.common.Item;
import com.jsala.common.ItemId;
import com.jsala.common.SaleListingId;
import java.util.NoSuchElementException;
import java.io.IOException;

public interface SellerInterface extends CommonUserInterface {
	// Inherited Methods
	//   Implement methods from CommonUserInterface
	// New Methods
	public abstract SaleListingId putOnSale(String sessionToken, Item item, int quantity) throws IOException, IllegalArgumentException;
	public abstract void changePriceOfItem(String sessionToken, ItemId itemId, float newPrice) throws IOException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException;
	/*
	public abstract void removeItemFromSale(String sessionToken, ItemId itemId, int quantity); // TO-DO: Figure out how to make this deterministic
	public abstract String displayItemsOnSale(String sessionToken);
	*/
}

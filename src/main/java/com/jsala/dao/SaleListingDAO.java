/**
 * Interface SaleListingDAO
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: An interface to access the sales listings (in Product database).
 */

package com.jsala.dao;
import com.jsala.common.ItemId;
import com.jsala.common.SaleListing;
import java.util.NoSuchElementException;

public interface SaleListingDAO {
	// NOTE: sellerId is used for authentication purposes; assume that we have already authenticated the seller's session token on the client-facing API
	public abstract void putItemOnSale(int sellerId, ItemId itemId, int quantity) throws IllegalArgumentException;
	// choose a sale matching these requirements and remove it from sale
	public abstract void removeItemFromSale(int sellerId, ItemId itemId, int quantity) throws NoSuchElementException;
}
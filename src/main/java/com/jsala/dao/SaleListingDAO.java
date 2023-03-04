/**
 * Interface SaleListingDAO
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: An interface to access the sales listings (in Product database).
 */

package com.jsala.dao;
import com.jsala.common.DetailedSaleListing;
import com.jsala.common.ItemId;
import com.jsala.common.SaleListing;
import com.jsala.common.SaleListingId;

import java.io.IOException;
import java.util.NoSuchElementException;

public interface SaleListingDAO {
	// NOTE: sellerId is used for authentication purposes; assume that we have already authenticated the seller's session token on the client-facing API
	public abstract SaleListingId putItemOnSale(int sellerId, ItemId itemId, int quantity) throws IOException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException;
	// choose a sale matching these requirements and remove it from sale
	public abstract void removeItemFromSale(int sellerId, ItemId itemId, int quantity) throws IOException, NoSuchElementException, UnsupportedOperationException;
	// can return an empty array
	public abstract SaleListing[] getSaleListingsBySeller(int sellerId) throws IOException;
	public abstract DetailedSaleListing[] getDetailedSaleListingsBySeller(int sellerId) throws IOException;
	public abstract void closeConnection() throws IOException;
}
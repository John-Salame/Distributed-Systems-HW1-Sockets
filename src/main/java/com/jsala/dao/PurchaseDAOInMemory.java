/**
 * Class PurchaseDAOInMemory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: A server-local implementation of PurchaseDAO which does not use a database.
 */

package com.jsala.dao;
import com.jsala.common.Purchase;
import java.util.List;
import java.util.ArrayList;

public class PurchaseDAOInMemory implements PurchaseDAO {
	private List<Purchase> purchases;

	public PurchaseDAOInMemory() {
		purchases = new ArrayList<Purchase>();
	}

	@Override
	public void addPurchase(Purchase purchase) {
		purchases.add(purchase);
	}
	@Override
	public void addPurchase(int buyerId, int itemId, int sellerId, int quantity) {
		// TO-DO: Implement this later!
		return;
	}
	@Override
	public Purchase[] getAllPurchasesByBuyer(int buyerId) {
		// TO-DO: Implement this later!
		return new Purchase[0];
	}
}

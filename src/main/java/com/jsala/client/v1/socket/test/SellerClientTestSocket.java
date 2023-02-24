/**
 * Class SellerClientTestSocket
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Tries various seller API functions, hardcoded, in order to test them out.
 * This should work for testing a single client at a time.
 */

package com.jsala.client.v1.socket.test;
import com.jsala.client.v1.*;
import com.jsala.client.v1.socket.SellerSocketClientV1;
import com.jsala.common.interfaces.SellerInterface;
import com.jsala.common.Seller;
import com.jsala.common.Item;
import com.jsala.common.ItemId;
import com.jsala.common.SaleListing;
import com.jsala.common.SaleListingId;
import java.io.IOException;

 public class SellerClientTestSocket {
	public static void main(String[] args) throws IOException {
		SellerInterface transport = new SellerSocketClientV1("localhost", 8200);
		SellerInterface sellerInterface = new SellerInterfaceClientImplV1(transport);

		// Basic seller API calls
		String seller1Username = "John";
		String seller1Password = "password321";
		int seller1Id = sellerInterface.createUser(seller1Username, seller1Password);
		System.out.println("Seller 1 id: " + seller1Id);
		String seller1SessionToken = sellerInterface.login(seller1Username, seller1Password);
		System.out.println("Seller 1 session token = " + seller1SessionToken);
		System.out.println("Seller 0 feedback:");
		int[] feedback = sellerInterface.getSellerRating(1); // change this once I have the capacity to add feedback via the client-side API
		System.out.println(Seller.displayFeedback(feedback));
		System.out.println("Seller 1 logging out");
		sellerInterface.logout(seller1SessionToken);

		/*
		// Create an item
		String[] keywords = {"Food", "Fruit"};
		ItemId itemId = new ItemId(0, 1);
		Item apple = new Item("apple", itemId, keywords, "New", (float) 0.79, seller1Id);
		System.out.println(apple);

		// Sell the item
		SaleListing sale = new SaleListing(new SaleListingId(itemId, 5));
		System.out.println(sale);
		System.out.println("Decreasing quantity by 2");
		sale.decrementQuantity(2);
		System.out.println("Removing sale listing");
		sale.removeListing();
		System.out.println(sale);
		try {
			sale.decrementQuantity(2);
		} catch(IllegalStateException e) {
			System.out.println("Correctly threw exception from trying to buy from removed listing");
		}
		SaleListing sale2 = new SaleListing(itemId, 5);
		System.out.println("\nBuying out all of sale2");
		sale2.decrementQuantity(3);
		int numSold = sale2.decrementQuantity(3);
		assert numSold == 2;
		System.out.println(sale2);

		// Test logging out
		System.out.println("Listing sessions");
		System.out.println(sessionDao.listSessions());
		System.out.println("Buyer 1 logging out");
		buyerInterface.logout(buyer1SessionToken);
		System.out.println(sessionDao.listSessions());
		System.out.println("Seller 1 logging out");
		sellerInterface.logout(seller1SessionToken);
		System.out.println(sessionDao.listSessions());
		*/
	}
 }

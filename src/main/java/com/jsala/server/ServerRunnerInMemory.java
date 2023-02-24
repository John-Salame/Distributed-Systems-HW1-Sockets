/**
 * Class ServerRunnerInMemory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Test the business logic using in-memory databases. Skip the client-server interaction.
 */

package com.jsala.server;
import com.jsala.common.interfaces.BuyerInterface;
import com.jsala.common.interfaces.SellerInterface;
import com.jsala.common.Buyer;
import com.jsala.common.Item;
import com.jsala.common.ItemId;
import com.jsala.common.SaleListing;
import com.jsala.common.SaleListingId;
import com.jsala.common.Seller;
import com.jsala.dao.*;
import com.jsala.dao.factory.*;
import com.jsala.db.customer.BuyerDAOInMemory;
import com.jsala.db.customer.CustomerDAOFactoryInMemory;
import com.jsala.db.customer.SellerDAOInMemory;
import com.jsala.db.customer.SessionDAOInMemory;
import com.jsala.db.product.ItemDAOInMemory;
import com.jsala.db.product.ProductDAOFactoryInMemory;
import com.jsala.server.v1.*;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.io.IOException;

 public class ServerRunnerInMemory {
	// NOTE: This file no longer works after adding the factories; the DAOs no longer relate to the same object between buyer and seller interfaces
	// Note: The BuyerInterface and sellerInterface will never throw IOException, but some edge cases might, such as serialization.
	public static void main(String[] args) throws IOException {
		System.out.println("Starting program");
		CustomerDAOFactory custDaoFact = new CustomerDAOFactoryInMemory();
		CustomerDAOFactory buyerDaoFactory = custDaoFact;
		CustomerDAOFactory sellerDaoFactory = custDaoFact;
		CustomerDAOFactory sessionDaoFactory = custDaoFact;
		ProductDAOFactory prodDaoFact = new ProductDAOFactoryInMemory();
		ProductDAOFactory itemDaoFactory = prodDaoFact;
		BuyerDAO buyerDao = buyerDaoFactory.createBuyerDao();
		SellerDAO sellerDao = sellerDaoFactory.createSellerDao();
		SessionDAO sessionDao = sessionDaoFactory.createSessionDao();
		ItemDAO itemDao = itemDaoFactory.createItemDao();
		System.out.println("Done creating DAOs");
		BuyerInterface buyerInterface = new BuyerInterfaceServerImplV1(buyerDaoFactory, sellerDaoFactory, sessionDaoFactory, itemDaoFactory);
		SellerInterface sellerInterface = new SellerInterfaceServerImplV1(sellerDaoFactory, sessionDaoFactory, itemDaoFactory);

		// Basic buyer API calls
		System.out.println("\n\nTesting Buyer edge cases");
		Buyer testBuyer;
		try {
			testBuyer = new Buyer();
			testBuyer.setName(null);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			testBuyer = new Buyer();
			testBuyer.setName("This is a very long name and should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			testBuyer = new Buyer();
			testBuyer.setPassword("");
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			testBuyer = new Buyer();
			testBuyer.setPassword(null);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			testBuyer = new Buyer();
			testBuyer.setId(0);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		// demonstrate that null does not interfere with the login credential verification and it will return false
		testBuyer = new Buyer();
		System.out.println("Buyer with no username and password should not match null username and password");
		System.out.println("Does it match? " + testBuyer.isMyLoginCredentials(null, null));
		// end of demonstration
		// BuyerDAO tests
		System.out.println("Testing BuyerDAO edge cases");
		try {
			testBuyer = new Buyer();
			buyerDao.createUser(null, "pass"); // make sure it throws NoSuchElementException
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			buyerDao.createUser("Test", null); // make sure it throws NoSuchElementException
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			buyerDao.createUser("This is a very long name and should throw an IllegalArgumentException", "pass"); // make sure it throws NoSuchElementException
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			buyerDao.getUserId(null, null); // make sure it throws NoSuchElementException
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		try {
			buyerDao.getUserId("Test", "pass"); // does not exist, so it should throw an exception
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		try {
			buyerDao.getBuyerById(-1); // does not exist, so it should throw an exception
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		try {
			buyerDao.getBuyerById(2); // does not exist, so it should throw an exception
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		System.out.println("Testing BuyerInterface edge cases");
		try {
			buyerInterface.login("John", "testing");
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		try {
			buyerInterface.logout("token");
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		try {
			buyerInterface.getSellerRating(2);
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		System.out.println("Done testing Buyer, BuyerDAO, and BuyerInterface edge cases");
		String buyer1Username = "Joe";
		String buyer1Password = "password123";
		int buyer1Id = buyerInterface.createUser(buyer1Username, buyer1Password);
		assert buyer1Id == buyerDao.getUserId(buyer1Username, buyer1Password);
		String buyer1SessionToken = buyerInterface.login(buyer1Username, buyer1Password);
		Buyer buyer1 = buyerDao.getBuyerById(buyer1Id);
		System.out.println("Printing buyer 1");
		System.out.println(buyer1);




		System.out.println("\n\nTesting Seller functions");
		Seller testSeller;
		try {
			testSeller = new Seller();
			testSeller.setName(null);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			testSeller = new Seller();
			testSeller.setName("This is a very long name and should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			testSeller = new Seller();
			testSeller.setPassword("");
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			testSeller = new Seller();
			testSeller.setPassword(null);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			testSeller = new Seller();
			testSeller.setId(0);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		System.out.println("Testing SellerDAO edge cases");
		try {
			sellerDao.createUser("", "password");
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			sellerDao.createUser("Test", null);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			sellerDao.getUserId("Test", null);
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		try {
			sellerDao.getUserId("", "password");
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		try {
			sellerDao.getUserId("test", "password"); // does not exist in database
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		try {
			sellerDao.getSellerById(-1);
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		try {
			sellerDao.getSellerById(2);
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		try {
			sellerDao.commitSeller(null);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		System.out.println("Testing SellerInterface edge cases");
		try {
			sellerInterface.login("John", "testing");
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		try {
			sellerInterface.logout("token");
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		try {
			sellerInterface.getSellerRating(2);
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		System.out.println("Done testing Seller, SellerDAO, and SellerInterface edge cases");
		String seller1Username = "John";
		String seller1Password = "password321";
		// do this until I start working on the seller interface
		int seller1Id = sellerInterface.createUser(seller1Username, seller1Password);
		assert seller1Id == sellerDao.getUserId(seller1Username, seller1Password);
		String seller1SessionToken = sellerInterface.login(seller1Username, seller1Password);
		Seller seller1 = sellerDao.getSellerById(seller1Id);
		System.out.println("Printing seller 1");
		System.out.println(seller1);
		int[] feedback = buyerInterface.getSellerRating(seller1Id);
		System.out.println(Seller.displayFeedback(feedback));



		// Create an item
		System.out.println("\n\nTesting Item functions");
		// First, try edge cases
		Item testItem;
		try {
			testItem = new Item();
			testItem.setId(1, 1);
			testItem.setCategory(2); // throw IllegalStateException
		} catch (IllegalStateException e) {
			System.out.println(e);
		}
		try {
			testItem = new Item();
			testItem.setId(1, 1);
			testItem.setSerial(2); // throw IllegalStateException
		} catch (IllegalStateException e) {
			System.out.println(e);
		}
		// test that an incomplete item sent over the network can deserialize without throwing an exception
		System.out.println("Testing serializing and deserializing an item");
		testItem = new Item();
		testItem.setCategory(2);
		byte[] serItem = Item.serialize(testItem);
		Item deserItem = Item.deserialize(serItem);
		System.out.println("Deserialized item:");
		System.out.println(deserItem);
		System.out.println("Make sure the deserialized item fails regular validation");
		try {
			Item.validateItem(deserItem);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		System.out.println("Testing ItemDAO edge cases");
		try {
			itemDao.createItem(null);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			testItem = new Item();
			testItem.setId(1, -2); // negative serial number
			itemDao.createItem(testItem);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			testItem = new Item();
			testItem.setId(1, 2); // this item does not exist in the database but its serial is already set; throw error
			itemDao.createItem(testItem);
		}
		catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			testItem = new Item();
			testItem.setId(10, 2); // illegal category
			itemDao.createItem(testItem);
		}
		catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			testItem = new Item();
			testItem.setId(-1, 2); // illegal category
			itemDao.createItem(testItem);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		try {
			itemDao.getItemById(null);
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		try {
			itemDao.getItemById(new ItemId(0, 2)); // this item id doesn't exist in database yet
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		try {
			itemDao.changePrice(null, 1, (float) 1.00); // should fail due to null itemId
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
		Item[] itemsBySeller = itemDao.getItemsBySeller(1);
		assert itemsBySeller.length == 0;
		Item[] itemsByCategory = itemDao.getItemsInCategory(-1);
		assert itemsByCategory.length == 0;
		System.out.println("Done testing Item, ItemDAO, and ItemInterface edge cases");

		System.out.println("\nCreating first item: apple");
		int seller2Id = seller1Id + 1;
		String[] keywords = {"Food", "Fruit", "Crunchy"};
		// ItemId itemId = new ItemId(0, 1);
		// Item apple = new Item("apple", itemId, keywords, "New", (float) 0.79, seller1Id);
		Item appleFirst = new Item("apple", 0, keywords, "New", (float) 0.79);
		appleFirst.setSellerId(seller1Id);
		ItemId itemId = itemDao.createItem(appleFirst);
		Item apple = itemDao.getItemById(itemId);
		System.out.println(apple);
		System.out.println("\n\nChanging price of apple to 0.99\n");
		itemDao.changePrice(itemId, seller1Id, (float) 0.99);
		try {
			itemDao.changePrice(itemId, seller2Id, (float) 1.22); // this one should fail
		} catch (UnsupportedOperationException e) {
			System.out.println("Correctly caught exception changing price using wrong user: " + e);
		}
		System.out.println(apple);
		System.out.println("\n\nChanging price of apple to 0.60 using sellerInterface:\n");
		sellerInterface.changePriceOfItem(buyer1SessionToken, itemId, (float) 0.60);
		try {
			sellerInterface.changePriceOfItem(null, itemId, (float) 0.60); // this one should fail
		} catch (NoSuchElementException e) {
			System.out.println("Correctly caught exception using bad token: " + e);
		}
		// Test functions to get items by category and get items by seller
		Item pear = new Item("pear", 0, new String[] {"Food", "Fruit", "Soft", "Sour", "Sweet"}, "New", (float) 1.23);
		pear.setSellerId(seller2Id);
		itemDao.createItem(pear);
		Item chair = new Item("chair", 1, new String[] {"Household", "Furniture"}, "Used", (float) 5.29);
		chair.setSellerId(seller1Id);
		itemDao.createItem(chair);
		System.out.println("\n\nItems by seller1:");
		System.out.println(Arrays.toString(itemDao.getItemsBySeller(seller1Id)));
		System.out.println("\n\nItems in category 0 (food):");
		System.out.println(Arrays.toString(itemDao.getItemsInCategory(0)));

		// Sell the item
		System.out.println("\n\n");
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

		// Search for fruits
		Item[] searchResults = buyerInterface.searchItem(buyer1SessionToken, 0, new String[] {"fruit"});
		System.out.println("\nSearch results for fruit:");
		System.out.println(Arrays.toString(searchResults));
		searchResults = buyerInterface.searchItem(buyer1SessionToken, 0, new String[] {"fruit", "sweet"});
		System.out.println("\nSearch results for sweet fruit:");
		System.out.println(Arrays.toString(searchResults));

		// Test logging out
		System.out.println("Listing sessions");
		System.out.println(sessionDao.listSessions());
		System.out.println("Buyer 1 logging out");
		buyerInterface.logout(buyer1SessionToken);
		System.out.println(sessionDao.listSessions());
		System.out.println("Seller 1 logging out");
		sellerInterface.logout(seller1SessionToken);
		System.out.println(sessionDao.listSessions());
	}
 }

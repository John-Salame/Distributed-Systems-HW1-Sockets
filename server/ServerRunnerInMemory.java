/**
 * Class ServerRunnerInMemory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Test the business logic using in-memory databases. Skip the client-server interaction.
 */

package server;
import dao.*;
import server.v1.*;
import common.Buyer;
import common.BuyerInterface;
import common.Seller;
import common.SellerInterface;
import common.Item;
import common.ItemId;
import common.SaleListing;
import common.SaleListingId;
import db.customer.BuyerDAOInMemory;
import db.customer.SellerDAOInMemory;
import db.customer.SessionDAOInMemory;
import db.product.ItemDAOInMemory;
import java.util.Arrays;

 public class ServerRunnerInMemory {
	public static void main(String[] args) {
		System.out.println("Starting program");
		BuyerDAO buyerDao = new BuyerDAOInMemory();
		SellerDAO sellerDao = new SellerDAOInMemory();
		SessionDAO sessionDao = new SessionDAOInMemory();
		ItemDAO itemDao = new ItemDAOInMemory();
		System.out.println("Done creating DAOs");
		BuyerInterface buyerInterface = new BuyerInterfaceServerV1(buyerDao, sellerDao, sessionDao, itemDao);
		SellerInterface sellerInterface = new SellerInterfaceServerV1(sellerDao, sessionDao, itemDao);

		// Basic buyer API calls
		String buyer1Username = "Joe";
		String buyer1Password = "password123";
		int buyer1Id = buyerInterface.createUser(buyer1Username, buyer1Password);
		assert buyer1Id == buyerDao.getUserId(buyer1Username, buyer1Password);
		String buyer1SessionToken = buyerInterface.login(buyer1Username, buyer1Password);
		Buyer buyer1 = buyerDao.getBuyerById(buyer1Id);
		System.out.println("Printing buyer 1");
		System.out.println(buyer1);

		// Basic seller API calls
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
		itemDao.changePrice(itemId, seller2Id, (float) 1.22); // this one should fail
		System.out.println(apple);
		// Test functions to get items by category and get items by seller
		Item pear = new Item("pear", 0, new String[] {"Food", "Fruit", "Sour", "Sweet"}, "New", (float) 1.23);
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

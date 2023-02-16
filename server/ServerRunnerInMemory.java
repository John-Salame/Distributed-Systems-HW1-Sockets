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

 public class ServerRunnerInMemory {
	public static void main(String[] args) {
		BuyerDAO buyerDao = new BuyerDAOInMemory();
		SellerDAO sellerDao = new SellerDAOInMemory();
		SessionDAO sessionDao = new SessionDAOInMemory();
		ItemDAO itemDao = null;
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
	}
 }

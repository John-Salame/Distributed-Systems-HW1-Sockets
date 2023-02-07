/**
 * Class ServerRunnerInMemory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Test the business logic using in-memory databases
 */

package server;
import dao.*;
import common.Buyer;
import common.BuyerInterface;
import common.Seller;

 public class ServerRunnerInMemory {
	public static void main(String[] args) {
		BuyerDAO buyerDao = new BuyerDAOInMemory();
		SellerDAO sellerDao = new SellerDAOInMemory();
		SessionDAO sessionDao = new SessionDAOInMemory();
		ItemDAO itemDao = null;
		BuyerInterface buyerInterface = new BuyerInterfaceServer(buyerDao, sellerDao, sessionDao, itemDao);

		String buyer1Username = "Joe";
		String buyer1Password = "password123";
		int buyer1Id = buyerInterface.createUser(buyer1Username, buyer1Password);
		assert buyer1Id == buyerDao.getUserId(buyer1Username, buyer1Password);
		String buyer1SessionToken = buyerInterface.login(buyer1Username, buyer1Password);
		Buyer buyer1 = buyerDao.getBuyerById(buyer1Id);
		System.out.println("Printing buyer 1");
		System.out.println(buyer1);

		String seller1Username = "John";
		String seller1Password = "password321";
		// do this until I start working on the seller interface
		int seller1Id = sellerDao.createUser(seller1Username, seller1Password);
		assert seller1Id == sellerDao.getUserId(seller1Username, seller1Password);
		Seller seller1 = sellerDao.getSellerById(seller1Id);
		System.out.println("Printing seller 1");
		System.out.println(seller1);
		System.out.println(buyerInterface.getSellerRating(seller1Id));

		System.out.println("Listing sessions");
		System.out.println(sessionDao.listSessions());
		System.out.println("Buyer 1 logging out");
		buyerInterface.logout(buyer1SessionToken);
		System.out.println(sessionDao.listSessions());
	}
 }

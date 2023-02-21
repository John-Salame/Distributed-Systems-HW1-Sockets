/**
 * Class SellerRunnerServer
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Test the seller interface by setting up the server-side transport layer (sockets)
 */

package server;
import server.v1.*;
import dao.*;
import common.Seller;
import common.SellerInterface;
import common.Item;
import common.ItemId;
import java.io.IOException;
import java.net.SocketException;

public class SellerRunnerServer {
	public static void main(String[] args) throws IOException {
		int serverPort = 8200;
		int maxConnections = 1;
		String customerDBHost = "localhost";
		int customerDBIp = 8300;
		String productDBHost = "localhost";
		int productDBIp = 8400;
		// customer database
		SellerDAO sellerDao = new DBCustomerSellerSocketClientV1(customerDBHost, customerDBIp);
		SessionDAO sessionDao = new DBCustomerSessionSocketClientV1(customerDBHost, customerDBIp);
		// product database
		ItemDAO itemDao = new DBProductItemSocketClientV1(productDBHost, productDBIp);
		SellerInterface sellerInterface = new SellerInterfaceServerV1(sellerDao, sessionDao, itemDao);

		// sample seller
		String seller1Username = "John";
		String seller1Password = "password321";
		int seller1Id = sellerInterface.createUser(seller1Username, seller1Password);
		assert seller1Id == 1;
		String sessionToken = sessionDao.createSession(seller1Id);
		// sample items
		int seller2Id = 2;
		String[] keywords = {"Food", "Fruit", "Crunchy"};
		ItemId appleItemId = null;
		try {
			Item apple = new Item("apple", 0, keywords, "New", (float) 0.79);
			apple.setSellerId(seller1Id);
			appleItemId = itemDao.createItem(apple);
			apple = itemDao.getItemById(appleItemId);
			System.out.println(apple);
			Item pear = new Item("pear", 0, new String[] {"Food", "Fruit", "Soft", "Sour", "Sweet"}, "New", (float) 1.23);
			pear.setSellerId(seller2Id);
			itemDao.createItem(pear);
			Item chair = new Item("chair", 1, new String[] {"Household", "Furniture"}, "Used", (float) 5.29);
			chair.setSellerId(seller1Id);
			itemDao.createItem(chair);
		} catch (IOException e) {
			System.out.println(e);
		}

		try {
			sellerInterface.changePriceOfItem(sessionToken, appleItemId, (float) 1.12);
		} catch (Exception e) {
			System.out.println(e);
		}

		SellerSocketServerListenerV1 server = new SellerSocketServerListenerV1(sellerInterface);
		server.startServer(serverPort, maxConnections);
	}
}

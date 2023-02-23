/**
 * Class SellerServerTestSocket
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Test the seller interface by setting up the server-side transport layer (sockets)
 * This should work for testing a single client at a time.
 */

package server.v1.socket.test;
import common.interfaces.factory.UserInterfaceFactory;
import common.interfaces.SellerInterface;
import common.Seller;
import common.Item;
import common.ItemId;
import dao.*;
import dao.factory.*;
import server.v1.*;
import server.v1.factory.*;
import server.v1.socket.*;
import java.io.IOException;
import java.net.SocketException;
import java.lang.reflect.InvocationTargetException;

public class SellerServerTestSocket {
	public static void main(String[] args) throws IOException {
		int serverPort = 8200;
		int maxConnections = 100;
		String customerDBHost = "localhost";
		int customerDBIp = 8300;
		String productDBHost = "localhost";
		int productDBIp = 8400;
		

		try {
			// Temporary solutions to allow the server to access the databases without using buyer or seller interfaces. This creates many extra sockets.
			// customer database
			CustomerDAOFactory custDaoFact = new DBCustomerDAOFactorySocketV1(customerDBHost, customerDBIp);
			CustomerDAOFactory sellerDaoFactory = custDaoFact;
			CustomerDAOFactory sessionDaoFactory = custDaoFact;
			// product database
			ProductDAOFactory prodDaoFact = new DBProductDAOFactorySocketV1(productDBHost, productDBIp);
			ProductDAOFactory itemDaoFactory = prodDaoFact;


			SellerDAO sellerDao = sellerDaoFactory.createSellerDao();
			SessionDAO sessionDao = sessionDaoFactory.createSessionDao();
			// product database
			ItemDAO itemDao = itemDaoFactory.createItemDao();
			SellerInterface sellerInterface = new SellerInterfaceServerImplV1(sellerDaoFactory, sessionDaoFactory, itemDaoFactory);
			// Note: These DAOs and SellerInterface will run detached from the rest of the server, unable to communicate with the client. They simply back-door to the databases with extra sockets.
		

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
		} catch (InvocationTargetException e) {
			System.out.println("Error running sample creations from seller server: " + e);
		}

		UserInterfaceFactory dbClientProgramFactory = new DBClientProgramFactorySocketV1(customerDBHost, customerDBIp, productDBHost, productDBIp);
		SellerSocketServerListenerV1 server = new SellerSocketServerListenerV1(dbClientProgramFactory);
		server.startServer(serverPort, maxConnections);
	}
}

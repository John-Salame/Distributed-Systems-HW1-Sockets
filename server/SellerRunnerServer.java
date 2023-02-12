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
import common.SellerInterface;

public class SellerRunnerServer {
	public static void main(String[] args) {
		SellerDAO sellerDao = new SellerDAOInMemory();
		SessionDAO sessionDao = new SessionDAOInMemory();
		ItemDAO itemDao = null;
		SellerInterface sellerInterface = new SellerInterfaceServerV1(sellerDao, sessionDao, itemDao);
		// make a sample user
		sellerDao.createUser("Mark", "hamSandwich");
		sellerDao.getSellerById(1).addThumbsUp(); // give Mark a positive review
		SellerSocketServerListenerV1 server = new SellerSocketServerListenerV1(sellerInterface);
		int port = 8200;
		int maxConnections = 1;
		server.startServer(port, maxConnections);
	}
}

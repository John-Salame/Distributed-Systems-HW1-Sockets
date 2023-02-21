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
import java.io.IOException;
import java.net.SocketException;

public class SellerRunnerServer {
	public static void main(String[] args) throws IOException {
		String customerDBHost = "localhost";
		int customerDBIp = 8300;
		SellerDAO sellerDao = new DBCustomerSellerSocketClientV1(customerDBHost, customerDBIp);
		SessionDAO sessionDao = new DBCustomerSessionSocketClientV1(customerDBHost, customerDBIp);
		ItemDAO itemDao = null;
		SellerInterface sellerInterface = new SellerInterfaceServerV1(sellerDao, sessionDao, itemDao);
		// make a sample user
		System.out.println("Remove this SellerDAO direct call later");
		sellerDao.createUser("Mark", "hamSandwich");
		Seller mark = sellerDao.getSellerById(1); // give Mark a positive review
		mark.addThumbsUp();
		sellerDao.commitSeller(mark);
		SellerSocketServerListenerV1 server = new SellerSocketServerListenerV1(sellerInterface);
		int port = 8200;
		int maxConnections = 1;
		server.startServer(port, maxConnections);
	}
}

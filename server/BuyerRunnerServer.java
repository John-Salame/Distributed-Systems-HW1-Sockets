/**
 * Class BuyerRunnerServer
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Test the buyer interface by setting up the server-side transport layer (sockets)
 */

package server;
import server.v1.*;
import dao.*;
import common.BuyerInterface;
import java.net.SocketException;

public class BuyerRunnerServer {
	public static void main(String[] args) throws SocketException {
		// connnections to the customer database
		String customerDBHost = "localhost";
		int customerDBIp = 8300;
		BuyerDAO buyerDao = new DBCustomerBuyerSocketClientV1(customerDBHost, customerDBIp);
		SellerDAO sellerDao = new DBCustomerSellerSocketClientV1(customerDBHost, customerDBIp);
		SessionDAO sessionDao = new DBCustomerSessionSocketClientV1(customerDBHost, customerDBIp);
		// other database connections
		ItemDAO itemDao = null;
		BuyerInterface buyerInterface = new BuyerInterfaceServerV1(buyerDao, sellerDao, sessionDao, itemDao);
		BuyerSocketServerListenerV1 server = new BuyerSocketServerListenerV1(buyerInterface);
		int port = 8100;
		int maxConnections = 100;
		server.startServer(port, maxConnections);
	}
}

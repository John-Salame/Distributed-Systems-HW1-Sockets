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
		int serverPort = 8100;
		int maxConnections = 100;
		String customerDBHost = "localhost";
		int customerDBIp = 8300;
		String productDBHost = "localhost";
		int productDBIp = 8400;
		// customer database
		BuyerDAO buyerDao = new DBCustomerBuyerSocketClientV1(customerDBHost, customerDBIp);
		SellerDAO sellerDao = new DBCustomerSellerSocketClientV1(customerDBHost, customerDBIp);
		SessionDAO sessionDao = new DBCustomerSessionSocketClientV1(customerDBHost, customerDBIp);
		// product database
		ItemDAO itemDao = new DBProductItemSocketClientV1(productDBHost, productDBIp);

		BuyerInterface buyerInterface = new BuyerInterfaceServerV1(buyerDao, sellerDao, sessionDao, itemDao);
		BuyerSocketServerListenerV1 server = new BuyerSocketServerListenerV1(buyerInterface);
		server.startServer(serverPort, maxConnections);
	}
}

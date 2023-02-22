/**
 * Class BuyerServerTestSocket
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Test the buyer interface by setting up the server-side transport layer (sockets)
 */

package server.v1.socket;
import server.v1.*;
import server.v1.factory.*;
import dao.*;
import dao.factory.*;
import common.BuyerInterface;
import java.net.SocketException;

public class BuyerServerTestSocket {
	public static void main(String[] args) /*throws SocketException*/ {
		int serverPort = 8100;
		int maxConnections = 100;
		String customerDBHost = "localhost";
		int customerDBIp = 8300;
		String productDBHost = "localhost";
		int productDBIp = 8400;
		// customer database
		CustomerDAOFactory custDaoFact = new DBCustomerDAOFactorySocketV1(customerDBHost, customerDBIp);
		CustomerDAOFactory buyerDaoFactory = custDaoFact;
		CustomerDAOFactory sellerDaoFactory = custDaoFact;
		CustomerDAOFactory sessionDaoFactory = custDaoFact;
		// product database
		ProductDAOFactory prodDaoFact = new DBProductDAOFactorySocketV1(productDBHost, productDBIp);
		ProductDAOFactory itemDaoFactory = prodDaoFact;

		BuyerInterface buyerInterface = new BuyerInterfaceServerV1(buyerDaoFactory, sellerDaoFactory, sessionDaoFactory, itemDaoFactory); // this is the pain point. Each thread shares the same dao objects. I need to replace these with factories.
		BuyerSocketServerListenerV1 server = new BuyerSocketServerListenerV1(buyerInterface);
		server.startServer(serverPort, maxConnections);
	}
}

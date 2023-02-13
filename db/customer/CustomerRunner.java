/**
 * Class CustomerRunner
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Set up the Customer database and IPC (currently sockets)
 */

package db.customer;
import db.customer.v1.*;
import dao.SellerDAO;

public class CustomerRunner {
	public static void main(String[] args) {
		SellerDAO sellerDaoV1 = new SellerDAOInMemory();
		DBCustomerSocketServerListenerV1 server = new DBCustomerSocketServerListenerV1(sellerDaoV1);
		int port = 8300;
		int maxConnections = 1;
		server.startServer(port, maxConnections);
	}
}

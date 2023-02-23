/**
 * Class DBCustomerRunner
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Set up the Customer database and IPC (currently sockets)
 */

package com.jsala.db.customer;
import com.jsala.db.customer.v1.*;
import com.jsala.dao.BuyerDAO;
import com.jsala.dao.SellerDAO;
import com.jsala.dao.SessionDAO;
import java.net.SocketException;

public class DBCustomerRunner {
	public static void main(String[] args) throws SocketException {
		int port = 8300;
		int maxConnections = 10;
		SellerDAO sellerDaoV1 = new SellerDAOInMemory();
		BuyerDAO buyerDaoV1 = new BuyerDAOInMemory();
		SessionDAO sessionDaoV1 = new SessionDAOInMemory();
		DBCustomerSocketServerListenerV1 server = new DBCustomerSocketServerListenerV1(buyerDaoV1, sellerDaoV1, sessionDaoV1);
		server.startServer(port, maxConnections);
	}
}

/**
 * Class DBProductrRunner
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Set up the Customer database and IPC (currently sockets)
 */

package com.jsala.db.product;
import com.jsala.dao.SaleListingDAO;
import com.jsala.db.product.v1.*;
import com.jsala.dao.ItemDAO;
import java.net.SocketException;

public class DBProductRunner {
	public static void main(String[] args) throws SocketException {
		int port = 8400;
		int maxConnections = 10;
		ItemDAO itemDaoV1 = new ItemDAOInMemory();
		SaleListingDAO saleListingDAOV1 = new SaleListingDAOInMemory(itemDaoV1);
		DBProductSocketServerListenerV1 server = new DBProductSocketServerListenerV1(itemDaoV1, saleListingDAOV1);
		server.startServer(port, maxConnections);
	}
}

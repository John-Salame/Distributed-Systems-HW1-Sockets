/**
 * Class DBProductrRunner
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Set up the Customer database and IPC (currently sockets)
 */

package db.product;
import db.product.v1.*;
import dao.ItemDAO;
import java.net.SocketException;

public class DBProductRunner {
	public static void main(String[] args) throws SocketException {
		int port = 8400;
		int maxConnections = 1;
		ItemDAO itemDaoV1 = new ItemDAOInMemory();
		DBProductSocketServerListenerV1 server = new DBProductSocketServerListenerV1(itemDaoV1);
		server.startServer(port, maxConnections);
	}
}

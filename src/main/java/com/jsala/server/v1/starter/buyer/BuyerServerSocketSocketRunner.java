/**
 * Class BuyerServerTestSocket
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Test the buyer interface by setting up the server-side transport layer (sockets to clients and to databases)
 * This should work for testing a single client at a time.
 */

package com.jsala.server.v1.starter.buyer;
import com.jsala.common.interfaces.factory.UserInterfaceFactory;
import com.jsala.common.interfaces.BuyerInterface;
import com.jsala.dao.*;
import com.jsala.dao.factory.*;
import com.jsala.server.v1.*;
import com.jsala.server.v1.factory.*;
import com.jsala.server.v1.socket.*;
import java.net.SocketException;

public class BuyerServerSocketSocketRunner {
	public static void main(String[] args) /*throws SocketException*/ {
		int serverPort = 8100;
		int maxConnections = 100;
		String customerDBHost = "localhost";
		int customerDBIp = 8300;
		String productDBHost = "localhost";
		int productDBIp = 8400;
		UserInterfaceFactory dbClientProgramFactory = new DBClientProgramFactorySocketV1(customerDBHost, customerDBIp, productDBHost, productDBIp);
		BuyerSocketServerListenerV1 server = new BuyerSocketServerListenerV1(dbClientProgramFactory);
		server.startServer(serverPort, maxConnections);
	}
}

/**
 * Class SellerServerTestSocket
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Test the seller interface by setting up the server-side transport layer (sockets)
 * This should work for testing a single client at a time.
 */

package com.jsala.server.v1.socket.test;
import com.jsala.common.interfaces.factory.UserInterfaceFactory;
import com.jsala.common.interfaces.SellerInterface;
import com.jsala.common.Seller;
import com.jsala.common.Item;
import com.jsala.common.ItemId;
import com.jsala.dao.*;
import com.jsala.dao.factory.*;
import com.jsala.server.v1.*;
import com.jsala.server.v1.factory.*;
import com.jsala.server.v1.socket.*;
import java.io.IOException;
import java.net.SocketException;
import java.lang.reflect.InvocationTargetException;

public class SellerServerTestSocket {
	public static void main(String[] args) throws IOException {
		int serverPort = 8200;
		int maxConnections = 100;
		String customerDBHost = "localhost";
		int customerDBIp = 8300;
		String productDBHost = "localhost";
		int productDBIp = 8400;

		UserInterfaceFactory dbClientProgramFactory = new DBClientProgramFactorySocketV1(customerDBHost, customerDBIp, productDBHost, productDBIp);
		SellerSocketServerListenerV1 server = new SellerSocketServerListenerV1(dbClientProgramFactory);
		server.startServer(serverPort, maxConnections);
	}
}

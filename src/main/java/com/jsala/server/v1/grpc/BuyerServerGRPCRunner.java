/**
 * Class BuyerServerGRPCRunner
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * Description: Creates the buyer server
 * This adds decoupling and on-demand DAO creation.
 */

package com.jsala.server.v1.grpc;
import com.jsala.common.interfaces.factory.UserInterfaceFactory;
import com.jsala.common.interfaces.BuyerInterface;
import com.jsala.dao.*;
import com.jsala.dao.factory.*;
import com.jsala.server.v1.factory.*;
import com.jsala.server.v1.socket.*;
import com.jsala.server.v1.*;
import java.net.SocketException;


public class BuyerServerGRPCRunner {
	public static void main(String args) {
		// use sockets to listen to the client for now
		int serverPort = 8100;
		int maxConnections = 100;
		String customerDBHost = "localhost";
		int customerDBIp = 8300;
		String productDBHost = "localhost";
		int productDBIp = 8400;
		UserInterfaceFactory dbClientProgramFactory = new DBClientProgramFactoryGRPCV1(customerDBHost, customerDBIp, productDBHost, productDBIp);
		BuyerSocketServerListenerV1 server = new BuyerSocketServerListenerV1(dbClientProgramFactory);
		server.startServer(serverPort, maxConnections);
	}
}

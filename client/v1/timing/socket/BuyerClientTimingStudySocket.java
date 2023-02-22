/**
 * Class BuyerClientTimingStudySocket
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Creates BuyerTimingInstance for X buyers and then uses TimingLog to aggregate the results.
 */

package client.v1.timing.socket;
import client.v1.*;
import client.v1.socket.*;
import client.v1.timing.*;
import common.Buyer;
import common.BuyerInterface;
import common.Seller;
import common.SellerInterface;
import common.Item;
import common.ItemId;
import common.SaleListing;
import common.SaleListingId;
import common.TimingLog;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

 public class BuyerClientTimingStudySocket {
	public static void main(String[] args) throws IOException {
		// System.runFinalizersOnExit(true); // run destructors on threads
		String serverHost = "localhost";
		int serverPort = 8100;
		// BuyerInterface transport = new BuyerSocketClientV1(serverHost, serverPort);
		// BuyerInterface buyerInterfaceClient = new BuyerInterfaceClientV1(transport);
		ClientInterfaceFactory buyerSocketFactory = new ClientSocketFactory(BuyerInterfaceClientV1.class, null, serverHost, serverPort);

		int numUsers = 2;
		int numCalls = 10;
		TimingLog timingLog = new TimingLog(numUsers);
		String errorLogName = "errorLog.txt";
		Thread[] threads = new Thread[numUsers];
		// create users and start their threads
		int userNumber;
		Runnable buyer;
		// if a timing instance fails to be created, just move on
		try {
			userNumber = 1;
			buyer = new BuyerClientTimingInstance("Joe", "password123", buyerSocketFactory, timingLog, userNumber, numCalls, errorLogName);
			threads[0] = new Thread(buyer);
		} catch (InvocationTargetException e) {
			System.out.println(e);
		}
		if (numUsers > 1) {
			try {
				userNumber = 2;
				buyer = new BuyerClientTimingInstance("Harry", "potter321", buyerSocketFactory, timingLog, userNumber, numCalls, errorLogName);
				threads[1] = new Thread(buyer);
			} catch (InvocationTargetException e) {
				System.out.println(e);
			}
		}
		// start the threads
		for(int i = 0; i < numUsers; i++) {
			threads[i].start();
		}
		// Error: Server closing thread closes it for all clients
		// Solution: Have each client timing instance create its own socket
	}
 }

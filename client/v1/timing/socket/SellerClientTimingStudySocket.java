/**
 * Class SellerClientTimingStudySocket
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Creates SellerTimingInstances for X sellers and then uses TimingLog to aggregate the results.
 */

package client.v1.timing.socket;
import client.v1.*;
import client.v1.socket.*;
import client.v1.timing.*;
import common.interfaces.factory.UserInterfaceFactory;
import common.interfaces.SellerInterface;
import common.Seller;
import common.TimingLog;
import java.lang.reflect.InvocationTargetException;

 public class SellerClientTimingStudySocket {
	public static void main(String[] args) {
		// System.runFinalizersOnExit(true); // run destructors on threads
		String serverHost = "localhost";
		int serverPort = 8200;
		// SellerInterface transport = new SellerSocketClientV1(serverHost, serverPort);
		// SellerInterface sellerInterface = new SellerInterfaceClientV1(transport);
		UserInterfaceFactory sellerSocketFactory = new ClientSocketFactory(null, SellerInterfaceClientV1.class, serverHost, serverPort);

		int numUsers = 2;
		int numCalls = 10;
		TimingLog timingLog = new TimingLog(numUsers);
		String errorLogName = "errorLog.txt";
		Thread[] threads = new Thread[numUsers];
		// create users and start their threads
		int userNumber;
		Runnable seller;
		// if a timing instance fails to be created, just move on
		try {
			userNumber = 1;
			seller = new SellerClientTimingInstance("Joe", "password123", sellerSocketFactory, timingLog, userNumber, numCalls, errorLogName);
			threads[0] = new Thread(seller);
		} catch (InvocationTargetException e) {
			System.out.println(e);
		}
		if (numUsers > 1) {
			try {
				userNumber = 2;
				seller = new SellerClientTimingInstance("Harry", "potter321", sellerSocketFactory, timingLog, userNumber, numCalls, errorLogName);
				threads[1] = new Thread(seller);
			} catch (InvocationTargetException e) {
				System.out.println(e);
			}
		}
		// start the threads
		for(int i = 0; i < numUsers; i++) {
			threads[i].start();
		}
	}
 }

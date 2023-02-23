/**
 * Class SellerClientTimingInstance
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: The seller client which will make API calls and measure average response time for a single buyer
 */

package client.v1.timing;
import client.v1.*;
import common.Buyer;
import common.Item;
import common.Seller;
import common.interfaces.SellerInterface;
import common.TimingLog;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class SellerClientTimingInstance implements Runnable {
	private String username;
	private String password;
	private int userId;
	private String sessionToken;
	private SellerInterface sellerInterface; // the API to call on
	private TimingLog timingLog;
	private int sellerNumber; // which index of the timing log are we?
	private int numCalls; // how many API calls to make
	BufferedWriter errorLog;

	public SellerClientTimingInstance(String username, String password, SellerInterface sellerInterface, TimingLog timingLog, int sellerNumber, int numCalls, String errorLogName) {
		this.username = username;
		this.password = password;
		this.userId = 0; // invalid user ID
		this.sessionToken = null;
		this.sellerInterface = sellerInterface;
		this.timingLog = timingLog;
		this.sellerNumber = sellerNumber;
		this.numCalls = numCalls - 3; // exclude createUser(), login(), and logout()
		try {
			this.errorLog = new BufferedWriter(new FileWriter(errorLogName));
		} catch (IOException i) {
			System.out.println(i);
		}
	}

	// destructor
	// https://www.javatpoint.com/java-destructor
	protected void finalize() {
		System.out.println("SellerClientTimingInstance " + sellerNumber + " exiting");
		try {
			errorLog.close();
		} catch (IOException i) {
			System.out.println(i);
		}
	}

	// Start the thread
	public void run() {
		// do nothing
	}
}

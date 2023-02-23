/**
 * Class BuyerClientTimingInstance
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: The buyer client which will make API calls and measure average response time for a single buyer
 */

package client.v1.timing;
import client.v1.*;
import common.interfaces.factory.UserInterfaceFactory;
import common.interfaces.BuyerInterface;
import common.Buyer;
import common.Item;
import common.TimingLog;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class BuyerClientTimingInstance implements Runnable {
	private String username;
	private String password;
	private int userId;
	private String sessionToken;
	BuyerInterface buyerInterface; // API to call on
	private TimingLog timingLog;
	private int buyerNumber; // which index of the timing log are we?
	private int numCalls; // how many API calls to make
	BufferedWriter errorLog;

	public BuyerClientTimingInstance(String username, String password, UserInterfaceFactory buyerInterfaceFactory, TimingLog timingLog, int buyerNumber, int numCalls, String errorLogName) throws InvocationTargetException {
		this.username = username;
		this.password = password;
		this.userId = 0; // invalid user ID
		this.sessionToken = null;
		this.buyerInterface = buyerInterfaceFactory.createBuyerInterface(); // should I catch or make this constructor throw something?
		this.timingLog = timingLog;
		this.buyerNumber = buyerNumber;
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
		System.out.println("BuyerClientTimingInstance " + buyerNumber + " exiting");
		try {
			errorLog.close();
		} catch (IOException i) {
			System.out.println(i);
		}
	}

	// Start the thread
	public void run() {
		try {
			System.out.println("Creating user");
			this.userId = this.buyerInterface.createUser(this.username, this.password);
			System.out.println("Created buyer " + this.userId);
			System.out.println("Logging in");
			this.sessionToken = this.buyerInterface.login(this.username, this.password);
			System.out.println("Buyer " + this.userId + " session token = " + sessionToken);
			System.out.println("Displaying feedback for seller 1");
			int[] feedback = this.buyerInterface.getSellerRating(1);
			System.out.println(Arrays.toString(feedback));
			System.out.println("Searching for fruit");
			Item[] searchResults;
			searchResults = this.buyerInterface.searchItem(this.sessionToken, 0, new String[] {"fruit"}); // apple should top the list
			System.out.println(Arrays.toString(searchResults));
			System.out.println("\nSearching for sweet fruit");
			searchResults = this.buyerInterface.searchItem(this.sessionToken, 0, new String[] {"fruit", "sweet"}); // pear should top the list
			System.out.println(Arrays.toString(searchResults));
			System.out.println("\nSearching for furniture in category 1");
			searchResults = this.buyerInterface.searchItem(this.sessionToken, 1, new String[] {"furniture"});
			System.out.println(Arrays.toString(searchResults));
			System.out.println("Buyer " + this.userId + " session token = " + this.sessionToken);
			System.out.println("Buyer " + this.userId + " logging out");
			this.buyerInterface.logout(this.sessionToken);
		} catch (Exception e) {
			// write the error to the log
			try {
				// if the lines end up jumbling together, use file locks https://www.baeldung.com/java-write-to-file
				System.out.println(e);
				errorLog.write(e.toString());
			} catch (IOException i) {
				// do nothing
			}
		}
	}
}

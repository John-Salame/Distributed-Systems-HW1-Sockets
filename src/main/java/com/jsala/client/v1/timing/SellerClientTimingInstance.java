/**
 * Class SellerClientTimingInstance
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: The seller client which will make API calls and measure average response time for a single seller
 */

package com.jsala.client.v1.timing;
import com.jsala.client.v1.*;
import com.jsala.common.*;
import com.jsala.common.interfaces.factory.UserInterfaceFactory;
import com.jsala.common.interfaces.SellerInterface;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.lang.reflect.InvocationTargetException;

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

	public SellerClientTimingInstance(String username, String password, UserInterfaceFactory sellerInterfaceFactory, TimingLog timingLog, int sellerNumber, int numCalls, String errorLogName) throws InvocationTargetException {
		this.username = username;
		this.password = password;
		this.userId = 0; // invalid user ID
		this.sessionToken = null;
		this.sellerInterface = sellerInterfaceFactory.createSellerInterface();
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
		try {
			System.out.println("Creating user");
			this.userId = sellerInterface.createUser(this.username, this.password);
			System.out.println("Created seller " + this.userId);
			System.out.println("Logging in");
			this.sessionToken = this.sellerInterface.login(this.username, this.password);
			System.out.println("Seller " + this.userId + " session token = " + sessionToken);
			System.out.println("Displaying feedback for seller " + this.userId);
			int[] feedback = this.sellerInterface.getSellerRating(1);
			System.out.println(Arrays.toString(feedback));
			// Create items
			int quantity = this.userId;
			Item apple = new Item("apple", 0, new String[] {"Food", "Fruit", "Crunchy"}, "New", (float) 0.79);
			SaleListingId appleId = sellerInterface.putOnSale(this.sessionToken, apple, quantity);
			Item pear = new Item("pear", 0, new String[] {"Food", "Fruit", "Soft", "Sour", "Sweet"}, "New", (float) 1.23);
			SaleListingId pearId = sellerInterface.putOnSale(this.sessionToken, pear, quantity);
			Item chair = new Item("chair", 1, new String[] {"Household", "Furniture"}, "Used", (float) 5.29);
			SaleListingId chairId = sellerInterface.putOnSale(this.sessionToken, chair, quantity);
			System.out.println("Changing price of apple");
			this.sellerInterface.changePriceOfItem(this.sessionToken, appleId.getItemId(), (float) 5.00);
			// remove the apple from sale
			sellerInterface.removeItemFromSale(this.sessionToken, appleId.getItemId(), quantity);
			// list all items this seller is selling
			String itemsOnSale = sellerInterface.displayItemsOnSale(this.sessionToken);
			System.out.println("Seller " + this.userId + " is selling:\n" + itemsOnSale);
			System.out.println("Seller " + this.userId + " session token = " + this.sessionToken);
			System.out.println("Seller " + this.userId + " logging out");
			this.sellerInterface.logout(this.sessionToken);
		} catch (Exception e) {
			// write the error to the log
			try {
				// if the lines end up jumbling together, use file locks https://www.baeldung.com/java-write-to-file
				System.out.println("Timing instance exception: " + e);
				errorLog.write(e.toString());
				this.sellerInterface.logout(this.sessionToken);
			} catch (IOException i) {
				// do nothing
			}
		}
	}
}

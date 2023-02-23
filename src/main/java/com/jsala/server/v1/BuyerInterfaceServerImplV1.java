/**
 * Class BuyerInterfaceServerImplV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Server-side implementation of the buyer API
 */

package com.jsala.server.v1;
import com.jsala.common.interfaces.BuyerInterface;
import com.jsala.common.Item;
import com.jsala.common.ItemId;
import com.jsala.dao.*;
import com.jsala.dao.factory.*;
import com.jsala.util.EditDistance;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.lang.reflect.InvocationTargetException;

public class BuyerInterfaceServerImplV1 implements BuyerInterface {

	private BuyerDAO buyerDao;
	private SellerDAO sellerDao;
	private SessionDAO sessionDao;
	private ItemDAO itemDao;
	
	// CONSTRUCTORS
	public BuyerInterfaceServerImplV1() {}
	public BuyerInterfaceServerImplV1(CustomerDAOFactory buyerDaoFactory, CustomerDAOFactory sellerDaoFactory, CustomerDAOFactory sessionDaoFactory, ProductDAOFactory itemDaoFactory) throws InvocationTargetException {
		this.buyerDao = buyerDaoFactory.createBuyerDao();
		this.sellerDao = sellerDaoFactory.createSellerDao();
		this.sessionDao = sessionDaoFactory.createSessionDao();
		this.itemDao = itemDaoFactory.createItemDao();
	}

	public int createUser(String username, String password) throws IOException, IllegalArgumentException {
		return buyerDao.createUser(username, password);
	}
	public String login(String username, String password) throws IOException, NoSuchElementException {
		String sessionToken;
		int userId = buyerDao.getUserId(username, password); // may raise a NoSuchElementException
		sessionToken = sessionDao.createSession(userId);
		return sessionToken;
	}
	// NoSuchElementException means the session is gone for sure. IOException means we should retry.
	public void logout(String sessionToken) throws IOException, NoSuchElementException {
		// try to log out until it works
		boolean logoutSuccess = false;
		while (!logoutSuccess) {
			try {
				sessionDao.expireSession(sessionToken);
				logoutSuccess = true;
			} catch (IOException e) {
				System.out.println("BuyerInterfaceServerImplV1 failed logout");
			}
		}
		// close all database connections associated with this user
		this.buyerDao.closeConnection();
		this.sellerDao.closeConnection();
		this.sessionDao.closeConnection();
		this.itemDao.closeConnection();
	}
	public int[] getSellerRating(int sellerId) throws IOException, NoSuchElementException {
		return sellerDao.getSellerById(sellerId).getFeedback();
	}
	/**
	 * Method searchItem()
	 * Search based on a modified Levenshtein distance, which is a form of edit distance for strings.
	 * Read util.EditDistance for more information.
	 * Search algorithm:
	 *   Each keyword in the search query records its best edit distance to the keywords of an item.
	 *   Then, take the sum of those edit distances. That is your distance score for that item.
	 *   Keep the top LIMIT items (lowest score is the best).
	 */
	public Item[] searchItem(String sessionToken, int category, String[] keywords) throws IOException {
		final int LIMIT = 20; // how many search results to show
		Item[] items = itemDao.getItemsInCategory(category); // may throw an IOException
		int numItemsInCategory = items.length;
		double[] scores = new double[numItemsInCategory];
		int scoreIndex = 0;
		int numKeywords = 0; // number of search keywords
		// truncate the keywords before comparing them to the item keywords
		String[] keywordsTruncated = new String[keywords.length];
		for(int i = 0; i < keywords.length; i++) {
			try {
				String keyword = keywords[i];
				keyword = Item.validateKeyword(keyword); // truncate, or throw error if keyword is null or empty string
				keywordsTruncated[numKeywords++] = keyword;
			} catch (IllegalArgumentException e) {
				// do nothing; prevents the null or empty keyword from being added
			}
		}
		// numKeywords is the number of non-empty keywords in keywordsTruncated
		for(Item item : items) {
			String[] itemKeywords = item.getKeywords();
			double[] editDist = new double[numKeywords]; // holds the best edit distance for each keyword in keywords
			double sum = 0.0;
			// prevent the cheat where an item with no keywords would get a score of 0 (best possible score)
			scores[scoreIndex] = 10.0;
			for(int i = 0; i < numKeywords; i++) {
				editDist[i] = 10.0;
				for(int j = 0; j < itemKeywords.length; j++) {
					double ed = 10.0; // arbitrarly large edit distance
					// Get the Levenshtein Distance of the keyword pair
					try {
						ed = EditDistance.levenshteinNormalized(keywordsTruncated[i], itemKeywords[j]);
					} catch (IllegalArgumentException e) {
						// not sure what to do here
					}
					editDist[i] = Math.min(editDist[i], ed);
					/*
					String levenshteinSummary = "Levenshtein Distance for words " + 
						keywordsTruncated[i] + " and " + itemKeywords[j] + " is " + ed;
					System.out.println(levenshteinSummary);
					*/
				}
				// System.out.println("Levenshtein Distance for keyword " + keywordsTruncated[i] + " is " + editDist[i]);
				sum += editDist[i];
			}
			// System.out.println("Search score for item " + item.getName() + " is " + sum);
			if (itemKeywords.length > 0) {
				scores[scoreIndex] = sum;
			}
			scoreIndex++; // after you finish scoring this item, move on to the next one
		}
		// now, sort the items; I'll just use selection sort
		for(int i = 0; i < numItemsInCategory - 1; i++) {
			double min = scores[i]; // start off assuming the first unsorted item has the minimum score
			int minIndex = i;
			for(int j = i+1; j < numItemsInCategory; j++) {
				if(scores[j] < scores[i]) {
					min = scores[j];
					minIndex = j;
				}
			}
			// swap scores[i] with scores[minIndex]. Also swap the items
			scores[minIndex] = scores[i];
			scores[i] = min;
			Item bestItem = items[minIndex];
			items[minIndex] = items[i];
			items[i] = bestItem;
		}
		// Now, limit the result to LIMIT items
		int numItemsInResult = Math.min(LIMIT, numItemsInCategory);
		Item[] returnArray = null;
		if(LIMIT > 0) {
			returnArray = new Item[numItemsInResult];
			int numItems = items.length;
			for(int i = 0; i < numItemsInResult && i < numItems; i++) {
				returnArray[i] = items[i];
			}
		}
		else {
			returnArray = items; // no limit; return all items from original search
		}
		return returnArray;
		// TO-DO: use the DAO to describe sales listings associated with each item
	}
}

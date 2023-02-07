/**
 * Class BuyerInterfaceServer
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Server-side implementation of the buyer interface
 */

package server;
import dao.*;
import common.BuyerInterface;
import common.Item;
import common.ItemId;
import util.EditDistance;

public class BuyerInterfaceServer implements BuyerInterface {

	private BuyerDAO buyerDao;
	private SellerDAO sellerDao;
	private SessionDAO sessionDao;
	private ItemDAO itemDao;
	
	// CONSTRUCTORS
	public BuyerInterfaceServer() {

	}
	public BuyerInterfaceServer(BuyerDAO buyerDao, SellerDAO sellerDao, SessionDAO sessionDao, ItemDAO itemDao) {
		this.buyerDao = buyerDao;
		this.sellerDao = sellerDao;
		this.sessionDao = sessionDao;
		this.itemDao = itemDao;
	}

	public int createUser(String username, String password) {
		return buyerDao.createUser(username, password);
	}
	public String login(String username, String password) {
		int userId = buyerDao.getUserId(username, password);
		String sessionToken = sessionDao.createSession(userId);
		return sessionToken;
	}
	public void logout(String sessionToken) {
		sessionDao.expireSession(sessionToken);
	}
	public int[] getSellerRating(int sellerId) {
		// throw new NotImplementedException("BuyerInterfaceServer: getSellerRating()");
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
	public Item[] searchItem(String sessionToken, int category, String[] keywords) {
		final int LIMIT = 20; // how many search results to show
		Item[] items = itemDao.getItemsInCategory(category);
		double[] scores = new double[items.length];
		int scoreIndex = 0;
		int numKeywords = keywords.length; // number of search keywords
		for(Item item : items) {
			String[] itemKeywords = item.getKeywords();
			double[] editDist = new double[numKeywords]; // holds the best edit distance for each keyword in keywords
			double sum = 0.0;
			for(int i = 0; i < numKeywords; i++) {
				editDist[i] = 0.0;
				for(int j = 0; j < itemKeywords.length; j++) {
					double ed = EditDistance.levenshteinNormalized(keywords[i], itemKeywords[j]);
					editDist[i] = Math.max(editDist[i], ed);
				}
				sum += editDist[i];
			}
			scores[scoreIndex] = sum;
			scoreIndex++; // after you finish scoring this item, move on to the next one
		}
		// now, sort the items; I'll just use selection sort
		for(int i = 0; i < numKeywords - 1; i++) {
			double min = scores[i];
			int minIndex = i;
			for(int j = i+1; j < numKeywords; j++) {
				if(scores[j] < scores[i]) {
					min = scores[j];
					minIndex = j;
				}
			}
			// swap scores[i] with scores[minIndex]
			scores[minIndex] = scores[i];
			scores[i] = min;
		}
		// Now, limit the result to LIMIT items
		Item[] returnArray = null;
		if(LIMIT > 0) {
			returnArray = new Item[LIMIT];
			int numItems = items.length;
			for(int i = 0; i < LIMIT && i < numItems; i++) {
				returnArray[i] = items[i];
			}
		}
		else {
			returnArray = items;
		}
		return returnArray;
		// TO-DO: use the DAO to describe sales listings associated with each item
	}
}

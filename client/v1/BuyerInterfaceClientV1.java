/**
 * Class BuyerInterfaceClientV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Client-side implementation of the buyer API
 * Note: This could theoretically be removed right now since it is just using delegation,
 * but it could be turned into middleware later on or connected to middlware.
 * I will return reasonable values when an Exception occurs. This should make it easier for the timing studies to make the required number of API calls.
 */

package client.v1;
import common.BuyerInterface;
import common.Item;
import java.io.IOException;

public class BuyerInterfaceClientV1 implements BuyerInterface {

	private BuyerInterface transport; // transport layer for inter-process communication
	
	// CONSTRUCTORS
	public BuyerInterfaceClientV1() {}
	public BuyerInterfaceClientV1(BuyerInterface transport) {
		this.transport = transport;
	}

	public int createUser(String username, String password) {
		int userId = 0;
		try {
			userId = transport.createUser(username, password);
		} catch (Exception e) {
			System.out.println(e);
		}
		return userId;
	}
	public String login(String username, String password) {
		String sessionToken = "";
		try {
			sessionToken = transport.login(username, password);
		} catch (Exception e) {
			System.out.println(e);
		}
		return sessionToken;
	}
	public void logout(String sessionToken) {
		try {
			transport.logout(sessionToken);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public int[] getSellerRating(int sellerId) {
		int[] sellerRating = new int[] {0, 0};
		try {
			sellerRating = transport.getSellerRating(sellerId);
		} catch (Exception e) {
			System.out.println(e);
		}
		return sellerRating;
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
		Item[] results = new Item[0];
		try {
			results = searchItem(sessionToken, category, keywords);
		} catch (Exception e) {
			System.out.println(e);
		}
		return results;
	}
}

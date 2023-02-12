/**
 * Class BuyerInterfaceClientV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Client-side implementation of the buyer API
 * Note: This could theoretically be removed right now since it is just using delegation,
 * but it could be turned into middleware later on or connected to middlware.
 */

package client.v1;
import common.BuyerInterface;
import common.Item;

public class BuyerInterfaceClientV1 implements BuyerInterface {

	private BuyerInterface transport; // transport layer for inter-process communication
	
	// CONSTRUCTORS
	public BuyerInterfaceClientV1() {}
	public BuyerInterfaceClientV1(BuyerInterface transport) {
		this.transport = transport;
	}

	public int createUser(String username, String password) {
		return transport.createUser(username, password);
	}
	public String login(String username, String password) {
		return transport.login(username, password);
	}
	public void logout(String sessionToken) {
		transport.logout(sessionToken);
	}
	public int[] getSellerRating(int sellerId) {
		return transport.getSellerRating(sellerId);
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
		return searchItem(sessionToken, category, keywords);
	}
}

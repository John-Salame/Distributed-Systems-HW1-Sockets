/**
 * Class SellerInterfaceClientV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Client-side implementation of the seller API
 * Note: This could theoretically be removed right now since it is just using delegation,
 * but it could be turned into middleware later on or connected to middlware.
 */

package client.v1;
import common.SellerInterface;
import common.Item;

public class SellerInterfaceClientV1 implements SellerInterface {

	private SellerInterface transport; // transport layer for inter-process communication
	
	// CONSTRUCTORS
	public SellerInterfaceClientV1() {}
	public SellerInterfaceClientV1(SellerInterface transport) {
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
	public void putOnSale(String sessionToken, Item item, int quantity) {
		transport.putOnSale(sessionToken, item, quantity);
	}
	/*
	public void changePriceOfItem(String sessionToken, ItemId itemId, float newPrice);
	public void removeItemFromSale(String sessionToken, ItemId itemId, int quantity);
	public String displayItemsOnSale(String sessionToken);
	*/
}

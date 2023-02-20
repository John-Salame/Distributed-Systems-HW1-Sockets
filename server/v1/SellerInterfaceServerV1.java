/**
 * Class SellerInterfaceServerV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Server-side implementation of the seller API
 */

package server.v1;
import dao.*;
import common.SellerInterface;
import common.Item;
import common.ItemId;
import common.SaleListingId;
import util.EditDistance;
import java.util.NoSuchElementException;

public class SellerInterfaceServerV1 implements SellerInterface {

	private SellerDAO sellerDao;
	private SessionDAO sessionDao;
	private ItemDAO itemDao;
	
	// CONSTRUCTORS
	public SellerInterfaceServerV1() {}
	public SellerInterfaceServerV1(SellerDAO sellerDao, SessionDAO sessionDao, ItemDAO itemDao) {
		this.sellerDao = sellerDao;
		this.sessionDao = sessionDao;
		this.itemDao = itemDao;
	}

	public int createUser(String username, String password) throws IllegalArgumentException {
		return sellerDao.createUser(username, password);
	}
	public String login(String username, String password) throws NoSuchElementException {
		int userId = sellerDao.getUserId(username, password);
		String sessionToken = sessionDao.createSession(userId);
		return sessionToken;
	}
	public void logout(String sessionToken) throws NoSuchElementException {
		sessionDao.expireSession(sessionToken);
	}
	public int[] getSellerRating(int sellerId) throws NoSuchElementException {
		return sellerDao.getSellerById(sellerId).getFeedback(); // not sure if this counts as stateless
	}
	public SaleListingId putOnSale(String sessionToken, Item item, int quantity) {
		throw new RuntimeException("SellerInterfaceServerV1: putOnSale() Not implemented");
	}
	public void changePriceOfItem(String sessionToken, ItemId itemId, float newPrice) throws NoSuchElementException, IllegalArgumentException, UnsupportedOperationException {
		int sellerId = sessionDao.getUserIdFromSession(sessionToken);
		itemDao.changePrice(itemId, sellerId, newPrice);
	}
	/*
	public void removeItemFromSale(String sessionToken, ItemId itemId, int quantity);
	public String displayItemsOnSale(String sessionToken);
	*/
}

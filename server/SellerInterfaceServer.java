/**
 * Class SellerInterfaceServer
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Server-side implementation of the seller API
 */

package server;
import dao.*;
import common.SellerInterface;
import common.Item;
import common.ItemId;
import util.EditDistance;

public class SellerInterfaceServer implements SellerInterface {

	private SellerDAO sellerDao;
	private SessionDAO sessionDao;
	private ItemDAO itemDao;
	
	// CONSTRUCTORS
	public SellerInterfaceServer() {}
	public SellerInterfaceServer(SellerDAO sellerDao, SessionDAO sessionDao, ItemDAO itemDao) {
		this.sellerDao = sellerDao;
		this.sessionDao = sessionDao;
		this.itemDao = itemDao;
	}

	public int createUser(String username, String password) {
		return sellerDao.createUser(username, password);
	}
	public String login(String username, String password) {
		int userId = sellerDao.getUserId(username, password);
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
	public void putOnSale(String sessionToken, Item item, int quantity) {
		throw new RuntimeException("SellerInterfaceServer: putOnSale() Not implemented");
	}
}

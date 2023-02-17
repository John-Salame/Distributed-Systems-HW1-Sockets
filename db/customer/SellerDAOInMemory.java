/**
 * Class SellerDAOInMemory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Provides access to the sellers database.
 */

package db.customer;
import common.Seller;
import dao.SellerDAO;
import java.util.List;
import java.util.ArrayList;

public class SellerDAOInMemory implements SellerDAO {
	static int nextId = 1;
	private List<Seller> sellers;

	// CONSTRUCTORS
	public SellerDAOInMemory() {
		sellers = new ArrayList<Seller>();
	}

	// METHODS

	/**
	 * Generate chooses the next unique user id
	 * Currently not thread-safe.
	 */
	private int generateUniqueId() {
		int currId = this.nextId;
		this.nextId++;
		return currId;
	}
	public int createUser(String username, String password) {
		int userId = this.getUserId(username, password);
		// if the user already exists, return the id of the existing user
		if(userId > 0) {
			return userId;
		}
		// create a new user
		userId = this.generateUniqueId();
		Seller newSeller = new Seller();
		newSeller.setName(username);
		newSeller.setPassword(password);
		newSeller.setId(userId);
		this.sellers.add(newSeller);
		return userId;
	}
	public int getUserId(String username, String password) {
		for(Seller seller : this.sellers) {
			if(seller.isMyLoginCredentials(username, password)) {
				return seller.getId();
			}
		}
		return 0; // no user with this id exists
	}
	public Seller getSellerById(int sellerId) {
		for(Seller seller: this.sellers) {
			if(seller.getId() == sellerId) {
				return seller;
			}
		}
		return null;
	}

	// not thread-safe / safe with multiple simultaneous operations
	public void commitSeller(Seller seller) {
		int id = seller.getId();
		// update if the seller exists
		for(int i = 0; i < sellers.size(); i++) {
			if(sellers.get(i).getId() == id) {
				sellers.set(i, seller);
				return;
			}
		}
		// add if seller not exists
		sellers.add(seller);
	}
}

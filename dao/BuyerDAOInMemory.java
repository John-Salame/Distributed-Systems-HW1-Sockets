/**
 * Class BuyerDAOInMemory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Provides access to a server-local database of buyers.
 */

package dao;
import common.Buyer;
import java.util.List;
import java.util.ArrayList;

public class BuyerDAOInMemory implements BuyerDAO {
	private static int nextId = 1;
	private List<Buyer> buyers;

	// CONSTRUCTORS
	public BuyerDAOInMemory() {
		buyers = new ArrayList<Buyer>();
	}

	// METHODS

	/**
	 * Generate choose the next unique user id
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
		Buyer newBuyer = new Buyer();
		newBuyer.setName(username);
		newBuyer.setPassword(password);
		newBuyer.setId(userId);
		this.buyers.add(newBuyer);
		return userId;
	}
	public int getUserId(String username, String password) {
		for(Buyer buyer : this.buyers) {
			if(buyer.isMyLoginCredentials(username, password)) {
				return buyer.getId();
			}
		}
		return 0; // no user with this id exists
	}
	public Buyer getBuyerById(int buyerId) {
		for(Buyer buyer: this.buyers) {
			if(buyer.getId() == buyerId) {
				return buyer;
			}
		}
		return null;
	}
}

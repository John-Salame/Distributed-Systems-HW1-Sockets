/**
 * Class BuyerDAOInMemory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Provides access to the buyers database.
 */

package db.customer;
import common.Buyer;
import dao.BuyerDAO;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.io.IOException;

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
	// could throw an IllegalArgumentException if the username is invalid
	public int createUser(String username, String password) throws IllegalArgumentException {
		// if the user already exists, return the id of the existing user
		try {
			int userId = this.getUserId(username, password);
			return userId;
		} catch (NoSuchElementException e) {
			// continue
		}
		// create a new user
		Buyer newBuyer = new Buyer();
		newBuyer.setName(username);
		newBuyer.setPassword(password);
		int userId = this.generateUniqueId();
		newBuyer.setId(userId);
		this.buyers.add(newBuyer);
		return userId;
	}
	public int getUserId(String username, String password) throws NoSuchElementException {
		// for large databases, speed up obviously invalid input
		try {
			Buyer.validateName(username);
			Buyer.validatePassword(password);
		} catch (IllegalArgumentException e) {
			throw new NoSuchElementException("Searching for buyer with invalid username or password");
		}
		for(Buyer buyer : this.buyers) {
			if(buyer.isMyLoginCredentials(username, password)) {
				return buyer.getId();
			}
		}
		throw new NoSuchElementException("No buyer exists with username " + username + " and the specified password");
	}
	public Buyer getBuyerById(int buyerId) throws NoSuchElementException {
		// for large databases, speed up obviously invalid input
		try {
			Buyer.validateId(buyerId);
		} catch (IllegalArgumentException e) {
			throw new NoSuchElementException("Searching for buyer with invalid buyer ID " + buyerId);
		}
		// search
		for(Buyer buyer: this.buyers) {
			if(buyer.getId() == buyerId) {
				return buyer;
			}
		}
		throw new NoSuchElementException("No buyer exists with id " + buyerId);
	}

	public void closeConnection() {
		// do nothing
	}
}

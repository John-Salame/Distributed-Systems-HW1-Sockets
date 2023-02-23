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
import java.util.NoSuchElementException;
import java.io.IOException;

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
	// could throw an IllegalArgumentException if the username is invalid
	public int createUser(String username, String password) throws IOException, IllegalArgumentException {
		// if the user already exists, return the id of the existing user
		try {
			int userId = this.getUserId(username, password);
			return userId;
		} catch (NoSuchElementException e) {
			// continue
		}
		// create a new user
		Seller newSeller = new Seller();
		newSeller.setName(username);
		newSeller.setPassword(password);
		int userId = this.generateUniqueId();
		newSeller.setId(userId);
		try {
			this.sellers.add(newSeller);
		} catch (Exception e) {
			throw new IOException("Error adding seller to database");
		}
		return userId;
	}
	public int getUserId(String username, String password) throws IOException, NoSuchElementException {
		// for large databases, speed up obviously invalid input
		try {
			Seller.validateName(username);
			Seller.validatePassword(password);
		} catch (IllegalArgumentException e) {
			throw new NoSuchElementException("Searching for buyer with invalid username or password");
		}
		// search
		try {
			for(Seller seller : this.sellers) {
				if(seller.isMyLoginCredentials(username, password)) {
					return seller.getId();
				}
			}
		} catch (Exception e) {
			throw new IOException("Error iterating through sellers in database");
		}
		throw new NoSuchElementException("No seller exists with username " + username + " and the specified password");
	}
	public Seller getSellerById(int sellerId) throws IOException, NoSuchElementException {
		// for large databases, speed up obviously invalid input
		try {
			Seller.validateId(sellerId);
		} catch (IllegalArgumentException e) {
			throw new NoSuchElementException("Searching for seller with invalid buyer ID " + sellerId);
		}
		// search
		try {
			for(Seller seller: this.sellers) {
				if(seller.getId() == sellerId) {
					return seller;
				}
			}
		} catch (Exception e) {
			throw new IOException("Error iterating through sellers in database");
		}
		throw new NoSuchElementException("No seller exists with id " + sellerId);
	}

	// not thread-safe / safe with multiple simultaneous operations
	// null seller is invalid
	public void commitSeller(Seller seller) throws IOException, IllegalArgumentException {
		if (seller == null) {
			throw new IllegalArgumentException("Cannot commit a null seller");
		}
		int id = seller.getId();
		try {
			// update if the seller exists
			for(int i = 0; i < sellers.size(); i++) {
				if(sellers.get(i).getId() == id) {
					sellers.set(i, seller);
					return;
				}
			}
			// add if seller not exists
			sellers.add(seller);
		} catch (Exception e) {
			throw new IOException("Error committing seller");
		}
	}

	public void closeConnection() {
		// do nothing
	}
}

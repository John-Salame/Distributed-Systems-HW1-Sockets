/**
 * Interface BuyerDAO
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: An interface to access the database of buyers (in Customer database).
 */

package dao;
import common.Buyer;
import java.util.NoSuchElementException;

public interface BuyerDAO {
	public abstract int createUser(String username, String password) throws IllegalArgumentException; // return the user id
	public abstract int getUserId(String username, String password) throws NoSuchElementException;
	public abstract Buyer getBuyerById(int buyerId) throws NoSuchElementException;
}

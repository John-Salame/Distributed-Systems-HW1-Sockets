/**
 * Interface CommonUserInterface
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Interface containing methods common to buyers and sellers.
 */

package common;
import java.io.IOException;
import java.util.NoSuchElementException;

public interface CommonUserInterface {
	public abstract int createUser(String username, String password) throws IOException, IllegalArgumentException; // return the user ID
	public abstract String login(String username, String password) throws IOException, NoSuchElementException; // return the session token
	public abstract void logout(String sessionToken) throws IOException, NoSuchElementException;
	public abstract int[] getSellerRating(int sellerId) throws IOException, NoSuchElementException;
}

/**
 * Interface CommonUserInterface
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Interface containing methods common to buyers and sellers.
 */

package common;

public interface CommonUserInterface {
	public abstract int createUser(String username, String password); // return the user ID
	public abstract String login(String username, String password); // return the session token
	public abstract void logout(String sessionToken);
	public abstract int[] getSellerRating(int sellerId);
}

/**
 * Interface BuyerInterface
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Buyer interface common to client and server
 */

package common;

public interface BuyerInterface extends CommonUserInterface {
	// Inherited Methods
	//   Implement methods from CommonUserInterface
	// New Methods
	public abstract Item[] searchItem(String sessionToken, int category, String[] keywords);
	// public abstract void addToCart(String sessionToken, ItemId itemId, int quantity);
	// public abstract void removeFromCart(String sessionToken, ItemId itemId, int quantity);
	// public abstract void clearCart(String sessionToken);
	// public abstract String displayCart(String sessionToken); // return a string that you can display later
	/**
	 * For feedback, rating=1 is thumbs up and rating=-1 is thumbs down.
	 * A user should only be able to provide feedback for an item once.
	 * Maybe return a String so I can give a proper message if the user attempts to provide feedback more than once.
	 */
	// public abstract void provideFeedback(String sessionToken, ItemId itemId, int rating); // TO-DO: Figure out how to limit to one vote
	// public abstract Purchase[] getPurchaseHistory(String sessionToken);
}

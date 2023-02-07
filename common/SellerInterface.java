/**
 * Interface SellerInterface
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Seller interface common to client and server
 */

package common;

public interface SellerInterface extends CommonUserInterface {
	// Inherited Methods
	//   Implement methods from CommonUserInterface
	// New Methods
	public abstract void putOnSale(String sessionToken, Item item, int quantity);
	public abstract void changePriceOfItem(String sessionToken, int itemId, float newPrice);
	public abstract void removeItemFromSale(String sessionToken, int itemId, int quantity);
	public abstract String displayItemsOnSale(String sessionToken);
}

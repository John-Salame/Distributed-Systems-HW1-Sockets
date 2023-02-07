/**
 * Interface ShoppingCartDAO
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: An interface to access the shopping cart (in Customer database).
 */

package dao;
import common.CartItem;
import common.ItemId;

public interface ShoppingCartDAO {
	public abstract void addToCart(int buyerId, ItemId itemId, int quantity);
	public abstract void addtoCart(CartItem cartItem);
	public abstract void removeFromCart(int buyerId, ItemId itemId, int quantity);
	public abstract void clearCart(int buyerId); // remove the user's cart items from the database
}
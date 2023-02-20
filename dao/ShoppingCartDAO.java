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
import java.util.NoSuchElementException;

public interface ShoppingCartDAO {
	public abstract void addToCart(int buyerId, ItemId itemId, int quantity) throws IllegalArgumentException;
	public abstract void addtoCart(CartItem cartItem) throws IllegalArgumentException;
	public abstract void removeFromCart(int buyerId, ItemId itemId, int quantity) throws NoSuchElementException;
	public abstract void clearCart(int buyerId) throws NoSuchElementException; // remove the user's cart items from the database
}
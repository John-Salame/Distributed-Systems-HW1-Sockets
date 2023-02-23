/**
 * Class CartItem
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Represents an item in the cart; the cart tracks an item ID and a quantity.
 */

package com.jsala.common;

public class CartItem {
	private int buyerId;
	private ItemId itemId;
	private int quantity;

	// CONSTRUCTORS
	public CartItem() {}
	public CartItem(int buyerId, ItemId itemId, int quantity) {
		this.setBuyerId(buyerId);
		this.setItemId(itemId);
		this.setQuantity(quantity);
	}

	// SETTERS
	public void setBuyerId(int buyerId) {
		this.buyerId = buyerId;
	}
	public void setItemId(ItemId itemId) {
		this.itemId = itemId;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	// GETTERS
	public int getBuyerId() {
		return this.buyerId;
	}
	public ItemId getItemId() {
		return this.itemId;
	}
	public int getQuantity() {
		return this.quantity;
	}

	@Override
	public String toString() {
		return "Cart Item: Buyer " + this.getBuyerId() + 
			" Item " + this.getItemId().toString() + 
			" Quantity " + this.getQuantity();
	}
}

/**
 * Class SaleListing
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: A class representing a sale listing (item and quantity)
 * The items are not intrinsically tied to the sale listing;
 * you can put the same kind of item on sale again after it has sold out.
 * However, once the actual sale is recorded, it will be recorded in another class and table which also permanently records the sale price in case it changes later.
 *
 * I'm not sure what my candidate key would be. The assignment implies that it is a combination of item ID and quantity, but what if I have multiple listings with the same quantity?
 * Perhaps I can't do partial sales and I can only sell the full quantity. Only then will the removal of a listing make sense.
 * Otherwise, we have an arbitrary numeric primary key and we query for all listings matching (item id, quantity) and arbitrarily sell one of them.
 *
 * In the future, I may denormalize some things such as sellerId and seller name so that I can get a nice toString() method.
 */

package com.jsala.common;

public class SaleListing {
	private SaleListingId id;
	private int quantityRemaining; // how many have not yet sold
	boolean isRemoved;

	// CONSTRUCTORS
	public SaleListing() {
		this.id = new SaleListingId();
		this.isRemoved = false;
	}
	public SaleListing(ItemId itemId, int quantity) {
		this.id = new SaleListingId(itemId, quantity);
		this.initializeQuantity(quantity);
		this.isRemoved = false;
	}
	public SaleListing(SaleListingId id) {
		this.id = id;
		this.initializeQuantity(id.getQuantity());
		this.isRemoved = false;
	}

	// SETTERS
	public void setItemId(ItemId itemId) {
		this.id.setItemId(itemId);
	}
	public void initializeQuantity(int q) {
		assert q >= 0;
		this.setOriginalQuantity(q);
		this.quantityRemaining = q;
	}
	private void setOriginalQuantity(int q) {
		this.id.setQuantity(q);
	}
	// used this function when selling the item; you can only sell up to <quantityRemaining> items
	// return the number of items sold
	public int decrementQuantity(int q) {
		if(isRemoved) {
			throw new IllegalStateException("Error: Cannot buy an item which has been removed from sale.");
		}
		int qSold = Math.min(q, this.quantityRemaining);
		this.quantityRemaining -= qSold;
		return qSold;
	}
	// use this to remove an item from sale
	public void removeListing() {
		this.isRemoved = true;
	}

	// GETTERS
	public ItemId getItemId() {
		return this.id.getItemId();
	}
	public int getOriginalQuantity() {
		return this.id.getQuantity();
	}
	public int getQuantityRemaining() {
		return this.quantityRemaining;
	}
	public int getQuantitySold() {
		return this.getOriginalQuantity() - this.quantityRemaining;
	}
	public boolean isSoldOut() {
		return this.quantityRemaining <= 0; // really it should only work for = 0, but who knows if something weird might happen?
	}
	public boolean isRemoved() {
		return this.isRemoved;
	}
	
	@Override
	public String toString() {
		String ret = "Sales Listing for item " + this.getItemId().toString() + "\n";
		ret = ret + this.getQuantityRemaining() + "/" + this.getOriginalQuantity() + " Remaining";
		if(this.isSoldOut()) {
			ret += " (Sold out)";
		}
		else if(this.isRemoved()) {
			ret += " (No longer available for purchase)";
		}
		ret += "\n";
		return ret;
	}
}

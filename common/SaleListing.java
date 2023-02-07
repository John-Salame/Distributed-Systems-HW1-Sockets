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

package common;

public class SaleListing {
	private ItemId itemId; // acts as a foreign key
	private int originalQuantity; // how many the seller put up for sale
	private int quantityRemaining; // how many have not yet sold
	boolean isRemoved;

	// CONSTRUCTORS
	public SaleListing() {
		this.isRemoved = false;
	}
	public SaleListing(ItemId itemId, int quantity) {
		this.itemId = itemId;
		this.initializeQuantity(quantity);
	}

	// used this function when selling the item; you can only sell up to <quantityRemaining> items
	// return the number of items sold
	public int decrementQuantity(int q) {
		int qSold = Math.min(q, this.quantityRemaining);
		this.quantityRemaining -= qSold;
		return qSold;
	}
	// use this to remove an item from sale
	public void removeListing() {
		this.isRemoved = true;
	}

	// SETTERS
	public void setItemId(ItemId itemId) {
		this.itemId = itemId;
	}
	public void initializeQuantity(int q) {
		this.originalQuantity = q;
		this.quantityRemaining = q;
	}

	// GETTERS
	public ItemId getItemId() {
		return this.itemId;
	}
	public int getOriginalQuantity() {
		return this.originalQuantity;
	}
	public int getQuantityRemaining() {
		return this.quantityRemaining;
	}
	public int getQuantitySold() {
		return this.originalQuantity - this.quantityRemaining;
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
			ret += " (No longer available)";
		}
		ret += "\n";
		return ret;
	}
}

/**
 * Class SaleListingId
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: This class acts as the candidate key for a sale listing.
 */

package common;

public class SaleListingId {
	private ItemId itemId; // acts as a foreign key
	private int quantity; // how many the seller put up for sale

	public SaleListingId() {}
	public SaleListingId(ItemId itemId, int quantity) {
		this.setItemId(itemId);
		this.setQuantity(quantity);
	}

	// SETTERS
	public void setItemId(ItemId itemId) {
		this.itemId = itemId;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	// GETTERS
	public ItemId getItemId() {
		return this.itemId;
	}
	public int getQuantity() {
		return this.quantity;
	}

	@Override
	public String toString() {
		return "Sales listing ID <" + this.getItemId() + ", " + this.getQuantity() + ">";
	}
	@Override
	public boolean equals(Object obj) {
		SaleListingId otherId = (SaleListingId) obj;
		return (this.itemId.equals(otherId.itemId) && this.quantity == otherId.quantity);
	}
}
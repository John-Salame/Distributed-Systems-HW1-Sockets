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

import com.jsala.common.transport.serialize.SerializeStringArray;

import java.io.*;

public class SaleListing {
	private int dbId; // the primary key in the database, used for determinism
	private SaleListingId id;
	private int quantityRemaining; // how many have not yet sold
	boolean isRemoved;

	// CONSTRUCTORS
	public SaleListing() {
		this.dbId = -1;
		this.id = new SaleListingId();
		this.isRemoved = false;
	}
	public SaleListing(ItemId itemId, int quantity) {
		this.dbId = -1;
		this.id = new SaleListingId(itemId, quantity);
		this.initializeQuantity(quantity);
		this.isRemoved = false;
	}
	public SaleListing(SaleListingId id) {
		this.dbId = -1;
		this.id = id;
		this.initializeQuantity(id.getQuantity());
		this.isRemoved = false;
	}

	// VALIDATION
	public static void validateDbId(int dbId) throws IllegalArgumentException {
		if(dbId < 1) {
			throw new IllegalArgumentException("Sale Listing database id must be positive");
		}
	}
	public static void validateQuantityRemaining(int quantityRemaining) {
		if(quantityRemaining < 0) {
			throw new IllegalArgumentException("Sale Listing quantity remaining must be non-negative");
		}
	}

	// SETTERS
	/**
	 * Method setDbId
	 * Precondition: The database id has not been initialized.
	 * The database id number is the primary key and thus is immutable once it has been initialized.
	 */
	public void setDbId(int dbId) throws IllegalArgumentException, IllegalStateException {
		if(this.dbId != -1) {
			throw new IllegalStateException("Error: Sale Listing database id cannot be changed after the sale listing has been created.");
		}
		// allow the database id to be -1 (helps with creating an item and passing it over the network before you know its database id)
		if (dbId != -1) {
			validateDbId(dbId);
		}
		this.dbId = dbId;
	}
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
	public int decrementQuantity(int q) throws IllegalStateException {
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
	public int getDbId() { return this.dbId; }
	public SaleListingId getId() {
		return this.id;
	}
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
	public boolean isAvailable() {
		return !this.isRemoved() && !this.isSoldOut();
	}

	public static byte[] serialize(SaleListing saleListing) throws IOException {
		// dbId, saleListingId, quantityRemaining, isRemoved
		ByteArrayOutputStream buf = new ByteArrayOutputStream(); // grow dynamically
		DataOutputStream writer = new DataOutputStream(buf);
		writer.writeInt(saleListing.getDbId());
		byte[] saleListingIdSer = SaleListingId.serialize(saleListing.getId());
		writer.write(saleListingIdSer);
		writer.writeInt(saleListing.getQuantityRemaining());
		writer.writeBoolean((saleListing.isRemoved()));
		byte ret[] = buf.toByteArray();
		writer.close();
		buf.close();
		return ret;
	}
	public static SaleListing deserialize(byte[] b) throws IOException, IllegalArgumentException {
		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream reader = new DataInputStream(buf);
		SaleListing saleListing = deserializeFromStream(reader);
		reader.close();
		buf.close();
		return saleListing;
	}
	public static SaleListing deserializeFromStream(DataInputStream reader) throws IOException, IllegalArgumentException {
		int dbId = reader.readInt();
		SaleListingId saleListingId = SaleListingId.deserializeFromStream(reader);
		int quantityRemaining = reader.readInt();
		boolean isRemoved = reader.readBoolean();
		SaleListing saleListing = new SaleListing(saleListingId);
		saleListing.setDbId(dbId);
		int originalQuantity = saleListing.getOriginalQuantity();
		saleListing.decrementQuantity(originalQuantity - quantityRemaining);
		if (isRemoved) {
			saleListing.removeListing();
		}
		return saleListing;
	}

	public static byte[] serializeArray(SaleListing[] saleListings) throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream(); // grow dynamically
		DataOutputStream writer = new DataOutputStream(buf);
		short numElements = (short) saleListings.length;
		writer.writeShort(numElements);
		for(short i = 0; i < numElements; i++) {
			byte[] saleListingSer = SaleListing.serialize(saleListings[i]);
			writer.write(saleListingSer);
		}
		byte ret[] = buf.toByteArray();
		writer.close();
		buf.close();
		return ret;
	}

	public static SaleListing[] deserializeArray(byte[] b) throws IOException, IllegalArgumentException {
		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream reader = new DataInputStream(buf);
		short numElements = reader.readShort();
		SaleListing[] saleListings = new SaleListing[numElements];
		for(short i = 0; i < numElements; i++) {
			saleListings[i] = SaleListing.deserializeFromStream(reader);
		}
		reader.close();
		buf.close();
		return saleListings;
	}
	
	@Override
	public String toString() {
		String ret = "Sales Listing for item (DB ID " + this.getDbId() + ") " + this.getItemId().toString() + "\n";
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

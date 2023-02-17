/**
 * Class ItemId
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: A composite key contiaining Item category and serial id
 */

package common;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class ItemId {
	private int category;
	private int serial;

	// CONSTRUCTORS
	// Default constructor; should not be used, as other parts of code check for null ItemId rather than checking for these default values
	public ItemId() {
		this.initializeDefaults();
	}
	public ItemId(int category, int serial) {
		this.initializeDefaults();
		this.setCategory(category);
		this.setSerial(serial);
	}

	private void initializeDefaults() {
		this.category = -1;
		this.serial = -1;
	}

	/**
	 * Throw an IllegalArgumentException if the category is invalid
	 */
	public static void validateCategory(int category) {
		if(category < 0 || category > 9) {
			throw new IllegalArgumentException("Category must be in range 0-9");
		}
	}

	// SETTERS
	/**
	 * Method setCategory
	 * Precondition: The item id has not been initialized.
	 * The category is part of the primary key and thus is immutable once the item id has been initialized.
	 */
	public void setCategory(int category) {
		this.validateCategory(category);
		if(this.category != -1) {
			throw new IllegalStateException("Error: Item category cannot be changed after the item has been created.");
		}
		this.category = category;
	}
	/**
	 * Method setSerial
	 * Precondition: The item id has not been initialized.
	 * The serial number is part of the primary key and thus is immutable once the item id has been initialized.
	 */
	public void setSerial(int serial) {
		if(this.serial != -1) {
			throw new IllegalStateException("Error: Item serial number cannot be changed after the item has been created.");
		}
		this.serial = serial;
	}

	// GETTERS
	public int getCategory() {
		return this.category;
	}
	public int getSerial() {
		return this.serial;
	}

	public static byte[] serialize(ItemId itemId) throws IOException {
		// category, serial
		ByteArrayOutputStream buf = new ByteArrayOutputStream(2*Integer.BYTES);
		DataOutputStream writer = new DataOutputStream(buf);
		writer.writeInt(itemId.getCategory());
		writer.writeInt(itemId.getSerial());
		byte ret[] = buf.toByteArray();
		writer.close();
		buf.close();
		return ret;
	}
	public static ItemId deserialize(byte[] b) throws IOException {
		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream reader = new DataInputStream(buf);
		ItemId itemId = deserializeFromStream(reader);
		reader.close();
		buf.close();
		return itemId;
	}
	public static ItemId deserializeFromStream(DataInputStream reader) throws IOException {
		int category = reader.readInt();
		int serial = reader.readInt();
		ItemId itemId = new ItemId(category, serial);
		return itemId;
	}

	@Override
	public String toString() {
		return "<" + this.category + ", " + this.serial + ">";
	}
	@Override
	public boolean equals(Object other) {
		ItemId otherItemId = (ItemId) other;
		return (this.category == otherItemId.category && this.serial == otherItemId.serial);
	}
 }

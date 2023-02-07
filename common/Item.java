/**
 * Class Item
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Item class common to client and server
 */

package common;

public class Item {
	private String name; // up to 32 characters
	private ItemId id; // unique provided by server; the data type might change. I need to ask about it.
	private String[] keywords;
	private int numKeywords;
	private static final int MAX_KEYWORDS = 5;
	private String condition; // New or Used
	private float price;
	private int sellerId; // the seller is intrinsically tied to the item since he can change the price

	// CONSTRUCTORS
	public Item() {
		initializeDefaults();
	}
	public Item(int sellerId) {
		initializeDefaults();
		this.sellerId = sellerId;
	}

	private void initializeDefaults() {
		this.name = "";
		this.id = new ItemId();
		this.keywords = new String[MAX_KEYWORDS];
		this.numKeywords = 0;
		this.condition = "Used";
		this.price = (float) 0.00;
		this.sellerId = 0; // impossible seller ID
	}

	/**
	 * Call this method after adding the item to the database.
	 */
	/*
	public void createId(int serial) {
		assert this.id == null;
		this.id = new ItemId(this.category, serial);
	}
	*/

	/**
	 * Method listKeywords()
	 * Represent the list of keywords as a string.
	 */
	public String listKeywords() {
		String result = "";
		for(int i = 0; i < numKeywords; i++) {
			result += keywords[i];
			if(i < numKeywords - 1) {
				result += ", ";
			}
		}
		return result;
	}

	// SETTERS
	public void setName(String name) {
		if(name.length() > 32) {
			throw new IllegalArgumentException("Item name is too long");
		}
		this.name = name;
	}
	public void setCategory(int category) {
		this.id.setCategory(category);
	}
	public void setSerial(int serial) {
		this.id.setSerial(serial);
	}
	public void setId(int category, int serial) {
		this.id.setCategory(category);
		this.id.setSerial(serial);
	}
	public void addKeyword(String keyword) {
		// if we can add no more keywords, do nothing; if we want to alert the seller, then change this logic
		if(numKeywords < MAX_KEYWORDS) {
			// limit keywords to 8 characters
			if(keyword.length() > 8) {
				// alternative would be to truncate the keyword
				throw new IllegalArgumentException("Item keywords must be 8 characters or less");
			}
			this.keywords[numKeywords++] = keyword;
		}
	}
	public void setCondition(String cond) {
		if(cond == "New" || cond == "Used") {
			this.condition = cond;
		}
		else {
			throw new IllegalArgumentException("Condition must be New or Used");
		}
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}

	// GETTERS
	public String getName() {
		return this.name;
	}
	public int getCategory() {
		return this.id.getCategory();
	}
	public int getSerial() {
		return this.id.getSerial();
	}
	public ItemId getId() {
		return this.id;
	}
	// return a copy of the keywords array without any blank keywords
	public String[] getKeywords() {
		String keywordsCopy[] = new String[this.numKeywords];
		for(int i = 0; i < numKeywords; i++) {
			keywordsCopy[i] = this.keywords[i];
		}
		return keywordsCopy;
	}
	public String getCondition() {
		return this.condition;
	}
	public float getPrice() {
		return this.price;
	}
	public int getSellerId() {
		return this.sellerId;
	}

	// TO-DO: Make a byte array
	public String serialize() {
		return "";
	}
	public static Item deserialize(String rawData) {
		return new Item();
	}

	@Override
	public String toString() {
		return "Name: " + this.getName() + "\n" +
			"Item ID: " + this.getId() + "\n" + 
			"Keywords: " + listKeywords() + "\n" + 
			"Condition: " + this.getCondition() + "\n" +
			"Price: " + this.getPrice() + "\n";
	}
}

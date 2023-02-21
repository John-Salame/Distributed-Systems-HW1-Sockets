/**
 * Class Item
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Item class common to client and server
 */

package common;
import common.Seller;
import common.transport.serialize.SerializeStringArray;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Item {
	private String name; // up to 32 characters
	private ItemId id; // unique provided by server
	private String[] keywords;
	private int numKeywords;
	private static final int MAX_KEYWORDS = 5;
	private String condition; // New or Used
	private float price;
	private int sellerId; // the seller is intrinsically tied to the item since they can change the price

	// CONSTRUCTORS
	public Item() {
		initializeDefaults();
	}
	public Item(int sellerId) throws IllegalArgumentException {
		initializeDefaults();
		this.setSellerId(sellerId);
	}
	public Item(String name, ItemId id, String[] keywords, String condition, float price, int sellerId) throws IllegalArgumentException {
		this.setName(name);
		this.id = id; // assume that the constraints in the creation of itemId have provided our validation for us
		this.keywords = new String[MAX_KEYWORDS];
		this.addKeywords(keywords);
		this.setCondition(condition);
		this.setPrice(price);
		this.setSellerId(sellerId);
	}
	// use this constructor for client-side request
	public Item(String name, int category, String[] keywords, String condition, float price) throws IllegalArgumentException {
		this.initializeDefaults();
		this.setName(name);
		this.setCategory(category);
		this.addKeywords(keywords);
		this.setCondition(condition);
		this.setPrice(price);
		// note: sellerId is still 0 and itemId is incomplete
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

	// VALIDATORS
	public static void validateName(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("Item name must not be null");
		}
		if (name.length() > 32) {
			throw new IllegalArgumentException("Item name is too long");
		}
	}
	public static void validateCategory(int category) throws IllegalArgumentException {
		ItemId.validateCategory(category);
	}
	public static void validateSerial(int serial) throws IllegalArgumentException {
		ItemId.validateSerial(serial);
	}
	public static void validatePrice(float price) throws IllegalArgumentException {
		if (price < 0.0) {
			throw new IllegalArgumentException("Price cannot be negative");
		}
	}
	// return the keyword, truncated to fit the max length
	public static String validateKeyword(String keyword) throws IllegalArgumentException {
		if (keyword == null) {
			throw new IllegalArgumentException("Keyword should not be null");
		}
		else if (keyword.equals("")) {
			throw new IllegalArgumentException("Keyword should be non-empty string");
		}
		if(keyword.length() > 8) {
			return keyword.substring(0,8);
		}
		return keyword;
	}
	public static void validateCondition(String condition) throws IllegalArgumentException {
		if (condition == null) {
			throw new IllegalArgumentException("Item has null condition");
		}
		if(! condition.equals("New") && ! condition.equals("Used")) {
			throw new IllegalArgumentException("Condition must be New or Used (uppercase matters)");
		}
	}
	public static void validateSellerId(int sellerId) throws IllegalArgumentException {
		Seller.validateId(sellerId);
	}
	

	// SETTERS
	public void setName(String name) throws IllegalArgumentException {
		validateName(name);
		this.name = name;
	}
	public void setCategory(int category) throws IllegalArgumentException, IllegalStateException {
		this.id.setCategory(category);
	}
	public void setSerial(int serial) throws IllegalArgumentException, IllegalStateException {
		this.id.setSerial(serial);
	}
	public void setId(int category, int serial) throws IllegalArgumentException, IllegalStateException {
		this.id.setCategory(category);
		this.id.setSerial(serial);
	}
	// truncate the keyword if it is too large
	public void addKeyword(String keyword) {
		// if we can add no more keywords, do nothing; if we want to alert the seller, then change this logic
		if(this.numKeywords < MAX_KEYWORDS) {
			// limit keywords to 8 characters (truncate)
			try {
				this.keywords[this.numKeywords] = validateKeyword(keyword);
				this.numKeywords++;
			} catch (IllegalArgumentException e) {
				System.out.println("Error adding keyword: " + e);
			}
		}
	}
	public void addKeywords(String[] keywords) {
		for(String keyword : keywords) {
			this.addKeyword(keyword);
		}
	}
	public void setCondition(String cond) throws IllegalArgumentException {
		validateCondition(cond);
		this.condition = cond;
	}
	public void setPrice(float price) throws IllegalArgumentException {
		validatePrice(price);
		this.price = price;
	}
	public void setSellerId(int sellerId) throws IllegalArgumentException {
		validateSellerId(sellerId);
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
		for(int i = 0; i < this.numKeywords; i++) {
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

	public static byte[] serialize(Item item) throws IOException {
		// name, itemId, keywords, condition, price, sellerId
		ByteArrayOutputStream buf = new ByteArrayOutputStream(); // grow dynamically
		DataOutputStream writer = new DataOutputStream(buf);
		writer.writeUTF(item.getName());
		byte[] itemIdSer = ItemId.serialize(item.getId());
		writer.write(itemIdSer);
		byte[] keywordsSer = SerializeStringArray.serialize(item.getKeywords()); // could throw IllegalArgumentException
		writer.write(keywordsSer);
		writer.writeUTF(item.getCondition());
		writer.writeFloat(item.getPrice());
		writer.writeInt(item.getSellerId());
		byte ret[] = buf.toByteArray();
		writer.close();
		buf.close();
		return ret;
	}
	public static Item deserialize(byte[] b) throws IOException, IllegalArgumentException {
		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream reader = new DataInputStream(buf);
		Item item = deserializeFromStream(reader);
		reader.close();
		buf.close();
		return item;
	}
	public static Item deserializeFromStream(DataInputStream reader) throws IOException, IllegalArgumentException {
		String name = reader.readUTF();
		ItemId itemId = ItemId.deserializeFromStream(reader);
		String[] keywords = SerializeStringArray.deserializeFromStream(reader);
		String condition = reader.readUTF();
		float price = reader.readFloat();
		int sellerId = reader.readInt();
		Item item = new Item(name, itemId, keywords, condition, price, sellerId);
		return item;
	}

	public static byte[] serializeArray(Item[] items) throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream(); // grow dynamically
		DataOutputStream writer = new DataOutputStream(buf);
		short numElements = (short) items.length;
		writer.writeShort(numElements);
		for(short i = 0; i < numElements; i++) {
			byte[] itemSer = Item.serialize(items[i]);
			writer.write(itemSer);
		}
		byte ret[] = buf.toByteArray();
		writer.close();
		buf.close();
		return ret;
	}

	public static Item[] deserializeArray(byte[] b) throws IOException, IllegalArgumentException {
		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream reader = new DataInputStream(buf);
		short numElements = reader.readShort();
		Item[] items = new Item[numElements];
		for(short i = 0; i < numElements; i++) {
			items[i] = Item.deserializeFromStream(reader);
		}
		reader.close();
		buf.close();
		return items;
	}

	@Override
	public String toString() {
		return "Name: " + this.getName() + "\n" +
			"Item ID: " + this.getId() + "\n" + 
			"Keywords: " + listKeywords() + "\n" + 
			"Condition: " + this.getCondition() + "\n" +
			"Price: " + this.getPrice() + "\n" + 
			"Seller ID: " + this.getSellerId() + "\n";
	}
}

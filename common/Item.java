package common;

public class Item {
	private String name; // up to 32 characters
	private int category; // integer 0-9
	private int id; // unique provided by server
	private String[] keywords;
	private int numKeywords;
	private static final int MAX_KEYWORDS = 5;
	private String condition; // New or Used
	private float price;

	// default constructor
	public Item() {
		this.name = "";
		this.category = 0;
		this.id = 0;
		this.keywords = new String[MAX_KEYWORDS];
		this.numKeywords = 0;
		this.condition = "Used";
		this.price = (float) 0.00;
	}

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
	public void setCategory(int cat) {
		if(cat < 0 || cat > 9) {
			throw new IllegalArgumentException("Category must be in range 0-9");
		}
		this.category = cat;
	}
	public void setId(int id) {
		this.id = id;
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

	// GETTERS
	public String getName() {
		return this.name;
	}
	public int getCategory() {
		return this.category;
	}
	public int getId() {
		return this.id;
	}
	public String[] getKeywords() {
		return this.keywords;
	}
	public String getCondition() {
		return this.condition;
	}
	public float getPrice() {
		return this.price;
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
		return "Name: " + this.name + "\n" +
			"Item ID: " + this.id + "\n" + 
			"Category: " + this.category + "\n" +
			"Keywords: " + listKeywords() + "\n" + 
			"Condition: " + this.condition + "\n" +
			"Price: " + this.price + "\n";
	}
}
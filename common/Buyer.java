package common;

public class Buyer {
	private String name; // provided by seller during account creation
	private int id; // provided by server during account creation
	private int numPurchases; // maintained by server

	// CONSTRUCTOR
	public Buyer() {
		this.name = "";
		this.id = 0;
		this.numPurchases = 0;
	}

	// SETTERS
	public void setName(String name) {
		if(name.length() > 32) {
			throw new IllegalArgumentException("Buyer name is too long");
		}
		this.name = name;
	}
	public void setId(int id) {
		this.id = id;
	}
	//temporary solution
	public void setNumPurchases(int num) {
		this.numPurchases = num;
	}

	// GETTERS
	public String getName() {
		return this.name;
	}
	public int getId() {
		return this.id;
	}
	public int getNumPurchases() {
		return numPurchases; // temporary implementation
	}

	// TO-DO: Make a byte array
	public String serialize() {
		return "";
	}
	public static Buyer deserialize(String rawData) {
		return new Buyer();
	}

	@Override
	public String toString() {
		return "Name: " + this.name + "\n" +
			"Buyer ID: " + this.id + "\n" + 
			"Items purchased: " + this.numPurchases + "\n";
	}
}
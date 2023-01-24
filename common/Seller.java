package common;

public class Seller {
	private String name;
	private int id;
	private int[] feedback;
	private int numSold;

	// default constructor
	public Seller() {
		this.name = "";
		this.id = 0;
		feedback = new int[2];
		this.feedback[0] = 0;
		this.feedback[1] = 0;
		this.numSold = 0;
	}

	// SETTERS
	public void setName(String name) {
		if(name.length() > 32) {
			throw new IllegalArgumentException("Seller name is too long");
		}
		this.name = name;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void addThumbsUp() {
		this.feedback[0] += 1;
	}
	public void addThumbsDown() {
		this.feedback[1] += 1;
	}
	// temporary solution
	public void setNumSold(int num) {
		this.numSold = num;
	}

	// GETTERS
	public String getName() {
		return this.name;
	}
	public int getId() {
		return this.id;
	}
	public int[] getFeedback() {
		return this.feedback;
	}
	public int getNumSold() {
		return this.numSold;
	}

	// TO-DO: Make a byte array
	public String serialize() {
		return "";
	}
	public static Seller deserialize(String rawData) {
		return new Seller();
	}

	@Override
	public String toString() {
		return "Name: " + this.name + "\n" +
			"Seller ID: " + this.id + "\n" + 
			"Feedback:\n" + 
			"  Positive: " + this.feedback[0] + "\n" +
			"  Negative: " + this.feedback[1] + "\n" +
			"Items sold: " + this.numSold + "\n";
	}
}
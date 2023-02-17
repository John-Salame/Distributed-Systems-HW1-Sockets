/**
 * Class Seller
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Seller class common to client and server
 */

package common;
import common.transport.serialize.SerializeIntArray;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Seller {
	private String name;
	private String password;
	private int id;
	private int[] feedback;
	private int numSold;

	// default constructor
	public Seller() {
		this.name = "";
		this.password = "";
		this.id = 0;
		feedback = new int[2];
		this.feedback[0] = 0;
		this.feedback[1] = 0;
		this.numSold = 0;
	}

	// Check if the login credentials match this user
	public boolean isMyLoginCredentials(String username, String password) {
		return (this.name.equals(username) && this.password.equals(password));
	}
	public String displayFeedbackSelf() {
		return displayFeedback(this.feedback);
	}
	public static String displayFeedback(int[] feedback) {
		return "Feedback:\n" + 
			"  Positive: " + feedback[0] + "\n" +
			"  Negative: " + feedback[1] + "\n";
	}

	// SETTERS
	public void setName(String name) {
		if(name.length() > 32) {
			throw new IllegalArgumentException("Seller name is too long");
		}
		this.name = name;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public static byte[] serialize(Seller seller) throws IOException {
		// name, id, feedback, numSold
		ByteArrayOutputStream buf = new ByteArrayOutputStream(); // grow dynamically
		DataOutputStream writer = new DataOutputStream(buf);
		writer.writeUTF(seller.getName());
		writer.writeInt(seller.getId());
		byte[] feedback = SerializeIntArray.serialize(seller.getFeedback());
		writer.write(feedback);
		writer.writeInt(seller.getNumSold());
		byte ret[] = buf.toByteArray();
		writer.close();
		buf.close();
		return ret;
	}
	public static Seller deserialize(byte[] b) throws IOException {
		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream reader = new DataInputStream(buf);
		Seller seller = new Seller();
		seller.setName(reader.readUTF());
		seller.setId(reader.readInt());
		seller.feedback = SerializeIntArray.deserializeFromStream(reader);
		seller.setNumSold(reader.readInt());
		reader.close();
		buf.close();
		return seller;
	}

	@Override
	public String toString() {
		return "Name: " + this.getName() + "\n" +
			"Seller ID: " + this.getId() + "\n" + 
			this.displayFeedbackSelf() +
			"Items sold: " + this.getNumSold() + "\n";
	}
}

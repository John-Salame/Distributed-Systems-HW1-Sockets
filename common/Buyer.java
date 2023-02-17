/**
 * Class Buyer
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Buyer class common to client and server
 */

package common;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Buyer {
	private String name; // provided by buyer during account creation
	private String password; // provided by buyer during account creation
	private int id; // provided by server during account creation
	private int numPurchases; // maintained by server

	// CONSTRUCTOR
	public Buyer() {
		this.name = "";
		this.password = "";
		this.id = 0;
		this.numPurchases = 0;
	}
	public Buyer(String name, String password, int id, int numPurchases) {
		this.setName(name);
		this.setPassword(password);
		this.setId(id);
		this.setNumPurchases(numPurchases);
	}

	// Check if the login credentials match this user
	public boolean isMyLoginCredentials(String username, String password) {
		return (this.name.equals(username) && this.password.equals(password));
	}

	// SETTERS
	public void setName(String name) {
		if(name.length() > 32) {
			throw new IllegalArgumentException("Buyer name is too long");
		}
		this.name = name;
	}
	public void setPassword(String password) {
		this.password = password;
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
		return numPurchases;
	}

	public static byte[] serialize(Buyer buyer) throws IOException {
		// name, id, numPurchases
		ByteArrayOutputStream buf = new ByteArrayOutputStream(); // grow dynamically
		DataOutputStream writer = new DataOutputStream(buf);
		writer.writeUTF(buyer.getName());
		writer.writeInt(buyer.getId());
		writer.writeInt(buyer.getNumPurchases());
		byte ret[] = buf.toByteArray();
		writer.close();
		buf.close();
		return ret;
	}
	public static Buyer deserialize(byte[] b) throws IOException {
	ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream reader = new DataInputStream(buf);
		Buyer buyer = new Buyer();
		buyer.setName(reader.readUTF());
		buyer.setId(reader.readInt());
		buyer.setNumPurchases(reader.readInt());
		reader.close();
		buf.close();
		return buyer;
	}

	@Override
	public String toString() {
		return "Name: " + this.getName() + "\n" +
			"Buyer ID: " + this.getId() + "\n" + 
			"Items purchased: " + this.getNumPurchases() + "\n";
	}
}

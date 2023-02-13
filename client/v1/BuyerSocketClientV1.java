/**
 * Class BuyerSocketClientV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of buyer client-server IPC on client side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package client.v1;
import common.BuyerInterface;
import common.transport.serialize.*;
import common.transport.socket.APIEnumV1;
import common.transport.socket.BuyerEnumV1;
import common.transport.socket.PacketPrefix;
import common.transport.socket.SocketMessage;
import common.Item;
import java.net.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BuyerSocketClientV1 implements BuyerInterface {
	private final short apiVer = 1;
	private final int api = APIEnumV1.BUYER.ordinal();
	private PacketPrefix packetPrefix; // use this to add important metadata to messages over the socket
	private Socket socket = null;
	private String serverIp;
	private int serverPort;
	private DataOutputStream out; // use this to write to the socket
	private DataInputStream in; // use this to read from the socket

	// CONSTRUCTORS
	// recommend serverIp = localhost
	public BuyerSocketClientV1(String serverIp, int serverPort) {
		this.packetPrefix = new PacketPrefix(this.apiVer, this.api);
		this.setup(serverIp, serverPort);
	}

	// New Methods
	private byte[] send(byte[] b, int funcId) {
		// fault tolerance -- let the client make new requests again after logging out
		if(socket == null) {
			this.setup(this.serverIp, this.serverPort);
		}
		// currently no resiliency for sending message once socket connection has been created
		try {
			byte[] msg = packetPrefix.prependPrefix(b, funcId); // prepare the message
			System.out.println("Sending " + new PacketPrefix((short) b.length, this.apiVer, this.api, funcId));
			this.out.write(msg); // send the message over the socket
		} catch (IOException i) {
			System.out.println(i);
		}
		// wait for a response
		return new byte[0];
	}
	private void cleanup() {
		try {
			this.in.close();
			this.out.close();
			this.socket.close();
		} catch (IOException i) {
			System.out.println(i);
		}
		this.socket = null;
	}
	public void setup(String serverIp, int serverPort) {
		if(this.socket != null) {
			return;
		}
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		// try to connect
		boolean setupComplete = false;
		int numFailures = 0;
		while(!setupComplete && numFailures < 5) {
			try {
				this.socket = new Socket(serverIp, serverPort);
				System.out.println("Connected to host " + serverIp + ", port " + serverPort);
				this.in = new DataInputStream(socket.getInputStream());
				this.out = new DataOutputStream(socket.getOutputStream());
				setupComplete = true;
				return;
			}
			catch (UnknownHostException u) {
				System.out.println(u);
			}
			catch (IOException i) {
				System.out.println(i);
			}
			numFailures++;
			// https://stackoverflow.com/questions/24104313/how-do-i-make-a-delay-in-java
			// sleep for a second in between connection attempts
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println(e);
				cleanup();
				return;
			}
		}
		throw new RuntimeException("Buyer client failed to connect.");
	}
	// send the message and return the response
	public byte[] sendAndReceive(byte[] msg, int funcId) throws IOException {
		this.send(msg, funcId);
		// wait for response and parse response
		SocketMessage inMsg = SocketMessage.readAndSplit(this.in);
		return inMsg.getMsg();
	}

	// Inherited Methods
	public int createUser(String username, String password) {
		int funcId = BuyerEnumV1.CREATE_USER.ordinal();
		int userId = 0;
		try {
			byte[] msg = SerializeLogin.serialize(username, password);
			byte[] buf = this.sendAndReceive(msg, funcId);
			userId = SerializeInt.deserialize(buf);
		}
		catch (IOException i) {
			System.out.println(i);
			return 0; // user id 0 indicates error
		}
		return userId;
	}
	public String login(String username, String password) {
		int funcId = BuyerEnumV1.LOGIN.ordinal();
		String sessionToken = null;
		try {
			byte[] msg = SerializeLogin.serialize(username, password);
			byte[] buf = this.sendAndReceive(msg, funcId);
			sessionToken = SerializeString.deserialize(buf);
		}
		catch (IOException i) {
			System.out.println(i);
		}
		return sessionToken;
	}
	// clean up (close buffers and close the connection) when finished
	public void logout(String sessionToken) {
		int funcId = BuyerEnumV1.LOGOUT.ordinal();
		try {
			byte[] msg = SerializeString.serialize(sessionToken);
			byte[] buf = this.sendAndReceive(msg, funcId);
			assert buf.length == 0; // server sending empty response after packet prefix
		}
		catch (IOException i) {
			System.out.println(i);
		}
		this.cleanup();
	}
	public int[] getSellerRating(int sellerId) {
		int funcId = BuyerEnumV1.GET_SELLER_RATING.ordinal();
		int[] rating = null;
		try {
			byte[] msg = SerializeInt.serialize(sellerId);
			byte[] buf = this.sendAndReceive(msg, funcId);
			rating = SerializeIntArray.deserialize(buf);
		}
		catch (IOException i) {
			System.out.println(i);
		}
		return rating;
	}
	public Item[] searchItem(String sessionToken, int category, String[] keywords) {
		int funcId = BuyerEnumV1.SEARCH_ITEM.ordinal();
		throw new RuntimeException("Method not implemented BuyerSocketClientV1: searchItem()");
	}
	// public abstract void addToCart(String sessionToken, ItemId itemId, int quantity);
	// public abstract void removeFromCart(String sessionToken, ItemId itemId, int quantity);
	// public abstract void clearCart(String sessionToken);
	// public abstract String displayCart(String sessionToken); // return a string that you can display later
	/**
	 * For feedback, rating=1 is thumbs up and rating=-1 is thumbs down.
	 * A user should only be able to provide feedback for an item once.
	 * Maybe return a String so I can give a proper message if the user attempts to provide feedback more than once.
	 */
	// public abstract void provideFeedback(String sessionToken, ItemId itemId, int rating); // TO-DO: Figure out how to limit to one vote
	// public abstract Purchase[] getPurchaseHistory(String sessionToken);
}

/**
 * Class SellerSocketClientV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of seller client-server IPC on client side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package client.v1;
import common.SellerInterface;
import common.transport.serialize.*;
import common.transport.socket.SellerEnumV1;
import common.transport.socket.PacketPrefix;
import common.transport.socket.SocketMessage;
import common.Item;
import java.net.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class SellerSocketClientV1 implements SellerInterface {
	private final short apiVer = 1;
	private PacketPrefix packetPrefix; // use this to add important metadata to messages over the socket
	private Socket socket = null;
	private String serverIp;
	private int serverPort;
	private DataOutputStream out; // use this to write to the socket
	private DataInputStream in; // use this to read from the socket

	// CONSTRUCTORS
	// recommend serverIp = localhost
	public SellerSocketClientV1(String serverIp, int serverPort) {
		this.packetPrefix = new PacketPrefix(this.apiVer);
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
			System.out.println("Sending " + new PacketPrefix((short) b.length, this.apiVer, funcId));
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
		throw new RuntimeException("Seller client failed to connect.");
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
		int funcId = SellerEnumV1.CREATE_USER.ordinal();
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
		int funcId = SellerEnumV1.LOGIN.ordinal();
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
		int funcId = SellerEnumV1.LOGOUT.ordinal();
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
		int funcId = SellerEnumV1.GET_SELLER_RATING.ordinal();
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
	public void putOnSale(String sessionToken, Item item, int quantity) {
		throw new RuntimeException("SellerSocketClientV1: putOnSale() Not implemented");
	}
	/*
	public void changePriceOfItem(String sessionToken, ItemId itemId, float newPrice);
	public void removeItemFromSale(String sessionToken, ItemId itemId, int quantity);
	public String displayItemsOnSale(String sessionToken);
	*/
}

/**
 * Class PacketPrefix
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Create a prefix for messages that go over the socket.
 * Format of the prefix is bytes(message size, api version, function id)
 * This class can also be used to parse the prefix of an incoming message on a listening socket
 */

package common.transport.socket;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PacketPrefix {
	public static final int PREFIX_SIZE = 2*Short.BYTES + 2*Integer.BYTES; // how many bytes the prefix takes up
	private short msgSize; // total length of the message (the message which follows the prefix, not including the prefix)
	private short apiVer; // API version associated with the message
	private int api; // which API this is for (buyer, seller, various DAOs, etc.)
	private int funcId; // identifier of the function

	// CONSTRUCTORS

	// this constructor is useful for client sockets who always use the same API version
	public PacketPrefix(short apiVer, int api) {
		this.apiVer = apiVer;
		this.api = api;
	}
	// this constructor is good for generating a prefix from a message you receive
	public PacketPrefix(short msgSize, short apiVer, int api, int funcId) {
		this.msgSize = msgSize;
		this.apiVer = apiVer;
		this.api = api;
		this.funcId = funcId;
	}

	// Methods

	// Return the prefix as a byte[] array
	public byte[] generatePrefix(short msgSize, int funcId) throws IOException {
		ByteArrayOutputStream prefix = new ByteArrayOutputStream(PREFIX_SIZE);
		DataOutputStream writer = new DataOutputStream(prefix); // write to underlying OutputStream "prefix"
		writer.writeShort(msgSize);
		writer.writeShort(this.apiVer);
		writer.writeInt(this.api);
		writer.writeInt(funcId);
		// writer.flush();
		byte[] ret = prefix.toByteArray();
		writer.close();
		prefix.close();
		return ret;
	}

	// Return a byte array with the prefix added to the beginning of the input byte array
	public byte[] prependPrefix(byte[] b, int funcId) throws IOException {
		//TO-DO: Throw an exception if length is too long to fit in a short
		short msgSize = (short) b.length; // size of the original message
		ByteArrayOutputStream msg = new ByteArrayOutputStream(PREFIX_SIZE + msgSize);
		DataOutputStream writer = new DataOutputStream(msg); // write to underlying OutputStream "prefix"
		writer.write(generatePrefix(msgSize, funcId));
		writer.write(b);
		byte[] ret = msg.toByteArray();
		writer.close();
		msg.close();
		return ret;
	}
	// pull the metadata out of a message you receive while it is in stream form
	public static PacketPrefix getPrefixFromMessage(DataInputStream stream) throws IOException {
		short msgSize = stream.readShort();
		short apiVer = stream.readShort();
		int api =  stream.readInt();
		int funcId = stream.readInt();
		return new PacketPrefix(msgSize, apiVer, api, funcId);
	}

	// GETTERS
	public short getMsgSize() {
		return this.msgSize;
	}
	public short getApiVer() {
		return this.apiVer;
	}
	public short getApiVersion() {
		return this.apiVer;
	}
	public int getApi() {
		return this.api;
	}
	public int getApiNum() {
		return this.api;
	}
	public int getFuncId() {
		return this.funcId;
	}
	public int getFunctionId() {
		return this.funcId;
	}

	@Override
	public String toString() {
		return "Message size " + this.msgSize + ", API Version " + this.apiVer + ", " + 
			"API " + this.api +  ", Function Identifier " + this.funcId;
	}
}

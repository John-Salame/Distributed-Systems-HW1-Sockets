/**
 * Class SocketMessage
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Split a message from a socket into a prefix and the rest of the message (msg)
 */

package com.jsala.common.transport.socket;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;

public class SocketMessage {
	private PacketPrefix prefix;
	private byte[] msg;

	public SocketMessage(PacketPrefix prefix, byte[] msg) {
		this.prefix = prefix;
		this.msg = msg;
	}

	// read a full message from the socket's DataInputStream and separate it into prefix and the rest of the message
	// returns null on failure (this doesn't seem to be true anymore)
	// throws an exception if the received message should be interpreted as an Exception
	public static SocketMessage readAndSplit(DataInputStream in) throws IOException, SocketException {
		PacketPrefix prefix = null;
		try {
			// read the message from the socket and parse out the metadata
			prefix = PacketPrefix.getPrefixFromMessage(in);
			System.out.println("Received " + prefix);
		} catch (IOException i) {
			System.out.println("SocketMessage readAndSplit() getPrefixFromMessage(): " + i);
			throw i;
		}
		short msgSize = prefix.getMsgSize();
		short apiVer = prefix.getApiVer();
		int api = prefix.getApi();
		int funcId = prefix.getFuncId();
		// read the actual message
		short bytesLeft = msgSize;
		byte[] buf = new byte[msgSize];
		// hopefully the data doesn't corrupt and cause this loop to hang
		while(bytesLeft > 0) {
			try {
				short bytesRead = (short) in.read(buf, 0, bytesLeft);
				bytesLeft -= bytesRead;
			} catch (IOException i) {
				System.out.println("SocketMessage readAndSplit() loop: " + i);
				throw i;
			}
		}
		return new SocketMessage(prefix, buf);
	}

	// GETTERS
	public PacketPrefix getPrefix() {
		return this.prefix;
	}
	public byte[] getMsg() {
		return this.msg;
	}
}

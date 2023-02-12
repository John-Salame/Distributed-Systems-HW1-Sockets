/**
 * Class SocketMessage
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Split a message from a socket into a prefix and the rest of the message (msg)
 */

package common.transport.socket;
import java.io.DataInputStream;
import java.io.IOException;

public class SocketMessage {
	private PacketPrefix prefix;
	private byte[] msg;

	public SocketMessage(PacketPrefix prefix, byte[] msg) {
		this.prefix = prefix;
		this.msg = msg;
	}

	// utility function that seemed like it might fit in this class
	public byte[] createEmptyMessage(short apiVer, int funcId) throws IOException {
		return new PacketPrefix(apiVer).generatePrefix((short) 0, funcId);
	}

	// read a full message from the socket's DataInputStream and separate it into prefix and the rest of the message
	// returns null on failure
	public static SocketMessage readAndSplit(DataInputStream in) throws IOException {
		PacketPrefix prefix = null;
		try {
			prefix = PacketPrefix.getPrefixFromMessage(in);
			System.out.println("Received " + prefix);
		} catch (IOException i) {
			System.out.println(i);
			return null;
		}
		short msgSize = prefix.getMsgSize();
		short apiVer = prefix.getApiVer();
		int funcId = prefix.getFuncId();
		// read the actual message
		short bytesLeft = msgSize;
		byte[] buf = new byte[msgSize];
		while(bytesLeft > 0) {
			try {
				short bytesRead = (short) in.read(buf, 0, bytesLeft);
				bytesLeft -= bytesRead;
			} catch (IOException i) {
				System.out.println(i);
				return null;
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

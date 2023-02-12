/**
 * Class SerializeInt
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: (De)serialize an integer
 */

package common.transport.serialize;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class SerializeInt {

	public static byte[] serialize(int val) throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream(Integer.BYTES);
		DataOutputStream writer = new DataOutputStream(buf);
		writer.writeInt(val);
		byte ret[] = buf.toByteArray();
		writer.close();
		buf.close();
		return ret;
	}

	public static int deserialize(byte[] b) throws IOException {
		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream reader = new DataInputStream(buf);
		int val = reader.readInt();
		reader.close();
		buf.close();
		return val;
	}
}

/**
 * Class SerializeString
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: (De)serialize a String using UTF format
 */

package common.transport.serialize;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class SerializeString {
	
	public static byte[] serialize(String str) throws IOException {
		if (str == null) {
			throw new IOException("SerializeString attempted to serialize null string");
		}
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		DataOutputStream writer = new DataOutputStream(buf);
		writer.writeUTF(str);
		byte ret[] = buf.toByteArray();
		writer.close();
		buf.close();
		return ret;
	}

	public static String deserialize(byte[] b) throws IOException {
		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream reader = new DataInputStream(buf);
		String str = reader.readUTF();
		reader.close();
		buf.close();
		return str;
	}
}

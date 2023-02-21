/**
 * Class SerializeStringArray
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: (De)serialize a String array
 * Format: (short numElements, int[] elements)
 */

package common.transport.serialize;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class SerializeStringArray {

	public static byte[] serialize(String[] strings) throws IOException {
		byte[] ret;
		if (strings == null) {
			throw new IOException("SerializeStringArray attempted to serialize null array");
		}
		short numElements = (short) strings.length;
		ByteArrayOutputStream buf = new ByteArrayOutputStream(); // dynamically grow
		DataOutputStream writer = new DataOutputStream(buf);
		writer.writeShort(numElements);
		try {
			// I don't remember if enumerators on arrays are ordered, so I will access elements by index
			for(short i = 0; i < numElements; i++) {
				writer.writeUTF(strings[i]);
			}
			ret = buf.toByteArray();
			writer.close();
			buf.close();
		} catch (NullPointerException e) {
			writer.close();
			buf.close();
			throw new IOException(e.getMessage());
		}
		return ret;
	}

	public static String[] deserialize(byte[] b) throws IOException {
		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream reader = new DataInputStream(buf);
		String[] ret = deserializeFromStream(reader);
		reader.close();
		buf.close();
		return ret;
	}

	public static String[] deserializeFromStream(DataInputStream reader) throws IOException {
		short numElements = reader.readShort();
		String[] ret = new String[numElements];
		for(short i = 0; i < numElements; i++) {
			ret[i] = reader.readUTF();
		}
		return ret;
	}
}

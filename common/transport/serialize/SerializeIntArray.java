/**
 * Class SerializeIntArray
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: (De)serialize an integer array
 * Format: (short numElements, int[] elements)
 */

package common.transport.serialize;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class SerializeIntArray {

	public static byte[] serialize(int[] ints) throws IOException {
		short numElements = (short) ints.length;
		ByteArrayOutputStream buf = new ByteArrayOutputStream(Short.BYTES + Integer.BYTES * numElements);
		DataOutputStream writer = new DataOutputStream(buf);
		writer.writeShort(numElements);
		// I don't remember if enumerators on arrays are ordered, so I will access elements by index
		for(short i = 0; i < numElements; i++) {
			writer.writeInt(ints[i]);
		}
		byte ret[] = buf.toByteArray();
		writer.close();
		buf.close();
		return ret;
	}

	public static int[] deserialize(byte[] b) throws IOException {
		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream reader = new DataInputStream(buf);
		short numElements = reader.readShort();
		int[] ret = new int[numElements];
		for(short i = 0; i < numElements; i++) {
			ret[i] = reader.readInt();
		}
		reader.close();
		buf.close();
		return ret;
	}
}

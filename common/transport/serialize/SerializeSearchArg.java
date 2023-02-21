/**
 * Class SerializeSearchArg
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: (De)serialize a (sessionToken, category, keywords) combo.
 */

package common.transport.serialize;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class SerializeSearchArg {
	private String sessionToken;
	private int category;
	private String[] keywords;

	public SerializeSearchArg(String sessionToken, int category, String[] keywords) {
		this.sessionToken = sessionToken;
		this.category = category;
		this.keywords = keywords;
	}

	// we are not enforcing username length at this point, though we could.
	public static byte[] serialize(String sessionToken, int category, String[] keywords) throws IOException {
		if (sessionToken == null || keywords == null) {
			throw new IOException("SerializeSearchArg attempted to serialize null arguments");
		}
		ByteArrayOutputStream buf = new ByteArrayOutputStream(); // grow dynamically
		DataOutputStream writer = new DataOutputStream(buf);
		writer.writeUTF(sessionToken);
		writer.writeInt(category);
		byte[] serKeywords = SerializeStringArray.serialize(keywords);
		writer.write(serKeywords);
		byte ret[] = buf.toByteArray();
		writer.close();
		buf.close();
		return ret;
	}

	public static SerializeSearchArg deserialize(byte[] b) throws IOException {
		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream reader = new DataInputStream(buf);
		String sessionToken = reader.readUTF();
		int category = reader.readInt();
		String[] keywords = SerializeStringArray.deserializeFromStream(reader);
		reader.close();
		buf.close();
		return new SerializeSearchArg(sessionToken, category, keywords);
	}


	// GETTERS
	public String getSessionToken() {
		return this.sessionToken;
	}
	public int getCategory() {
		return this.category;
	}
	public String[] getKeywords() {
		return this.keywords;
	}
}

/**
 * Class SerializePriceArgClientServer
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: (De)serialize the arguments for SellerInterface changePriceOfItem()
 */

package common.transport.serialize;
import common.ItemId;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class SerializePriceArgClientServer {
	private String sessionToken;
	private ItemId itemId;
	private float price;

	public SerializePriceArgClientServer() {}
	public SerializePriceArgClientServer(String sessionToken, ItemId itemId, float price) {
		this.sessionToken = sessionToken;
		this.itemId = itemId;
		this.price = price;
	}

	// we are not enforcing username length at this point, though we could.
	public static byte[] serialize(String sessionToken, ItemId itemId, float price) throws IOException {
		if (sessionToken == null) {
			throw new IOException("SerializePriceArgClientServer attempted to serialize null sessionToken");
		}
		ByteArrayOutputStream buf = new ByteArrayOutputStream(); // grow dynamically
		DataOutputStream writer = new DataOutputStream(buf);
		writer.writeUTF(sessionToken);
		byte[] itemIdSer = ItemId.serialize(itemId);
		writer.write(itemIdSer);
		writer.writeFloat(price);
		byte ret[] = buf.toByteArray();
		writer.close();
		buf.close();
		return ret;
	}

	public static SerializePriceArgClientServer deserialize(byte[] b) throws IOException, IllegalArgumentException {
		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream reader = new DataInputStream(buf);
		String sessionToken = reader.readUTF();
		ItemId itemId = ItemId.deserializeFromStream(reader); // can throw IllegalArgumentException
		float price = reader.readFloat();
		reader.close();
		buf.close();
		return new SerializePriceArgClientServer(sessionToken, itemId, price);
	}

	// GETTERS
	public String getSessionToken() {
		return this.sessionToken;
	}
	public ItemId getItemId() {
		return this.itemId;
	}
	public float getPrice() {
		return this.price;
	}
}

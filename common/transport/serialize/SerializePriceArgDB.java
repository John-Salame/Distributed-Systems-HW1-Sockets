/**
 * Class SerializePriceArgDB
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: (De)serialize the arguments for ItemDAO changePrice()
 */

package common.transport.serialize;
import common.ItemId;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class SerializePriceArgDB {
	private ItemId itemId;
	private int sellerId;
	private float price;

	public SerializePriceArgDB() {}
	public SerializePriceArgDB(ItemId itemId, int sellerId, float price) {
		this.itemId = itemId;
		this.sellerId = sellerId;
		this.price = price;
	}

	// we are not enforcing username length at this point, though we could.
	public static byte[] serialize(ItemId itemId, int sellerId, float price) throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream(); // grow dynamically
		DataOutputStream writer = new DataOutputStream(buf);
		byte[] itemIdSer = ItemId.serialize(itemId);
		writer.write(itemIdSer);
		writer.writeInt(sellerId);
		writer.writeFloat(price);
		byte ret[] = buf.toByteArray();
		writer.close();
		buf.close();
		return ret;
	}

	public static SerializePriceArgDB deserialize(byte[] b) throws IOException {
		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream reader = new DataInputStream(buf);
		ItemId itemId = ItemId.deserializeFromStream(reader);
		int sellerId = reader.readInt();
		float price = reader.readFloat();
		reader.close();
		buf.close();
		return new SerializePriceArgDB(itemId, sellerId, price);
	}

	// GETTERS
	public ItemId getItemId() {
		return this.itemId;
	}
	public int getSellerId() {
		return this.sellerId;
	}
	public float getPrice() {
		return this.price;
	}
}

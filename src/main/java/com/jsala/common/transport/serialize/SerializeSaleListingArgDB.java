/**
 * Class SerializeSaleListingArgDB
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * Description: (De)serialize the arguments for putItemOnSale() and removeItemFromSale() between server and database
 */

package com.jsala.common.transport.serialize;

import com.jsala.common.ItemId;

import java.io.*;

public class SerializeSaleListingArgDB {
    private int sellerId;
    private ItemId itemId;
    private int quantity;

    // CONSTRUCTORS
    public SerializeSaleListingArgDB() {}
    public SerializeSaleListingArgDB(int sellerId, ItemId itemId, int quantity) {
        this.sellerId = sellerId;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public static byte[] serialize(int sellerId, ItemId itemId, int quantity) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream(); // grow dynamically
        DataOutputStream writer = new DataOutputStream(buf);
        writer.writeInt(sellerId);
        byte[] itemIdSer = ItemId.serialize(itemId);
        writer.write(itemIdSer);
        writer.writeInt(quantity);
        byte ret[] = buf.toByteArray();
        writer.close();
        buf.close();
        return ret;
    }

    public static SerializeSaleListingArgDB deserialize(byte[] b) throws IOException, IllegalArgumentException {
        ByteArrayInputStream buf = new ByteArrayInputStream(b);
        DataInputStream reader = new DataInputStream(buf);
        int sellerId = reader.readInt();
        ItemId itemId = ItemId.deserializeFromStream(reader); // may throw IllegalArgumentException
        int quantity = reader.readInt();
        reader.close();
        buf.close();
        return new SerializeSaleListingArgDB(sellerId, itemId, quantity);
    }

    public int getSellerId() {
        return sellerId;
    }
    public ItemId getItemId() {
        return itemId;
    }
    public int getQuantity() {
        return quantity;
    }
}

/**
 * Class SerializeSaleListingArg
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * Description: (De)serialize the arguments for putItemOnSale() and removeItemFromSale()
 */

package com.jsala.common.transport.serialize;

import com.jsala.common.ItemId;

import java.io.*;

public class SerializeSaleListingArg {
    private int sellerId;
    private ItemId itemId;
    private int quantity;

    // CONSTRUCTORS
    public SerializeSaleListingArg() {}
    public SerializeSaleListingArg(int sellerId, ItemId itemId, int quantity) {
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

    public static SerializeSaleListingArg deserialize(byte[] b) throws IOException, IllegalArgumentException {
        ByteArrayInputStream buf = new ByteArrayInputStream(b);
        DataInputStream reader = new DataInputStream(buf);
        int sellerId = reader.readInt();
        ItemId itemId = ItemId.deserializeFromStream(reader); // may throw IllegalArgumentException
        int quantity = reader.readInt();
        reader.close();
        buf.close();
        return new SerializeSaleListingArg(sellerId, itemId, quantity);
    }

    public int getSellerId() {
        return sellerId;
    }
    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }
    public ItemId getItemId() {
        return itemId;
    }
    public void setItemId(ItemId itemId) {
        this.itemId = itemId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

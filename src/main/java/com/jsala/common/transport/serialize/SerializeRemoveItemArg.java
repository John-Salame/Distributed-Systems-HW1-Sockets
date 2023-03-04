/**
 * Class SerializeRemoveItemArg
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * Description: (De)serialize the arguments for removeItemFromSale() between client and server
 */


package com.jsala.common.transport.serialize;

import com.jsala.common.ItemId;

import java.io.*;

public class SerializeRemoveItemArg {
    private String sessionToken;
    private ItemId itemId;
    private int quantity;

    // CONSTRUCTORS
    public SerializeRemoveItemArg() {}
    public SerializeRemoveItemArg(String sessionToken, ItemId itemId, int quantity) {
        this.sessionToken = sessionToken;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public static byte[] serialize(String sessionToken, ItemId itemId, int quantity) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream(); // grow dynamically
        DataOutputStream writer = new DataOutputStream(buf);
        writer.writeUTF(sessionToken);
        byte[] itemIdSer = ItemId.serialize(itemId);
        writer.write(itemIdSer);
        writer.writeInt(quantity);
        byte ret[] = buf.toByteArray();
        writer.close();
        buf.close();
        return ret;
    }

    public static SerializeRemoveItemArg deserialize(byte[] b) throws IOException, IllegalArgumentException {
        ByteArrayInputStream buf = new ByteArrayInputStream(b);
        DataInputStream reader = new DataInputStream(buf);
        String sessionToken = reader.readUTF();
        ItemId itemId = ItemId.deserializeFromStream(reader); // may throw IllegalArgumentException
        int quantity = reader.readInt();
        reader.close();
        buf.close();
        return new SerializeRemoveItemArg(sessionToken, itemId, quantity);
    }

    public String getSessionToken() {
        return sessionToken;
    }
    public ItemId getItemId() {
        return itemId;
    }
    public int getQuantity() {
        return quantity;
    }
}

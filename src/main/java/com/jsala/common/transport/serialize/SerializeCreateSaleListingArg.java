/**
 * Class SerializeCreateSaleListingArg
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * Description: (De)serialize the arguments for putOnSale() between client and server
 */


package com.jsala.common.transport.serialize;

import com.jsala.common.Item;
import com.jsala.common.ItemId;
import java.io.*;

public class SerializeCreateSaleListingArg {
    private String sessionToken;
    private Item item;
    private int quantity;

    // CONSTRUCTORS
    public SerializeCreateSaleListingArg() {}
    public SerializeCreateSaleListingArg(String sessionToken, Item item, int quantity) {
        this.sessionToken = sessionToken;
        this.item = item;
        this.quantity = quantity;
    }

    public static byte[] serialize(String sessionToken, Item item, int quantity) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream(); // grow dynamically
        DataOutputStream writer = new DataOutputStream(buf);
        writer.writeUTF(sessionToken);
        byte[] itemSer = Item.serialize(item);
        writer.write(itemSer);
        writer.writeInt(quantity);
        byte ret[] = buf.toByteArray();
        writer.close();
        buf.close();
        return ret;
    }

    public static SerializeCreateSaleListingArg deserialize(byte[] b) throws IOException, IllegalArgumentException {
        ByteArrayInputStream buf = new ByteArrayInputStream(b);
        DataInputStream reader = new DataInputStream(buf);
        String sessionToken = reader.readUTF();
        Item item = Item.deserializeFromStream(reader); // may throw IllegalArgumentException
        int quantity = reader.readInt();
        reader.close();
        buf.close();
        return new SerializeCreateSaleListingArg(sessionToken, item, quantity);
    }

    public String getSessionToken() {
        return sessionToken;
    }
    public Item getItem() {
        return item;
    }
    public int getQuantity() {
        return quantity;
    }
}

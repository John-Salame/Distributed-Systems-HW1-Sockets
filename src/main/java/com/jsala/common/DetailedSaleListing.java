/**
 * Class DetailedSaleListing
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * Description: A combination of a sale listing and the item it corresponds to.
 */

package com.jsala.common;

import java.io.*;

public class DetailedSaleListing {
    private SaleListing saleListing;
    private Item item;
    private final String line = "<<---------------------------------->>\n";

    // CONSTRUCTOR
    public DetailedSaleListing(SaleListing saleListing, Item item) {
        this.saleListing = saleListing;
        this.item = item;
    }

    public static byte[] serialize(DetailedSaleListing dsl) throws IOException {
        // saleListing, item
        ByteArrayOutputStream buf = new ByteArrayOutputStream(); // grow dynamically
        DataOutputStream writer = new DataOutputStream(buf);
        byte[] saleListingSer = SaleListing.serialize(dsl.getSaleListing());
        byte[] itemSer = Item.serialize(dsl.getItem());
        writer.write(saleListingSer);
        writer.write(itemSer);
        byte ret[] = buf.toByteArray();
        writer.close();
        buf.close();
        return ret;
    }
    public static byte[] serialize(SaleListing saleListing, Item item) throws IOException {
        // saleListing, item
        ByteArrayOutputStream buf = new ByteArrayOutputStream(); // grow dynamically
        DataOutputStream writer = new DataOutputStream(buf);
        byte[] saleListingSer = SaleListing.serialize(saleListing);
        byte[] itemSer = Item.serialize(item);
        writer.write(saleListingSer);
        writer.write(itemSer);
        byte ret[] = buf.toByteArray();
        writer.close();
        buf.close();
        return ret;
    }
    public static DetailedSaleListing deserialize(byte[] b) throws IOException, IllegalArgumentException {
        ByteArrayInputStream buf = new ByteArrayInputStream(b);
        DataInputStream reader = new DataInputStream(buf);
        DetailedSaleListing dsl = deserializeFromStream(reader);
        reader.close();
        buf.close();
        return dsl;
    }
    public static DetailedSaleListing deserializeFromStream(DataInputStream reader) throws IOException, IllegalArgumentException {
        SaleListing saleListing = SaleListing.deserializeFromStream(reader);
        Item item = Item.deserializeFromStream(reader);
        return new DetailedSaleListing(saleListing, item);
    }

    public static byte[] serializeArray(DetailedSaleListing[] detailedSaleListings) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream(); // grow dynamically
        DataOutputStream writer = new DataOutputStream(buf);
        short numElements = (short) detailedSaleListings.length;
        writer.writeShort(numElements);
        for(short i = 0; i < numElements; i++) {
            byte[] detailedSaleListingSer = DetailedSaleListing.serialize(detailedSaleListings[i]);
            writer.write(detailedSaleListingSer);
        }
        byte ret[] = buf.toByteArray();
        writer.close();
        buf.close();
        return ret;
    }

    public static DetailedSaleListing[] deserializeArray(byte[] b) throws IOException, IllegalArgumentException {
        ByteArrayInputStream buf = new ByteArrayInputStream(b);
        DataInputStream reader = new DataInputStream(buf);
        short numElements = reader.readShort();
        DetailedSaleListing[] detailedSaleListings = new DetailedSaleListing[numElements];
        for(short i = 0; i < numElements; i++) {
            detailedSaleListings[i] = DetailedSaleListing.deserializeFromStream(reader);
        }
        reader.close();
        buf.close();
        return detailedSaleListings;
    }

    public SaleListing getSaleListing() {
        return saleListing;
    }
    public Item getItem() {
        return item;
    }

    @Override
    public String toString() {
        return line + this.saleListing.toString() + this.item.toString() + line;
    }
}

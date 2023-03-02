/**
 * Class DBProductSaleListingSocketClientV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * API version 1
 * Description: Socket implementation of session server-database IPC on server side
 */

package com.jsala.server.v1.socket;

import com.jsala.common.ItemId;
import com.jsala.common.SaleListing;
import com.jsala.common.SaleListingId;
import com.jsala.common.transport.serialize.SerializeInt;
import com.jsala.common.transport.serialize.SerializeSaleListingArg;
import com.jsala.common.transport.socket.APIEnumV1;
import com.jsala.common.transport.socket.BaseSocketClient;
import com.jsala.common.transport.socket.DBItemEnumV1;
import com.jsala.common.transport.socket.DBSaleListingEnumV1;
import com.jsala.dao.SaleListingDAO;

import java.io.IOException;
import java.util.NoSuchElementException;

public class DBProductSaleListingSocketClientV1 extends BaseSocketClient implements SaleListingDAO {

    // CONSTRUCTORS
    // recommend serverIp = localhost
    public DBProductSaleListingSocketClientV1(String serverIp, int serverPort) /*throws SocketException*/ {
        super(serverIp, serverPort, (short) 1, APIEnumV1.DB_SALE_LISTING.ordinal());
    }

    @Override
    public SaleListingId putItemOnSale(int sellerId, ItemId itemId, int quantity) throws IOException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException {
        int funcId = DBSaleListingEnumV1.PUT_ITEM_ON_SALE.ordinal();
        byte[] msg = SerializeSaleListingArg.serialize(sellerId, itemId, quantity);
        byte[] buf = this.sendAndReceive(msg, funcId);
        return SaleListingId.deserialize(buf);
    }

    @Override
    public void removeItemFromSale(int sellerId, ItemId itemId, int quantity) throws IOException, NoSuchElementException, UnsupportedOperationException {
        int funcId = DBSaleListingEnumV1.REMOVE_ITEM_FROM_SALE.ordinal();
        byte[] msg = SerializeSaleListingArg.serialize(sellerId, itemId, quantity);
        byte[] buf = this.sendAndReceive(msg, funcId);
        assert buf.length == 0;
    }

    @Override
    public SaleListing[] getSaleListingsBySeller(int sellerId) throws IOException {
        int funcId = DBSaleListingEnumV1.GET_SALE_LISTINGS_BY_SELLER.ordinal();
        byte[] msg = SerializeInt.serialize(sellerId);
        byte[] buf = this.sendAndReceive(msg, funcId);
        return SaleListing.deserializeArray(buf);
    }

    @Override
    public void closeConnection() throws IOException {
        this.cleanup();
    }
}

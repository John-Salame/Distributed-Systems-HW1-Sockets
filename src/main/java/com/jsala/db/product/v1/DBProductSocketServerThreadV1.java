/**
 * Class DBProductSocketServerThreadV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of customer server-database IPC on database side
 * Socket programming reference: https://www.geeksforgeeks.org/socket-programming-in-java/
 */

package com.jsala.db.product.v1;
import com.jsala.common.*;
import com.jsala.common.transport.serialize.*;
import com.jsala.common.transport.socket.APIEnumV1;
import com.jsala.common.transport.socket.BaseSocketServerThread;
import com.jsala.common.transport.socket.DBItemEnumV1;
import com.jsala.common.transport.socket.DBSaleListingEnumV1;
import com.jsala.dao.ItemDAO;
import com.jsala.dao.SaleListingDAO;

import java.io.*;
import java.net.*;
import java.util.NoSuchElementException;

public class DBProductSocketServerThreadV1 extends BaseSocketServerThread implements ItemDAO, SaleListingDAO {
	private ItemDAO itemDaoV1;
	private SaleListingDAO saleListingDaoV1;
	private DBItemEnumV1[] dbItemEnumV1Values; // for translating function ID to enum value
	private DBSaleListingEnumV1[] dbSaleListingEnumV1Values;
	private APIEnumV1[] apiEnumV1Values;

	// CONSTRUCTORS
	// Use this Constructor for threads that have an active connection
	public DBProductSocketServerThreadV1(ItemDAO itemDaoV1, SaleListingDAO saleListingDaoV1, Socket socket) {
		super(socket);
		this.itemDaoV1 = itemDaoV1;
		this.saleListingDaoV1 = saleListingDaoV1;
		this.dbItemEnumV1Values = DBItemEnumV1.values();
		this.dbSaleListingEnumV1Values = DBSaleListingEnumV1.values();
		this.apiEnumV1Values = APIEnumV1.values();
	}

	@Override
	protected byte[] demux(short apiVer, int api, int funcId, byte[] msg) throws IOException {
		switch (apiVer) {
			case 1:
				return this.demuxV1(api, funcId, msg);
			default:
				throw new RuntimeException("Err SellerSocketServerThreadV1: Received message with invalid API version.");
		}
	}
	private byte[] demuxV1(int api, int funcId, byte[] msg) throws IOException {
		APIEnumV1 apiName = this.apiEnumV1Values[api];
		switch (apiName) {
			case DB_ITEM:
				return this.demuxV1DBItem(funcId, msg);
			case DB_SALE_LISTING:
				return this.demuxV1DBSaleListing(funcId, msg);
			default:
				throw new RuntimeException("Err SellerSocketServerThreadV1: Received message with invalid API identifier.");
		}
	}
	private byte[] demuxV1DBItem(int funcId, byte[] msg) throws IOException {
		DBItemEnumV1 functionName = this.dbItemEnumV1Values[funcId];
		switch (functionName) {
			case CREATE_ITEM:
				return this.bytesCreateItem(msg);
			case GET_ITEM_BY_ID:
				return this.bytesGetItemById(msg);
			case CHANGE_PRICE:
				return this.bytesChangePrice(msg);
			case GET_ITEMS_BY_SELLER:
				return this.bytesGetItemsBySeller(msg);
			case GET_ITEMS_IN_CATEGORY:
				return this.bytesGetItemsInCategory(msg);
			default:
				throw new RuntimeException("Err SellerSocketServerThreadV1: Unsupported method triggered by DBItemV1 enum.");
		}
	}
	private byte[] demuxV1DBSaleListing(int funcId, byte[] msg) throws IOException {
		DBSaleListingEnumV1 functionName = this.dbSaleListingEnumV1Values[funcId];
		switch (functionName) {
			case PUT_ITEM_ON_SALE:
				return this.bytesPutItemOnSale(msg);
			case REMOVE_ITEM_FROM_SALE:
				return this.bytesRemoveItemFromSale(msg);
			case GET_SALE_LISTINGS_BY_SELLER:
				return this.bytesGetSaleListingsBySeller(msg);
			case GET_DETAILED_SALE_LISTINGS_BY_SELLER:
				return this.bytesGetDetailedSaleListingsBySeller(msg);
			default:
				throw new RuntimeException("Err SellerSocketServerThreadV1: Unsupported method triggered by DBSaleListingV1 enum.");
		}
	}
	

	// ITEM METHODS
	// take in an item with an incomplete item id and update the id.
	@Override
	public ItemId createItem(Item item) throws IOException, IllegalArgumentException {
		return itemDaoV1.createItem(item);
	}
	private byte[] bytesCreateItem(byte[] msg) throws IOException, IllegalArgumentException {
		Item item = Item.deserialize(msg);
		ItemId itemId = this.createItem(item);
		return ItemId.serialize(itemId);
	}
	@Override
	public Item getItemById(ItemId itemId) throws IOException, NoSuchElementException {
		return itemDaoV1.getItemById(itemId);
	}
	private byte[] bytesGetItemById(byte[] msg) throws IOException, NoSuchElementException {
		ItemId itemId = ItemId.deserialize(msg);
		Item item = this.getItemById(itemId);
		return Item.serialize(item);
	}
	// use sellerId to verify that you are the correct seller to change the price
	@Override
	public void changePrice(ItemId itemId, int sellerId, float newPrice) throws IOException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException {
		itemDaoV1.changePrice(itemId, sellerId, newPrice);
	}
	private byte[] bytesChangePrice(byte[] msg) throws IOException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException {
		SerializePriceArgDB priceArg = SerializePriceArgDB.deserialize(msg);
		this.changePrice(priceArg.getItemId(), priceArg.getSellerId(), priceArg.getPrice());
		return new byte[0];
	}
	@Override
	public Item[] getItemsBySeller(int sellerId) throws IOException {
		return itemDaoV1.getItemsBySeller(sellerId);
	}
	private byte[] bytesGetItemsBySeller(byte[] msg) throws IOException {
		int sellerId = SerializeInt.deserialize(msg);
		Item[] sellerItems = this.getItemsBySeller(sellerId);
		return Item.serializeArray(sellerItems);
	}
	@Override
	public Item[] getItemsInCategory(int category) throws IOException {
		return itemDaoV1.getItemsInCategory(category);
	}
	private byte[] bytesGetItemsInCategory(byte[] msg) throws IOException {
		int category = SerializeInt.deserialize(msg);
		Item[] catItems = this.getItemsInCategory(category);
		return Item.serializeArray(catItems);
	}

	// SALE LISTING METHODS

	@Override
	public SaleListingId putItemOnSale(int sellerId, ItemId itemId, int quantity) throws IOException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException {
		return saleListingDaoV1.putItemOnSale(sellerId, itemId, quantity);
	}
	private byte[] bytesPutItemOnSale(byte[] msg) throws IOException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException {
		SerializeSaleListingArgDB saleListingArg = SerializeSaleListingArgDB.deserialize(msg);
		SaleListingId ret = this.putItemOnSale(saleListingArg.getSellerId(), saleListingArg.getItemId(), saleListingArg.getQuantity());
		return SaleListingId.serialize(ret);
	}
	@Override
	public void removeItemFromSale(int sellerId, ItemId itemId, int quantity) throws IOException, NoSuchElementException, UnsupportedOperationException {
		saleListingDaoV1.removeItemFromSale(sellerId, itemId, quantity);
	}
	private byte[] bytesRemoveItemFromSale(byte[] msg) throws IOException, NoSuchElementException, UnsupportedOperationException {
		SerializeSaleListingArgDB saleListingArg = SerializeSaleListingArgDB.deserialize(msg);
		this.removeItemFromSale(saleListingArg.getSellerId(), saleListingArg.getItemId(), saleListingArg.getQuantity());
		return new byte[0];
	}
	@Override
	public SaleListing[] getSaleListingsBySeller(int sellerId) throws IOException {
		return saleListingDaoV1.getSaleListingsBySeller(sellerId);
	}
	private byte[] bytesGetSaleListingsBySeller(byte[] msg) throws IOException {
		int sellerId = SerializeInt.deserialize(msg);
		SaleListing[] saleListings = this.getSaleListingsBySeller(sellerId);
		return SaleListing.serializeArray(saleListings);
	}
	@Override
	public DetailedSaleListing[] getDetailedSaleListingsBySeller(int sellerId) throws IOException {
		return saleListingDaoV1.getDetailedSaleListingsBySeller(sellerId);
	}
	private byte[] bytesGetDetailedSaleListingsBySeller(byte[] msg) throws IOException {
		int sellerId = SerializeInt.deserialize(msg);
		DetailedSaleListing[] detailedSaleListings = this.getDetailedSaleListingsBySeller(sellerId);
		return DetailedSaleListing.serializeArray(detailedSaleListings);
	}

	@Override
	public void closeConnection() throws IOException {
		itemDaoV1.closeConnection();
		saleListingDaoV1.closeConnection();
	}
}

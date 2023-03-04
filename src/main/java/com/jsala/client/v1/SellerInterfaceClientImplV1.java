/**
 * Class SellerInterfaceClientImplV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Client-side implementation of the seller API
 * Note: This could theoretically be removed right now since it is just using delegation,
 * but it could be turned into middleware later on or connected to middlware.
 */

package com.jsala.client.v1;
import com.jsala.common.interfaces.SellerInterface;
import com.jsala.common.Item;
import com.jsala.common.ItemId;
import com.jsala.common.SaleListingId;

public class SellerInterfaceClientImplV1 implements SellerInterface {

	private SellerInterface transport; // transport layer for inter-process communication
	
	// CONSTRUCTORS
	public SellerInterfaceClientImplV1() {}
	public SellerInterfaceClientImplV1(SellerInterface transport) {
		this.transport = transport;
	}

	@Override
	public int createUser(String username, String password) {
		int userId = 0;
		try {
			userId = transport.createUser(username, password);
		} catch (Exception e) {
			System.out.println(e);
		}
		return userId;
	}
	@Override
	public String login(String username, String password) {
		String sessionToken = "";
		try {
			sessionToken = transport.login(username, password);
		} catch (Exception e) {
			System.out.println(e);
		}
		return sessionToken;
	}
	@Override
	public void logout(String sessionToken) {
		try {
			transport.logout(sessionToken);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	@Override
	public int[] getSellerRating(int sellerId) {
		int[] sellerRating = new int[] {0, 0};
		try {
			sellerRating = transport.getSellerRating(sellerId);
		} catch (Exception e) {
			System.out.println(e);
		}
		return sellerRating;
	}
	@Override
	public SaleListingId putOnSale(String sessionToken, Item item, int quantity) {
		SaleListingId saleId = null;
		try {
			saleId = transport.putOnSale(sessionToken, item, quantity);
		} catch (Exception e) {
			System.out.println(e);
		}
		return saleId;
	}
	@Override
	public void changePriceOfItem(String sessionToken, ItemId itemId, float newPrice) {
		try {
			transport.changePriceOfItem(sessionToken, itemId, newPrice);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	@Override
	public void removeItemFromSale(String sessionToken, ItemId itemId, int quantity) {
		try {
			transport.removeItemFromSale(sessionToken, itemId, quantity);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	@Override
	public String displayItemsOnSale(String sessionToken) {
		String ret = "";
		try {
			ret = transport.displayItemsOnSale(sessionToken);
		} catch (Exception e) {
			System.out.println(e);
		}
		return ret;
	}
}

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
import java.util.NoSuchElementException;

public class SellerInterfaceClientImplV1 implements SellerInterface {

	private SellerInterface transport; // transport layer for inter-process communication
	
	// CONSTRUCTORS
	public SellerInterfaceClientImplV1() {}
	public SellerInterfaceClientImplV1(SellerInterface transport) {
		this.transport = transport;
	}

	public int createUser(String username, String password) {
		int userId = 0;
		try {
			userId = transport.createUser(username, password);
		} catch (Exception e) {
			System.out.println(e);
		}
		return userId;
	}
	public String login(String username, String password) {
		String sessionToken = "";
		try {
			sessionToken = transport.login(username, password);
		} catch (Exception e) {
			System.out.println(e);
		}
		return sessionToken;
	}
	public void logout(String sessionToken) {
		try {
			transport.logout(sessionToken);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public int[] getSellerRating(int sellerId) {
		int[] sellerRating = new int[] {0, 0};
		try {
			sellerRating = transport.getSellerRating(sellerId);
		} catch (Exception e) {
			System.out.println(e);
		}
		return sellerRating;
	}
	public SaleListingId putOnSale(String sessionToken, Item item, int quantity) {
		SaleListingId saleId = null;
		try {
			saleId = transport.putOnSale(sessionToken, item, quantity);
		} catch (Exception e) {
			System.out.println(e);
		}
		return saleId;
	}
	public void changePriceOfItem(String sessionToken, ItemId itemId, float newPrice) {
		try {
			transport.changePriceOfItem(sessionToken, itemId, newPrice);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	/*
	public void removeItemFromSale(String sessionToken, ItemId itemId, int quantity);
	public String displayItemsOnSale(String sessionToken);
	*/
}

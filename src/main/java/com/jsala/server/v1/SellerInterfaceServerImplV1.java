/**
 * Class SellerInterfaceServerImplV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Server-side implementation of the seller API
 */

package com.jsala.server.v1;
import com.jsala.common.SaleListing;
import com.jsala.common.interfaces.SellerInterface;
import com.jsala.common.Item;
import com.jsala.common.ItemId;
import com.jsala.common.SaleListingId;
import com.jsala.dao.*;
import com.jsala.dao.factory.*;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class SellerInterfaceServerImplV1 implements SellerInterface {
	private SellerDAO sellerDao;
	private SessionDAO sessionDao;
	private ItemDAO itemDao;
	private SaleListingDAO saleListingDao;
	
	// CONSTRUCTORS
	public SellerInterfaceServerImplV1() {}
	public SellerInterfaceServerImplV1(CustomerDAOFactory sellerDaoFactory, CustomerDAOFactory sessionDaoFactory, ProductDAOFactory itemDaoFactory, ProductDAOFactory saleListingDaoFactory) throws InvocationTargetException {
		this.sellerDao = sellerDaoFactory.createSellerDao();
		this.sessionDao = sessionDaoFactory.createSessionDao();
		this.itemDao = itemDaoFactory.createItemDao();
		this.saleListingDao = saleListingDaoFactory.createSaleListingDao();
	}

	@Override
	public int createUser(String username, String password) throws IOException, IllegalArgumentException {
		return sellerDao.createUser(username, password);
	}
	@Override
	public String login(String username, String password) throws IOException, NoSuchElementException {
		String sessionToken;
		int userId = sellerDao.getUserId(username, password);
		sessionToken = sessionDao.createSession(userId);
		return sessionToken;
	}
	@Override
	// NoSuchElementException means the session is gone for sure. IOException means we should retry.
	public void logout(String sessionToken) throws IOException, NoSuchElementException {
		// try to log out until it works
		boolean logoutSuccess = false;
		while (!logoutSuccess) {
			try {
				sessionDao.expireSession(sessionToken);
				logoutSuccess = true;
			} catch (IOException e) {
				System.out.println("SellerInterfaceServerImplV1 failed logout");
			}
		}
		// close all database connections associated with this user
		this.sellerDao.closeConnection();
		this.sessionDao.closeConnection();
		this.itemDao.closeConnection();
	}
	@Override
	public int[] getSellerRating(int sellerId) throws IOException, NoSuchElementException {
		return sellerDao.getSellerById(sellerId).getFeedback(); // not sure if this counts as stateless
	}
	@Override
	public SaleListingId putOnSale(String sessionToken, Item item, int quantity) throws IOException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException {
		int sellerId = sessionDao.getUserIdFromSession(sessionToken);
		item.setSellerId(sellerId);
		ItemId itemId = itemDao.createItem(item);
		return saleListingDao.putItemOnSale(sellerId, itemId, quantity);
	}
	@Override
	public void changePriceOfItem(String sessionToken, ItemId itemId, float newPrice) throws IOException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException {
		int sellerId = sessionDao.getUserIdFromSession(sessionToken);
		itemDao.changePrice(itemId, sellerId, newPrice);
	}
	public void removeItemFromSale(String sessionToken, ItemId itemId, int quantity)  throws IOException, NoSuchElementException, UnsupportedOperationException {
		int sellerId = sessionDao.getUserIdFromSession(sessionToken);
		saleListingDao.removeItemFromSale(sellerId, itemId, quantity);
	}
	public String displayItemsOnSale(String sessionToken) throws IOException {
		int sellerId = 0;
		SaleListing[] saleListings = new SaleListing[0];
		try {
			sellerId = sessionDao.getUserIdFromSession(sessionToken);
			saleListings = saleListingDao.getSaleListingsBySeller(sellerId);
		} catch (Exception e) {
			System.out.println("SellerInterfaceServerImplV1 failed to display items on sale: " + e);
		}
		return Arrays.toString(saleListings);
	}
}

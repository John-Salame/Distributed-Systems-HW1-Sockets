/**
 * Class SellerInterfaceServerImplV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Server-side implementation of the seller API
 */

package com.jsala.server.v1;
import com.jsala.common.interfaces.SellerInterface;
import com.jsala.common.Item;
import com.jsala.common.ItemId;
import com.jsala.common.SaleListingId;
import com.jsala.dao.*;
import com.jsala.dao.factory.*;
import java.util.NoSuchElementException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class SellerInterfaceServerImplV1 implements SellerInterface {
	private SellerDAO sellerDao;
	private SessionDAO sessionDao;
	private ItemDAO itemDao;
	
	// CONSTRUCTORS
	public SellerInterfaceServerImplV1() {}
	public SellerInterfaceServerImplV1(CustomerDAOFactory sellerDaoFactory, CustomerDAOFactory sessionDaoFactory, ProductDAOFactory itemDaoFactory) throws InvocationTargetException {
		this.sellerDao = sellerDaoFactory.createSellerDao();
		this.sessionDao = sessionDaoFactory.createSessionDao();
		this.itemDao = itemDaoFactory.createItemDao();
	}

	public int createUser(String username, String password) throws IOException, IllegalArgumentException {
		return sellerDao.createUser(username, password);
	}
	public String login(String username, String password) throws IOException, NoSuchElementException {
		String sessionToken;
		int userId = sellerDao.getUserId(username, password);
		sessionToken = sessionDao.createSession(userId);
		return sessionToken;
	}
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
	public int[] getSellerRating(int sellerId) throws IOException, NoSuchElementException {
		return sellerDao.getSellerById(sellerId).getFeedback(); // not sure if this counts as stateless
	}
	public SaleListingId putOnSale(String sessionToken, Item item, int quantity) throws IOException, IllegalArgumentException {
		int sellerId = sessionDao.getUserIdFromSession(sessionToken);
		throw new RuntimeException("SellerInterfaceServerImplV1: putOnSale() Not implemented");
	}
	public void changePriceOfItem(String sessionToken, ItemId itemId, float newPrice) throws IOException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException {
		int sellerId = sessionDao.getUserIdFromSession(sessionToken);
		itemDao.changePrice(itemId, sellerId, newPrice);
	}
	/*
	public void removeItemFromSale(String sessionToken, ItemId itemId, int quantity);
	public String displayItemsOnSale(String sessionToken);
	*/
}

/**
 * Class CustomerDAOFactoryInMemory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Creates concrete DAOs associated with the Customer database.
 * This adds decoupling and on-demand DAO creation.
 */

package com.jsala.db.customer;
import com.jsala.dao.*;
import com.jsala.dao.factory.CustomerDAOFactory;
import java.lang.reflect.InvocationTargetException;

public class CustomerDAOFactoryInMemory implements CustomerDAOFactory {
	private BuyerDAO buyerDao;
	private SellerDAO sellerDao;
	private SessionDAO sessionDao;

	// CONSTRUCTORS
	// All people who use this factory use the same DAO in memory.
	public CustomerDAOFactoryInMemory() {
		this.buyerDao = new BuyerDAOInMemory();
		this.sellerDao = new SellerDAOInMemory();
		this.sessionDao = new SessionDAOInMemory();
	}
	public BuyerDAO createBuyerDao() throws InvocationTargetException {
		return this.buyerDao;
	}
	public SellerDAO createSellerDao() throws InvocationTargetException {
		return this.sellerDao;
	}
	public SessionDAO createSessionDao() throws InvocationTargetException {
		return this.sessionDao;
	}
}

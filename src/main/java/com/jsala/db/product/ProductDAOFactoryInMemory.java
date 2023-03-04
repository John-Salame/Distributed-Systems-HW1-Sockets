/**
 * Class ProductDAOFactoryInMemory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description:  Creates concrete in-memory DAOs associated with the Product database.
 * This adds decoupling and on-demand DAO creation.
 */

package com.jsala.db.product;
import com.jsala.dao.*;
import com.jsala.dao.factory.ProductDAOFactory;
import java.lang.reflect.InvocationTargetException;

public class ProductDAOFactoryInMemory implements ProductDAOFactory {
	private ItemDAO itemDao;
	private SaleListingDAO saleListingDao;

	public ProductDAOFactoryInMemory() {
		this.itemDao = new ItemDAOInMemory();
		this.saleListingDao = new SaleListingDAOInMemory(this.itemDao);
	}

	@Override
	public ItemDAO createItemDao() throws InvocationTargetException {
		return this.itemDao;
	}
	@Override
	public SaleListingDAO createSaleListingDao() throws InvocationTargetException {
		return this.saleListingDao;
	}
}

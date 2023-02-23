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

	public ProductDAOFactoryInMemory() {
		this.itemDao = new ItemDAOInMemory();
	}

	public ItemDAO createItemDao() throws InvocationTargetException {
		return this.itemDao;
	}
}

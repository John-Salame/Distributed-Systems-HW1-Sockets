/**
 * Class CustomerDAOFactoryInMemory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Creates concrete DAOs associated with the Customer database.
 * This adds decoupling and on-demand DAO creation.
 */

package db.customer;
import dao.*;
import dao.factory.CustomerDAOFactory;
import java.lang.reflect.InvocationTargetException;

public class CustomerDAOFactoryInMemory implements CustomerDAOFactory {
	public BuyerDAO createBuyerDao() throws InvocationTargetException {
		return new BuyerDAOInMemory();
	}
	public SellerDAO crateSellerDao() throws InvocationTargetException {
		return new SellerDAOInMemory();
	}
	public SessionDAO createSessionDao() throws InvocationTargetException {
		return new SessionDAOInMemory();
	}
}

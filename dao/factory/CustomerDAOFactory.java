/**
 * Interface CustomerDAOFactory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Creates concrete DAOs associated with the Customer database.
 * This adds decoupling and on-demand DAO creation.
 */

package dao.factory;
import dao.*;
import java.lang.reflect.InvocationTargetException;

public interface CustomerDAOFactory {
	public abstract BuyerDAO createBuyerDao() throws InvocationTargetException;
	public abstract SellerDAO crateSellerDao() throws InvocationTargetException;
	public abstract SessionDAO createSessionDao() throws InvocationTargetException;
}

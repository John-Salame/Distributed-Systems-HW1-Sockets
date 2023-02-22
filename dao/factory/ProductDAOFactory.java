/**
 * Interface ProductDAOFactory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Creates concrete DAOs associated with the Product database.
 * This adds decoupling and on-demand DAO creation.
 */

package dao.factory;
import dao.*;
import java.lang.reflect.InvocationTargetException;

public interface ProductDAOFactory {
	public abstract ItemDAO createItemDao() throws InvocationTargetException;
}

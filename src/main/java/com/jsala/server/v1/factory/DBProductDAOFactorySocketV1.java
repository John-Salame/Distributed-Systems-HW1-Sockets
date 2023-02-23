/**
 * Class DBProductDAOFactorySocketV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Creates concrete client socket-based DAOs associated with the Product database.
 * This adds decoupling and on-demand DAO creation.
 */

package server.v1.factory;
import com.jsala.dao.*;
import com.jsala.dao.factory.ProductDAOFactory;
import com.jsala.server.v1.socket.DBProductItemSocketClientV1;
import java.lang.reflect.InvocationTargetException;

public class DBProductDAOFactorySocketV1 implements ProductDAOFactory {
	private String dbHost;
	private int dbPort;

	public DBProductDAOFactorySocketV1(String host, int port) {
		this.dbHost = host;
		this.dbPort = port;
	}

	public ItemDAO createItemDao() throws InvocationTargetException {
		return new DBProductItemSocketClientV1(this.dbHost, this.dbPort);
	}
}

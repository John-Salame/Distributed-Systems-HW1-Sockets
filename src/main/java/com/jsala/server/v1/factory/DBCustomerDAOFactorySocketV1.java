/**
 * Class DBCustomerDAOFactorySocketV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Creates concrete client socket-based DAOs associated with the Customer database.
 * This adds decoupling and on-demand DAO creation.
 */

package server.v1.factory;
import com.jsala.dao.*;
import com.jsala.dao.factory.CustomerDAOFactory;
import com.jsala.server.v1.socket.DBCustomerBuyerSocketClientV1;
import com.jsala.server.v1.socket.DBCustomerSellerSocketClientV1;
import com.jsala.server.v1.socket.DBCustomerSessionSocketClientV1;
import java.lang.reflect.InvocationTargetException;

public class DBCustomerDAOFactorySocketV1 implements CustomerDAOFactory {
	private String dbHost;
	private int dbPort;

	public DBCustomerDAOFactorySocketV1(String host, int port) {
		this.dbHost = host;
		this.dbPort = port;
	}

	public BuyerDAO createBuyerDao() throws InvocationTargetException {
		return new DBCustomerBuyerSocketClientV1(this.dbHost, this.dbPort);
	}
	public SellerDAO createSellerDao() throws InvocationTargetException {
		return new DBCustomerSellerSocketClientV1(this.dbHost, this.dbPort);
	}
	public SessionDAO createSessionDao() throws InvocationTargetException {
		return new DBCustomerSessionSocketClientV1(this.dbHost, this.dbPort);
	}
}

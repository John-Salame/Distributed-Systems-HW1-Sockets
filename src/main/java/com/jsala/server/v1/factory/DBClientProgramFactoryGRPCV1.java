/**
 * Class DBClientProgramFactoryGRPCV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Create an instance of a business logic unit (BuyerInterface or SellerInterface) which generates a GRPC client.
 * The server has a ManagedChannelBuilder for GRPC, and it creates a thread and a MangedChannel every time a buyer or seller client connects.
 */

package com.jsala.server.v1.factory;
import com.jsala.common.interfaces.factory.UserInterfaceFactory;
import com.jsala.common.interfaces.BuyerInterface;
import com.jsala.common.interfaces.SellerInterface;
import com.jsala.dao.factory.CustomerDAOFactory;
import com.jsala.dao.factory.ProductDAOFactory;
import com.jsala.server.v1.BuyerInterfaceServerImplV1;
import com.jsala.server.v1.SellerInterfaceServerImplV1;
import io.grpc.ManagedChannelBuilder;
import java.lang.reflect.InvocationTargetException;

public class DBClientProgramFactoryGRPCV1 implements UserInterfaceFactory {
	private String customerDBHost;
	private int customerDBIp;
	private String productDBHost;
	private int productDBIp;

	public DBClientProgramFactoryGRPCV1(String customerDBHost, int customerDBIp, String productDBHost, int productDBIp) {
		this.customerDBHost = customerDBHost;
		this.customerDBIp = customerDBIp;
		this.productDBHost = productDBHost;
		this.productDBIp = productDBIp;
	}
	public BuyerInterface createBuyerInterface() throws InvocationTargetException {
		// I think it may be a bug to have the customerDaoFactory created in the constructor instead of here.
		// Up in the constructor, it creates only one channel in the BuyerServerGRPCRunner instead of upon receiving a login.
		try {
			CustomerDAOFactory customerDaoFactory = new DBCustomerDAOFactoryGRPCV1(customerDBHost, customerDBIp);
			ProductDAOFactory productDaoFactory = null;
			CustomerDAOFactory buyerDAOFactory = customerDaoFactory;
			CustomerDAOFactory sellerDAOFactory = customerDaoFactory;
			CustomerDAOFactory sessionDAOFactory = customerDaoFactory;
			ProductDAOFactory itemDAOFactory = productDaoFactory;
			return new BuyerInterfaceServerImplV1(buyerDAOFactory, sellerDAOFactory, sessionDAOFactory, itemDAOFactory);
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		}
	}
	public SellerInterface createSellerInterface() throws InvocationTargetException {
		try {
			CustomerDAOFactory customerDaoFactory = new DBCustomerDAOFactoryGRPCV1(customerDBHost, customerDBIp);
			ProductDAOFactory productDaoFactory = null;
			CustomerDAOFactory sellerDAOFactory = customerDaoFactory;
			CustomerDAOFactory sessionDAOFactory = customerDaoFactory;
			ProductDAOFactory itemDAOFactory = productDaoFactory;
			return new SellerInterfaceServerImplV1(sellerDAOFactory, sessionDAOFactory, itemDAOFactory);
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		}
	}
}

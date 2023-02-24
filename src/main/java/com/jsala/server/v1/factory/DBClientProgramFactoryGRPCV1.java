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
	private CustomerDAOFactory customerDaoFactory;
	private ProductDAOFactory productDaoFactory;

	public DBClientProgramFactoryGRPCV1(String customerDBHost, int customerDBIp, String productDBHost, int productDBIp) {
		this.customerDaoFactory = new DBCustomerDAOFactoryGRPCV1(customerDBHost, customerDBIp);
		// this.productDaoFactory = new DBProductDAOFactorySocketV1(productDBHost, productDBIp);
	}
	public BuyerInterface createBuyerInterface() throws InvocationTargetException {
		CustomerDAOFactory buyerDAOFactory = customerDaoFactory;
		CustomerDAOFactory sellerDAOFactory = customerDaoFactory;
		CustomerDAOFactory sessionDAOFactory = customerDaoFactory;
		ProductDAOFactory itemDAOFactory = productDaoFactory;
		return new BuyerInterfaceServerImplV1(buyerDAOFactory, sellerDAOFactory, sessionDAOFactory, itemDAOFactory);
	}
	public SellerInterface createSellerInterface() throws InvocationTargetException {
		CustomerDAOFactory sellerDAOFactory = customerDaoFactory;
		CustomerDAOFactory sessionDAOFactory = customerDaoFactory;
		ProductDAOFactory itemDAOFactory = productDaoFactory;
		return new SellerInterfaceServerImplV1(sellerDAOFactory, sessionDAOFactory, itemDAOFactory);
	}
}

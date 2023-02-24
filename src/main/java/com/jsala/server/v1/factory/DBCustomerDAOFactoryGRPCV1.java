/**
 * Class DBCustomerDAOFactoryGRPCV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * Description: Creates concrete client GRPC-based DAOs associated with the Customer database.
 * This adds decoupling and on-demand DAO creation.
 */

package com.jsala.server.v1.factory;
import com.jsala.dao.*;
import com.jsala.dao.factory.CustomerDAOFactory;
import com.jsala.server.v1.grpc.DBCustomerBuyerGRPCClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.lang.reflect.InvocationTargetException;

public class DBCustomerDAOFactoryGRPCV1 implements CustomerDAOFactory {
	// private ManagedChannelBuilder channelBuilder;
	private ManagedChannel channel;

	// CONSTRUCTORS
	public DBCustomerDAOFactoryGRPCV1(String hostname, int port) {
		// all database connections on this thread will use the same channel
		// each database client is allowed to shut down the channel, but that's ok because we want to stop using all of them once we stop using one.
		this.channel = ManagedChannelBuilder.forAddress(hostname, port).usePlaintext().build();
	}

	public BuyerDAO createBuyerDao() throws InvocationTargetException {
		return new DBCustomerBuyerGRPCClient(this.channel);
	}
	public SellerDAO createSellerDao() throws InvocationTargetException {
		throw new UnsupportedOperationException("GRPC Customer DAO Factory: sellerDao unimplemented");
	}
	public SessionDAO createSessionDao() throws InvocationTargetException {
		throw new UnsupportedOperationException("GRPC Customer DAO Factory: sessionDao unimplemented");
	}
}

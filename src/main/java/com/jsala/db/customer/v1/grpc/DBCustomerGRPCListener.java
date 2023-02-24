/**
 * Class DBCustomerGRPCListener
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * API version 1
 * Description: Starts the grpc server for the customer database
 * GRPC tutorial: https://www.youtube.com/watch?v=JFzAe9SvNaU&list=PLI5t0u6ye3FGXJMh5kU2RvN0xrul67p7R
 */

package com.jsala.db.customer.v1.grpc;
import com.jsala.dao.BuyerDAO;
import com.jsala.dao.factory.CustomerDAOFactory;
import com.jsala.db.customer.BuyerDAOInMemory;
import com.jsala.grpc.generated.CustomerGRPC;
import com.jsala.grpc.generated.CustomerGRPC.BuyerGRPC;
import com.jsala.grpc.generated.CustomerGRPC.BuyerIdGRPC;
import com.jsala.grpc.generated.CustomerGRPC.Empty;
import com.jsala.grpc.generated.CustomerGRPC.LoginArgsGRPC;
import com.jsala.grpc.generated.CustomerGRPC.SellerIdGRPC;
import com.jsala.grpc.generated.CustomerGRPC.SellerRatingGRPC;
import com.jsala.grpc.generated.DBCustomerBuyerGrpc;
import com.jsala.grpc.generated.DBCustomerBuyerGrpc.DBCustomerBuyerBlockingStub;
import com.jsala.grpc.generated.DBCustomerBuyerGrpc.DBCustomerBuyerImplBase;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.util.NoSuchElementException;

public class DBCustomerGRPCListener {
	// keep things simple and just run from this class for now
	public static void main(String args) {
		int port = 8300;
		BuyerDAO buyerDao = new BuyerDAOInMemory();
		Server server = null;
		try {
			server = ServerBuilder.forPort(port)
				.addService(new DBCustomerBuyerGRPCServer(buyerDao))
				.build();
			server.start();
			System.out.println("Started GRPC server on Customer database");
		} catch (IOException e) {
			System.out.println(e);
		}
		try {
			if (server != null) {
				server.awaitTermination();
				System.out.println("Closed GRPC server on Customer database (probably won't print");
			}
		} catch (InterruptedException e) {
			System.out.println(e);
		}
		System.out.println("Ending buyer server process");
	}
}

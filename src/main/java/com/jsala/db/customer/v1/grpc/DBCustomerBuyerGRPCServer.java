/**
 * Class DBCustomerBuyerGRPCServer
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * API version 1
 * Description: Grpc implementation of the database-side interface for the buyer database on the customers database group
 * GRPC tutorial: https://www.youtube.com/watch?v=JFzAe9SvNaU&list=PLI5t0u6ye3FGXJMh5kU2RvN0xrul67p7R
 */

package com.jsala.db.customer.v1.grpc;
import com.jsala.common.transport.grpc.GRPCAdaptor;
import com.jsala.common.transport.grpc.GRPCErrorCodes;
import com.jsala.common.Buyer;
import com.jsala.common.Seller;
import com.jsala.dao.BuyerDAO;
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
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.util.NoSuchElementException;

// I don't actually need to implement BuyerDAO, but it will notify me if I have any missing functions.
public class DBCustomerBuyerGRPCServer extends DBCustomerBuyerImplBase implements BuyerDAO {
	private BuyerDAO buyerDaoV1; // the actual database

	public DBCustomerBuyerGRPCServer(BuyerDAO buyerDaoV1) {
		this.buyerDaoV1 = buyerDaoV1;
	}

	// BuyerDAO methods

	// return the user id
	@Override
	public int createUser(String username, String password) throws IOException, IllegalArgumentException {
		return this.buyerDaoV1.createUser(username, password);
	}
	@Override
	public int getUserId(String username, String password) throws IOException, NoSuchElementException {
		return this.buyerDaoV1.getUserId(username, password);
	}
	@Override
	public Buyer getBuyerById(int buyerId) throws IOException, NoSuchElementException {
		return this.buyerDaoV1.getBuyerById(buyerId);
	}
	@Override
	public void closeConnection() {
		// do nothing
	}

	// GRPC methods
	@Override
	public void createBuyer(LoginArgsGRPC request, StreamObserver<BuyerIdGRPC> responseObserver) {
		String username = request.getUsername();
		String password = request.getPassword();
		try {
			int buyerId = this.createUser(username, password);
			BuyerIdGRPC ret = BuyerIdGRPC.newBuilder().setBuyerId(buyerId).build();
			responseObserver.onNext(ret); // send the response
		} catch (Exception e) {
			System.out.println(e);
		}
		responseObserver.onCompleted(); // finish the call
	}
	@Override
	public void getBuyerId(LoginArgsGRPC request, StreamObserver<BuyerIdGRPC> responseObserver) {
		String username = request.getUsername();
		String password = request.getPassword();
		try {
			int buyerId = this.getUserId(username, password);
			BuyerIdGRPC ret = BuyerIdGRPC.newBuilder().setBuyerId(buyerId).build();
			responseObserver.onNext(ret); // send the response
		} catch (Exception e) {
			System.out.println(e);
		}
		responseObserver.onCompleted(); // finish the call
	}
	@Override
	public void getBuyerById(BuyerIdGRPC request, StreamObserver<BuyerGRPC> responseObserver) {
		int buyerId = request.getBuyerId();
		try {
			Buyer buyer = this.getBuyerById(buyerId);
			BuyerGRPC ret = GRPCAdaptor.toBuyerGRPC(buyer);
			responseObserver.onNext(ret); // send the response
		} catch (Exception e) {
			System.out.println(e);
		}
		responseObserver.onCompleted(); // finish the call
	}
	@Override
	public void closeConnection(Empty request, StreamObserver<Empty> responseObserver) {
		try {
			Empty ret = Empty.newBuilder().build();
			responseObserver.onNext(ret); // send the response
		} catch (Exception e) {
			System.out.println(e);
		}
		responseObserver.onCompleted(); // finish the call
	}
}

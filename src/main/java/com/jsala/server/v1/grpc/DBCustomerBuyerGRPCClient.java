/**
 * Class DBCustomerBuyerGRPCClient
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * API version 1
 * Description: Grpc implementation of the server-side connection to the buyer database
 * GRPC tutorial: https://www.youtube.com/watch?v=JFzAe9SvNaU&list=PLI5t0u6ye3FGXJMh5kU2RvN0xrul67p7R
 */

package com.jsala.server.v1.grpc;
import com.jsala.common.transport.grpc.GRPCAdaptor;
import com.jsala.common.transport.grpc.GRPCErrorCodes;
import com.jsala.common.Buyer;
import com.jsala.dao.BuyerDAO;
import com.jsala.grpc.generated.CustomerGRPC;
import com.jsala.grpc.generated.CustomerGRPC.BuyerGRPC;
import com.jsala.grpc.generated.CustomerGRPC.BuyerGRPC.Builder;
import com.jsala.grpc.generated.CustomerGRPC.BuyerIdGRPC;
import com.jsala.grpc.generated.CustomerGRPC.LoginArgsGRPC;
import com.jsala.grpc.generated.DBCustomerBuyerGrpc;
import com.jsala.grpc.generated.DBCustomerBuyerGrpc.DBCustomerBuyerBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.io.IOException;
import java.util.NoSuchElementException;

public class DBCustomerBuyerGRPCClient implements BuyerDAO {
	private String hostname; // remote host
	private int port; // remote port
	private ManagedChannel channel;
	private DBCustomerBuyerBlockingStub stub;

	public DBCustomerBuyerGRPCClient(ManagedChannel channel) {
		this.channel = channel;
		this.stub = DBCustomerBuyerGrpc.newBlockingStub(channel);
	}

	// BuyerDAO methods

	// return the user id
	@Override
	public int createUser(String username, String password) throws IOException, IllegalArgumentException {
		try {
			CustomerGRPC.LoginArgsGRPC loginArgs = CustomerGRPC.LoginArgsGRPC.newBuilder()
				.setUsername(username)
				.setPassword(password)
				.build();
			CustomerGRPC.BuyerIdGRPC response = this.stub.createBuyer(loginArgs);
			return response.getBuyerId();
		} catch (Exception e) {
			throw new IOException(e.getMessage()); // I will abandon all other types of errors for now
		}
		// once I figure out how, get the status code and run it through the method that might throw an error
		//   com.jsala.common.transport.grpc.GRPCErrorCodes.generateExceptionFromStatus
	}
	@Override
	public int getUserId(String username, String password) throws IOException, NoSuchElementException {
		try {
			CustomerGRPC.LoginArgsGRPC loginArgs = CustomerGRPC.LoginArgsGRPC.newBuilder()
				.setUsername(username)
				.setPassword(password)
				.build();
			CustomerGRPC.BuyerIdGRPC response = this.stub.getBuyerId(loginArgs);
			return response.getBuyerId();
		} catch (Exception e) {
			throw new IOException(e.getMessage()); // I will abandon all other types of errors for now
		}
	}
	@Override
	public Buyer getBuyerById(int buyerId) throws IOException, NoSuchElementException {
		try {
			BuyerIdGRPC buyerIdGrpc = BuyerIdGRPC.newBuilder().setBuyerId(buyerId).build();
			BuyerGRPC response = this.stub.getBuyerById(buyerIdGrpc);
			return GRPCAdaptor.toBuyer(response);
		} catch (Exception e) {
			throw new IOException(e.getMessage()); // I will abandon all other types of errors for now
		}
	}
	// do not call an RPC, just close this end.
	@Override
	public void closeConnection() throws IOException {
		this.channel.shutdown();
	}
}

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
import com.jsala.common.Buyer;
import com.jsala.dao.BuyerDAO;
import com.jsala.grpc.generated.CustomerGRPC;
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
	private DBCustomerBuyerBlockingStub stub;

	public DBCustomerBuyerGRPCClient(ManagedChannelBuilder channelBuilder) {
		ManagedChannel channel = ManagedChannelBuilder.forAddress(this.hostname, this.port).usePlaintext().build();
		this.stub = DBCustomerBuyerGrpc.newBlockingStub(channel);
	}

	// return the user id
	public int createUser(String username, String password) throws IOException, IllegalArgumentException {
		try {
			CustomerGRPC.LoginArgsGRPC loginArgs = CustomerGRPC.LoginArgsGRPC.newBuilder()
				.setUsername(username)
				.setPassword(password)
				.build();
			CustomerGRPC.BuyerIdGRPC response = this.stub.createUser(loginArgs);
			return response.getBuyerId();
		} catch (Exception e) {
			throw new IOException(e.getMessage()); // I will abandon all other types of errors for now
		}
		// once I figure out how, get the status code and run it through the method that might throw an error
		//   com.jsala.common.transport.grpc.GRPCErrorCodes.generateExceptionFromStatus
	}
	public int getUserId(String username, String password) throws IOException, NoSuchElementException {
		CustomerGRPC.LoginArgsGRPC loginArgs = CustomerGRPC.LoginArgsGRPC.newBuilder()
			.setUsername(username)
			.setPassword(password)
			.build();
		CustomerGRPC.BuyerIdGRPC response = this.stub.getUserId(loginArgs);
		return response.getBuyerId();
	}
	public Buyer getBuyerById(int buyerId) throws IOException, NoSuchElementException {
		return null;
	}
	public void closeConnection() throws IOException {

	}
}

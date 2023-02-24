/**
 * Class GRPCAdaptor
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * API version 1
 * Description: Convert from business logic items to GRPC items
 */

package com.jsala.common.transport.grpc;
import com.jsala.common.Buyer;
import com.jsala.grpc.generated.CustomerGRPC.BuyerGRPC;
import com.jsala.grpc.generated.CustomerGRPC.BuyerIdGRPC;

public class GRPCAdaptor {
	public static Buyer toBuyer(BuyerGRPC buyerGrpc) throws IllegalArgumentException {
		String name = buyerGrpc.getName();
		String password = buyerGrpc.getPassword();
		BuyerIdGRPC buyerId = buyerGrpc.getBuyerId();
		int numPurchases = buyerGrpc.getNumPurchases();
		return new Buyer(name, password, buyerId.getBuyerId(), numPurchases);
	}

	public static BuyerGRPC toBuyerGRPC(Buyer buyer) {
		BuyerGRPC.Builder buyerBuilder = BuyerGRPC.newBuilder();
		buyerBuilder.setName(buyer.getName());
		buyerBuilder.setPassword("dummy"); // never actually retrieve a password
		BuyerIdGRPC buyerId = BuyerIdGRPC.newBuilder().setBuyerId(buyer.getId()).build();
		buyerBuilder.setBuyerId(buyerId);
		buyerBuilder.setNumPurchases(buyer.getNumPurchases());
		return buyerBuilder.build();
	}
}

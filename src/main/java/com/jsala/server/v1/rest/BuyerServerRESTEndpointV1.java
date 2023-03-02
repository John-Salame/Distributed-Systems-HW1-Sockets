/**
 * Class BuyerServerRESTEndpointV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * Description: REST server for the buyer
 * Following tutorial https://www.javachinna.com/creating-spring-rest-api-without-using-spring-boot/
 */

package com.jsala.server.v1.rest;
import com.jsala.common.Item;
import com.jsala.common.interfaces.BuyerInterface;

import java.io.IOException;
import java.util.NoSuchElementException;

public class BuyerServerRESTEndpointV1 implements BuyerInterface {
    public BuyerServerRESTEndpointV1() {}

    // COMMON USER METHODS
    @Override
    public int createUser(String username, String password) throws IOException, IllegalArgumentException {
        return 0;
    }
    @Override
    public String login(String username, String password) throws IOException, NoSuchElementException {
        return null;
    }
    @Override
    public void logout(String sessionToken) throws IOException, NoSuchElementException {

    }
    @Override
    public int[] getSellerRating(int sellerId) throws IOException, NoSuchElementException {
        return new int[0];
    }

    // BUYER METHODS
    @Override
    public Item[] searchItem(String sessionToken, int category, String[] keywords) throws IOException {
        return new Item[0];
    }
}

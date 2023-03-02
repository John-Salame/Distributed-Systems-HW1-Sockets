/**
 * Class BuyerServerGRPCRunner
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * Description: Creates the buyer server using REST to client and sockets to connect to the database
 */

package com.jsala.server.v1.starter.buyer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BuyerServerRESTSocketRunner {
    public static void main(String[] args) {
        SpringApplication.run(BuyerServerRESTSocketRunner.class, args);
    }
}

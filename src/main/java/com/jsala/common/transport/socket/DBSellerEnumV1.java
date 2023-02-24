/**
 * Enum DBSellerEnumV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Used by sockets to contact the seller database (on customer database); alligns with DAO functions
 */

package com.jsala.common.transport.socket;

public enum DBSellerEnumV1 {
	CREATE_USER,
	GET_USER_ID,
	GET_SELLER_BY_ID,
	COMMIT_SELLER
}

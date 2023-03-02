/**
 * Enum APIEnumV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Used by sockets to find out which API an "rpc" belongs to when multiple services or APIs use the same socket
 */

package com.jsala.common.transport.socket;

public enum APIEnumV1 {
	ERROR,
	BUYER,
	SELLER,
	DB_SELLER,
	DB_BUYER,
	DB_SESSION,
	DB_ITEM,
	DB_SALE_LISTING
}
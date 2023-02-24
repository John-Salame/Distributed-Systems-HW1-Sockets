/**
 * Enum SellerEnumV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Used by sockets to produce or look up function IDs in order to implement rudimentary RPC
 */

package com.jsala.common.transport.socket;

public enum SellerEnumV1 {
	CREATE_USER,
	LOGIN,
	LOGOUT,
	GET_SELLER_RATING,
	PUT_ON_SALE,
	CHANGE_PRICE_OF_ITEM,
	REMOVE_ITEM_FROM_SALE,
	DISPLAY_ITEMS_ON_SALE
}

/**
 * Enum BuyerEnumV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Used by sockets to produce or look up function IDs in order to implement rudimentary RPC
 */

package com.jsala.common.transport.socket;

public enum BuyerEnumV1 {
	CREATE_USER,
	LOGIN,
	LOGOUT,
	GET_SELLER_RATING,
	SEARCH_ITEM,
	ADD_TO_CART,
	REMOVE_FROM_CART,
	CLEAR_CART,
	DISPLAY_CART,
	PROVIDE_FEEDBACK,
	GET_PURCHASE_HISTORY
}

/**
 * Enum DBSellerEnumV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Used by sockets to contact the seller database (on customer database); alligns with DAO functions
 */

package common.transport.socket;

public enum DBItemEnumV1 {
	CREATE_ITEM,
	GET_ITEM_BY_ID,
	CHANGE_PRICE,
	GET_ITEMS_BY_SELLER,
	GET_ITEMS_IN_CATEGORY
}

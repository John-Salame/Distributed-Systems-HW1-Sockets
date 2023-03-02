/**
 * Enum DBSaleListingEnumV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Used by sockets to contact the sale listing database (on products database); aligns with DAO functions
 */

package com.jsala.common.transport.socket;

public enum DBSaleListingEnumV1 {
    PUT_ITEM_ON_SALE,
    REMOVE_ITEM_FROM_SALE,
    GET_SALE_LISTINGS_BY_SELLER,
    CLOSE_CONNECTION
}

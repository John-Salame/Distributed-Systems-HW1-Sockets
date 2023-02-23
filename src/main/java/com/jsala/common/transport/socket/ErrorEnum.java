/**
 * Enum ErrorEnum
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Used by sockets to encode error messages over the wire
 * On these conditions, we don't need a versioned API for errors and we can use this in socket base classes or SocketMessage:
 *   1. No ErrorEnum values are ever removed; new values may be added.
 *   2. APIEnum always has the same ordinal for ERROR in each version (should be ordinal 0)
 *   3. We throw a generic error whenever we receive an unknown error type.
 * 
 * Currently, BaseSocketServerThread sends an Exception over the network inside of run(),
 * and BaseSocketClient uses SocketMessage.deserializeException() via sendAndReceive() to throw the received Exception.
 */

package com.jsala.common.transport.socket;

public enum ErrorEnum {
	IO_EXCEPTION,
	ILLEGAL_ARGUMENT_EXCEPTION,
	NO_SUCH_ELEMENT_EXCEPTION,
	UNSUPPORTED_OPERATION_EXCEPTION
}

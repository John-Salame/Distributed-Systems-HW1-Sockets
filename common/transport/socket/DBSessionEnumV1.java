/**
 * Enum DBSessionEnumV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Used by sockets to contact the session database (on customer database); alligns with DAO functions
 */

package common.transport.socket;

public enum DBSessionEnumV1 {
	CREATE_SESSION,
	EXPIRE_SESSION,
	GET_USER_ID_FROM_SESSION,
	LIST_SESSIONS
}

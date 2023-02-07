/**
 * Interface SessionDAO
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Interface to access the mapping of sessions to user IDs.
 */

package dao;

public interface SessionDAO {
	// Called by login() on server
	public abstract String createSession(int userId); // return the session key generated for the user
	// Called by logout() on server
	public abstract void expireSession(String sessionKey);
	// might throw an error if the session does not exist
	public abstract int getUserIdFromSession(String sessionKey);
	// just for debugging purposes
	public abstract String listSessions();
}
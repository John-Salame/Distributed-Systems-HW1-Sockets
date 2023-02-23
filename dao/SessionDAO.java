/**
 * Interface SessionDAO
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Interface to access the mapping of sessions to user IDs.
 */

package dao;
import java.io.IOException;
import java.util.NoSuchElementException;

public interface SessionDAO {
	// Called by login() on server
	public abstract String createSession(int userId) throws IOException; // return the session key generated for the user
	// Called by logout() on server
	public abstract void expireSession(String sessionKey) throws IOException, NoSuchElementException;
	// might throw an error if the session does not exist
	public abstract int getUserIdFromSession(String sessionKey) throws IOException, NoSuchElementException;
	// just for debugging purposes
	public abstract String listSessions() throws IOException;
	public abstract void closeConnection() throws IOException;
}
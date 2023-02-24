/**
 * Class SessionDaoInMemory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Keep a mapping of sessions to user IDs.
 */

package com.jsala.db.customer;
import com.jsala.dao.SessionDAO;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.io.IOException;

public class SessionDAOInMemory implements SessionDAO {
	// https://www.geeksforgeeks.org/map-interface-java-examples/
	private Map<String, Integer> sessions;

	public SessionDAOInMemory() {
		sessions = new HashMap<String, Integer>();
	}

	/**
	 * Method genereateUniqueSessionToken
	 * Generate a session token for a user.
	 * Make this simple for now, since the write-up says to address security concerns in a later assignment
	 * A user may have multiple session tokens associated with him or her (one per client).
	 * That way, logging out on one client does not disrupt any other client.
	 * We can use information about the client, such as time of login or user-agent field, in order to generate a unique session token.
	 * Private methods are allowed since Java 9 https://www.tutorialspoint.com/can-we-have-a-private-method-or-private-static-method-in-an-interface-in-java-9
	 * However, I don't have Java 9, so I won't put this method in the interface declaration.
	 */
	private String generateUniqueSessionToken(int userId) {
		String token = "";
		long salt = userId * userId;
		boolean duplicate = true;
		while(duplicate) {
			duplicate = false;
			long time = System.currentTimeMillis();
			token = Long.toHexString(Long.valueOf(time+salt));
			/* 
			byte[] data = 
			// resources for md5sum to hexidecimal:
			// https://stackoverflow.com/questions/332079/in-java-how-do-i-convert-a-byte-array-to-a-string-of-hex-digits-while-keeping-l
			// https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/digest/DigestUtils.html
			token = md5Hex(data);
			*/
			if(this.sessions.containsKey(token)) {
				duplicate = true;
			}
		}
		return token;
	}

	// Called by login() on server
	// Note: Since we don't know whether a buyer or seller is logging in, we can't validate the userId here and must assume it is correct.
	public String createSession(int userId) throws IOException {
		String sessionKey = this.generateUniqueSessionToken(userId);
		try {
			sessions.put(sessionKey, Integer.valueOf(userId)); // I don't know if this can cause an Exception
		} catch (Exception e) {
			throw new IOException("Failed to create session for user " + userId);
		}
		return sessionKey;
	}

	// Called by logout() on server
	public void expireSession(String sessionKey) throws IOException, NoSuchElementException {
		if (!sessions.containsKey(sessionKey)) {
			throw new NoSuchElementException("Cannot log out session which does not exist");
		}
		// put this try/catch in case somebody logs out between the containsKey check and now. I don't think this is possible.
		try {
			sessions.remove(sessionKey); // may cause an Exception
		} catch (Exception e) {
			System.out.println(e);
			throw new IOException("Somebody already logged out from this session");
		}
	}

	// might throw an error if the session does not exist
	public int getUserIdFromSession(String sessionKey) throws IOException, NoSuchElementException {
		if (!sessions.containsKey(sessionKey)) {
			throw new NoSuchElementException("Cannot get user associated with a session that does not exist");
		}
		// put this try/catch in case somebody logs out between the containsKey check and now. Replace with some locking mechanism later.
		try {
			return sessions.get(sessionKey).intValue();
		} catch (Exception e) {
			System.out.println(e);
			throw new IOException("Somebody already logged out from this session");
		}
	}

	public String listSessions() throws IOException {
		String ret = "Current Sessions:";
		// Using iterator to access elements of HashMap: https://www.tutorialspoint.com/traversing-contents-of-a-hash-map-in-java
		try {
			for(Map.Entry el : this.sessions.entrySet()) {
				ret = ret + "\n  Session Token " + el.getKey() + " corresponds to user id " + el.getValue();
			}
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
		return ret;
	}

	public void closeConnection() {
		// do nothing
	}
}

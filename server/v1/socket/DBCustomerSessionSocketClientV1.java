/**
 * Class DBCustomerSessionSocketClientV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * API version 1
 * Description: Socket implementation of session server-database IPC on server side
 */

package server.v1.socket;
import dao.SessionDAO;
import common.transport.serialize.*;
import common.transport.socket.APIEnumV1;
import common.transport.socket.BaseSocketClient;
import common.transport.socket.DBSessionEnumV1;
import common.Seller;
import java.io.IOException;
import java.net.SocketException;

public class DBCustomerSessionSocketClientV1 extends BaseSocketClient implements SessionDAO {

	// CONSTRUCTORS
	// recommend serverIp = localhost
	public DBCustomerSessionSocketClientV1(String serverIp, int serverPort) /*throws SocketException*/ {
		super(serverIp, serverPort, (short) 1, APIEnumV1.DB_SESSION.ordinal());
	}

	// SellerDAO Methods
	public String createSession(int userId) {
		int funcId = DBSessionEnumV1.CREATE_SESSION.ordinal();
		String sessionToken = null;
		try {
			byte[] msg = SerializeInt.serialize(userId);
			byte[] buf = this.sendAndReceive(msg, funcId);
			sessionToken = SerializeString.deserialize(buf);
		}
		catch (IOException i) {
			System.out.println(i);
		}
		return sessionToken;
	}
	public void expireSession(String sessionKey) {
		int funcId = DBSessionEnumV1.EXPIRE_SESSION.ordinal();
		try {
			byte[] msg = SerializeString.serialize(sessionKey);
			byte[] buf = this.sendAndReceive(msg, funcId);
			assert buf.length == 0;
		}
		catch (IOException i) {
			System.out.println(i);
		}
	}
	public int getUserIdFromSession(String sessionKey) {
		int funcId = DBSessionEnumV1.GET_USER_ID_FROM_SESSION.ordinal();
		int userId = 0; // user id 0 indicates error
		try {
			byte[] msg = SerializeString.serialize(sessionKey);
			byte[] buf = this.sendAndReceive(msg, funcId);
			userId = SerializeInt.deserialize(buf);
		}
		catch (IOException i) {
			System.out.println(i);
		}
		return userId;
	}
	public String listSessions() {
		int funcId = DBSessionEnumV1.LIST_SESSIONS.ordinal();
		String sessions = null;
		try {
			byte[] msg = new byte[0];
			byte[] buf = this.sendAndReceive(msg, funcId);
			sessions = SerializeString.deserialize(buf);
		}
		catch (IOException i) {
			System.out.println(i);
		}
		return sessions;
	}
}

/**
 * Class SerializeLogin
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: (De)serialize a (username, password) combo.
 */

package common.transport.serialize;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class SerializeLogin {
	private String username;
	private String password;

	public SerializeLogin(String username, String password) {
		this.username = username;
		this.password = password;
	}

	// we are not enforcing username length at this point, though we could.
	public static byte[] serialize(String username, String password) throws IOException {
		if (username == null || password == null) {
			throw new IOException("SerializeLogin attempted to write null username or password");
		}
		ByteArrayOutputStream buf = new ByteArrayOutputStream(); // grow dynamically
		DataOutputStream writer = new DataOutputStream(buf);
		writer.writeUTF(username);
		writer.writeUTF(password);
		byte ret[] = buf.toByteArray();
		writer.close();
		buf.close();
		return ret;
	}

	public static SerializeLogin deserialize(byte[] b) throws IOException {
		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream reader = new DataInputStream(buf);
		String username = reader.readUTF();
		String password = reader.readUTF();
		reader.close();
		buf.close();
		return new SerializeLogin(username, password);
	}


	// GETTERS
	public String getUsername() {
		return this.username;
	}
	public String getPassword() {
		return this.password;
	}
}

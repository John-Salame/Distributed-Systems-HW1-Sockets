/**
 * Class BuyerClientRESTV1
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * Description: REST client for the buyer interface using JSON
 * Following a Java REST tutorial https://www.youtube.com/watch?v=9oq7Y8n1t00
 */

package com.jsala.client.v1.rest;
import com.jsala.common.interfaces.BuyerInterface;
import com.jsala.common.transport.serialize.*;
import com.jsala.common.Item;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class BuyerClientRESTV1 implements BuyerInterface {
	private String hostname;
	private int port;

	// CONSTRUCTOR
	public BuyerClientRESTV1(String hostname, int port) {
		if (port > 65535) {
			throw new IllegalArgumentException("Invalid port");
		}
		if(port < 1024 && port != 80) {
			this.port = 80; // default port; if you choose a reserved port, switch to default port instead
		}
		else {
			this.port = port;
		}
		this.hostname = hostname;
	}

	// helper function
	private URI createURI(String path) {
		return new URI("http://" + this.hostname + ":" + this.port + "/" + path);
	}

	// INHERITED METHODS
	// POST
	public int createUser(String username, String password) throws IOException {
		byte[] payload = SerializeLogin.serialize(username, password);
		String resource = "createUser";
		HttpRequest request = HttpRequest.newBuilder()
			.uri(this.createURI(resource))
			.POST(BodyPublishers.ofByteArray(payload))
			.build();
		HttpClient httpClient = HttpClient.newHttpClient();
		
		HttpResponse<byte[]> response = httpClient.send(request, BodyHandlers.ofByteArray());
		return SerializeInt.deserialize(response.body());
	}
	// POST
	public String login(String username, String password) {
		throw new UnsupportedOperationException("REST buyer login() unimplemented");
	}
	// DELETE
	public void logout(String sessionToken) {
		throw new UnsupportedOperationException("REST buyer logout() unimplemented");
	}
	// GET
	public int[] getSellerRating(int sellerId) {
		throw new UnsupportedOperationException("REST buyer getSellerRating() unimplemented");
	}
	// GET
	// I will use comma-separated words for keywords as mentioned as an option here: https://www.atatus.com/blog/rest-api-best-practices-for-parameter-and-query-string-usage/
	public Item[] searchItem(String sessionToken, int category, String[] keywords) {
		throw new UnsupportedOperationException("REST buyer searchItem() unimplemented");
	}
}

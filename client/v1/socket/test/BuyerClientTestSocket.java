/**
 * Class BuyerClientTestSocket
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Tries various buyer API functions, hardcoded, in order to test them out.
 */

package client.v1.socket.test;
import client.v1.*;
import client.v1.socket.BuyerSocketClientV1;
import common.Buyer;
import common.BuyerInterface;
import common.Seller;
import common.SellerInterface;
import common.Item;
import common.ItemId;
import common.SaleListing;
import common.SaleListingId;
import java.io.IOException;
import java.util.Arrays;

 public class BuyerClientTestSocket {
	public static void main(String[] args) throws IOException {
		String serverIp = "localhost";
		int serverPort = 8100;
		BuyerInterface transport = new BuyerSocketClientV1(serverIp, serverPort);
		BuyerInterface buyerInterfaceClient = new BuyerInterfaceClientV1(transport);

		// Basic buyer API calls
		String buyer1Username = "Joe";
		String buyer1Password = "password123";
		int buyer1Id = buyerInterfaceClient.createUser(buyer1Username, buyer1Password);
		System.out.println("Buyer 1 id: " + buyer1Id);
		String buyer1SessionToken = buyerInterfaceClient.login(buyer1Username, buyer1Password);
		System.out.println("Searching for fruit");
		Item[] searchResults;
		searchResults = buyerInterfaceClient.searchItem(buyer1SessionToken, 0, new String[] {"fruit"}); // apple should top the list
		System.out.println(Arrays.toString(searchResults));
		System.out.println("\nSearching for sweet fruit");
		searchResults = buyerInterfaceClient.searchItem(buyer1SessionToken, 0, new String[] {"fruit", "sweet"}); // pear should top the list
		System.out.println(Arrays.toString(searchResults));
		System.out.println("\nSearching for furniture in category 1");
		searchResults = buyerInterfaceClient.searchItem(buyer1SessionToken, 1, new String[] {"furniture"});
		System.out.println(Arrays.toString(searchResults));
		System.out.println("Buyer 1 session token = " + buyer1SessionToken);
		System.out.println("Buyer 1 logging out");
		buyerInterfaceClient.logout(buyer1SessionToken);
	}
 }

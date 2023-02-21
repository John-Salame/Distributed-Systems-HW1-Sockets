/**
 * Interface SellerDAO
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: An interface to access the database of sellers (in Customer database).
 */

package dao;
import common.Seller;
import java.io.IOException;
import java.util.NoSuchElementException;

public interface SellerDAO {
	public abstract int createUser(String username, String password) throws IOException, IllegalArgumentException; // return the user id
	public abstract int getUserId(String username, String password) throws IOException, NoSuchElementException;
	public abstract Seller getSellerById(int sellerId) throws IOException, NoSuchElementException;
	public abstract void commitSeller(Seller seller) throws IOException, IllegalArgumentException;
}
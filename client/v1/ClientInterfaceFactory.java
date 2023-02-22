/**
 * Interface ClientInterfaceFactory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Create an instance of an interface when needed. Different subclasses of this can implement different transport technologies such as RPC or Socket.
 * The main purpose was to allow clients to create their sockets independently of each other.
 *   * I previously had a bug where all clients were using the same socket connection and would all lose connection when one logged off.
 */

package client.v1;
import common.BuyerInterface;
import common.SellerInterface;
import java.lang.reflect.InvocationTargetException;

public interface ClientInterfaceFactory {
	public abstract BuyerInterface createBuyerInterface() throws InvocationTargetException;
	public abstract SellerInterface createSellerInterface() throws InvocationTargetException;
}

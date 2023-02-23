/**
 * Interface UserInterfaceFactory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Create an instance of a buyer or seller interface when needed. Different subclasses of this can implement different transport technologies such as REST, gRPC, or Socket.
 * The main purpose was to allow threads to create client sockets independently of each other.
 *   * I previously had a bug where all clients were using the same socket connection and would all lose connection when one logged off.
 */

package common.interfaces.factory;
import common.interfaces.BuyerInterface;
import common.interfaces.SellerInterface;
import java.lang.reflect.InvocationTargetException;

public interface UserInterfaceFactory {
	public abstract BuyerInterface createBuyerInterface() throws InvocationTargetException;
	public abstract SellerInterface createSellerInterface() throws InvocationTargetException;
}

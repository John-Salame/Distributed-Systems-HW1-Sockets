/**
 * Class ClientSocketFactory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 1 - Sockets
 * Description: Get a BuyerInterface or SellerInterface which creates a socket when you call the createBuyerInterface() or createSellerInterface() methods.
 * This isn't very powerful with what kind of wrapper you can use. The wrapper must have a constructor which takes only one BuyerInterface or SellerInterface.
 * This is necessary to create one socket per buyer or seller, so their traffic doesn't trample each other
 */

package client.v1.socket;
import common.interfaces.factory.UserInterfaceFactory;
import common.interfaces.BuyerInterface;
import common.interfaces.SellerInterface;
import java.lang.reflect.InvocationTargetException;

public class ClientSocketFactory implements UserInterfaceFactory {
	private Class wrapperBuyerInterface; // these wrappers should be classes, such as BuyerInterfaceClientV1.class
	private Class wrapperSellerInterface;
	private String serverHost;
	private int serverPort;

	public ClientSocketFactory(Class wrapperBuyerInterface, Class wrapperSellerInterface, String serverHost, int serverPort) {
		this.wrapperBuyerInterface = wrapperBuyerInterface;
		this.wrapperSellerInterface = wrapperSellerInterface;
		this.serverHost = serverHost;
		this.serverPort = serverPort;
	}

	public BuyerInterface createBuyerInterface() throws InvocationTargetException {
		BuyerInterface socket;
		try {
			socket = new BuyerSocketClientV1(this.serverHost, this.serverPort);
			if (wrapperBuyerInterface != null) {
				// https://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Class.html
				return (BuyerInterface) wrapperBuyerInterface.getConstructor(new Class[] {BuyerInterface.class}).newInstance(socket);
			}
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		}
		return socket;
	}
	public SellerInterface createSellerInterface() throws InvocationTargetException {
		return null;
	}
}

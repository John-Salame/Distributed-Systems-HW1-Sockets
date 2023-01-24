package common;

public interface BuyerInterface {
	// TO-DO: Figure out how to do error handling
	public abstract void Login(String username, String password);
	public abstract void Logout();

}
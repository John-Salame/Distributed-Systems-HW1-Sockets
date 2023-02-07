import common.Seller;
import common.Buyer;
import common.Item;

public class Runner {
	public static void main (String[] args) {
		System.out.println("Hello world!");
		System.out.println("");

		Seller mySeller = new Seller();
		mySeller.setName("John");
		mySeller.setId(123);
		mySeller.addThumbsUp();
		mySeller.addThumbsUp();
		mySeller.addThumbsDown();
		mySeller.setNumSold(3);
		System.out.println(mySeller);

		Buyer myBuyer = new Buyer();
		myBuyer.setName("Joe");
		myBuyer.setId(456);
		myBuyer.setNumPurchases(2);
		System.out.println(myBuyer);

		Item apple = new Item();
		apple.setName("apple");
		apple.setId(1, 13);
		apple.addKeyword("food");
		apple.addKeyword("fruit");
		apple.setCondition("New");
		apple.setPrice((float) 0.79);
		System.out.println(apple);
	}
}
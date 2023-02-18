# Modified from https://www.cs.swarthmore.edu/~newhall/unixhelp/javamakefiles.html
# use current directory as class path
CLASSPATH = -cp "${PWD}"
JFLAGS = -g -Xlint:deprecation -Xlint:unchecked $(CLASSPATH)
JC = "/c/Program Files/Common Files/Oracle/Java/javapath/javac"
JR = "/c/Program Files/Common Files/Oracle/Java/javapath/java" $(CLASSPATH)
RM = rm
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	client/v1/BuyerInterfaceClientV1.java \
	client/v1/BuyerSocketClientV1.java \
	client/v1/SellerInterfaceClientV1.java \
	client/v1/SellerSocketClientV1.java \
	client/BuyerRunnerClient.java \
	client/SellerRunnerClient.java \
	common/transport/serialize/SerializeInt.java \
	common/transport/serialize/SerializeIntArray.java \
	common/transport/serialize/SerializeLogin.java \
	common/transport/serialize/SerializeString.java \
	common/transport/serialize/SerializeStringArray.java \
	common/transport/socket/APIEnumV1.java \
	common/transport/socket/BaseSocketClient.java \
	common/transport/socket/BaseSocketServerThread.java \
	common/transport/socket/BuyerEnumV1.java \
	common/transport/socket/DBBuyerEnumV1.java \
	common/transport/socket/DBSellerEnumV1.java \
	common/transport/socket/DBSessionEnumV1.java \
	common/transport/socket/PacketPrefix.java \
	common/transport/socket/SellerEnumV1.java \
	common/transport/socket/SocketMessage.java \
	common/Buyer.java \
	common/BuyerInterface.java \
	common/CartItem.java \
	common/CommonUserInterface.java \
	common/Item.java \
	common/ItemId.java \
	common/SaleListing.java \
	common/SaleListingId.java \
	common/Seller.java \
	common/SellerInterface.java \
	dao/BuyerDAO.java \
	dao/ItemDAO.java \
	dao/SaleListingDAO.java \
	dao/SellerDAO.java \
	dao/SessionDAO.java \
	dao/ShoppingCartDAO.java \
	db/customer/v1/DBCustomerSocketServerListenerV1.java \
	db/customer/v1/DBCustomerSocketServerThreadV1.java \
	db/customer/BuyerDAOInMemory.java \
	db/customer/SellerDAOInMemory.java \
	db/customer/SessionDAOInMemory.java \
	db/product/ItemDAOInMemory.java \
	server/v1/BuyerInterfaceServerV1.java \
	server/v1/BuyerSocketServerListenerV1.java \
	server/v1/BuyerSocketServerThreadV1.java \
	server/v1/DBCustomerBuyerSocketClientV1.java \
	server/v1/DBCustomerSellerSocketClientV1.java \
	server/v1/DBProductItemSocketClientV1.java \
	server/v1/SellerInterfaceServerV1.java \
	server/v1/SellerSocketServerListenerV1.java \
	server/v1/SellerSocketServerThreadV1.java \
	server/ServerRunnerInMemory.java \
	server/BuyerRunnerServer.java \
	server/SellerRunnerServer.java \
	util/EditDistance.java

all: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) **/*.class

# Run the server's test program using server-local in-memory "databases"
run_server_in_memory:
	$(JR) server/ServerRunnerInMemory

run_buyer_client:
	$(JR) client/BuyerRunnerClient.java

run_buyer_server:
	$(JR) server/BuyerRunnerServer.java

run_seller_client:
	$(JR) client/SellerRunnerClient.Java

run_seller_server:
	$(JR) server/SellerRunnerServer.java

run_db_customer:
	$(JR) db/customer/CustomerRunner.java

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
	client/v1/socket/test/BuyerClientTestSocket.java \
	client/v1/socket/test/SellerClientTestSocket.java \
	client/v1/socket/BuyerSocketClientV1.java \
	client/v1/socket/ClientSocketFactory.java \
	client/v1/socket/SellerSocketClientV1.java \
	client/v1/timing/socket/BuyerClientTimingStudySocket.java \
	client/v1/timing/socket/SellerClientTimingStudySocket.java \
	client/v1/timing/BuyerClientTimingInstance.java \
	client/v1/timing/SellerClientTimingInstance.java \
	client/v1/BuyerInterfaceClientV1.java \
	client/v1/SellerInterfaceClientV1.java \
	common/interfaces/factory/UserInterfaceFactory.java \
	common/interfaces/BuyerInterface.java \
	common/interfaces/CommonUserInterface.java \
	common/interfaces/SellerInterface.java \
	common/transport/serialize/SerializeInt.java \
	common/transport/serialize/SerializeIntArray.java \
	common/transport/serialize/SerializeLogin.java \
	common/transport/serialize/SerializePriceArgClientServer.java \
	common/transport/serialize/SerializePriceArgDB.java \
	common/transport/serialize/SerializeSearchArg.java \
	common/transport/serialize/SerializeString.java \
	common/transport/serialize/SerializeStringArray.java \
	common/transport/socket/APIEnumV1.java \
	common/transport/socket/BaseSocketClient.java \
	common/transport/socket/BaseSocketServerThread.java \
	common/transport/socket/BuyerEnumV1.java \
	common/transport/socket/DBBuyerEnumV1.java \
	common/transport/socket/DBItemEnumV1.java \
	common/transport/socket/DBSellerEnumV1.java \
	common/transport/socket/DBSessionEnumV1.java \
	common/transport/socket/ErrorEnum.java \
	common/transport/socket/PacketPrefix.java \
	common/transport/socket/SellerEnumV1.java \
	common/transport/socket/SocketMessage.java \
	common/Buyer.java \
	common/CartItem.java \
	common/Item.java \
	common/ItemId.java \
	common/SaleListing.java \
	common/SaleListingId.java \
	common/Seller.java \
	common/TimingLog.java \
	dao/factory/CustomerDAOFactory.java \
	dao/factory/ProductDAOFactory.java \
	dao/BuyerDAO.java \
	dao/ItemDAO.java \
	dao/SaleListingDAO.java \
	dao/SellerDAO.java \
	dao/SessionDAO.java \
	dao/ShoppingCartDAO.java \
	db/customer/v1/DBCustomerSocketServerListenerV1.java \
	db/customer/v1/DBCustomerSocketServerThreadV1.java \
	db/customer/BuyerDAOInMemory.java \
	db/customer/CustomerDAOFactoryInMemory.java \
	db/customer/DBCustomerRunner.java \
	db/customer/SellerDAOInMemory.java \
	db/customer/SessionDAOInMemory.java \
	db/product/v1/DBProductSocketServerListenerV1.java \
	db/product/v1/DBProductSocketServerThreadV1.java \
	db/product/DBProductRunner.java \
	db/product/ItemDAOInMemory.java \
	db/product/ProductDAOFactoryInMemory.java \
	server/v1/factory/DBClientProgramFactorySocketV1.java \
	server/v1/factory/DBCustomerDAOFactorySocketV1.java \
	server/v1/factory/DBProductDAOFactorySocketV1.java \
	server/v1/socket/test/BuyerServerTestSocket.java \
	server/v1/socket/test/SellerServerTestSocket.java \
	server/v1/socket/BuyerSocketServerListenerV1.java \
	server/v1/socket/BuyerSocketServerThreadV1.java \
	server/v1/socket/DBCustomerBuyerSocketClientV1.java \
	server/v1/socket/DBCustomerSellerSocketClientV1.java \
	server/v1/socket/DBCustomerSessionSocketClientV1.java \
	server/v1/socket/DBProductItemSocketClientV1.java \
	server/v1/socket/SellerSocketServerListenerV1.java \
	server/v1/socket/SellerSocketServerThreadV1.java \
	server/v1/BuyerInterfaceServerImplV1.java \
	server/v1/SellerInterfaceServerImplV1.java \
	util/EditDistance.java

all: classes

classes: $(CLASSES:.java=.class)

clean:
	find . -name "*.class" -type f -delete

run_buyer_client:
	$(JR) client/v1/timing/socket/BuyerClientTimingStudySocket.java

run_buyer_server:
	$(JR) server/v1/socket/test/BuyerServerTestSocket.java

run_seller_client:
	$(JR) client/v1/timing/socket/SellerClientTimingStudySocket.java

run_seller_server:
	$(JR) server/v1/socket/test/SellerServerTestSocket.java

run_db_customer:
	$(JR) db/customer/DBCustomerRunner.java

run_db_product:
	$(JR) db/product/DBProductRunner.java

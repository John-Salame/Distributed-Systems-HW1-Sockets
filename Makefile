# Modified from https://www.cs.swarthmore.edu/~newhall/unixhelp/javamakefiles.html
# use current directory as class path
CLASSPATH = -cp "${PWD}"
JFLAGS = -g -Xlint:deprecation $(CLASSPATH)
JC = "/c/Program Files/Common Files/Oracle/Java/javapath/javac"
JR = "/c/Program Files/Common Files/Oracle/Java/javapath/java" $(CLASSPATH)
RM = rm
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	client/v1/BuyerInterfaceClientV1.java \
	client/v1/BuyerSocketClientV1.java \
	client/BuyerRunnerClient.java \
	common/transport/serialize/SerializeLogin.java \
	common/transport/socket/BuyerEnumV1.java \
	common/transport/socket/PacketPrefix.java \
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
	dao/BuyerDAOInMemory.java \
	dao/ItemDAO.java \
	dao/SaleListingDAO.java \
	dao/SellerDAO.java \
	dao/SellerDAOInMemory.java \
	dao/SessionDAO.java \
	dao/SessionDAOInMemory.java \
	dao/ShoppingCartDAO.java \
	server/v1/BuyerInterfaceServerV1.java \
	server/v1/BuyerSocketServerListenerV1.java \
	server/v1/BuyerSocketServerThreadV1.java \
	server/v1/SellerInterfaceServerV1.java \
	server/ServerRunnerInMemory.java \
	server/BuyerRunnerServer.java \
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

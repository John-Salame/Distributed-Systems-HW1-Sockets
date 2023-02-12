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
	Runner.java \
	common/Buyer.java \
	common/BuyerInterface.java \
	common/CartItem.java \
	common/CommonUserInterface.java \
	common/Item.java \
	common/ItemId.java \
	common/SaleListing.java \
	common/Seller.java \
	common/SellerInterface.java \
	dao/BuyerDAO.java \
	dao/BuyerDAOInMemory.java \
	dao/SellerDAO.java \
	dao/SellerDAOInMemory.java \
	dao/SessionDAO.java \
	dao/SessionDAOInMemory.java \
	server/BuyerInterfaceServer.java \
	server/SellerInterfaceServer.java \
	server/ServerRunnerInMemory.java \
	util/EditDistance.java

all: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) **/*.class
	$(RM) *.class

run:
	$(JR) Runner

# Run the server's test program using server-local in-memory "databases"
run_server_in_memory:
	$(JR) server/ServerRunnerInMemory

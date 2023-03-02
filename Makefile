# Modified from https://www.cs.swarthmore.edu/~newhall/unixhelp/javamakefiles.html
# use current directory as class path
# Help with classpath: https://www3.ntu.edu.sg/home/ehchua/programming/java/J9c_PackageClasspath.html

# change this if your Maven downloads dependencies elsewhere
MAVEN_LOCAL_REPO = "/c/Users/${USER}/.m2"
MAVEN_ARGS = -e -Dmaven.repo.local=$(MAVEN_LOCAL_REPO)
# which directory to compile classes to
CLASSDEST = target/classes
# where to look for classes
CLASSPATH = "$(CLASSDEST);$(MAVEN_LOCAL_REPO)"
# where to look for java files during compilation
SOURCEPATH = -sourcepath "${PWD}/src/main/java"
JFLAGS = -g -Xlint:deprecation -Xlint:unchecked $(SOURCEPATH) -cp $(CLASSPATH) -d $(CLASSDEST)
JC = "/c/Program Files/Common Files/Oracle/Java/javapath/javac"
JR = "/c/Program Files/Common Files/Oracle/Java/javapath/java" -cp $(CLASSPATH)
RM = rm
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	src/main/java/com/jsala/client/v1/rest/BuyerClientRESTV1.java \
	src/main/java/com/jsala/client/v1/timing/socket/BuyerClientTimingStudySocket.java \
	src/main/java/com/jsala/client/v1/timing/socket/SellerClientTimingStudySocket.java \
	src/main/java/com/jsala/server/v1/starter/buyer/BuyerServerSocketSocketRunner.java \
	src/main/java/com/jsala/server/v1/starter/seller/SellerServerSocketSocketRunner.java \
	src/main/java/com/jsala/db/customer/DBCustomerRunner.java \
	src/main/java/com/jsala/db/product/DBProductRunner.java

CLASSES_DB_SOCKET = \
	src/main/java/com/jsala/db/customer/DBCustomerRunner.java \
	src/main/java/com/jsala/db/product/DBProductRunner.java

CLASSES_CLIENT_REST = \
	src/main/java/com/jsala/client/v1/rest/BuyerClientRESTV1.java

all: mvn_install \
	mvn_install_buyer_client \
	mvn_install_buyer_server \
	mvn_install_seller_client \
	mvn_install_seller_server \
	mvn_install_db_customer \
	mvn_install_db_product

clean:
	mvn clean
	# find . -name "*.class" -type f -delete


# use Maven to compile the code and place the jars in MAVEN_LOCAL_REPO
mvn_install:
	mvn $(MAVEN_ARGS) install

classes: $(CLASSES:.java=.class)

db_socket: $(CLASSES_DB_SOCKET:.java=.class)

db_rest:
	$(CLASSES_DB_REST:.java=.class)

mvn_install_buyer_client:
	mvn $(MAVEN_ARGS) -f pom_buyer_client.xml install
run_buyer_client:
	java -jar $(MAVEN_LOCAL_REPO)/com/jsala/distributed-systems-rpc-buyer-client/1.0-SNAPSHOT/distributed-systems-rpc-buyer-client-1.0-SNAPSHOT.jar
mvn_install_buyer_server:
	mvn $(MAVEN_ARGS) -f pom_buyer_server.xml install
run_buyer_server:
	java -jar $(MAVEN_LOCAL_REPO)/com/jsala/distributed-systems-rpc-buyer-server/1.0-SNAPSHOT/distributed-systems-rpc-buyer-server-1.0-SNAPSHOT.jar
mvn_install_seller_client:
	mvn $(MAVEN_ARGS) -f pom_seller_client.xml install
run_seller_client:
	java -jar $(MAVEN_LOCAL_REPO)/com/jsala/distributed-systems-rpc-seller-client/1.0-SNAPSHOT/distributed-systems-rpc-seller-client-1.0-SNAPSHOT.jar
mvn_install_seller_server:
	mvn $(MAVEN_ARGS) -f pom_seller_server.xml install
run_seller_server:
	java -jar $(MAVEN_LOCAL_REPO)/com/jsala/distributed-systems-rpc-seller-server/1.0-SNAPSHOT/distributed-systems-rpc-seller-server-1.0-SNAPSHOT.jar
mvn_install_db_customer:
	mvn $(MAVEN_ARGS) -f pom_db_customer.xml install
run_db_customer:
	java -jar $(MAVEN_LOCAL_REPO)/com/jsala/distributed-systems-rpc-db-customer/1.0-SNAPSHOT/distributed-systems-rpc-db-customer-1.0-SNAPSHOT.jar
mvn_install_db_product:
	mvn $(MAVEN_ARGS) -f pom_db_product.xml install
run_db_product:
	java -jar $(MAVEN_LOCAL_REPO)/com/jsala/distributed-systems-rpc-db-product/1.0-SNAPSHOT/distributed-systems-rpc-db-product-1.0-SNAPSHOT.jar

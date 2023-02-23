# Modified from https://www.cs.swarthmore.edu/~newhall/unixhelp/javamakefiles.html
# use current directory as class path
# Help with classpath: https://www3.ntu.edu.sg/home/ehchua/programming/java/J9c_PackageClasspath.html

# change this if your Maven downloads dependencies elsewhere
MAVEN_LOCAL_REPO = "/c/Users/${USER}/.m2"
# which directory to compile classes to
CLASSDEST = -d target/classes
# where to look for classes
CLASSPATH = -cp "$(CLASSDEST);$(MAVEN_LOCAL_REPO)/repository"
# where to look for java files during compilation
SOURCEPATH = -sourcepath "${PWD}/src/main/java"
JFLAGS = -g -Xlint:deprecation -Xlint:unchecked $(SOURCEPATH) $(CLASSPATH) $(CLASSDEST)
JC = "/c/Program Files/Common Files/Oracle/Java/javapath/javac"
JR = "/c/Program Files/Common Files/Oracle/Java/javapath/java" $(CLASSPATH)
RM = rm
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	src/main/java/com/jsala/client/v1/rest/BuyerClientRESTV1.java \
	src/main/java/com/jsala/client/v1/timing/socket/BuyerClientTimingStudySocket.java \
	src/main/java/com/jsala/client/v1/timing/socket/SellerClientTimingStudySocket.java \
	src/main/java/com/jsala/server/v1/socket/test/BuyerServerTestSocket.java \
	src/main/java/com/jsala/server/v1/socket/test/SellerServerTestSocket.java \
	src/main/java/com/jsala/db/customer/DBCustomerRunner.java \
	src/main/java/com/jsala/db/product/DBProductRunner.java

CLASSES_DB_SOCKET = \
	src/main/java/com/jsala/db/customer/DBCustomerRunner.java \
	src/main/java/com/jsala/db/product/DBProductRunner.java

CLASSES_CLIENT_REST = \
	src/main/java/com/jsala/client/v1/rest/BuyerClientRESTV1.java

all: classes

clean:
	find . -name "*.class" -type f -delete

# maven build
mvn_build:
	mvn -e -Dmaven.repo.local=$(MAVEN_LOCAL_REPO) compile

classes: $(CLASSES:.java=.class)

db_socket: $(CLASSES_DB_SOCKET:.java=.class)

db_rest:
	$(CLASSES_DB_REST:.java=.class)

run_buyer_client:
	$(JR) src/client/v1/timing/socket/BuyerClientTimingStudySocket.java

run_buyer_server:
	$(JR) src/server/v1/socket/test/BuyerServerTestSocket.java

run_seller_client:
	$(JR) src/client/v1/timing/socket/SellerClientTimingStudySocket.java

run_seller_server:
	$(JR) src/server/v1/socket/test/SellerServerTestSocket.java

run_db_customer:
	$(JR) src/db/customer/DBCustomerRunner.java

run_db_product:
	$(JR) src/db/product/DBProductRunner.java

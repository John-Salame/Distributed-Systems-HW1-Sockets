# Modified from https://www.cs.swarthmore.edu/~newhall/unixhelp/javamakefiles.html
# use current directory as class path
# Help with classpath: https://www3.ntu.edu.sg/home/ehchua/programming/java/J9c_PackageClasspath.html

# change this if your Maven downloads dependencies elsewhere
MAVEN_LOCAL_REPO = "/c/Users/${USER}/.m2"
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
	src/main/java/com/jsala/server/v1/socket/test/BuyerServerTestSocket.java \
	src/main/java/com/jsala/server/v1/socket/test/SellerServerTestSocket.java \
	src/main/java/com/jsala/db/customer/DBCustomerRunner.java \
	src/main/java/com/jsala/db/product/DBProductRunner.java

CLASSES_DB_SOCKET = \
	src/main/java/com/jsala/db/customer/DBCustomerRunner.java \
	src/main/java/com/jsala/db/product/DBProductRunner.java

CLASSES_CLIENT_REST = \
	src/main/java/com/jsala/client/v1/rest/BuyerClientRESTV1.java

all: mvn_build

clean:
	mvn clean
	# find . -name "*.class" -type f -delete

# use Maven to compile the code
mvn_build:
	mvn -e -Dmaven.repo.local=$(MAVEN_LOCAL_REPO) compile

classes: $(CLASSES:.java=.class)

db_socket: $(CLASSES_DB_SOCKET:.java=.class)

db_rest:
	$(CLASSES_DB_REST:.java=.class)

run_buyer_client:
	$(JR) com/jsala/client/v1/timing/socket/BuyerClientTimingStudySocket.java

run_buyer_server:
	$(JR) com/jsala/server/v1/socket/test/BuyerServerTestSocket.java

run_seller_client:
	$(JR) com/jsala/client/v1/timing/socket/SellerClientTimingStudySocket.java

run_seller_server:
	$(JR) com/jsala/server/v1/socket/test/SellerServerTestSocket.java

run_db_customer:
	$(JR) com/jsala/db/customer/DBCustomerRunner.java

run_db_product:
	$(JR) com/jsala/db/product/DBProductRunner.java

# Modified from https://www.cs.swarthmore.edu/~newhall/unixhelp/javamakefiles.html
# use current directory as class path
JFLAGS = -g -cp "${PWD}"
JC = "/c/Program Files/Common Files/Oracle/Java/javapath/javac"
JR = "/c/Program Files/Common Files/Oracle/Java/javapath/java"
RM = rm
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Runner.java \
	common/Buyer.java \
	common/Item.java \
	common/Seller.java

all: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) **/*.class
	$(RM) *.class

run:
	$(JR) Runner

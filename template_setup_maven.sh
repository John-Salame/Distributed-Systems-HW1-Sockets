#!/bin/bash

# set up environment required for Maven
# this is a template only; fill in the values for your system in a file called setup_maven.sh
# then, in bash, run "source setup_maven.sh"

export JAVA_HOME="/c/Program Files/Java/jdk-16.01"
export PATH="/c/Program Files/Common Files/Oracle/Java/javapath:$PATH"
export PATH="$PATH:$JAVA_HOME/bin"
export MAVEN_HOME="/c/Program Files/apache-maven-3.8.1-bin/apache-maven-3.8.1"
export PATH="$PATH:$MAVEN_HOME/bin"


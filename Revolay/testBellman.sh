#!/bin/bash

# Author : Hugo Triolet (and Matvei Pavlov)

mode=$1
slf4j="jars/slf4j-api-1.7.36.jar"
slf4j_simple="jars/slf4j-simple-1.7.36.jar"
amqp="jars/amqp-client-5.16.0.jar"

# make mode
if [ "$1" == "make" ]; then
  echo "remove *.class"
  rm ./src/*.class
  javac -cp .:${amqp}:${slf4j}:${slf4j_simple}:src/ ./src/Physical*.java
  exit 1
fi

# clean mode
if [ "$1" == "clean" ]; then
  echo "remove *.class"
  rm ./src/*.class
  exit 1
fi

# run test node (test the Bellman-Ford algo)
if [ "${mode}" == "run" ]; then
  java -cp .:${amqp}:${slf4j}:${slf4j_simple}:src/ ./src/PhysicalNetwork.java
  exit 1
fi
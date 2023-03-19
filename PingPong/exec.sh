#!/bin/bash

mode=$1
slf4j="jars/slf4j-api-1.7.36.jar"
slf4j_simple="jars/slf4j-simple-1.7.36.jar"
amqp="jars/amqp-client-5.16.0.jar"


echo "execution of $1"

if [ "${mode}" == "make" ]; then
    CLASSPATH=$(pwd)/src javac -cp .:${amqp}:${slf4j}:${slf4j_simple} src/*.java
fi

if [ "${mode}" == "pong" ]; then
    java -cp .:${amqp}:${slf4j}:${slf4j_simple}:src/ src/Consumer.java
fi

if [ "${mode}" == "ping" ]; then
    java -cp .:${amqp}:${slf4j}:${slf4j_simple}:src/ src/Producer.java
fi

if [ "${mode}" == "clean" ]; then
    rm src/*.class
fi

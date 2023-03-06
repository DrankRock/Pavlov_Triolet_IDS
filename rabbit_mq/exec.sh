#!/bin/bash

mode=$1
slf4j="jars/slf4j-api-1.7.36.jar"
slf4j_simple="jars/slf4j-simple-1.7.36.jar"
amqp="jars/amqp-client-5.16.0.jar"

echo "execution of $1"

if [ "${mode}" == "client" ]; then
    java -cp .:${amqp}:${slf4j}:${slf4j_simple} src/direct_communication/Client.java
fi
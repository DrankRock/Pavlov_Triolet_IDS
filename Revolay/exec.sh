#!/bin/bash

# Author : Matvei Pavlov

mode=$1
numberOfElements=$2
slf4j="jars/slf4j-api-1.7.36.jar"
slf4j_simple="jars/slf4j-simple-1.7.36.jar"
amqp="jars/amqp-client-5.16.0.jar"

# make mode
if [ "$1" == "make" ]; then
  echo "remove *.class"
  rm ./src/*.class
  javac -cp .:${amqp}:${slf4j}:${slf4j_simple}:src/ ./src/*.java
  exit 1
fi

# clean mode
if [ "$1" == "clean" ]; then
  echo "remove *.class"
  rm ./*.class
  exit 1
fi

# single node
# syntax : bash exec.sh single nElements currentElementNumber <connected 1> <...>
if [ "${mode}" == "single" ]; then
  java -cp .:${amqp}:${slf4j}:${slf4j_simple}:src/ src/Main.java $3 ${numberOfElements} ${@:4} &
  exit 1
fi
if [ "${mode}" == "run" ]; then
    for i in $(seq 0 $((numberOfElements - 1)))
    do
      text=""
      for j in $(seq 0 $((numberOfElements - 1)))
      do
        if [[ $i -ne $j ]]; then
          text="$text $j"
        fi
      done
      java -cp .:${amqp}:${slf4j}:${slf4j_simple}:src/ src/Main.java ${i} ${numberOfElements} ${text} &
      # echo "java -cp .:${amqp}:${slf4j}:${slf4j_simple}:src/ src/NodeLauncher.java ${i} ${numberOfElements} &"
    done
    exit 1
fi
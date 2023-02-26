#!/bin/bash

# Author : Matvei Pavlov

# kill java running in background appart from the first one launched
if [[ "$1" == "kill_java" || "$2" == "kill_java" ]]; then
  echo "kill java if already running"
  pids=$(pidof java)
  last_pid=$(echo $pids | awk '{print $NF}') 
  for pid in $pids; do 
    [[ $pid != $last_pid ]] && kill $pid; 
  done
  if [ "$1" == "kill_java" ]; then
    exit 1
  fi
fi

# check if xdotool is installed
xdo=$(command -v xdotool)
if [ "$xdo" == "" ]
then
  if [ "$1" != "noXdo" ]
  then
    echo "Requesting sudo to install xdotool"
    sudo apt-get install xdotool
  fi
fi

# make mode
if [ "$1" == "make" ]; then
  echo "remove *.class"
  rm ./*.class
  javac *.java
  exit 1
fi

# clean mode
if [ "$1" == "clean" ]; then
  echo "remove *.class"
  rm ./*.class
  echo "${foo}"
  exit 1
fi

# client mode
if [ "$1" == "client" ]; then
  javac *.java
  java ClientImpl.java localhost
  exit 1
fi

# noXdotool mode
if [ "$1" == "noXdo" ]; then
  echo "remove *.class"
  # rm ./*.class
  echo "compile *"
  javac *.java
  pid=$(pidof rmiregistry)
  echo "kill rmiregistry if already running"
  # Check if the process ID is not empty
  if [ -n "$pid" ]
  then
    # Kill the rmiregistry process
    kill $pid > /dev/null
  else
    # Do nothing if rmiregistry is not active
    echo "" > /dev/null
  fi # by ChatGPT lol
  rmiregistry &
  java ./HelloServer.java & 2> /dev/null
  echo "wait for server to be ready"
  sleep 2
  echo -ne '\n'
  exit 1
fi

### Automatically launch the server in a new terminal
echo "remove *.class"
# rm ./*.class
echo "compile *"
javac *.java
# Get the process ID of rmiregistry
pid=$(pidof rmiregistry)
echo "kill rmiregistry if already running"
# Check if the process ID is not empty
if [ -n "$pid" ]
then
  # Kill the rmiregistry process
  kill $pid > /dev/null
else
  # Do nothing if rmiregistry is not active
  echo "" > /dev/null
fi # by ChatGPT lol
echo "open new terminal"
xdotool key "ctrl+shift+t"; xdotool type "rmiregistry &"; xdotool key Return; xdotool type "java ./HelloServer.java"; xdotool key Return; xdotool key "alt+shift+1";
echo "server successfully running."
echo "wait for server to start"
sleep 1

#!/bin/bash

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
echo "test HelloClient"
expect=$(java HelloClient.java localhost)
if [ "$expect" == "Hello world ! int is : 42" ]; then
  echo "HelloClient works !"
else :
  echo "Something went wrong, sorry :c"
fi
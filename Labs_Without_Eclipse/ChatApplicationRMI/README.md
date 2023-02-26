## Project Chat Apllication IDS

The goal of this exercice was to build a chat application, where participants can dynamically join, leave, exchange messages
and have a history of their conversations. Plus, the chat system is persistent (persistent history of messages). This means that if the server
shuts down and restarts, it can recover the message history.\\
There is also a graphical user interface implemented for the chat system.\\


For this app, we implement our chat  according to the architecture of an RMI application 

## Java version

We used the Java 19 version (both jdk and jre).

## Functionalities

A repository called \textbf{docs} contains all the javadoc of the projet.

Cuurents functionalities are the following :
- 


## Compilation

A file `exec.sh` is here to take of the compilation of the app.

To comile/run the app, see the following command and iots options :
```sh
bash exec.sh [OPTIONS] 
```

Options are the following :

|Implemented          | Option  | Description                                              	  |
|:-------------------:|:-------:|:-----------------------------------------------------------:|
| :heavy_check_mark:  | make    | Compile only                                          	  |
| :heavy_check_mark:  | clean   | Delete all .class files                                  	  |
| :heavy_check_mark:  | client  | Recompile and run a client                                  |
| :heavy_check_mark:  | noXdo   | If problems with `xdotool`                              	  |


To execute in `~/ChapApplicationRMI` with xdotool (ask you to install it if it isn't):
```sh
bash exec.sh // compile all and run the server into a diffrent shell
```
-------> compile all and run the server into a different shell

To execute in `~/ChapApplicationRMI` without xdotool (if the previous command doesn't work on your machine):
```sh
bash exec.sh noXdo 
```
--------> compile all and run the server into a different shell

## Run

After compiling and server runing, you just have to open a new terminal in the same folder and execute the command for a client 
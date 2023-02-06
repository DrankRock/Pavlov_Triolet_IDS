# Pavlov_Triolet_IDS
## In this repo are our work for the IDS class in the S8 of M1 Computer Science at the UGA (of France, not Georgia)

### First Lab : Sockets
#### 6.1 : Calculator Server
The protocol here is simply for the client to send <integer 1>, enter, <operand>, enter, <integer 2>, and the server will reply with the result.
Here is a video showing this part working : 

https://user-images.githubusercontent.com/32172257/216834156-5651306e-ef4d-4fb5-818f-efe58327fedc.mp4

#### 6.2 : Phone Registry
This part was made with a server being a state machine, normally waiting, but depending on what the user sends, it changes to add, get, search or getall.  
The server keeps track of the registry, and converts what is asked into String to facilitate sending to the client. The Person class contains method to convert Person to String and back. 

Here is a video showing this part working :

https://user-images.githubusercontent.com/32172257/216834905-d0441b5a-7207-4ade-b866-1335814e69e7.mp4

#### 6.3 - 7 : Messaging server with broadcast and messages with multiple clients
The Server and Thread code is heavily inspired by the code given to us. Each Client has a dedicated thread to communicate with the server. The server keeps track of the connected sockets with their respective ID, to enable communication between each client.  
We offer multiple options to the client : 
```
Client Starting on 127.0.0.1 : 8081
Given ID : 2
# Instructions : 
# b <message> to broadcast,
# m <id> <message> to send message to specific client,
# l to list all clients
# q to quit
```
each first letter of the input is sent to the client's thread, that will then accept different arguments (or not) depending on the case.
 - Broadcast sends a message to everyone through the server
 - Message sends a message to a scpecific user if it is in the list of active users
 - List shows a list of the active users
 - Quit closes the current client and removes it from the server's list

Here is a video example of the execution of this Server/client with 3 clients and their discussions :

https://user-images.githubusercontent.com/32172257/216833914-bb0f3136-b450-4ae7-8f97-fdcf1a299d97.mp4

### Second Lab : RMI

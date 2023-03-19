# Ping Pong with RabbitMQ

## Prerequities

First, you have to make sure you have installed and set up the rabbitMQ on your mahcine.\
If not, follow this link we use to do so : https://www.cherryservers.com/blog/how-to-install-and-start-using-rabbitmq-on-ubuntu-22-04.

Our implementation works with 3 particulars jars : 
- `amqp-client.5.16.0` 
- `slf4-api-1.7.36`
- `slf4-simple-1.7.36`\
They are needed to run the queues with RabbitMQ and located in the `/jars` folder.

## Warnings

**We use a custom username, password and virtual host (they are writed in the code, not the best but just to play with) on** `localhost`.\
------> *IF YOU WANT YOUR PERSONAL USERNAME/PASSWORD/VIRTUAL HOST, you'll need to change it directly into the `Client` class*


## How to launch it

Once your RabbitMQ is installed on your machine, launch it by doing : 
```bash
sudo sudo rabbitmq-server
```

Then check if it launched correctly :
```bash
sudo systemctl status rabbitmq-server
```


To compile the Ping-Pong queue :

```bash
bash exec.sh make # compile
```
To run the Ping-Pong queue :
```bash
bash exec.sh ping # to run a producer
bash exec.sh pong # to run a consumer
```

To clean the .class produced :
```bash
bash exec.sh clean # rm .class
```

## Javadoc

You can consult the javadoc of this piece of code in the folder `/docs`.
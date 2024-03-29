import com.rabbitmq.client.*;


import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Physical Node, connecting to its peers with RabbitMQ, sending messages to its neighbors.
 * This has the objective of being a representation of a physical node on a single process.
 * @author Matvei Pavlov
 *
 */
public class PhysicalNode {
    /**
     * The value of the node
     */
    private int value;
    /**
     * The total number of nodes
     */
    private int numberOfElements;
    /**
     * The neighbors of the node
     */
    private ArrayList<Integer> connectedTo;
    /**
     * Variable used with RabbitMq for connectivity purpose
     */
    private ConnectionFactory factory;
    /**
     * Variable used with RabbitMq for connectivity purpose
     */
    private Connection connection;
    /**
     * The channel to receive messages sent to this node
     */
    private Channel channel;
    /**
     * The direction in which the message needs to be sent to reach the final destination the fastest.
     */
    private HashMap<Integer,Integer> directionOf;
    /**
     * The caller of this class
     */
    private NodeRunner caller;

    /**
     * Constructor of the PhysicalNode
     * @param value the value of the node
     * @param connectedTo the neighbors of the node, as an ArrayList of Integers
     * @param numberOfElements the total number of nodes
     * @param caller the class who called this PhysicalNode (a NodeRunner)
     */
    public PhysicalNode(int value, ArrayList<Integer> connectedTo, int numberOfElements, NodeRunner caller) {
        this.value = value;
        this.numberOfElements = numberOfElements;
        //System.out.println("Created physicalNode "+this.value+" connected to "+ Arrays.toString(connectedTo.toArray()));
        this.connectedTo = connectedTo;
        try {
            this.setupFactory();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        this.directionOf = new HashMap<>();
        this.caller = caller;
    }

    /**
     * Setups the connectionFactory, and the channel, to ensure communication between consumer and prodcuer
     * @throws IOException exceptions with factory, connection and channel
     * @throws TimeoutException exceptions with factory.newConnection()
     */
    private void setupFactory() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("matvei");
        factory.setPassword("Passw0rd");
        factory.setVirtualHost("cherry_broker");
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare("To_"+value, false, false, false, null);
        setupRec("To_"+value);
    }

    /**
     * Setup the receiving of messages
     * @param channelName the name of the channel to which other nodes can send a message
     * @throws IOException
     */
    private void setupRec(String channelName) throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                Message msg = Message.fromBytes(delivery.getBody());
                if (msg.getTo() == value){
                    System.out.println("["+this.value+"]"+"[RECV] - Destination reached, from : "+msg.getFrom()+", to : "+msg.getTo()+"\nmessage : "+msg.getMessage());
                    caller.received(msg);
                } else {
                    this.sendMessage(msg);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (AlreadyClosedException e){
                System.out.println("Error : AlreadyClosedException with node "+this.value+" on message received .");
                e.printStackTrace();
            }
        };
        channel.basicConsume(channelName, true, deliverCallback, consumerTag -> { });
    }


    /**
     * Send a message to another node
     * @param msg Message containing the destinator and the message
     * @throws IOException in case of issues with basicPublish
     */
    public void sendMessage(Message msg) throws IOException {
        int to = msg.getTo();
        try {
            for (int directConnection : connectedTo) {
                if (directConnection == to) {
                    System.out.println("[" + this.value + "]" + "Sending '" + msg + " -- Direct link");
                    channel.basicPublish("", "To_" + to, null, msg.toBytes());
                    return;
                }
            }
            if (directionOf.containsKey(to)) {
                int direction = directionOf.get(to);
                System.out.println("[" + this.value + "]" + "Sending '" + msg + " -- Passing by " + this.value + " -- direction : " + direction);
                channel.basicPublish("", "To_" + direction, null, msg.toBytes());
            } else {
                throw new RuntimeException("Exception caught : no path was found to reach " + msg.getTo() + " from " + value + ".\n");
            }
        } catch (AlreadyClosedException e){
            System.out.println("Error : AlreadyClosedException with node "+this.value+" on message sent .");
            e.printStackTrace();
        }
    }

    /**
     * Add a direction to 'to', passing by 'next'.
     * @param to the target
     * @param next the node to go to as a next step
     */
    public void addDirection(int to, int next){
        directionOf.put(to, next);
    }
}

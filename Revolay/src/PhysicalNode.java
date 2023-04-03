import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;


import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author Matvei Pavlov
 * Physical Network Node, connecting to its peers with RabbitMQ, sending messages to its neighbors.
 * This has the objective of being a representation of a network entered as a Graph.
 */
public class PhysicalNode {
    private int value;
    private int numberOfElements;
    private ArrayList<Integer> connectedTo;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private HashMap<Integer,Integer> directionOf;

    public PhysicalNode(int value, ArrayList<Integer> connectedTo) {
        this.value = value;
        //System.out.println("Created physicalNode "+this.value+" connected to "+ Arrays.toString(connectedTo.toArray()));
        this.connectedTo = connectedTo;
        try {
            this.setupFactory();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        this.directionOf = new HashMap<>();
    }

    public PhysicalNode(int value, ArrayList<Integer> connectedTo, int numberOfElements) {
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
    }

    public PhysicalNode(int value) {
        this.value = value;
        this.connectedTo = new ArrayList<>();
        //System.out.println("Created physicalNode "+this.value+" connected to "+ Arrays.toString(connectedTo.toArray()));
        try {
            this.setupFactory();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        this.directionOf = new HashMap<>();
    }

    public void addConnection(int node){
        connectedTo.add(node);
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
/*        for (int i : connectedTo){
            String channelName = "CNL"+i;
            channel.queueDeclare(channelName, false, false, false, null);
        } */
        channel.queueDeclare("To_"+value, false, false, false, null);
        setupRec("To_"+value);
    }

    private void setupRec(String channelName) throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                Message msg = Message.fromBytes(delivery.getBody());
                if (msg.getTo() == value){
                    System.out.println("["+this.value+"]"+"[RECV] - Destination reached, from : "+msg.getFrom()+", to : "+msg.getTo()+"\nmessage : "+msg.getMessage());
                } else {
                    this.sendMessage(msg);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        };
        channel.basicConsume(channelName, true, deliverCallback, consumerTag -> { });
    }


    public void sendMessage(Message msg) throws IOException {
        int to = msg.getTo();
        for (int directConnection : connectedTo){
            if (directConnection == to){
                System.out.println("["+this.value+"]"+"Sending '"+msg+" -- Direct link");
                channel.basicPublish("", "To_"+to, null, msg.toBytes());
                return;
            }
        }
        if (directionOf.containsKey(to)){
            int direction = directionOf.get(to);
            System.out.println("["+this.value+"]"+"Sending '"+msg+" -- Passing by "+this.value+" -- direction : "+direction);
            channel.basicPublish("", "To_"+direction, null, msg.toBytes());
        } else {
            throw new RuntimeException("Exception caught : no path was found to reach "+msg.getTo()+" from "+value+".\n");
        }
    }

    public void addDirection(int to, int next){
        directionOf.put(to, next);
    }

}

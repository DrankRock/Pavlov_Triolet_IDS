import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Client class, used to create Consumer and Producer, implements some basic connection functions
 *
 * @author Matvei Pavlov
 * @author Hugo Triolet
 */
public abstract class Client {
    public final Controller controller;
    public ConnectionFactory factory;
    public Connection connection;
    public final String PING_NAME;
    public final String PONG_NAME;
    public Channel channel;

    /**
     * Constructor, sets up variable and calls the setup function for clarity
     * @param mode "Producer" or "Consumer"
     * @throws IOException in case of exception with setupFactory
     * @throws TimeoutException in case of exception with setupFactory
     */
    public Client(String mode) throws IOException, TimeoutException {
        controller = new Controller(this, mode);
        PING_NAME = "ping";
        PONG_NAME = "pong";
        setupFactory();
    }

    /**
     * Publish a ping containing a color to the Ping channel
     * @param c the color to ping
     */
    public void sendPing(Color c)  {
        String text = ""+c.getRed()+","+c.getGreen()+","+c.getBlue()+";"+"ping !";
        try {
            channel.basicPublish("", PING_NAME, null, text.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        channel.queueDeclare(PING_NAME, false, false, false, null);
        channel.queueDeclare(PONG_NAME, false, false, false, null);
    }


}

package direct_communication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class Client {

    private final static String QUEUE_NAME = "client";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("matvei");
        factory.setPassword("Passw0rd");
        factory.setVirtualHost("cherry_broker");
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("[-] "+channel.consumerCount("client")+" clients connected");
            // Queue name,
            // true if queue should survive a restart,
            // true if queue is accessible only by broker who declared it
            // true if queue auto deletes when everyone unsubscribes to it
            // null, a map of other arguments
            String message = "Hello World!";

            System.out.println("Starting client ... Enter 'q' to quit");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String received = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + received + "'");
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });

            while (true) {
                String toSend = System.console().readLine();
                if (toSend.equals("q")){
                    break;
                }
                if (channel.consumerCount(QUEUE_NAME) == 1){
                    System.out.println(" [ ] - no other clients connected");
                } else {
                    channel.basicPublish("", QUEUE_NAME, null, toSend.getBytes(StandardCharsets.UTF_8));
                    System.out.println(" [x] Sent '" + toSend + "'");
                }

            }
        }
    }
}
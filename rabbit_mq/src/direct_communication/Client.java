package direct_communication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Client {

    private int ID = ThreadLocalRandom.current().nextInt(0, 10);
    private boolean started = false;
    private final static String QUEUE_NAME1 = "sender";
    private final static String QUEUE_NAME2 = "receiver";

    public static void bindChannel(Channel ch1, Channel ch2, String send_q, String recv_q) throws Exception{
        /* method to bind the channel according of the connexions */

        // Queue name,
        // true if queue should survive a restart,
        // true if queue is accessible only by broker who declared it
        // true if queue auto deletes when everyone unsubscribes to it
        // null, a map of other arguments

        // set up the sender channel
        ch1.exchangeDeclare("sending", "direct", true);
        ch1.queueDeclareNoWait(send_q, false, false, false, null);
        ch1.queueBind(send_q, "sending", "black");
        
        // set up the receiver channel
        ch2.exchangeDeclare("receiving", "direct", true);
        ch2.queueDeclareNoWait(recv_q, false, false, false, null);
        ch2.queueBind(recv_q, "receiving", "white");
    }

    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("Ogawakin");
        factory.setPassword("Darksider58$");
        factory.setVirtualHost("cherry_broker");

        try (Connection connection = factory.newConnection();
            Channel channel_send = connection.createChannel();
            Channel channel_recv = connection.createChannel()) 
        {
            if(channel_recv.consumerCount("receiver") > 0){
                bindChannel(channel_recv, channel_send, QUEUE_NAME2, QUEUE_NAME1);
                System.out.println("[-] "+channel_send.consumerCount("sender")+" senders connected");
                System.out.println("[-] "+channel_recv.consumerCount("receiver")+" receivers connected");

                System.out.println("Starting client ... Enter 'q' to quit");

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String received = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    System.out.println("[ ] " + received);
                };
                channel_send.basicConsume(QUEUE_NAME1, true, deliverCallback, consumerTag -> { });

                while (true) {
                    String toSend = System.console().readLine();
                    System.out.print(String.format("\033[%dA", 1));
                    System.out.print("\033[2K");

                    if (toSend.equals("q")){
                        break;
                    }
                    if (channel_recv.consumerCount(QUEUE_NAME2) == 1){
                        System.out.println(" [ ] - no other sender connected");
                    } else {
                        String new_toSend = "Hugo" + String.valueOf(7) + " : '" + toSend + "'";
                        channel_recv.basicPublish("sending", "black", null, toSend.getBytes(StandardCharsets.UTF_8));
                        System.out.println(" [x] You : '" + toSend + "'");
                    }
                }
            }
            else{
                bindChannel(channel_send, channel_recv, QUEUE_NAME1, QUEUE_NAME2);

                System.out.println("[-] "+channel_send.consumerCount("sender")+" senders connected");
                System.out.println("[-] "+channel_recv.consumerCount("receiver")+" receivers connected");
            
                System.out.println("Starting client ... Enter 'q' to quit");
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String received = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    //System.out.println(" [x] Received '" + received + "'");
                    System.out.println("[ ] " + received);
                };
                channel_recv.basicConsume(QUEUE_NAME2, true, deliverCallback, consumerTag -> { });

                while (true) {
                    String toSend = System.console().readLine();
                    System.out.print(String.format("\033[%dA", 1));
                    System.out.print("\033[2K");

                    if (toSend.equals("q")){
                        break;
                    }
                    if (channel_send.consumerCount(QUEUE_NAME1) == 1){
                        System.out.println(" [ ] - no other sender connected");
                    } else {
                        String new_toSend = "Hugo" + String.valueOf(7) + " : '" + toSend + "'";
                        channel_send.basicPublish("sending", "black", null, toSend.getBytes(StandardCharsets.UTF_8));
                        System.out.println(" [x] You : '" + toSend + "'");
                    }
                }
            }            
        }
    }
}
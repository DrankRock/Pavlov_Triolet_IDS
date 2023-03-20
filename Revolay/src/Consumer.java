import com.rabbitmq.client.DeliverCallback;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Consumer class, receives Pings and sends back Pongs
 *
 * @author Matvei Pavlov
 * @author Hugo Triolet
 */
public class Consumer extends Client{
    /**
     * Constructor of a Consumer, sets up behavior in case of Ping received
     * @throws IOException when han exception occurs in basicConsume
     * @throws TimeoutException when an excpeiton occurs in super
     */
    public Consumer() throws IOException, TimeoutException {
        super("Consumer");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String received = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String color = received.split(";")[0];
            String[] colors = color.split(",");
            Color newColor = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
            controller.setColor(newColor);

            pong();

            //System.out.println("Color change received, launching Pong");
        };
        channel.basicConsume(PING_NAME, true, deliverCallback, consumerTag -> { });

    }

    /**
     * Launch a pong in the background, after a 1sec wait (for visual effect)
     */
    private void pong(){
        Thread anon = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                channel.basicPublish("", PONG_NAME, null, "Pong".getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        anon.start();
    }

    /**
     * Main function, creates a new Consumer
     * @param args command line arguments
     */
    @SuppressWarnings("unused")
    public static void main(String[] args){
        try {
            Consumer c = new Consumer();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}

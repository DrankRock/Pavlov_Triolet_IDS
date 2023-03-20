import com.rabbitmq.client.DeliverCallback;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Producer class, prodces the pings that will be received by a consumer, recieves pongs.
 *
 * @author Matvei Pavlov
 * @author Hugo Triolet
 */
public class Producer extends Client{
    /**
     * Constructor of the producer, sets up the behavior to have in case the reception of a pong
     * @throws IOException in case of error with basicConsume
     * @throws TimeoutException in case of error in the super()
     */
    public Producer() throws IOException, TimeoutException {
        super("Producer");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String received = new String(delivery.getBody(), StandardCharsets.UTF_8);
            if (received.equals("Pong")) {
                controller.pong();
            }
        };
        channel.basicConsume(PONG_NAME, true, deliverCallback, consumerTag -> { });
    }

    /**
     * Main function, creates a new producer
     * @param args command line arguments
     */
    public static void main(String[] args){
        try {
            Producer prod = new Producer();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}

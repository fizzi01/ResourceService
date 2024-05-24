package it.unisalento.pasproject.resourceservice.business.io.consumer;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MessageConsumer is a service class that consumes messages using a specific strategy.
 * The strategy is injected via constructor and can be changed at runtime.
 */
@Service
@Setter
public class MessageConsumer {
    private MessageConsumerStrategy strategy;

    /**
     * Constructs a new MessageConsumer with the given strategy.
     *
     * @param strategy the strategy to use for consuming messages
     */
    @Autowired
    public MessageConsumer(MessageConsumerStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Consumes a message using the current strategy.
     *
     * @param <T> the type of the message
     * @param message the message to consume
     * @return the result of the consumption, as defined by the strategy
     */
    public <T> T consumeMessage(T message) {
        return strategy.consumeMessage(message);
    }

    /**
     * Consumes a message using the current strategy and a specific queue name.
     *
     * @param message the message to consume
     * @param queueName the name of the queue from which the message was received
     * @return the result of the consumption, as defined by the strategy
     */
    public String consumeMessage(String message, String queueName) {
        return strategy.consumeMessage(message, queueName);
    }
}

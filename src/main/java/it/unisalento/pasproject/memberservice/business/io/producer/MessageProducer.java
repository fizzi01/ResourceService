package it.unisalento.pasproject.memberservice.business.io.producer;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MessageProducer is a service class that produces messages using a specific strategy.
 * The strategy is injected via constructor and can be changed at runtime.
 */
@Setter
@Service
public class MessageProducer {

    private MessageProducerStrategy strategy;

    /**
     * Constructs a new MessageProducer with the given strategy.
     *
     * @param strategy the strategy to use for producing messages
     */
    @Autowired
    public MessageProducer(MessageProducerStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Sends a message of a generic type using the current strategy.
     *
     * @param <T> the type of the message
     * @param messageDTO the message to send
     * @param routingKey the routing key for the message
     * @param exchange the exchange to use for the message
     */
    public <T> void sendMessage(T messageDTO, String routingKey, String exchange) {
        strategy.sendMessage(messageDTO, routingKey, exchange);
    }

    /**
     * Sends a message of a generic type using the current strategy and a specific replyTo address.
     *
     * @param <T> the type of the message
     * @param messageDTO the message to send
     * @param routingKey the routing key for the message
     * @param exchange the exchange to use for the message
     * @param replyTo the address to which replies to the message should be sent
     */
    public <T> void sendMessage(T messageDTO, String routingKey, String exchange, String replyTo) {
        strategy.sendMessage(messageDTO, routingKey, exchange, replyTo);
    }

}

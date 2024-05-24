package it.unisalento.pasproject.resourceservice.business.io.producer;

/**
 * The MessageProducerStrategy interface provides a contract for implementing different
 * strategies for producing messages. The strategies can be used interchangeably in a
 * MessageProducer.
 */
public interface MessageProducerStrategy {

    /**
     * Sends a message of a generic type.
     *
     * @param <T> the type of the message
     * @param messageDTO the message to send
     * @param routingKey the routing key for the message
     * @param exchange the exchange to use for the message
     */
    <T> void sendMessage(T messageDTO,String routingKey, String exchange);

    /**
     * Sends a message of a generic type with a specific replyTo address.
     *
     * @param <T> the type of the message
     * @param messageDTO the message to send
     * @param routingKey the routing key for the message
     * @param exchange the exchange to use for the message
     * @param replyTo the address to which replies to the message should be sent
     */
    <T> void sendMessage(T messageDTO,String routingKey, String exchange, String replyTo);
}

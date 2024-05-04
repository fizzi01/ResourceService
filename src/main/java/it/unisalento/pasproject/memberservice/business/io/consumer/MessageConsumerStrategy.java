package it.unisalento.pasproject.memberservice.business.io.consumer;

/**
 * The MessageConsumerStrategy interface provides a contract for implementing different
 * strategies for consuming messages. The strategies can be used interchangeably in a
 * MessageConsumer.
 */
public interface MessageConsumerStrategy {

    /**
     * Consumes a message of a generic type.
     *
     * @param <T> the type of the message
     * @param message the message to consume
     * @return the result of the consumption, as defined by the specific strategy
     */
    <T> T consumeMessage(T message);

    /**
     * Consumes a message of type String from a specific queue.
     *
     * @param message the message to consume
     * @param queueName the name of the queue from which the message was received
     * @return the result of the consumption, as defined by the specific strategy
     */
    String consumeMessage(String message, String queueName);
}
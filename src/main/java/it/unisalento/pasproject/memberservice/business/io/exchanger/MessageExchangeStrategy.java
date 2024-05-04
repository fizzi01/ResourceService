package it.unisalento.pasproject.memberservice.business.io.exchanger;

/**
 * The MessageExchangeStrategy interface provides a contract for implementing different
 * strategies for exchanging messages. The strategies can be used interchangeably in a
 * MessageExchanger.
 */
public interface MessageExchangeStrategy {

    /**
     * Exchanges a message of type String and returns a response of a generic type.
     *
     * @param <T> the type of the response
     * @param message the message to exchange
     * @param routingKey the routing key for the message exchange
     * @param exchange the exchange to use for the message exchange
     * @param object the class of the response object
     * @return the result of the exchange, as defined by the specific strategy
     */
    <T> T exchangeMessage(String message, String routingKey,String exchange, Class<T> object);

    /**
     * Exchanges a message of a generic type and returns a response of a generic type.
     *
     * @param <T> the type of the message
     * @param <R> the type of the response
     * @param message the message to exchange
     * @param routingKey the routing key for the message exchange
     * @param exchange the exchange to use for the message exchange
     * @param responseType the class of the response object
     * @return the result of the exchange, as defined by the specific strategy
     */
    <T, R> R exchangeMessage( T message, String routingKey, String exchange, Class<R> responseType);
}

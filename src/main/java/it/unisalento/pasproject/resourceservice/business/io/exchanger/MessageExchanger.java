package it.unisalento.pasproject.resourceservice.business.io.exchanger;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MessageExchanger is a service class that exchanges messages using a specific strategy.
 * The strategy is injected via constructor and can be changed at runtime.
 */
@Service
@Setter
public class MessageExchanger {

    private MessageExchangeStrategy strategy;

    /**
     * Constructs a new MessageExchanger with the given strategy.
     *
     * @param messageExchangeStrategy the strategy to use for exchanging messages
     */
    @Autowired
    public MessageExchanger(MessageExchangeStrategy messageExchangeStrategy) {
        this.strategy = messageExchangeStrategy;
    }

    /**
     * Exchanges a message of type String using the current strategy.
     *
     * @param <T> the type of the response
     * @param message the message to exchange
     * @param routingKey the routing key for the message exchange
     * @param exchange the exchange to use for the message exchange
     * @param object the class of the response object
     * @return the result of the exchange, as defined by the strategy
     */
    public <T> T exchangeMessage(String message, String routingKey,String exchange, Class<T> object) {
        return strategy.exchangeMessage(message, routingKey, exchange,object);
    }

    /**
     * Exchanges a message of a generic type using the current strategy.
     *
     * @param <T> the type of the message
     * @param <R> the type of the response
     * @param message the message to exchange
     * @param routingKey the routing key for the message exchange
     * @param exchange the exchange to use for the message exchange
     * @param responseType the class of the response object
     * @return the result of the exchange, as defined by the strategy
     */
    public <T, R> R exchangeMessage(T message, String routingKey, String exchange, Class<R> responseType) {
        return strategy.exchangeMessage(message, routingKey, exchange, responseType);
    }
}

package it.unisalento.pasproject.memberservice.business.io.exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

/**
 * RabbitMQExchange is a service class that implements the MessageExchangeStrategy interface.
 * It uses RabbitMQ as the message broker for exchanging messages.
 */
@Service("RabbitMQExchange")
public class RabbitMQExchange implements MessageExchangeStrategy {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Constructs a new RabbitMQExchange with the given RabbitTemplate.
     *
     * @param rabbitTemplate the RabbitTemplate to use for message exchange
     */
    @Autowired
    public RabbitMQExchange(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQExchange.class);

    /**
     * Exchanges a message of type String and returns a response of a generic type.
     * The exchange is done using RabbitMQ.
     *
     * @param <T> the type of the response
     * @param message the message to exchange
     * @param routingKey the routing key for the message exchange
     * @param exchange the exchange to use for the message exchange
     * @param object the class of the response object
     * @return the result of the exchange, as defined by the specific strategy
     */
    @Override
    public <T> T exchangeMessage(String message, String routingKey, String exchange, Class<T> object) {
        rabbitTemplate.setReplyTimeout(1000); // Timeout di 1 secondo
        T response = rabbitTemplate.convertSendAndReceiveAsType(exchange, routingKey, message,
                ParameterizedTypeReference.forType(object));
        LOGGER.info("Message received: {}", response);
        return response;
    }

    /**
     * Exchanges a message of a generic type and returns a response of a generic type.
     * The exchange is done using RabbitMQ.
     *
     * @param <T> the type of the message
     * @param <R> the type of the response
     * @param message the message to exchange
     * @param routingKey the routing key for the message exchange
     * @param exchange the exchange to use for the message exchange
     * @param responseType the class of the response object
     * @return the result of the exchange, as defined by the specific strategy
     */
    @Override
    public <T, R> R exchangeMessage(T message, String routingKey, String exchange, Class<R> responseType) {
        rabbitTemplate.setReplyTimeout(1000); // Timeout di 1 secondo
        R response = rabbitTemplate.convertSendAndReceiveAsType(exchange, routingKey, message,
                ParameterizedTypeReference.forType(responseType));
        LOGGER.info("Message received: {}", response);
        return response;
    }
}

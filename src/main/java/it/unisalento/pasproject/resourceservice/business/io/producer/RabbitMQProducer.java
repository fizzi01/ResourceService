package it.unisalento.pasproject.resourceservice.business.io.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RabbitMQProducer is a service class that implements the MessageProducerStrategy interface.
 * It uses RabbitMQ as the message broker for producing messages.
 */
@Service("RabbitMQProducer")
public class RabbitMQProducer implements MessageProducerStrategy {

    /**
     * Logger instance for logging events.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    /**
     * RabbitTemplate instance for sending messages to RabbitMQ.
     */
    private final RabbitTemplate rabbitTemplate;

    /**
     * Constructs a new RabbitMQProducer with the given RabbitTemplate.
     *
     * @param rabbitTemplate the RabbitTemplate to use for message production
     */
    @Autowired
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Sends a message of a generic type using RabbitMQ.
     *
     * @param <T> the type of the message
     * @param messageDTO the message to send
     * @param routingKey the routing key for the message
     * @param exchange the exchange to use for the message
     */
    @Override
    public <T> void sendMessage(T messageDTO, String routingKey, String exchange) {
        LOGGER.info(String.format("RabbitMQ message sent: %s", messageDTO.toString()));
        rabbitTemplate.convertAndSend(exchange, routingKey, messageDTO);
    }

    /**
     * Sends a message of a generic type using RabbitMQ and a specific replyTo address.
     * The receiving service can automatically send the response to the appropriate queue without additional configurations or hardcoded values.
     *
     * @param <T> the type of the message
     * @param messageDTO the message to send
     * @param routingKey the routing key for the message
     * @param exchange the exchange to use for the message
     * @param replyTo the address to which replies to the message should be sent
     */
    @Override
    public <T> void sendMessage(T messageDTO, String routingKey, String exchange, String replyTo) {
        LOGGER.info(String.format("RabbitMQ message sent: %s", messageDTO.toString()));
        rabbitTemplate.convertAndSend(exchange, routingKey, messageDTO, m -> {
            m.getMessageProperties().setReplyTo(replyTo);
            return m;
        });
    }
}

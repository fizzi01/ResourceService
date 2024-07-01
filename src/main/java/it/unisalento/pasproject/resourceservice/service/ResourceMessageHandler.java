package it.unisalento.pasproject.resourceservice.service;

import it.unisalento.pasproject.resourceservice.business.io.exchanger.MessageExchanger;
import it.unisalento.pasproject.resourceservice.business.io.producer.MessageProducer;
import it.unisalento.pasproject.resourceservice.domain.Resource;
import it.unisalento.pasproject.resourceservice.dto.*;
import it.unisalento.pasproject.resourceservice.exceptions.ResourceNotFoundException;
import it.unisalento.pasproject.resourceservice.repositories.ResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The ResourceMessageHandler class provides methods for handling messages related to resources.
 * It includes methods for sending new resource messages, updating resource messages, and receiving resource assignment and usage messages.
 * It uses a ResourceRepository for accessing resource data and a MessageProducer for sending messages.
 */
@Service
public class ResourceMessageHandler {
    private final ResourceRepository resourceRepository;
    private final MessageProducer messageProducer;
    private final MessageExchanger messageExchanger;

    @Value("${rabbitmq.exchange.data.name}")
    private String dataExchange;

    @Value("${rabbitmq.routing.newresource.key}")
    private String newResourceTopic;

    @Value("${rabbitmq.exchange.score.name}")
    private String scoreExchange;

    @Value("${rabbitmq.routing.score.key}")
    private String scoreTopic;

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceMessageHandler.class);

    /**
     * Constructor for the ResourceMessageHandler.
     * @param resourceRepository The ResourceRepository to be used for accessing resource data.
     * @param messageProducer The MessageProducer to be used for sending messages.
     */
    @Autowired
    public ResourceMessageHandler(ResourceRepository resourceRepository, MessageProducer messageProducer, MessageExchanger messageExchanger) {
        this.resourceRepository = resourceRepository;
        this.messageProducer = messageProducer;
        this.messageExchanger = messageExchanger;
    }

    /**
     * Sends a new resource message.
     * @param message The message to be sent.
     */
    public void sendNewResourceMessage(ResourceMessageDTO message) {
        messageProducer.sendMessage(message, newResourceTopic, dataExchange);
    }

    /**
     * Sends an update resource message.
     * @param message The message to be sent.
     */
    public void sendUpdateResourceMessage(ResourceMessageDTO message) {
        messageProducer.sendMessage(message, newResourceTopic, dataExchange);
    }

    public ScoreDTO requestResourceScore(ScoreMessageDTO message) {
        return messageExchanger.exchangeMessage(message, scoreTopic, scoreExchange, ScoreDTO.class);
    }

    /**
     * Receives a resource assignment message.
     * @param message The received message.
     */
    @RabbitListener(queues = "${rabbitmq.queue.resourceassignment.name}")
    public void receiveResourceAssignmentMessage(ResourceMessageStatusDTO message) {
        Optional<Resource> resource = resourceRepository.findById(message.getId());

        if (resource.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found");
        }

        Resource retResource = resource.get();

        LOGGER.info("Received resource assignment message with status: " + message.getStatus());

        Optional.ofNullable(message.getStatus())
                .map(Enum::name)
                .map(Resource.Status::valueOf)
                .ifPresent(retResource::setStatus);
        Optional.ofNullable(message.getCurrentTaskId()).ifPresent(retResource::setCurrentTaskId);

        resourceRepository.save(retResource);
    }

    /**
     * Receives a resource usage message.
     * @param message The received message.
     */
    @RabbitListener(queues = "${rabbitmq.queue.resourcedeallocation.name}")
    public void receiveResourceDeallocationMessage(ResourceMessageStatusDTO message) {
        Optional<Resource> resource = resourceRepository.findById(message.getId());

        if (resource.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found");
        }

        Resource retResource = resource.get();

        LOGGER.info("Received resource assignment message with status: " + message.getStatus());

        Optional.ofNullable(message.getStatus())
                .map(Enum::name)
                .map(Resource.Status::valueOf)
                .ifPresent(retResource::setStatus);

        Optional.ofNullable(message.getCurrentTaskId()).ifPresent(retResource::setCurrentTaskId);

        resourceRepository.save(retResource);
    }
}

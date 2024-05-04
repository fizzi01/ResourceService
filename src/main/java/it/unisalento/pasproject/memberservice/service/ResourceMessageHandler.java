package it.unisalento.pasproject.memberservice.service;

import it.unisalento.pasproject.memberservice.business.io.producer.MessageProducer;
import it.unisalento.pasproject.memberservice.domain.Resource;
import it.unisalento.pasproject.memberservice.dto.MessageDTO;
import it.unisalento.pasproject.memberservice.dto.ResourceMessageAssignDTO;
import it.unisalento.pasproject.memberservice.dto.ResourceMessageDTO;
import it.unisalento.pasproject.memberservice.repositories.ResourceRepository;
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

    /**
     * Constructor for the ResourceMessageHandler.
     * @param resourceRepository The ResourceRepository to be used for accessing resource data.
     * @param messageProducer The MessageProducer to be used for sending messages.
     */
    @Autowired
    public ResourceMessageHandler(ResourceRepository resourceRepository, MessageProducer messageProducer) {
        this.resourceRepository = resourceRepository;
        this.messageProducer = messageProducer;
    }

    @Value("${rabbitmq.routing.newresource.key}")
    private String newResourceTopic;

    @Value("${rabbitmq.exchange.data.name}")
    private String dataExchange;

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

    /**
     * Receives a resource assignment message.
     * @param message The received message.
     * @return A MessageDTO indicating the result of the operation.
     */
    @RabbitListener(queues = "${rabbitmq.queue.resourceassignment.name}")
    public MessageDTO receiveResourceAssignmentMessage(ResourceMessageAssignDTO message) {
        Optional<Resource> resource = resourceRepository.findById(message.getIdResource());

        if (resource.isEmpty()) {
            return new MessageDTO("Resource not found", 404);
        }

        Resource retResource = resource.get();

        Optional.ofNullable(message.getAssignedUser()).ifPresent(retResource::setAssignedUser);

        return new MessageDTO("Resource assigned", 200);
    }

    /**
     * Receives a resource usage message.
     * @param message The received message.
     * @return A MessageDTO indicating the result of the operation.
     */
    @RabbitListener(queues = "${rabbitmq.queue.resourceusage.name}")
    public MessageDTO receiveResourceUsageMessage(ResourceMessageDTO message) {
        Optional<Resource> resource = resourceRepository.findById(message.getId());

        if (resource.isEmpty()) {
            return new MessageDTO("Resource not found", 404);
        }

        Resource retResource = resource.get();

        Optional.ofNullable(message.getIsAvailable()).ifPresent(retResource::setIsAvailable);

        return new MessageDTO("Resource used", 200);
    }
}

package it.unisalento.pasproject.memberservice.service;

import it.unisalento.pasproject.memberservice.business.io.producer.MessageProducer;
import it.unisalento.pasproject.memberservice.domain.Resource;
import it.unisalento.pasproject.memberservice.dto.MessageDTO;
import it.unisalento.pasproject.memberservice.dto.ResourceDTO;
import it.unisalento.pasproject.memberservice.dto.ResourceMessageAssignDTO;
import it.unisalento.pasproject.memberservice.dto.ResourceMessageDTO;
import it.unisalento.pasproject.memberservice.repositories.ResourceRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResourceMessageHandler {
    private final ResourceRepository resourceRepository;
    private final MessageProducer messageProducer;

    @Autowired
    public ResourceMessageHandler(ResourceRepository resourceRepository, MessageProducer messageProducer) {
        this.resourceRepository = resourceRepository;
        this.messageProducer = messageProducer;
    }

    @Value("${rabbitmq.routing.newresource.key}")
    private String newResourceTopic;

    @Value("${rabbitmq.exchange.data.name}")
    private String dataExchange;

    public void sendNewResourceMessage(ResourceMessageDTO message) {
        messageProducer.sendMessage(message, newResourceTopic, dataExchange);
    }

    public void sendUpdateResourceMessage(ResourceMessageDTO message) {
        messageProducer.sendMessage(message, newResourceTopic, dataExchange);
    }

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

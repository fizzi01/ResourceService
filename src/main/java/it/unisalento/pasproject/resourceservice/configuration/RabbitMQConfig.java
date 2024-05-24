package it.unisalento.pasproject.resourceservice.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQConfig is a configuration class that sets up the RabbitMQ message broker.
 * It defines the queues, exchanges, and bindings used in the application, as well as the message converter and AMQP template.
 */
@Configuration
public class RabbitMQConfig {

    // ------  SECURITY  ------

    // Needed by authentication service
    @Value("${rabbitmq.queue.security.name}")
    private String securityResponseQueue;

    @Value("${rabbitmq.exchange.security.name}")
    private String securityExchange;

    @Value("${rabbitmq.routing.security.key}")
    private String securityRequestRoutingKey;

    /**
     * Defines the security response queue.
     *
     * @return a new Queue instance
     */
    @Bean
    public Queue securityResponseQueue() {
        return new Queue(securityResponseQueue);
    }

    /**
     * Defines the security exchange.
     *
     * @return a new TopicExchange instance
     */
    @Bean
    public TopicExchange securityExchange() {
        return new TopicExchange(securityExchange);
    }

    /**
     * Defines the binding between the security response queue and the security exchange.
     *
     * @return a new Binding instance
     */
    @Bean
    public Binding securityBinding() {
        return BindingBuilder
                .bind(securityResponseQueue())
                .to(securityExchange())
                .with(securityRequestRoutingKey);
    }

    // ------  END SECURITY  ------ //

    // ------  RESOURCE MESSAGES  ------ //
    @Value("${rabbitmq.queue.newresource.name}")
    private String newResourceQueue;

    @Value("${rabbitmq.routing.newresource.key}")
    private String newResourceTopic;

    @Value("${rabbitmq.queue.resourceassignment.name}")
    private String resourceAssignmentQueue;

    @Value("${rabbitmq.routing.resourceassignment.key}")
    private String resourceAssignmentTopic;

    //TODO: Vedere se questa coda e questo topic sono necessari
    @Value("${rabbitmq.queue.resourceusage.name}")
    private String resourceUsageQueue;

    @Value("${rabbitmq.routing.resourceusage.key}")
    private String resourceUsageTopic;

    // Others queues and topics for resource messages

    @Value("${rabbitmq.exchange.data.name}")
    private String resourceDataExchange;

    /**
     * Defines the new resource queue.
     *
     * @return a new Queue instance
     */
    @Bean
    public Queue newResourceQueue() {
        return new Queue(newResourceQueue);
    }

    /**
     * Defines the resource assignment queue.
     *
     * @return a new Queue instance
     */
    @Bean
    public Queue resourceAssignmentQueue() {
        return new Queue(resourceAssignmentQueue);
    }

    /**
     * Defines the resource usage queue.
     *
     * @return a new Queue instance
     */
    @Bean
    public Queue resourceUsageQueue() {
        return new Queue(resourceUsageQueue);
    }

    /**
     * Defines the resource data exchange.
     *
     * @return a new TopicExchange instance
     */
    @Bean
    public TopicExchange resourceDataExchange() {
        return new TopicExchange(resourceDataExchange);
    }

    /**
     * Defines the binding between the new resource queue and the resource data exchange.
     *
     * @return a new Binding instance
     */
    @Bean
    public Binding newResourceBinding() {
        return BindingBuilder
                .bind(newResourceQueue())
                .to(resourceDataExchange())
                .with(newResourceTopic);
    }

    /**
     * Defines the binding between the resource assignment queue and the resource data exchange.
     *
     * @return a new Binding instance
     */
    @Bean
    public Binding resourceAssignmentBinding() {
        return BindingBuilder
                .bind(resourceAssignmentQueue())
                .to(resourceDataExchange())
                .with(resourceAssignmentTopic);
    }

    /**
     * Defines the binding between the resource usage queue and the resource data exchange.
     *
     * @return a new Binding instance
     */
    @Bean
    public Binding resourceUsageBinding() {
        return BindingBuilder
                .bind(resourceUsageQueue())
                .to(resourceDataExchange())
                .with(resourceUsageTopic);
    }

    // ------  END RESOURCE MESSAGES  ------ //

    /**
     * Creates a message converter for JSON messages.
     *
     * @return a new Jackson2JsonMessageConverter instance.
     */
    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Creates an AMQP template for sending messages.
     *
     * @param connectionFactory the connection factory to use.
     * @return a new RabbitTemplate instance.
     */
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}

package it.unisalento.pasproject.memberservice.service;


import it.unisalento.pasproject.memberservice.business.io.exchanger.MessageExchangeStrategy;
import it.unisalento.pasproject.memberservice.business.io.exchanger.MessageExchanger;
import it.unisalento.pasproject.memberservice.exceptions.UserNotAuthorized;
import it.unisalento.pasproject.memberservice.security.UserDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The UserCheckService class provides methods for checking user details.
 * It includes methods for loading user details by username, checking user roles, and checking if a user is enabled.
 * It uses a MessageExchanger for exchanging messages and a MessageExchangeStrategy for defining the message exchange strategy.
 */
@Service
public class UserCheckService {
    /**
     * The role of the service.
     */
    @Value("${service.role.name}")
    public String ROLE;

    /**
     * The MessageExchanger used for exchanging messages.
     */
    @Autowired
    private MessageExchanger messageExchanger;

    /**
     * The MessageExchangeStrategy used for defining the message exchange strategy.
     */
    @Autowired
    @Qualifier("RabbitMQExchange")
    private MessageExchangeStrategy messageExchangeStrategy;

    /**
     * The name of the security exchange.
     */
    @Value("${rabbitmq.exchange.security.name}")
    private String securityExchange;

    /**
     * The routing key for security requests.
     */
    @Value("${rabbitmq.routing.security.key}")
    private String securityRequestRoutingKey;

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCheckService.class);

    /**
     * Loads user details by username.
     * @param email The email of the user.
     * @return The UserDetailsDTO of the user.
     * @throws UsernameNotFoundException if the user is not found.
     */
    public UserDetailsDTO loadUserByUsername(String email) throws UsernameNotFoundException {
        messageExchanger.setStrategy(messageExchangeStrategy);

        // MQTT call to CQRS to get user details
        UserDetailsDTO user = messageExchanger.exchangeMessage(email,securityRequestRoutingKey,securityExchange,UserDetailsDTO.class);

        if(user == null) {
            throw new UserNotAuthorized("User not found with email: " + email);
        }

        LOGGER.info(String.format("User %s found with role: %s and enabled %s", user.getEmail(), user.getRole(), user.getEnabled()));

        return user;
    }

    /**
     * Checks if a role matches the service role.
     * @param role The role to check.
     * @return True if the role matches the service role, false otherwise.
     */
    public Boolean roleCheck(String role) {
        return role.equals(ROLE);
    }

    /**
     * Checks if a user is enabled.
     * @param enable The enabled status of the user.
     * @return The enabled status of the user.
     */
    public Boolean isEnable(Boolean enable) {
        return enable;
    }
}

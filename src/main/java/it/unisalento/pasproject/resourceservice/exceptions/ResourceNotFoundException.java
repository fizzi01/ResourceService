package it.unisalento.pasproject.resourceservice.exceptions;

import org.springframework.http.HttpStatus;

/**
 * The ResourceNotFoundException class is a custom exception class that represents a resource not found error.
 * It is annotated with @ResponseStatus, which marks it as an exception class for the purpose of automatic exception handling in Spring MVC.
 * The HTTP status code for this exception is 404 (NOT_FOUND), and the reason is "Resource not found".
 */
public class ResourceNotFoundException extends CustomErrorException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

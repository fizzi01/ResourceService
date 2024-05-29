package it.unisalento.pasproject.resourceservice.exceptions;

import org.springframework.http.HttpStatus;

public class ExistingResourceException extends CustomErrorException {
    public ExistingResourceException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}

package it.unisalento.pasproject.resourceservice.exceptions;

import it.unisalento.pasproject.resourceservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class ExistingResourceException extends CustomErrorException {
    public ExistingResourceException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}

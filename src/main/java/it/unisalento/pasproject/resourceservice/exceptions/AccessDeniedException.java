package it.unisalento.pasproject.resourceservice.exceptions;

import it.unisalento.pasproject.resourceservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class AccessDeniedException extends CustomErrorException {
    public AccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}

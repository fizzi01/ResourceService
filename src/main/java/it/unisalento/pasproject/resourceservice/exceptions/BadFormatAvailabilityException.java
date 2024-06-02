package it.unisalento.pasproject.resourceservice.exceptions;

import it.unisalento.pasproject.resourceservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class BadFormatAvailabilityException extends CustomErrorException {
    public BadFormatAvailabilityException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

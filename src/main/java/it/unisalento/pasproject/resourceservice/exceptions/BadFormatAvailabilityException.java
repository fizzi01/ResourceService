package it.unisalento.pasproject.resourceservice.exceptions;

import org.springframework.http.HttpStatus;

public class BadFormatAvailabilityException extends CustomErrorException {
    public BadFormatAvailabilityException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

package it.unisalento.pasproject.resourceservice.exceptions;

import it.unisalento.pasproject.resourceservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class UserNotAuthorizedException extends CustomErrorException {

    public UserNotAuthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}

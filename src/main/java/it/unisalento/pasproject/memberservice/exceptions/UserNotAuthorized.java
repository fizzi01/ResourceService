package it.unisalento.pasproject.memberservice.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotAuthorized extends CustomErrorException {
    public UserNotAuthorized(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}

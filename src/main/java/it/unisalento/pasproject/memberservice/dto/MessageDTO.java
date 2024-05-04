package it.unisalento.pasproject.memberservice.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The MessageDTO class is a data transfer object that represents a response message.
 * It includes properties such as response and code.
 */
@Getter
@Setter
public class MessageDTO {
    /**
     * The response message.
     */
    private String response;

    /**
     * The HTTP-style response code (200, 404, 500, etc.).
     */
    private int code;

    /**
     * Default constructor for the MessageDTO class.
     */
    public MessageDTO() {}

    /**
     * Constructs a new MessageDTO with the given response message and code.
     *
     * @param response the response message
     * @param code the HTTP-style response code
     */
    public MessageDTO(String response, int code) {
        this.response = response;
        this.code = code;
    }
}

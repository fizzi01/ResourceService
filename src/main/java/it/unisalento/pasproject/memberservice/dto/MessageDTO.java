package it.unisalento.pasproject.memberservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
    //Response message
    private String response;

    //Http-style response code (200, 404, 500, ...)
    private int code;

    public MessageDTO() {}

    public MessageDTO(String response, int code) {
        this.response = response;
        this.code = code;
    }
}

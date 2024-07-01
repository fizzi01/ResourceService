package it.unisalento.pasproject.resourceservice.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The ResourceMessageAssignDTO class is a data transfer object that represents a resource assignment message.
 * It includes properties such as idResource and assignedUser.
 */
@Getter
@Setter
public class ResourceMessageStatusDTO {
    public enum Status {
        AVAILABLE,
        BUSY,
        UNAVAILABLE
    }

    private String id;

    private Status status;

    private String currentTaskId;
}

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
    private String id;

    private Boolean isAvailable;

    private String currentTaskId;
}

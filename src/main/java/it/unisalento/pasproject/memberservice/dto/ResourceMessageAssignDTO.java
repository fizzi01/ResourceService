package it.unisalento.pasproject.memberservice.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The ResourceMessageAssignDTO class is a data transfer object that represents a resource assignment message.
 * It includes properties such as idResource and assignedUser.
 */
@Getter
@Setter
public class ResourceMessageAssignDTO {
    /**
     * The ID of the resource to be assigned.
     */
    private String idResource;

    /**
     * The user to whom the resource is to be assigned.
     */
    private String assignedUser;
}

package it.unisalento.pasproject.resourceservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The ResourceListDTO class is a data transfer object that represents a list of resources.
 * It includes a property resourcesList which is a list of ResourceDTO objects.
 */
@Getter
@Setter
public class ResourceListDTO {
    /**
     * The list of resources.
     */
    private List<ResourceDTO> resourcesList;

    /**
     * Default constructor for the ResourceListDTO class.
     * Initializes the resourcesList as a new ArrayList.
     */
    public ResourceListDTO() {
        this.resourcesList = new ArrayList<>();
    }
}
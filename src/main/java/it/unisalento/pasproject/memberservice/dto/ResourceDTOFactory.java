package it.unisalento.pasproject.memberservice.dto;

/**
 * The ResourceDTOFactory class is a factory class that creates and returns instances of ResourceDTO subclasses.
 * It uses the ResourceDTOType enum to determine the type of ResourceDTO to create.
 */
public class ResourceDTOFactory {
    /**
     * The ResourceDTOType enum lists the types of ResourceDTOs that can be created by the ResourceDTOFactory.
     */
    public enum ResourceDTOType { GPU, CPU }

    /**
     * Returns a new instance of a ResourceDTO subclass based on the given ResourceDTOType.
     *
     * @param type the type of ResourceDTO to create
     * @return a new ResourceDTO instance
     */
    public ResourceDTO getResourceDTOType(ResourceDTOType type) {
        if (type == null) {
            type = ResourceDTOType.CPU;
        }

        return switch (type) {
            case GPU -> new ResourceGpuDTO();
            case CPU -> new ResourceCpuDTO();
        };
    }
}
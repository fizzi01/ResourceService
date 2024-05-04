package it.unisalento.pasproject.memberservice.domain;

/**
 * The ResourceFactory class is a factory class that creates and returns instances of Resource subclasses.
 * It uses the ResourceType enum to determine the type of Resource to create.
 */
public class ResourceFactory {
    /**
     * The ResourceType enum lists the types of Resources that can be created by the ResourceFactory.
     */
    public enum ResourceType { GPU, CPU }

    /**
     * Returns a new instance of a Resource subclass based on the given ResourceType.
     *
     * @param type the type of Resource to create
     * @return a new Resource instance
     */
    public Resource getResourceType(ResourceType type) {
        if (type == null) {
            type = ResourceType.CPU;
        }

        return switch (type) {
            case GPU -> new ResourceGPU();
            case CPU -> new ResourceCPU();
        };
    }
}
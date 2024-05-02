package it.unisalento.pasproject.memberservice.domain;

public class ResourceFactory {
    public enum ResourceType { GPU, CPU }

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

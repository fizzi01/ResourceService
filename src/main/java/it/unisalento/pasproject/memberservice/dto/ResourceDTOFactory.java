package it.unisalento.pasproject.memberservice.dto;

public class ResourceDTOFactory {
    public enum ResourceDTOType { GPU, CPU }

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

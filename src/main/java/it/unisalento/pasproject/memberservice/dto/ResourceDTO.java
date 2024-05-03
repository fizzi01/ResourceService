package it.unisalento.pasproject.memberservice.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ResourceCpuDTO.class, name = "cpu"),
        @JsonSubTypes.Type(value = ResourceGpuDTO.class, name = "gpu")
})
@Getter
@Setter
public abstract class ResourceDTO {
    private String id;
    private String name;
    private String type;
    private String brand;
    private String model;
    private String greenEnergyType;
    private int availableHours;
    private double kWh;
    private String memberEmail;
    private Boolean isAvailable;
    private String assignedUser;
}

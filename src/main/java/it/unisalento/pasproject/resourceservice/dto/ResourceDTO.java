package it.unisalento.pasproject.resourceservice.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.unisalento.pasproject.resourceservice.domain.Availability;
import it.unisalento.pasproject.resourceservice.service.AvailabilityDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The ResourceDTO class is an abstract data transfer object that represents a resource.
 * It includes properties such as id, name, type, brand, model, greenEnergyType, availableHours, kWh, memberEmail, isAvailable, and assignedUser.
 * It uses the JsonTypeInfo and JsonSubTypes annotations to support JSON polymorphism.
 * The actual type of resource (CPU or GPU) is determined by the "type" property in the JSON data.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ResourceCpuDTO.class, name = "cpu"),
        @JsonSubTypes.Type(value = ResourceGpuDTO.class, name = "gpu"),
        @JsonSubTypes.Type(value = ResourceSoCDTO.class, name = "soc")
})
@Getter
@Setter
public abstract class ResourceDTO {
    /**
     * The ID of the resource.
     */
    private String id;

    /**
     * The name of the resource.
     */
    private String name;

    /**
     * The type of the resource (CPU or GPU).
     */
    private String type;

    /**
     * The brand of the resource.
     */
    private String brand;

    /**
     * The model of the resource.
     */
    private String model;

    /**
     * The type of green energy used by the resource.
     */
    private String greenEnergyType;

    /**
     * The number of hours the resource is available.
     */
    @JsonDeserialize(using = AvailabilityDeserializer.class)
    private List<Availability> availability;

    /**
     * The energy consumption of the resource in kilowatt-hours (kWh).
     */
    private double kWh;

    /**
     * The email of the member who owns the resource.
     */
    private String memberEmail;

    /**
     * Whether the resource is currently available.
     */
    private Boolean isAvailable;

    /**
     * The user to whom the resource is currently assigned.
     */
    private String currentTaskId;
}

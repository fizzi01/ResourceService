package it.unisalento.pasproject.resourceservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * The Resource class is an abstract class that represents a generic resource.
 * It is a MongoDB document, and it is used as a base class for specific types of resources.
 * It includes common properties such as id, name, type, brand, model, greenEnergyType, availableHours, kWh, memberEmail, isAvailable, and assignedUser.
 */
@Getter
@Setter
@Document(collection = "resource")
public abstract class Resource {
    public enum Status {
        AVAILABLE,
        BUSY,
        UNAVAILABLE
    }

    /**
     * The unique identifier of the resource.
     */
    @Id
    private String id;

    /**
     * The name of the resource.
     */
    private String name;

    /**
     * The type of the resource.
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
     * The type of green energy the resource uses.
     */
    private String greenEnergyType;

    private String country;
    private String region;
    private String city;

    /**
     * The number of hours the resource is available.
     */
    private List<Availability> availability;

    /**
     * The kilowatt-hours (kWh) the resource uses.
     */
    private double kWh;

    /**
     * The email of the member associated with the resource.
     */
    private String memberEmail;

    /**
     * Whether the resource is available.
     */
    private Status status;

    /**
     * The user to whom the resource is assigned.
     */
    private String currentTaskId;
}

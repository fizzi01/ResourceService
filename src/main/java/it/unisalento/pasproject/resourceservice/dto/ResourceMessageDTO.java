package it.unisalento.pasproject.resourceservice.dto;

import it.unisalento.pasproject.resourceservice.domain.Availability;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The ResourceMessageDTO class is a data transfer object that represents a resource message.
 * It includes properties such as id, availableHours, kWh, memberEmail, isAvailable, assignedUser, tdp, singleCoreScore, multicoreScore, openclScore, vulkanScore, and cudaScore.
 */
@Getter
@Setter
public class ResourceMessageDTO {
    public enum Status {
        AVAILABLE,
        BUSY,
        UNAVAILABLE
    }

    /**
     * The ID of the resource.
     */
    private String id;

    private String name;

    /**
     * The number of hours the resource is available.
     */
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
    private Status status;

    /**
     * The user to whom the resource is currently assigned.
     */
    private String currentTaskId;

    /**
     * The single core score of the resource.
     */
    private double singleCoreScore;

    /**
     * The multicore score of the resource.
     */
    private double multicoreScore;

    /**
     * The OpenCL score of the resource.
     */
    private double openclScore;

    /**
     * The Vulkan score of the resource.
     */
    private double vulkanScore;

    /**
     * The CUDA score of the resource.
     */
    private double cudaScore;

    /**
     * Default constructor for the ResourceMessageDTO class.
     */
    public ResourceMessageDTO() {}
}

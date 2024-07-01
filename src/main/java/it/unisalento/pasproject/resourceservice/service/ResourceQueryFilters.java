package it.unisalento.pasproject.resourceservice.service;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResourceQueryFilters {
    public enum Status {
        AVAILABLE,
        BUSY,
        UNAVAILABLE,
    }

    private String name;
    private String type;
    private String greenEnergyType;
    private String country;
    private String region;
    private String city;
    private LocalDateTime from;
    private LocalDateTime to;
    private Double kWh;
    private String memberEmail;
    private Status status;
}

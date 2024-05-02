package it.unisalento.pasproject.memberservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "resource")
public abstract class Resource {
    @Id
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
}

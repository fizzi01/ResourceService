package it.unisalento.pasproject.memberservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "resource")
public class Resource {
    //Questa collezione descrive una capacità computazionale
    //TODO: Modificare il documento opportunamente

    @Id
    private String id;
    private String name;
    private String processorBrand;
    private String processorModel;
    private double processorSpeedGHz;
    private int numberOfCores;
    private int cacheSizeMB;
    private int memorySizeGB;
    private String memoryType;
    private String operatingSystem;
    private String greenEnergyType;
    private int availableHours;
    private double kWh;
    private double computePower;
    private String memberEmail;
}

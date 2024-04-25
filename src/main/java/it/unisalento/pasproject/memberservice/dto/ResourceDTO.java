package it.unisalento.pasproject.memberservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceDTO {
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

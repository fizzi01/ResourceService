package it.unisalento.pasproject.resourceservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceSoCDTO extends ResourceDTO {
    private String architecture;
    private int cpuCores;
    private int gpuCores;
    private double cpuBaseFrequency;
    private double cpuMaxFrequency;
    private double gpuBaseFrequency;
    private double gpuMaxFrequency;
    private double tdp;

    private double singleCoreScore;
    private double multicoreScore;
    private double openclScore;
    private double vulkanScore;
    private double cudaScore;
}

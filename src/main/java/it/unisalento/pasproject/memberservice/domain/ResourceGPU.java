package it.unisalento.pasproject.memberservice.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceGPU extends Resource {
    private String architecture;
    private String vramType;
    private int vramSize;
    private double coreClock;
    private double boostClock;
    private String memoryClock;
    private double tdp;
    private boolean rayTracingSupport;
    private boolean dlssSupport;
    private double openclScore;
    private double vulkanScore;
    private double cudaScore;

    public ResourceGPU() {}
}

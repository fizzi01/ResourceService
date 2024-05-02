package it.unisalento.pasproject.memberservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceGpuDTO extends ResourceDTO {
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

    public ResourceGpuDTO() {}
}

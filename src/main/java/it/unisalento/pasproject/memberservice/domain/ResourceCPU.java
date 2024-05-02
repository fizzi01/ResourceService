package it.unisalento.pasproject.memberservice.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceCPU extends Resource {
    private String architecture;
    private int cores;
    private int threads;
    private double baseFrequency;
    private double maxFrequency;
    private int cacheSize;
    private double tdp;
    private boolean hyperThreading;
    private boolean overclockingSupport;
    private double singleCoreScore;
    private double multicoreScore;

    public ResourceCPU() {}
}

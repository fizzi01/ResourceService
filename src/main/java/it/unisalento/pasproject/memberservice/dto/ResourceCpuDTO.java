package it.unisalento.pasproject.memberservice.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The ResourceCpuDTO class is a data transfer object that represents a CPU resource.
 * It extends the ResourceDTO class and includes additional properties such as architecture, cores, threads, baseFrequency, maxFrequency, cacheSize, tdp, hyperThreading, overclockingSupport, singleCoreScore, and multicoreScore.
 */
@Getter
@Setter
public class ResourceCpuDTO extends ResourceDTO {
    /**
     * The architecture of the CPU.
     */
    private String architecture;

    /**
     * The number of cores in the CPU.
     */
    private int cores;

    /**
     * The number of threads in the CPU.
     */
    private int threads;

    /**
     * The base frequency of the CPU.
     */
    private double baseFrequency;

    /**
     * The maximum frequency of the CPU.
     */
    private double maxFrequency;

    /**
     * The cache size of the CPU.
     */
    private int cacheSize;

    /**
     * The Thermal Design Power (TDP) of the CPU.
     */
    private double tdp;

    /**
     * Whether the CPU supports hyper-threading.
     */
    private boolean hyperThreading;

    /**
     * Whether the CPU supports overclocking.
     */
    private boolean overclockingSupport;

    /**
     * The single-core score of the CPU.
     */
    private double singleCoreScore;

    /**
     * The multi-core score of the CPU.
     */
    private double multicoreScore;

    /**
     * Default constructor for the ResourceCpuDTO class.
     */
    public ResourceCpuDTO() {}
}

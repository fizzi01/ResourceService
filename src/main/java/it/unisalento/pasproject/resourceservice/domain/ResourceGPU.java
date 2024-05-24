package it.unisalento.pasproject.resourceservice.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * The ResourceGPU class is a subclass of Resource that represents a GPU resource.
 * It includes additional properties such as architecture, vramType, vramSize, coreClock, boostClock, memoryClock, tdp, rayTracingSupport, dlssSupport, openclScore, vulkanScore, and cudaScore.
 */
@Getter
@Setter
public class ResourceGPU extends Resource {
    /**
     * The architecture of the GPU.
     */
    private String architecture;

    /**
     * The type of VRAM in the GPU.
     */
    private String vramType;

    /**
     * The size of the VRAM in the GPU.
     */
    private int vramSize;

    /**
     * The core clock speed of the GPU.
     */
    private double coreClock;

    /**
     * The boost clock speed of the GPU.
     */
    private double boostClock;

    /**
     * The memory clock speed of the GPU.
     */
    private String memoryClock;

    /**
     * The Thermal Design Power (TDP) of the GPU.
     */
    private double tdp;

    /**
     * Whether the GPU supports ray tracing.
     */
    private boolean rayTracingSupport;

    /**
     * Whether the GPU supports DLSS (Deep Learning Super Sampling).
     */
    private boolean dlssSupport;

    /**
     * The OpenCL score of the GPU.
     */
    private double openclScore;

    /**
     * The Vulkan score of the GPU.
     */
    private double vulkanScore;

    /**
     * The CUDA score of the GPU.
     */
    private double cudaScore;

    /**
     * Default constructor for the ResourceGPU class.
     */
    public ResourceGPU() {}
}
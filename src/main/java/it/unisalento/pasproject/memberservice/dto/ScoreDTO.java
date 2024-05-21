package it.unisalento.pasproject.memberservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreDTO {
    private double score;
    private double multicore_score;
    private double opencl;
    private double vulkan;
    private double cuda;
}

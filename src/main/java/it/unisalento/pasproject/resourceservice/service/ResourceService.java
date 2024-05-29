package it.unisalento.pasproject.resourceservice.service;

import it.unisalento.pasproject.resourceservice.domain.*;
import it.unisalento.pasproject.resourceservice.dto.*;
import it.unisalento.pasproject.resourceservice.exceptions.BadFormatAvailabilityException;
import it.unisalento.pasproject.resourceservice.repositories.ResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The ResourceService class provides methods for managing resources.
 * It includes methods for getting and updating resources, and converting resources to and from DTOs.
 * It uses a MongoTemplate for performing MongoDB operations.
 */
@Service
public class ResourceService {
    //TODO: Vedere se fino alla fine inglobare il Metal
    private final MongoTemplate mongoTemplate;

    private final ResourceDTOFactory resourceDTOFactory;

    private final ResourceFactory resourceFactory;

    private final ResourceMessageHandler resourceMessageHandler;

    private final ResourceRepository resourceRepository;

    private final int AVAILABILITY_TRESHOLD = 30;

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class);

    /**
     * Constructor for the ResourceService.
     * @param mongoTemplate The MongoTemplate to be used for MongoDB operations.
     */
    @Autowired
    public ResourceService(MongoTemplate mongoTemplate, ResourceMessageHandler resourceMessageHandler, ResourceRepository resourceRepository) {
        this.resourceFactory = new ResourceFactory();
        this.resourceDTOFactory = new ResourceDTOFactory();
        this.mongoTemplate = mongoTemplate;
        this.resourceMessageHandler = resourceMessageHandler;
        this.resourceRepository = resourceRepository;
    }

    public Resource getResource(ResourceDTO resourceDTO) {
        switch (resourceDTO) {
            case ResourceCpuDTO resourceCpuDTO -> {
                LOGGER.info("ResourceCpuDTO: " + resourceCpuDTO.getName());
                ScoreDTO scoreDTO = resourceMessageHandler.requestResourceScore(getScoreMessageDTO(resourceDTO));

                if (scoreDTO == null) {
                    return null;
                }

                ResourceCPU resourceCPU = (ResourceCPU) resourceFactory.getResourceType(ResourceFactory.ResourceType.CPU);

                // Set common properties
                Optional.ofNullable(resourceCpuDTO.getName()).ifPresent(resourceCPU::setName);
                Optional.ofNullable(resourceCpuDTO.getType()).ifPresent(resourceCPU::setType);
                Optional.ofNullable(resourceCpuDTO.getBrand()).ifPresent(resourceCPU::setBrand);
                Optional.ofNullable(resourceCpuDTO.getModel()).ifPresent(resourceCPU::setModel);
                Optional.ofNullable(resourceCpuDTO.getGreenEnergyType()).ifPresent(resourceCPU::setGreenEnergyType);
                checkTimeDifference(resourceCpuDTO.getAvailability());
                Optional.ofNullable(resourceCpuDTO.getAvailability()).ifPresent(resourceCPU::setAvailability);
                Optional.of(resourceCpuDTO.getKWh()).ifPresent(resourceCPU::setKWh);
                Optional.ofNullable(resourceCpuDTO.getMemberEmail()).ifPresent(resourceCPU::setMemberEmail);
                Optional.ofNullable(resourceCpuDTO.getIsAvailable()).ifPresent(resourceCPU::setIsAvailable);
                Optional.ofNullable(resourceCpuDTO.getCurrentTaskId()).ifPresent(resourceCPU::setCurrentTaskId);

                // Set CPU-specific properties
                Optional.ofNullable(resourceCpuDTO.getArchitecture()).ifPresent(resourceCPU::setArchitecture);
                Optional.of(resourceCpuDTO.getCores()).ifPresent(resourceCPU::setCores);
                Optional.of(resourceCpuDTO.getThreads()).ifPresent(resourceCPU::setThreads);
                Optional.of(resourceCpuDTO.getBaseFrequency()).ifPresent(resourceCPU::setBaseFrequency);
                Optional.of(resourceCpuDTO.getMaxFrequency()).ifPresent(resourceCPU::setMaxFrequency);
                Optional.of(resourceCpuDTO.getCacheSize()).ifPresent(resourceCPU::setCacheSize);
                Optional.of(resourceCpuDTO.getTdp()).ifPresent(resourceCPU::setTdp);
                Optional.of(resourceCpuDTO.isHyperThreading()).ifPresent(resourceCPU::setHyperThreading);
                Optional.of(resourceCpuDTO.isOverclockingSupport()).ifPresent(resourceCPU::setOverclockingSupport);
                Optional.of(scoreDTO.getScore()).ifPresent(resourceCPU::setSingleCoreScore);
                Optional.of(scoreDTO.getMulticore_score()).ifPresent(resourceCPU::setMulticoreScore);

                LOGGER.info("ResourceCpuDTO: " + resourceCPU.getSingleCoreScore());

                return resourceCPU;
            }
            case ResourceGpuDTO resourceGpuDTO -> {
                ScoreDTO scoreDTO = resourceMessageHandler.requestResourceScore(getScoreMessageDTO(resourceDTO));

                if (scoreDTO == null) {
                    return null;
                }

                ResourceGPU resourceGPU = (ResourceGPU) resourceFactory.getResourceType(ResourceFactory.ResourceType.GPU);

                // Set common properties
                Optional.ofNullable(resourceGpuDTO.getName()).ifPresent(resourceGPU::setName);
                Optional.ofNullable(resourceGpuDTO.getType()).ifPresent(resourceGPU::setType);
                Optional.ofNullable(resourceGpuDTO.getBrand()).ifPresent(resourceGPU::setBrand);
                Optional.ofNullable(resourceGpuDTO.getModel()).ifPresent(resourceGPU::setModel);
                Optional.ofNullable(resourceGpuDTO.getGreenEnergyType()).ifPresent(resourceGPU::setGreenEnergyType);
                checkTimeDifference(resourceGpuDTO.getAvailability());
                Optional.ofNullable(resourceGpuDTO.getAvailability()).ifPresent(resourceGPU::setAvailability);
                Optional.of(resourceGpuDTO.getKWh()).ifPresent(resourceGPU::setKWh);
                Optional.ofNullable(resourceGpuDTO.getMemberEmail()).ifPresent(resourceGPU::setMemberEmail);
                Optional.ofNullable(resourceGpuDTO.getIsAvailable()).ifPresent(resourceGPU::setIsAvailable);
                Optional.ofNullable(resourceGpuDTO.getCurrentTaskId()).ifPresent(resourceGPU::setCurrentTaskId);

                // Set GPU-specific properties
                Optional.ofNullable(resourceGpuDTO.getArchitecture()).ifPresent(resourceGPU::setArchitecture);
                Optional.ofNullable(resourceGpuDTO.getVramType()).ifPresent(resourceGPU::setVramType);
                Optional.of(resourceGpuDTO.getVramSize()).ifPresent(resourceGPU::setVramSize);
                Optional.of(resourceGpuDTO.getCoreClock()).ifPresent(resourceGPU::setCoreClock);
                Optional.of(resourceGpuDTO.getBoostClock()).ifPresent(resourceGPU::setBoostClock);
                Optional.ofNullable(resourceGpuDTO.getMemoryClock()).ifPresent(resourceGPU::setMemoryClock);
                Optional.of(resourceGpuDTO.getTdp()).ifPresent(resourceGPU::setTdp);
                Optional.of(resourceGpuDTO.isRayTracingSupport()).ifPresent(resourceGPU::setRayTracingSupport);
                Optional.of(resourceGpuDTO.isDlssSupport()).ifPresent(resourceGPU::setDlssSupport);
                Optional.of(scoreDTO.getOpencl()).ifPresent(resourceGPU::setOpenclScore);
                Optional.of(scoreDTO.getVulkan()).ifPresent(resourceGPU::setVulkanScore);
                Optional.of(scoreDTO.getCuda()).ifPresent(resourceGPU::setCudaScore);

                return resourceGPU;
            }
            case ResourceSoCDTO resourceSoCDTO -> {
                ScoreDTO scoreDTO = resourceMessageHandler.requestResourceScore(getScoreMessageDTO(resourceDTO));

                if (scoreDTO == null) {
                    return null;
                }

                ResourceSoC resourceSoC = (ResourceSoC) resourceFactory.getResourceType(ResourceFactory.ResourceType.SOC);

                // Set common properties
                Optional.ofNullable(resourceSoCDTO.getName()).ifPresent(resourceSoC::setName);
                Optional.ofNullable(resourceSoCDTO.getType()).ifPresent(resourceSoC::setType);
                Optional.ofNullable(resourceSoCDTO.getBrand()).ifPresent(resourceSoC::setBrand);
                Optional.ofNullable(resourceSoCDTO.getModel()).ifPresent(resourceSoC::setModel);
                Optional.ofNullable(resourceSoCDTO.getGreenEnergyType()).ifPresent(resourceSoC::setGreenEnergyType);
                checkTimeDifference(resourceSoCDTO.getAvailability());
                Optional.ofNullable(resourceSoCDTO.getAvailability()).ifPresent(resourceSoC::setAvailability);
                Optional.of(resourceSoCDTO.getKWh()).ifPresent(resourceSoC::setKWh);
                Optional.ofNullable(resourceSoCDTO.getMemberEmail()).ifPresent(resourceSoC::setMemberEmail);
                Optional.ofNullable(resourceSoCDTO.getIsAvailable()).ifPresent(resourceSoC::setIsAvailable);
                Optional.ofNullable(resourceSoCDTO.getCurrentTaskId()).ifPresent(resourceSoC::setCurrentTaskId);

                // Set SoC-specific properties
                Optional.ofNullable(resourceSoCDTO.getArchitecture()).ifPresent(resourceSoC::setArchitecture);
                Optional.of(resourceSoCDTO.getCpuCores()).ifPresent(resourceSoC::setCpuCores);
                Optional.of(resourceSoCDTO.getGpuCores()).ifPresent(resourceSoC::setGpuCores);
                Optional.of(resourceSoCDTO.getCpuBaseFrequency()).ifPresent(resourceSoC::setCpuBaseFrequency);
                Optional.of(resourceSoCDTO.getCpuMaxFrequency()).ifPresent(resourceSoC::setCpuMaxFrequency);
                Optional.of(resourceSoCDTO.getGpuBaseFrequency()).ifPresent(resourceSoC::setGpuBaseFrequency);
                Optional.of(resourceSoCDTO.getGpuMaxFrequency()).ifPresent(resourceSoC::setGpuMaxFrequency);
                Optional.of(resourceSoCDTO.getTdp()).ifPresent(resourceSoC::setTdp);
                Optional.of(scoreDTO.getScore()).ifPresent(resourceSoC::setSingleCoreScore);
                Optional.of(scoreDTO.getMulticore_score()).ifPresent(resourceSoC::setMulticoreScore);
                Optional.of(scoreDTO.getOpencl()).ifPresent(resourceSoC::setOpenclScore);
                Optional.of(scoreDTO.getVulkan()).ifPresent(resourceSoC::setVulkanScore);
                Optional.of(scoreDTO.getCuda()).ifPresent(resourceSoC::setCudaScore);

                return resourceSoC;
            }
            case null, default -> {
                return null;
            }
        }
    }

    private void checkTimeDifference(List<Availability> availabilities) {
        for (Availability availability : availabilities) {
            LocalTime startTime = availability.getStartTime();
            LocalTime endTime = availability.getEndTime();

            long minutes = ChronoUnit.MINUTES.between(startTime, endTime);

            if (minutes % AVAILABILITY_TRESHOLD != 0) {
                throw new BadFormatAvailabilityException("The difference between endTime and startTime must be a multiple of 30 minutes.");
            }
        }
    }

    public ResourceDTO getResourceDTO(Resource resource) {
        switch (resource) {
            case ResourceCPU resourceCPU -> {
                ResourceCpuDTO resourceCpuDTO = (ResourceCpuDTO) resourceDTOFactory.getResourceDTOType(ResourceDTOFactory.ResourceDTOType.CPU);

                // Set common properties
                Optional.ofNullable(resourceCPU.getId()).ifPresent(resourceCpuDTO::setId);
                Optional.ofNullable(resourceCPU.getName()).ifPresent(resourceCpuDTO::setName);
                Optional.ofNullable(resourceCPU.getType()).ifPresent(resourceCpuDTO::setType);
                Optional.ofNullable(resourceCPU.getBrand()).ifPresent(resourceCpuDTO::setBrand);
                Optional.ofNullable(resourceCPU.getModel()).ifPresent(resourceCpuDTO::setModel);
                Optional.ofNullable(resourceCPU.getGreenEnergyType()).ifPresent(resourceCpuDTO::setGreenEnergyType);
                Optional.ofNullable(resourceCPU.getAvailability()).ifPresent(resourceCpuDTO::setAvailability);
                Optional.of(resourceCPU.getKWh()).ifPresent(resourceCpuDTO::setKWh);
                Optional.ofNullable(resourceCPU.getMemberEmail()).ifPresent(resourceCpuDTO::setMemberEmail);
                Optional.ofNullable(resourceCPU.getIsAvailable()).ifPresent(resourceCpuDTO::setIsAvailable);
                Optional.ofNullable(resourceCPU.getCurrentTaskId()).ifPresent(resourceCpuDTO::setCurrentTaskId);

                // Set CPU-specific properties
                Optional.ofNullable(resourceCPU.getArchitecture()).ifPresent(resourceCpuDTO::setArchitecture);
                Optional.of(resourceCPU.getCores()).ifPresent(resourceCpuDTO::setCores);
                Optional.of(resourceCPU.getThreads()).ifPresent(resourceCpuDTO::setThreads);
                Optional.of(resourceCPU.getBaseFrequency()).ifPresent(resourceCpuDTO::setBaseFrequency);
                Optional.of(resourceCPU.getMaxFrequency()).ifPresent(resourceCpuDTO::setMaxFrequency);
                Optional.of(resourceCPU.getCacheSize()).ifPresent(resourceCpuDTO::setCacheSize);
                Optional.of(resourceCPU.getTdp()).ifPresent(resourceCpuDTO::setTdp);
                Optional.of(resourceCPU.isHyperThreading()).ifPresent(resourceCpuDTO::setHyperThreading);
                Optional.of(resourceCPU.isOverclockingSupport()).ifPresent(resourceCpuDTO::setOverclockingSupport);
                Optional.of(resourceCPU.getSingleCoreScore()).ifPresent(resourceCpuDTO::setSingleCoreScore);
                Optional.of(resourceCPU.getMulticoreScore()).ifPresent(resourceCpuDTO::setMulticoreScore);

                return resourceCpuDTO;
            }
            case ResourceGPU resourceGPU -> {
                ResourceGpuDTO resourceGpuDTO = (ResourceGpuDTO) resourceDTOFactory.getResourceDTOType(ResourceDTOFactory.ResourceDTOType.GPU);

                // Set common properties
                Optional.ofNullable(resourceGPU.getId()).ifPresent(resourceGpuDTO::setId);
                Optional.ofNullable(resourceGPU.getName()).ifPresent(resourceGpuDTO::setName);
                Optional.ofNullable(resourceGPU.getType()).ifPresent(resourceGpuDTO::setType);
                Optional.ofNullable(resourceGPU.getBrand()).ifPresent(resourceGpuDTO::setBrand);
                Optional.ofNullable(resourceGPU.getModel()).ifPresent(resourceGpuDTO::setModel);
                Optional.ofNullable(resourceGPU.getGreenEnergyType()).ifPresent(resourceGpuDTO::setGreenEnergyType);
                Optional.ofNullable(resourceGPU.getAvailability()).ifPresent(resourceGpuDTO::setAvailability);
                Optional.of(resourceGPU.getKWh()).ifPresent(resourceGpuDTO::setKWh);
                Optional.ofNullable(resourceGPU.getMemberEmail()).ifPresent(resourceGpuDTO::setMemberEmail);
                Optional.ofNullable(resourceGPU.getIsAvailable()).ifPresent(resourceGpuDTO::setIsAvailable);
                Optional.ofNullable(resourceGPU.getCurrentTaskId()).ifPresent(resourceGpuDTO::setCurrentTaskId);

                // Set GPU-specific properties
                Optional.ofNullable(resourceGPU.getArchitecture()).ifPresent(resourceGpuDTO::setArchitecture);
                Optional.ofNullable(resourceGPU.getVramType()).ifPresent(resourceGpuDTO::setVramType);
                Optional.of(resourceGPU.getVramSize()).ifPresent(resourceGpuDTO::setVramSize);
                Optional.of(resourceGPU.getCoreClock()).ifPresent(resourceGpuDTO::setCoreClock);
                Optional.of(resourceGPU.getBoostClock()).ifPresent(resourceGpuDTO::setBoostClock);
                Optional.ofNullable(resourceGPU.getMemoryClock()).ifPresent(resourceGpuDTO::setMemoryClock);
                Optional.of(resourceGPU.getTdp()).ifPresent(resourceGpuDTO::setTdp);
                Optional.of(resourceGPU.isRayTracingSupport()).ifPresent(resourceGpuDTO::setRayTracingSupport);
                Optional.of(resourceGPU.isDlssSupport()).ifPresent(resourceGpuDTO::setDlssSupport);
                Optional.of(resourceGPU.getOpenclScore()).ifPresent(resourceGpuDTO::setOpenclScore);
                Optional.of(resourceGPU.getVulkanScore()).ifPresent(resourceGpuDTO::setVulkanScore);
                Optional.of(resourceGPU.getCudaScore()).ifPresent(resourceGpuDTO::setCudaScore);

                return resourceGpuDTO;
            }
            case ResourceSoC resourceSoC -> {
                ResourceSoCDTO resourceSoCDTO = (ResourceSoCDTO) resourceDTOFactory.getResourceDTOType(ResourceDTOFactory.ResourceDTOType.SOC);

                // Set common properties
                Optional.ofNullable(resourceSoC.getId()).ifPresent(resourceSoCDTO::setId);
                Optional.ofNullable(resourceSoC.getName()).ifPresent(resourceSoCDTO::setName);
                Optional.ofNullable(resourceSoC.getType()).ifPresent(resourceSoCDTO::setType);
                Optional.ofNullable(resourceSoC.getBrand()).ifPresent(resourceSoCDTO::setBrand);
                Optional.ofNullable(resourceSoC.getModel()).ifPresent(resourceSoCDTO::setModel);
                Optional.ofNullable(resourceSoC.getGreenEnergyType()).ifPresent(resourceSoCDTO::setGreenEnergyType);
                Optional.ofNullable(resourceSoC.getAvailability()).ifPresent(resourceSoCDTO::setAvailability);
                Optional.of(resourceSoC.getKWh()).ifPresent(resourceSoCDTO::setKWh);
                Optional.ofNullable(resourceSoC.getMemberEmail()).ifPresent(resourceSoCDTO::setMemberEmail);
                Optional.ofNullable(resourceSoC.getIsAvailable()).ifPresent(resourceSoCDTO::setIsAvailable);
                Optional.ofNullable(resourceSoC.getCurrentTaskId()).ifPresent(resourceSoCDTO::setCurrentTaskId);

                // Set SoC-specific properties
                Optional.ofNullable(resourceSoC.getArchitecture()).ifPresent(resourceSoCDTO::setArchitecture);
                Optional.of(resourceSoC.getCpuCores()).ifPresent(resourceSoCDTO::setCpuCores);
                Optional.of(resourceSoC.getGpuCores()).ifPresent(resourceSoCDTO::setGpuCores);
                Optional.of(resourceSoC.getCpuBaseFrequency()).ifPresent(resourceSoCDTO::setCpuBaseFrequency);
                Optional.of(resourceSoC.getCpuMaxFrequency()).ifPresent(resourceSoCDTO::setCpuMaxFrequency);
                Optional.of(resourceSoC.getGpuBaseFrequency()).ifPresent(resourceSoCDTO::setGpuBaseFrequency);
                Optional.of(resourceSoC.getGpuMaxFrequency()).ifPresent(resourceSoCDTO::setGpuMaxFrequency);
                Optional.of(resourceSoC.getTdp()).ifPresent(resourceSoCDTO::setTdp);
                Optional.of(resourceSoC.getSingleCoreScore()).ifPresent(resourceSoCDTO::setSingleCoreScore);
                Optional.of(resourceSoC.getMulticoreScore()).ifPresent(resourceSoCDTO::setMulticoreScore);
                Optional.of(resourceSoC.getOpenclScore()).ifPresent(resourceSoCDTO::setOpenclScore);
                Optional.of(resourceSoC.getVulkanScore()).ifPresent(resourceSoCDTO::setVulkanScore);
                Optional.of(resourceSoC.getCudaScore()).ifPresent(resourceSoCDTO::setCudaScore);

                return resourceSoCDTO;
            }
            case null, default -> {
                return null;
            }
        }
    }

    public ResourceDTO insertResource(ResourceDTO resourceDTO) {
        Resource existingResource = resourceRepository.findByNameAndMemberEmail(resourceDTO.getName(), resourceDTO.getMemberEmail());

        if (existingResource != null) {
            return null;
        }

        Resource resource = getResource(resourceDTO);

        resource = resourceRepository.save(resource);

        resourceMessageHandler.sendNewResourceMessage(getResourceMessageDTO(resource));

        return getResourceDTO(resource);
    }

    public ResourceDTO updateResource(ResourceDTO resourceDTO) {
        Optional<Resource> resource = resourceRepository.findById(resourceDTO.getId());

        if(resource.isEmpty()) {
            return null;
        }

        Resource retResource = resource.get();

        if (resourceDTO instanceof ResourceCpuDTO resourceCpuDTO && retResource instanceof ResourceCPU resourceCPU) {
            if(!resourceCpuDTO.getName().equals(resourceCPU.getName())) {
                ScoreDTO scoreDTO = resourceMessageHandler.requestResourceScore(getScoreMessageDTO(resourceCpuDTO));

                if (scoreDTO != null) {
                    resourceCpuDTO.setSingleCoreScore(scoreDTO.getScore());
                    resourceCpuDTO.setMulticoreScore(scoreDTO.getMulticore_score());
                } else {
                    return null;
                }

                Optional.of(resourceCpuDTO.getSingleCoreScore()).ifPresent(resourceCPU::setSingleCoreScore);
                Optional.of(resourceCpuDTO.getMulticoreScore()).ifPresent(resourceCPU::setMulticoreScore);
            }

            Optional.ofNullable(resourceDTO.getName()).ifPresent(resourceCPU::setName);
            Optional.ofNullable(resourceDTO.getType()).ifPresent(resourceCPU::setType);
            Optional.ofNullable(resourceDTO.getBrand()).ifPresent(resourceCPU::setBrand);
            Optional.ofNullable(resourceDTO.getModel()).ifPresent(resourceCPU::setModel);
            Optional.ofNullable(resourceDTO.getGreenEnergyType()).ifPresent(resourceCPU::setGreenEnergyType);
            checkTimeDifference(resourceDTO.getAvailability());
            Optional.ofNullable(resourceDTO.getAvailability()).ifPresent(resourceCPU::setAvailability);
            Optional.of(resourceDTO.getKWh()).ifPresent(retResource::setKWh);
            Optional.ofNullable(resourceDTO.getMemberEmail()).ifPresent(resourceCPU::setMemberEmail);
            Optional.ofNullable(resourceDTO.getIsAvailable()).ifPresent(resourceCPU::setIsAvailable);
            Optional.ofNullable(resourceDTO.getCurrentTaskId()).ifPresent(resourceCPU::setCurrentTaskId);

            Optional.ofNullable(resourceCpuDTO.getArchitecture()).ifPresent(resourceCPU::setArchitecture);
            Optional.of(resourceCpuDTO.getCores()).ifPresent(resourceCPU::setCores);
            Optional.of(resourceCpuDTO.getThreads()).ifPresent(resourceCPU::setThreads);
            Optional.of(resourceCpuDTO.getBaseFrequency()).ifPresent(resourceCPU::setBaseFrequency);
            Optional.of(resourceCpuDTO.getMaxFrequency()).ifPresent(resourceCPU::setMaxFrequency);
            Optional.of(resourceCpuDTO.getCacheSize()).ifPresent(resourceCPU::setCacheSize);
            Optional.of(resourceCpuDTO.getTdp()).ifPresent(resourceCPU::setTdp);
            Optional.of(resourceCpuDTO.isHyperThreading()).ifPresent(resourceCPU::setHyperThreading);
            Optional.of(resourceCpuDTO.isOverclockingSupport()).ifPresent(resourceCPU::setOverclockingSupport);

            resourceCPU = resourceRepository.save(resourceCPU);

            resourceMessageHandler.sendUpdateResourceMessage(getResourceMessageDTO(resourceCPU));

            return getResourceDTO(resourceCPU);

        } else if (retResource instanceof ResourceGPU resourceGPU && resourceDTO instanceof ResourceGpuDTO resourceGpuDTO) {
            if(!resourceGpuDTO.getName().equals(resourceGPU.getName())) {
                ScoreDTO scoreDTO = resourceMessageHandler.requestResourceScore(getScoreMessageDTO(resourceGpuDTO));

                if (scoreDTO != null) {
                    resourceGpuDTO.setOpenclScore(scoreDTO.getOpencl());
                    resourceGpuDTO.setVulkanScore(scoreDTO.getVulkan());
                    resourceGpuDTO.setCudaScore(scoreDTO.getCuda());
                } else {
                    return null;
                }

                Optional.of(resourceGpuDTO.getOpenclScore()).ifPresent(resourceGPU::setOpenclScore);
                Optional.of(resourceGpuDTO.getVulkanScore()).ifPresent(resourceGPU::setVulkanScore);
                Optional.of(resourceGpuDTO.getCudaScore()).ifPresent(resourceGPU::setCudaScore);
            }

            Optional.ofNullable(resourceDTO.getName()).ifPresent(resourceGPU::setName);
            Optional.ofNullable(resourceDTO.getType()).ifPresent(resourceGPU::setType);
            Optional.ofNullable(resourceDTO.getBrand()).ifPresent(resourceGPU::setBrand);
            Optional.ofNullable(resourceDTO.getModel()).ifPresent(resourceGPU::setModel);
            Optional.ofNullable(resourceDTO.getGreenEnergyType()).ifPresent(resourceGPU::setGreenEnergyType);
            checkTimeDifference(resourceDTO.getAvailability());
            Optional.ofNullable(resourceDTO.getAvailability()).ifPresent(resourceGPU::setAvailability);
            Optional.of(resourceDTO.getKWh()).ifPresent(resourceGPU::setKWh);
            Optional.ofNullable(resourceDTO.getMemberEmail()).ifPresent(resourceGPU::setMemberEmail);
            Optional.ofNullable(resourceDTO.getIsAvailable()).ifPresent(resourceGPU::setIsAvailable);
            Optional.ofNullable(resourceDTO.getCurrentTaskId()).ifPresent(resourceGPU::setCurrentTaskId);

            Optional.ofNullable(resourceGpuDTO.getArchitecture()).ifPresent(resourceGPU::setArchitecture);
            Optional.ofNullable(resourceGpuDTO.getVramType()).ifPresent(resourceGPU::setVramType);
            Optional.of(resourceGpuDTO.getVramSize()).ifPresent(resourceGPU::setVramSize);
            Optional.of(resourceGpuDTO.getCoreClock()).ifPresent(resourceGPU::setCoreClock);
            Optional.of(resourceGpuDTO.getBoostClock()).ifPresent(resourceGPU::setBoostClock);
            Optional.ofNullable(resourceGpuDTO.getMemoryClock()).ifPresent(resourceGPU::setMemoryClock);
            Optional.of(resourceGpuDTO.getTdp()).ifPresent(resourceGPU::setTdp);
            Optional.of(resourceGpuDTO.isRayTracingSupport()).ifPresent(resourceGPU::setRayTracingSupport);
            Optional.of(resourceGpuDTO.isDlssSupport()).ifPresent(resourceGPU::setDlssSupport);

            resourceGPU = resourceRepository.save(resourceGPU);

            resourceMessageHandler.sendUpdateResourceMessage(getResourceMessageDTO(resourceGPU));

            return getResourceDTO(resourceGPU);

        } else if (retResource instanceof ResourceSoC resourceSoC && resourceDTO instanceof ResourceSoCDTO resourceSoCDTO) {
            if(!resourceSoCDTO.getName().equals(resourceSoC.getName())) {
                ScoreDTO scoreDTO = resourceMessageHandler.requestResourceScore(getScoreMessageDTO(resourceSoCDTO));

                if (scoreDTO != null) {
                    resourceSoCDTO.setSingleCoreScore(scoreDTO.getScore());
                    resourceSoCDTO.setMulticoreScore(scoreDTO.getMulticore_score());
                    resourceSoCDTO.setOpenclScore(scoreDTO.getOpencl());
                    resourceSoCDTO.setVulkanScore(scoreDTO.getVulkan());
                    resourceSoCDTO.setCudaScore(scoreDTO.getCuda());
                } else {
                    return null;
                }

                Optional.of(resourceSoCDTO.getSingleCoreScore()).ifPresent(resourceSoC::setSingleCoreScore);
                Optional.of(resourceSoCDTO.getMulticoreScore()).ifPresent(resourceSoC::setMulticoreScore);
                Optional.of(resourceSoCDTO.getOpenclScore()).ifPresent(resourceSoC::setOpenclScore);
                Optional.of(resourceSoCDTO.getVulkanScore()).ifPresent(resourceSoC::setVulkanScore);
                Optional.of(resourceSoCDTO.getCudaScore()).ifPresent(resourceSoC::setCudaScore);
            }

            Optional.ofNullable(resourceDTO.getName()).ifPresent(resourceSoC::setName);
            Optional.ofNullable(resourceDTO.getType()).ifPresent(resourceSoC::setType);
            Optional.ofNullable(resourceDTO.getBrand()).ifPresent(resourceSoC::setBrand);
            Optional.ofNullable(resourceDTO.getModel()).ifPresent(resourceSoC::setModel);
            Optional.ofNullable(resourceDTO.getGreenEnergyType()).ifPresent(resourceSoC::setGreenEnergyType);
            checkTimeDifference(resourceDTO.getAvailability());
            Optional.ofNullable(resourceDTO.getAvailability()).ifPresent(resourceSoC::setAvailability);
            Optional.of(resourceDTO.getKWh()).ifPresent(resourceSoC::setKWh);
            Optional.ofNullable(resourceDTO.getMemberEmail()).ifPresent(resourceSoC::setMemberEmail);
            Optional.ofNullable(resourceDTO.getIsAvailable()).ifPresent(resourceSoC::setIsAvailable);
            Optional.ofNullable(resourceDTO.getCurrentTaskId()).ifPresent(resourceSoC::setCurrentTaskId);

            Optional.ofNullable(resourceSoCDTO.getArchitecture()).ifPresent(resourceSoC::setArchitecture);
            Optional.of(resourceSoCDTO.getCpuCores()).ifPresent(resourceSoC::setCpuCores);
            Optional.of(resourceSoCDTO.getGpuCores()).ifPresent(resourceSoC::setGpuCores);
            Optional.of(resourceSoCDTO.getCpuBaseFrequency()).ifPresent(resourceSoC::setCpuBaseFrequency);
            Optional.of(resourceSoCDTO.getCpuMaxFrequency()).ifPresent(resourceSoC::setCpuMaxFrequency);
            Optional.of(resourceSoCDTO.getGpuBaseFrequency()).ifPresent(resourceSoC::setGpuBaseFrequency);
            Optional.of(resourceSoCDTO.getGpuMaxFrequency()).ifPresent(resourceSoC::setGpuMaxFrequency);
            Optional.of(resourceSoCDTO.getTdp()).ifPresent(resourceSoC::setTdp);

            resourceSoC = resourceRepository.save(resourceSoC);

            resourceMessageHandler.sendUpdateResourceMessage(getResourceMessageDTO(resourceSoC));

            return getResourceDTO(resourceSoC);

        } else {
            return null;
        }
    }

    public ResourceDTO updateIsAvailable(String id, boolean available) {
        Optional<Resource> resource = resourceRepository.findById(id);

        if(resource.isEmpty()) {
            return null;
        }

        Resource retResource = resource.get();

        retResource.setIsAvailable(available);
        retResource = resourceRepository.save(retResource);

        switch (retResource) {
            case ResourceCPU resourceCPU -> {
                resourceMessageHandler.sendUpdateResourceMessage(getResourceMessageDTO(resourceCPU));
                return getResourceDTO(resourceCPU);
            }
            case ResourceGPU resourceGPU -> {
                resourceMessageHandler.sendUpdateResourceMessage(getResourceMessageDTO(resourceGPU));
                return getResourceDTO(resourceGPU);
            }
            case ResourceSoC resourceSoC -> {
                resourceMessageHandler.sendUpdateResourceMessage(getResourceMessageDTO(resourceSoC));
                return getResourceDTO(resourceSoC);
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Gets a ResourceMessageDTO from a Resource.
     * @param resource The Resource to convert.
     * @return The resulting ResourceMessageDTO.
     */
    public ResourceMessageDTO getResourceMessageDTO(Resource resource) {
        ResourceMessageDTO resourceMessageDTO = new ResourceMessageDTO();

        Optional.ofNullable(resource.getId()).ifPresent(resourceMessageDTO::setId);
        Optional.ofNullable(resource.getName()).ifPresent(resourceMessageDTO::setName);
        Optional.ofNullable(resource.getAvailability()).ifPresent(resourceMessageDTO::setAvailability);
        Optional.of(resource.getKWh()).ifPresent(resourceMessageDTO::setKWh);
        Optional.ofNullable(resource.getMemberEmail()).ifPresent(resourceMessageDTO::setMemberEmail);
        Optional.ofNullable(resource.getIsAvailable()).ifPresent(resourceMessageDTO::setIsAvailable);
        Optional.ofNullable(resource.getCurrentTaskId()).ifPresent(resourceMessageDTO::setCurrentTaskId);
        if (resource instanceof ResourceCPU) {
            Optional.of(((ResourceCPU) resource).getSingleCoreScore()).ifPresent(resourceMessageDTO::setSingleCoreScore);
            Optional.of(((ResourceCPU) resource).getMulticoreScore()).ifPresent(resourceMessageDTO::setMulticoreScore);
        } else if (resource instanceof ResourceGPU) {
            Optional.of(((ResourceGPU) resource).getOpenclScore()).ifPresent(resourceMessageDTO::setOpenclScore);
            Optional.of(((ResourceGPU) resource).getVulkanScore()).ifPresent(resourceMessageDTO::setVulkanScore);
            Optional.of(((ResourceGPU) resource).getCudaScore()).ifPresent(resourceMessageDTO::setCudaScore);
        }

        return resourceMessageDTO;
    }

    public ScoreMessageDTO getScoreMessageDTO(Resource resource) {
        ScoreMessageDTO scoreMessageDTO = new ScoreMessageDTO();
        scoreMessageDTO.setResourceName(resource.getName());
        scoreMessageDTO.setResourceType(resource.getType());
        return scoreMessageDTO;
    }

    public ScoreMessageDTO getScoreMessageDTO(ResourceDTO resourceDTO) {
        ScoreMessageDTO scoreMessageDTO = new ScoreMessageDTO();
        scoreMessageDTO.setResourceName(resourceDTO.getName());
        scoreMessageDTO.setResourceType(resourceDTO.getType());
        return scoreMessageDTO;
    }

    /**
     * Finds resources based on the provided parameters.
     * @param name The name of the resource.
     * @param type The type of the resource.
     * @param greenEnergyType The green energy type of the resource.
     * @param from The start date of the availability of the resource.
     * @param to The end date of the availability of the resource.
     * @param kWh The kWh of the resource.
     * @param memberMail The member mail of the resource.
     * @param isAvailable The availability of the resource.
     * @return A list of resources that match the provided parameters.
     */
    public List<Resource> findResources(String name, String type, String greenEnergyType, LocalDateTime from, LocalDateTime to, Double kWh, String memberMail, Boolean isAvailable) {
        Query query = new Query();

        query.addCriteria(Criteria.where("type").is(type));

        // If available is not provided, only return available resources
        query.addCriteria(Criteria.where("isAvailable").is(Objects.requireNonNullElse(isAvailable, true)));

        // Add conditions based on parameters provided
        if (name != null) {
            query.addCriteria(Criteria.where("name").is(name));
        }

        if (greenEnergyType != null) {
            query.addCriteria(Criteria.where("greenEnergyType").is(greenEnergyType));
        }
        if (from != null) {
            query.addCriteria(Criteria.where("availability.availableFrom").gte(from));
        }
        if (to != null) {
            query.addCriteria(Criteria.where("availability.availableUntil").lte(to));
        }
        if (kWh != null) {
            query.addCriteria(Criteria.where("kWh").lte(kWh));
        }
        if (memberMail != null) {
            query.addCriteria(Criteria.where("memberMail").is(memberMail));
        }

        LOGGER.info("\n{}\n", query);

        List<Resource> resources = mongoTemplate.find(query, Resource.class, mongoTemplate.getCollectionName(Resource.class));

        LOGGER.info("\nResources: {}\n", resources);

        return resources;
    }
}
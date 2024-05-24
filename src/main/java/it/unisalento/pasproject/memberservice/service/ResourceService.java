package it.unisalento.pasproject.memberservice.service;

import it.unisalento.pasproject.memberservice.domain.*;
import it.unisalento.pasproject.memberservice.dto.*;
import it.unisalento.pasproject.memberservice.exceptions.BadFormatAvailabilityException;
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
    private final MongoTemplate mongoTemplate;

    private final ResourceDTOFactory resourceDTOFactory;

    private final ResourceFactory resourceFactory;

    private final int AVAILABILITY_TRESHOLD = 30;

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class);

    /**
     * Constructor for the ResourceService.
     * @param mongoTemplate The MongoTemplate to be used for MongoDB operations.
     */
    @Autowired
    public ResourceService(MongoTemplate mongoTemplate) {
        this.resourceFactory = new ResourceFactory();
        this.resourceDTOFactory = new ResourceDTOFactory();
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Gets a ResourceCPU from a ResourceCpuDTO.
     * @param resourceCpuDTO The ResourceCpuDTO to convert.
     * @return The resulting ResourceCPU.
     */
    public ResourceCPU getResourceCPU(ResourceCpuDTO resourceCpuDTO) {
        ResourceCPU resourceCPU = (ResourceCPU) resourceFactory.getResourceType(ResourceFactory.ResourceType.CPU);

        Optional.ofNullable(resourceCpuDTO.getName()).ifPresent(resourceCPU::setName);
        Optional.ofNullable(resourceCpuDTO.getType()).ifPresent(resourceCPU::setType);
        Optional.ofNullable(resourceCpuDTO.getBrand()).ifPresent(resourceCPU::setBrand);
        Optional.ofNullable(resourceCpuDTO.getModel()).ifPresent(resourceCPU::setModel);
        Optional.ofNullable(resourceCpuDTO.getGreenEnergyType()).ifPresent(resourceCPU::setGreenEnergyType);
        Optional.ofNullable(resourceCpuDTO.getAvailability()).ifPresent(resourceCPU::setAvailability);
        Optional.of(resourceCpuDTO.getKWh()).ifPresent(resourceCPU::setKWh);
        Optional.ofNullable(resourceCpuDTO.getMemberEmail()).ifPresent(resourceCPU::setMemberEmail);
        Optional.ofNullable(resourceCpuDTO.getIsAvailable()).ifPresent(resourceCPU::setIsAvailable);
        Optional.ofNullable(resourceCpuDTO.getCurrentTaskId()).ifPresent(resourceCPU::setCurrentTaskId);
        Optional.ofNullable(resourceCpuDTO.getArchitecture()).ifPresent(resourceCPU::setArchitecture);
        Optional.of(resourceCpuDTO.getCores()).ifPresent(resourceCPU::setCores);
        Optional.of(resourceCpuDTO.getThreads()).ifPresent(resourceCPU::setThreads);
        Optional.of(resourceCpuDTO.getBaseFrequency()).ifPresent(resourceCPU::setBaseFrequency);
        Optional.of(resourceCpuDTO.getMaxFrequency()).ifPresent(resourceCPU::setMaxFrequency);
        Optional.of(resourceCpuDTO.getCacheSize()).ifPresent(resourceCPU::setCacheSize);
        Optional.of(resourceCpuDTO.getTdp()).ifPresent(resourceCPU::setTdp);
        Optional.of(resourceCpuDTO.isHyperThreading()).ifPresent(resourceCPU::setHyperThreading);
        Optional.of(resourceCpuDTO.isOverclockingSupport()).ifPresent(resourceCPU::setOverclockingSupport);
        Optional.of(resourceCpuDTO.getSingleCoreScore()).ifPresent(resourceCPU::setSingleCoreScore);
        Optional.of(resourceCpuDTO.getMulticoreScore()).ifPresent(resourceCPU::setMulticoreScore);

        return resourceCPU;
    }

    /**
     * Gets a ResourceGPU from a ResourceGpuDTO.
     * @param resourceGpuDTO The ResourceGpuDTO to convert.
     * @return The resulting ResourceGPU.
     */
    public ResourceGPU getResourceGPU(ResourceGpuDTO resourceGpuDTO) {
        ResourceGPU resourceGPU = (ResourceGPU) resourceFactory.getResourceType(ResourceFactory.ResourceType.GPU);

        Optional.ofNullable(resourceGpuDTO.getName()).ifPresent(resourceGPU::setName);
        Optional.ofNullable(resourceGpuDTO.getType()).ifPresent(resourceGPU::setType);
        Optional.ofNullable(resourceGpuDTO.getBrand()).ifPresent(resourceGPU::setBrand);
        Optional.ofNullable(resourceGpuDTO.getModel()).ifPresent(resourceGPU::setModel);
        Optional.ofNullable(resourceGpuDTO.getGreenEnergyType()).ifPresent(resourceGPU::setGreenEnergyType);
        Optional.ofNullable(resourceGpuDTO.getAvailability()).ifPresent(resourceGPU::setAvailability);
        Optional.of(resourceGpuDTO.getKWh()).ifPresent(resourceGPU::setKWh);
        Optional.ofNullable(resourceGpuDTO.getMemberEmail()).ifPresent(resourceGPU::setMemberEmail);
        Optional.ofNullable(resourceGpuDTO.getIsAvailable()).ifPresent(resourceGPU::setIsAvailable);
        Optional.ofNullable(resourceGpuDTO.getCurrentTaskId()).ifPresent(resourceGPU::setCurrentTaskId);
        Optional.ofNullable(resourceGpuDTO.getArchitecture()).ifPresent(resourceGPU::setArchitecture);
        Optional.ofNullable(resourceGpuDTO.getVramType()).ifPresent(resourceGPU::setVramType);
        Optional.of(resourceGpuDTO.getVramSize()).ifPresent(resourceGPU::setVramSize);
        Optional.of(resourceGpuDTO.getCoreClock()).ifPresent(resourceGPU::setCoreClock);
        Optional.of(resourceGpuDTO.getBoostClock()).ifPresent(resourceGPU::setBoostClock);
        Optional.ofNullable(resourceGpuDTO.getMemoryClock()).ifPresent(resourceGPU::setMemoryClock);
        Optional.of(resourceGpuDTO.getTdp()).ifPresent(resourceGPU::setTdp);
        Optional.of(resourceGpuDTO.isRayTracingSupport()).ifPresent(resourceGPU::setRayTracingSupport);
        Optional.of(resourceGpuDTO.isDlssSupport()).ifPresent(resourceGPU::setDlssSupport);
        Optional.of(resourceGpuDTO.getOpenclScore()).ifPresent(resourceGPU::setOpenclScore);
        Optional.of(resourceGpuDTO.getVulkanScore()).ifPresent(resourceGPU::setVulkanScore);
        Optional.of(resourceGpuDTO.getCudaScore()).ifPresent(resourceGPU::setCudaScore);

        return resourceGPU;
    }

    //TODO: VEDERE SE QUESTO METODO COMPATTO PER UPDATE FUNZIONA, DA PROVARE
    public Resource getResource(ResourceDTO resourceDTO) {
        Resource resource;

        if (resourceDTO instanceof ResourceCpuDTO) {
            ResourceCPU resourceCPU = (ResourceCPU) resourceFactory.getResourceType(ResourceFactory.ResourceType.CPU);
            ResourceCpuDTO resourceCpuDTO = (ResourceCpuDTO) resourceDTO;

            // Set common properties
            Optional.ofNullable(resourceCpuDTO.getName()).ifPresent(resourceCPU::setName);
            Optional.ofNullable(resourceCpuDTO.getType()).ifPresent(resourceCPU::setType);
            Optional.ofNullable(resourceCpuDTO.getBrand()).ifPresent(resourceCPU::setBrand);
            Optional.ofNullable(resourceCpuDTO.getModel()).ifPresent(resourceCPU::setModel);
            Optional.ofNullable(resourceCpuDTO.getGreenEnergyType()).ifPresent(resourceCPU::setGreenEnergyType);
            checkTimeDifference(resourceDTO.getAvailability());
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
            Optional.of(resourceCpuDTO.getSingleCoreScore()).ifPresent(resourceCPU::setSingleCoreScore);
            Optional.of(resourceCpuDTO.getMulticoreScore()).ifPresent(resourceCPU::setMulticoreScore);

            resource = resourceCPU;
        } else if (resourceDTO instanceof ResourceGpuDTO) {
            ResourceGPU resourceGPU = (ResourceGPU) resourceFactory.getResourceType(ResourceFactory.ResourceType.GPU);
            ResourceGpuDTO resourceGpuDTO = (ResourceGpuDTO) resourceDTO;

            // Set common properties
            Optional.ofNullable(resourceGpuDTO.getName()).ifPresent(resourceGPU::setName);
            Optional.ofNullable(resourceGpuDTO.getType()).ifPresent(resourceGPU::setType);
            Optional.ofNullable(resourceGpuDTO.getBrand()).ifPresent(resourceGPU::setBrand);
            Optional.ofNullable(resourceGpuDTO.getModel()).ifPresent(resourceGPU::setModel);
            Optional.ofNullable(resourceGpuDTO.getGreenEnergyType()).ifPresent(resourceGPU::setGreenEnergyType);
            checkTimeDifference(resourceDTO.getAvailability());
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
            Optional.of(resourceGpuDTO.getOpenclScore()).ifPresent(resourceGPU::setOpenclScore);
            Optional.of(resourceGpuDTO.getVulkanScore()).ifPresent(resourceGPU::setVulkanScore);
            Optional.of(resourceGpuDTO.getCudaScore()).ifPresent(resourceGPU::setCudaScore);

            resource = resourceGPU;
        } else {
            throw new IllegalArgumentException("Unsupported resource type: " + resourceDTO.getClass());
        }

        return resource;
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

    /**
     * Gets a ResourceCpuDTO from a ResourceCPU.
     * @param resourceCPU The ResourceCPU to convert.
     * @return The resulting ResourceCpuDTO.
     */
    public ResourceCpuDTO getResourceCpuDTO(ResourceCPU resourceCPU) {
        ResourceCpuDTO dto = (ResourceCpuDTO) resourceDTOFactory.getResourceDTOType(ResourceDTOFactory.ResourceDTOType.CPU);

        Optional.ofNullable(resourceCPU.getId()).ifPresent(dto::setId);
        Optional.ofNullable(resourceCPU.getName()).ifPresent(dto::setName);
        Optional.ofNullable(resourceCPU.getType()).ifPresent(dto::setType);
        Optional.ofNullable(resourceCPU.getBrand()).ifPresent(dto::setBrand);
        Optional.ofNullable(resourceCPU.getModel()).ifPresent(dto::setModel);
        Optional.ofNullable(resourceCPU.getGreenEnergyType()).ifPresent(dto::setGreenEnergyType);
        Optional.ofNullable(resourceCPU.getAvailability()).ifPresent(dto::setAvailability);
        Optional.of(resourceCPU.getKWh()).ifPresent(dto::setKWh);
        Optional.ofNullable(resourceCPU.getMemberEmail()).ifPresent(dto::setMemberEmail);
        Optional.ofNullable(resourceCPU.getIsAvailable()).ifPresent(dto::setIsAvailable);
        Optional.ofNullable(resourceCPU.getCurrentTaskId()).ifPresent(dto::setCurrentTaskId);
        Optional.ofNullable(resourceCPU.getArchitecture()).ifPresent(dto::setArchitecture);
        Optional.of(resourceCPU.getCores()).ifPresent(dto::setCores);
        Optional.of(resourceCPU.getThreads()).ifPresent(dto::setThreads);
        Optional.of(resourceCPU.getBaseFrequency()).ifPresent(dto::setBaseFrequency);
        Optional.of(resourceCPU.getMaxFrequency()).ifPresent(dto::setMaxFrequency);
        Optional.of(resourceCPU.getCacheSize()).ifPresent(dto::setCacheSize);
        Optional.of(resourceCPU.getTdp()).ifPresent(dto::setTdp);
        Optional.of(resourceCPU.isHyperThreading()).ifPresent(dto::setHyperThreading);
        Optional.of(resourceCPU.isOverclockingSupport()).ifPresent(dto::setOverclockingSupport);
        Optional.of(resourceCPU.getSingleCoreScore()).ifPresent(dto::setSingleCoreScore);
        Optional.of(resourceCPU.getMulticoreScore()).ifPresent(dto::setMulticoreScore);

        return dto;
    }

    /**
     * Gets a ResourceGpuDTO from a ResourceGPU.
     * @param resourceGPU The ResourceGPU to convert.
     * @return The resulting ResourceGpuDTO.
     */
    public ResourceGpuDTO getResourceGpuDTO(ResourceGPU resourceGPU) {
        ResourceGpuDTO dto = (ResourceGpuDTO) resourceDTOFactory.getResourceDTOType(ResourceDTOFactory.ResourceDTOType.GPU);

        Optional.ofNullable(resourceGPU.getId()).ifPresent(dto::setId);
        Optional.ofNullable(resourceGPU.getName()).ifPresent(dto::setName);
        Optional.ofNullable(resourceGPU.getType()).ifPresent(dto::setType);
        Optional.ofNullable(resourceGPU.getBrand()).ifPresent(dto::setBrand);
        Optional.ofNullable(resourceGPU.getModel()).ifPresent(dto::setModel);
        Optional.ofNullable(resourceGPU.getGreenEnergyType()).ifPresent(dto::setGreenEnergyType);
        Optional.ofNullable(resourceGPU.getAvailability()).ifPresent(dto::setAvailability);
        Optional.of(resourceGPU.getKWh()).ifPresent(dto::setKWh);
        Optional.ofNullable(resourceGPU.getMemberEmail()).ifPresent(dto::setMemberEmail);
        Optional.ofNullable(resourceGPU.getIsAvailable()).ifPresent(dto::setIsAvailable);
        Optional.ofNullable(resourceGPU.getCurrentTaskId()).ifPresent(dto::setCurrentTaskId);
        Optional.ofNullable(resourceGPU.getArchitecture()).ifPresent(dto::setArchitecture);
        Optional.ofNullable(resourceGPU.getVramType()).ifPresent(dto::setVramType);
        Optional.of(resourceGPU.getVramSize()).ifPresent(dto::setVramSize);
        Optional.of(resourceGPU.getCoreClock()).ifPresent(dto::setCoreClock);
        Optional.of(resourceGPU.getBoostClock()).ifPresent(dto::setBoostClock);
        Optional.ofNullable(resourceGPU.getMemoryClock()).ifPresent(dto::setMemoryClock);
        Optional.of(resourceGPU.getTdp()).ifPresent(dto::setTdp);
        Optional.of(resourceGPU.isRayTracingSupport()).ifPresent(dto::setRayTracingSupport);
        Optional.of(resourceGPU.isDlssSupport()).ifPresent(dto::setDlssSupport);
        Optional.of(resourceGPU.getOpenclScore()).ifPresent(dto::setOpenclScore);
        Optional.of(resourceGPU.getVulkanScore()).ifPresent(dto::setVulkanScore);
        Optional.of(resourceGPU.getCudaScore()).ifPresent(dto::setCudaScore);

        return dto;
    }

    //TODO: VEDERE SE QUESTO METODO COMPATTO PER UPDATE FUNZIONA, DA PROVARE
    public ResourceDTO getResourceDTO(Resource resource) {
        ResourceDTO dto;

        if (resource instanceof ResourceCPU) {
            ResourceCPU resourceCPU = (ResourceCPU) resource;
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

            dto = resourceCpuDTO;
        } else if (resource instanceof ResourceGPU) {
            ResourceGPU resourceGPU = (ResourceGPU) resource;
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

            dto = resourceGpuDTO;
        } else {
            throw new IllegalArgumentException("Unsupported resource type: " + resource.getClass());
        }

        return dto;
    }

    /**
     * Updates a Resource with data from a ResourceDTO.
     * @param resource The Resource to update.
     * @param resourceDTO The ResourceDTO to get the data from.
     * @return The updated Resource.
     */
    public Resource updateResource(Resource resource, ResourceDTO resourceDTO) {
        Optional.ofNullable(resourceDTO.getName()).ifPresent(resource::setName);
        Optional.ofNullable(resourceDTO.getType()).ifPresent(resource::setType);
        Optional.ofNullable(resourceDTO.getBrand()).ifPresent(resource::setBrand);
        Optional.ofNullable(resourceDTO.getModel()).ifPresent(resource::setModel);
        Optional.ofNullable(resourceDTO.getGreenEnergyType()).ifPresent(resource::setGreenEnergyType);
        Optional.ofNullable(resourceDTO.getAvailability()).ifPresent(resource::setAvailability);
        Optional.of(resourceDTO.getKWh()).ifPresent(resource::setKWh);
        Optional.ofNullable(resourceDTO.getMemberEmail()).ifPresent(resource::setMemberEmail);
        Optional.ofNullable(resourceDTO.getIsAvailable()).ifPresent(resource::setIsAvailable);
        Optional.ofNullable(resourceDTO.getCurrentTaskId()).ifPresent(resource::setCurrentTaskId);

        return resource;
    }

    /**
     * Updates a ResourceCPU with data from a ResourceCpuDTO.
     * @param resourceCPU The ResourceCPU to update.
     * @param resourceCpuDTO The ResourceCpuDTO to get the data from.
     * @return The updated ResourceCPU.
     */
    public ResourceCPU updateResourceCPU(ResourceCPU resourceCPU, ResourceCpuDTO resourceCpuDTO, boolean nameChanged) {
        Optional.ofNullable(resourceCpuDTO.getArchitecture()).ifPresent(resourceCPU::setArchitecture);
        Optional.of(resourceCpuDTO.getCores()).ifPresent(resourceCPU::setCores);
        Optional.of(resourceCpuDTO.getThreads()).ifPresent(resourceCPU::setThreads);
        Optional.of(resourceCpuDTO.getBaseFrequency()).ifPresent(resourceCPU::setBaseFrequency);
        Optional.of(resourceCpuDTO.getMaxFrequency()).ifPresent(resourceCPU::setMaxFrequency);
        Optional.of(resourceCpuDTO.getCacheSize()).ifPresent(resourceCPU::setCacheSize);
        Optional.of(resourceCpuDTO.getTdp()).ifPresent(resourceCPU::setTdp);
        Optional.of(resourceCpuDTO.isHyperThreading()).ifPresent(resourceCPU::setHyperThreading);
        Optional.of(resourceCpuDTO.isOverclockingSupport()).ifPresent(resourceCPU::setOverclockingSupport);

        if (nameChanged) {
            Optional.of(resourceCpuDTO.getSingleCoreScore()).ifPresent(resourceCPU::setSingleCoreScore);
            Optional.of(resourceCpuDTO.getMulticoreScore()).ifPresent(resourceCPU::setMulticoreScore);
        }

        return resourceCPU;
    }

    /**
     * Updates a ResourceGPU with data from a ResourceGpuDTO.
     * @param resourceGPU The ResourceGPU to update.
     * @param resourceGpuDTO The ResourceGpuDTO to get the data from.
     * @return The updated ResourceGPU.
     */
    public ResourceGPU updateResourceGPU(ResourceGPU resourceGPU, ResourceGpuDTO resourceGpuDTO, boolean nameChanged) {
        Optional.ofNullable(resourceGpuDTO.getArchitecture()).ifPresent(resourceGPU::setArchitecture);
        Optional.ofNullable(resourceGpuDTO.getVramType()).ifPresent(resourceGPU::setVramType);
        Optional.of(resourceGpuDTO.getVramSize()).ifPresent(resourceGPU::setVramSize);
        Optional.of(resourceGpuDTO.getCoreClock()).ifPresent(resourceGPU::setCoreClock);
        Optional.of(resourceGpuDTO.getBoostClock()).ifPresent(resourceGPU::setBoostClock);
        Optional.ofNullable(resourceGpuDTO.getMemoryClock()).ifPresent(resourceGPU::setMemoryClock);
        Optional.of(resourceGpuDTO.getTdp()).ifPresent(resourceGPU::setTdp);
        Optional.of(resourceGpuDTO.isRayTracingSupport()).ifPresent(resourceGPU::setRayTracingSupport);
        Optional.of(resourceGpuDTO.isDlssSupport()).ifPresent(resourceGPU::setDlssSupport);

        if (nameChanged) {
            Optional.of(resourceGpuDTO.getOpenclScore()).ifPresent(resourceGPU::setOpenclScore);
            Optional.of(resourceGpuDTO.getVulkanScore()).ifPresent(resourceGPU::setVulkanScore);
            Optional.of(resourceGpuDTO.getCudaScore()).ifPresent(resourceGPU::setCudaScore);
        }

        return resourceGPU;
    }

    //TODO: VEDERE SE QUESTO METODO COMPATTO PER UPDATE FUNZIONA, DA PROVARE
    public Resource updateResource(Resource resource, ResourceDTO resourceDTO, boolean nameChanged) {
        Optional.ofNullable(resourceDTO.getName()).ifPresent(resource::setName);
        Optional.ofNullable(resourceDTO.getType()).ifPresent(resource::setType);
        Optional.ofNullable(resourceDTO.getBrand()).ifPresent(resource::setBrand);
        Optional.ofNullable(resourceDTO.getModel()).ifPresent(resource::setModel);
        Optional.ofNullable(resourceDTO.getGreenEnergyType()).ifPresent(resource::setGreenEnergyType);
        checkTimeDifference(resourceDTO.getAvailability());
        Optional.ofNullable(resourceDTO.getAvailability()).ifPresent(resource::setAvailability);
        Optional.of(resourceDTO.getKWh()).ifPresent(resource::setKWh);
        Optional.ofNullable(resourceDTO.getMemberEmail()).ifPresent(resource::setMemberEmail);
        Optional.ofNullable(resourceDTO.getIsAvailable()).ifPresent(resource::setIsAvailable);
        Optional.ofNullable(resourceDTO.getCurrentTaskId()).ifPresent(resource::setCurrentTaskId);

        if (resource instanceof ResourceCPU && resourceDTO instanceof ResourceCpuDTO) {
            ResourceCPU resourceCPU = (ResourceCPU) resource;
            ResourceCpuDTO resourceCpuDTO = (ResourceCpuDTO) resourceDTO;
            Optional.ofNullable(resourceCpuDTO.getArchitecture()).ifPresent(resourceCPU::setArchitecture);
            Optional.of(resourceCpuDTO.getCores()).ifPresent(resourceCPU::setCores);
            Optional.of(resourceCpuDTO.getThreads()).ifPresent(resourceCPU::setThreads);
            Optional.of(resourceCpuDTO.getBaseFrequency()).ifPresent(resourceCPU::setBaseFrequency);
            Optional.of(resourceCpuDTO.getMaxFrequency()).ifPresent(resourceCPU::setMaxFrequency);
            Optional.of(resourceCpuDTO.getCacheSize()).ifPresent(resourceCPU::setCacheSize);
            Optional.of(resourceCpuDTO.getTdp()).ifPresent(resourceCPU::setTdp);
            Optional.of(resourceCpuDTO.isHyperThreading()).ifPresent(resourceCPU::setHyperThreading);
            Optional.of(resourceCpuDTO.isOverclockingSupport()).ifPresent(resourceCPU::setOverclockingSupport);

            if (nameChanged) {
                Optional.of(resourceCpuDTO.getSingleCoreScore()).ifPresent(resourceCPU::setSingleCoreScore);
                Optional.of(resourceCpuDTO.getMulticoreScore()).ifPresent(resourceCPU::setMulticoreScore);
            }
        } else if (resource instanceof ResourceGPU && resourceDTO instanceof ResourceGpuDTO) {
            ResourceGPU resourceGPU = (ResourceGPU) resource;
            ResourceGpuDTO resourceGpuDTO = (ResourceGpuDTO) resourceDTO;
            Optional.ofNullable(resourceGpuDTO.getArchitecture()).ifPresent(resourceGPU::setArchitecture);
            Optional.ofNullable(resourceGpuDTO.getVramType()).ifPresent(resourceGPU::setVramType);
            Optional.of(resourceGpuDTO.getVramSize()).ifPresent(resourceGPU::setVramSize);
            Optional.of(resourceGpuDTO.getCoreClock()).ifPresent(resourceGPU::setCoreClock);
            Optional.of(resourceGpuDTO.getBoostClock()).ifPresent(resourceGPU::setBoostClock);
            Optional.ofNullable(resourceGpuDTO.getMemoryClock()).ifPresent(resourceGPU::setMemoryClock);
            Optional.of(resourceGpuDTO.getTdp()).ifPresent(resourceGPU::setTdp);
            Optional.of(resourceGpuDTO.isRayTracingSupport()).ifPresent(resourceGPU::setRayTracingSupport);
            Optional.of(resourceGpuDTO.isDlssSupport()).ifPresent(resourceGPU::setDlssSupport);

            if (nameChanged) {
                Optional.of(resourceGpuDTO.getOpenclScore()).ifPresent(resourceGPU::setOpenclScore);
                Optional.of(resourceGpuDTO.getVulkanScore()).ifPresent(resourceGPU::setVulkanScore);
                Optional.of(resourceGpuDTO.getCudaScore()).ifPresent(resourceGPU::setCudaScore);
            }
        }

        return resource;
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
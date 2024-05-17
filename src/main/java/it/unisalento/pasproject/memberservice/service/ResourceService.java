package it.unisalento.pasproject.memberservice.service;

import it.unisalento.pasproject.memberservice.domain.Resource;
import it.unisalento.pasproject.memberservice.domain.ResourceCPU;
import it.unisalento.pasproject.memberservice.domain.ResourceFactory;
import it.unisalento.pasproject.memberservice.domain.ResourceGPU;
import it.unisalento.pasproject.memberservice.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        Optional.ofNullable(resourceCpuDTO.getAssignedUser()).ifPresent(resourceCPU::setAssignedUser);
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
        Optional.ofNullable(resourceGpuDTO.getAssignedUser()).ifPresent(resourceGPU::setAssignedUser);
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
        Optional.ofNullable(resourceCPU.getAssignedUser()).ifPresent(dto::setAssignedUser);
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
        Optional.ofNullable(resourceGPU.getAssignedUser()).ifPresent(dto::setAssignedUser);
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
        Optional.ofNullable(resourceDTO.getAssignedUser()).ifPresent(resource::setAssignedUser);

        return resource;
    }

    /**
     * Updates a ResourceCPU with data from a ResourceCpuDTO.
     * @param resourceCPU The ResourceCPU to update.
     * @param resourceCpuDTO The ResourceCpuDTO to get the data from.
     * @return The updated ResourceCPU.
     */
    public ResourceCPU updateResourceCPU(ResourceCPU resourceCPU, ResourceCpuDTO resourceCpuDTO) {
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
     * Updates a ResourceGPU with data from a ResourceGpuDTO.
     * @param resourceGPU The ResourceGPU to update.
     * @param resourceGpuDTO The ResourceGpuDTO to get the data from.
     * @return The updated ResourceGPU.
     */
    public ResourceGPU updateResourceGPU(ResourceGPU resourceGPU, ResourceGpuDTO resourceGpuDTO) {
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

    /**
     * Gets a ResourceMessageDTO from a Resource.
     * @param resource The Resource to convert.
     * @return The resulting ResourceMessageDTO.
     */
    public ResourceMessageDTO getResourceMessageDTO(Resource resource) {
        ResourceMessageDTO resourceMessageDTO = new ResourceMessageDTO();

        Optional.ofNullable(resource.getId()).ifPresent(resourceMessageDTO::setId);
        Optional.ofNullable(resource.getAvailability()).ifPresent(resourceMessageDTO::setAvailability);
        Optional.of(resource.getKWh()).ifPresent(resourceMessageDTO::setKWh);
        Optional.ofNullable(resource.getMemberEmail()).ifPresent(resourceMessageDTO::setMemberEmail);
        Optional.ofNullable(resource.getIsAvailable()).ifPresent(resourceMessageDTO::setIsAvailable);
        Optional.ofNullable(resource.getAssignedUser()).ifPresent(resourceMessageDTO::setAssignedUser);
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
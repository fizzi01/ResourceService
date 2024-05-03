package it.unisalento.pasproject.memberservice.service;

import it.unisalento.pasproject.memberservice.domain.Resource;
import it.unisalento.pasproject.memberservice.domain.ResourceCPU;
import it.unisalento.pasproject.memberservice.domain.ResourceFactory;
import it.unisalento.pasproject.memberservice.domain.ResourceGPU;
import it.unisalento.pasproject.memberservice.dto.ResourceCpuDTO;
import it.unisalento.pasproject.memberservice.dto.ResourceDTO;
import it.unisalento.pasproject.memberservice.dto.ResourceDTOFactory;
import it.unisalento.pasproject.memberservice.dto.ResourceGpuDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ResourceService {
    private final MongoTemplate mongoTemplate;

    private final ResourceDTOFactory resourceDTOFactory;

    private final ResourceFactory resourceFactory;

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class);

    @Autowired
    public ResourceService(MongoTemplate mongoTemplate) {
        this.resourceFactory = new ResourceFactory();
        this.resourceDTOFactory = new ResourceDTOFactory();
        this.mongoTemplate = mongoTemplate;
    }

    public ResourceCPU getResourceCPU(ResourceCpuDTO resourceCpuDTO) {
        ResourceCPU resourceCPU = (ResourceCPU) resourceFactory.getResourceType(ResourceFactory.ResourceType.CPU);

        resourceCPU.setName(resourceCpuDTO.getName());
        resourceCPU.setType(resourceCpuDTO.getType());
        resourceCPU.setBrand(resourceCpuDTO.getBrand());
        resourceCPU.setModel(resourceCpuDTO.getModel());
        resourceCPU.setGreenEnergyType(resourceCpuDTO.getGreenEnergyType());
        resourceCPU.setAvailableHours(resourceCpuDTO.getAvailableHours());
        resourceCPU.setKWh(resourceCpuDTO.getKWh());
        resourceCPU.setMemberEmail(resourceCpuDTO.getMemberEmail());
        resourceCPU.setIsAvailable(resourceCpuDTO.getIsAvailable());
        resourceCPU.setArchitecture(resourceCpuDTO.getArchitecture());
        resourceCPU.setCores(resourceCpuDTO.getCores());
        resourceCPU.setThreads(resourceCpuDTO.getThreads());
        resourceCPU.setBaseFrequency(resourceCpuDTO.getBaseFrequency());
        resourceCPU.setMaxFrequency(resourceCpuDTO.getMaxFrequency());
        resourceCPU.setCacheSize(resourceCpuDTO.getCacheSize());
        resourceCPU.setTdp(resourceCpuDTO.getTdp());
        resourceCPU.setHyperThreading(resourceCpuDTO.isHyperThreading());
        resourceCPU.setOverclockingSupport(resourceCpuDTO.isOverclockingSupport());
        resourceCPU.setSingleCoreScore(resourceCpuDTO.getSingleCoreScore());
        resourceCPU.setMulticoreScore(resourceCpuDTO.getMulticoreScore());

        return resourceCPU;
    }

    public ResourceGPU getResourceGPU(ResourceGpuDTO resourceGpuDTO) {
        ResourceGPU resourceGPU = (ResourceGPU) resourceFactory.getResourceType(ResourceFactory.ResourceType.GPU);

        resourceGPU.setName(resourceGpuDTO.getName());
        resourceGPU.setType(resourceGpuDTO.getType());
        resourceGPU.setBrand(resourceGpuDTO.getBrand());
        resourceGPU.setModel(resourceGpuDTO.getModel());
        resourceGPU.setGreenEnergyType(resourceGpuDTO.getGreenEnergyType());
        resourceGPU.setAvailableHours(resourceGpuDTO.getAvailableHours());
        resourceGPU.setKWh(resourceGpuDTO.getKWh());
        resourceGPU.setMemberEmail(resourceGpuDTO.getMemberEmail());
        resourceGPU.setIsAvailable(resourceGpuDTO.getIsAvailable());
        resourceGPU.setArchitecture(resourceGpuDTO.getArchitecture());
        resourceGPU.setVramType(resourceGpuDTO.getVramType());
        resourceGPU.setVramSize(resourceGpuDTO.getVramSize());
        resourceGPU.setCoreClock(resourceGpuDTO.getCoreClock());
        resourceGPU.setBoostClock(resourceGpuDTO.getBoostClock());
        resourceGPU.setMemoryClock(resourceGpuDTO.getMemoryClock());
        resourceGPU.setTdp(resourceGpuDTO.getTdp());
        resourceGPU.setRayTracingSupport(resourceGpuDTO.isRayTracingSupport());
        resourceGPU.setDlssSupport(resourceGpuDTO.isDlssSupport());
        resourceGPU.setOpenclScore(resourceGpuDTO.getOpenclScore());
        resourceGPU.setVulkanScore(resourceGpuDTO.getVulkanScore());
        resourceGPU.setCudaScore(resourceGpuDTO.getCudaScore());

        return resourceGPU;
    }

    public ResourceCpuDTO getResourceCpuDTO(ResourceCPU resourceCPU) {
        ResourceCpuDTO dto = (ResourceCpuDTO) resourceDTOFactory.getResourceDTOType(ResourceDTOFactory.ResourceDTOType.CPU);

        dto.setId(resourceCPU.getId());
        dto.setName(resourceCPU.getName());
        dto.setType(resourceCPU.getType());
        dto.setBrand(resourceCPU.getBrand());
        dto.setModel(resourceCPU.getModel());
        dto.setGreenEnergyType(resourceCPU.getGreenEnergyType());
        dto.setAvailableHours(resourceCPU.getAvailableHours());
        dto.setKWh(resourceCPU.getKWh());
        dto.setMemberEmail(resourceCPU.getMemberEmail());
        dto.setIsAvailable(resourceCPU.getIsAvailable());
        dto.setAssignedUser(resourceCPU.getAssignedUser());
        dto.setArchitecture(resourceCPU.getArchitecture());
        dto.setCores(resourceCPU.getCores());
        dto.setThreads(resourceCPU.getThreads());
        dto.setBaseFrequency(resourceCPU.getBaseFrequency());
        dto.setMaxFrequency(resourceCPU.getMaxFrequency());
        dto.setCacheSize(resourceCPU.getCacheSize());
        dto.setTdp(resourceCPU.getTdp());
        dto.setHyperThreading(resourceCPU.isHyperThreading());
        dto.setOverclockingSupport(resourceCPU.isOverclockingSupport());
        dto.setSingleCoreScore(resourceCPU.getSingleCoreScore());
        dto.setMulticoreScore(resourceCPU.getMulticoreScore());

        return dto;
    }

    public ResourceGpuDTO getResourceGpuDTO(ResourceGPU resourceGPU) {
        ResourceGpuDTO dto = (ResourceGpuDTO) resourceDTOFactory.getResourceDTOType(ResourceDTOFactory.ResourceDTOType.GPU);

        dto.setId(resourceGPU.getId());
        dto.setName(resourceGPU.getName());
        dto.setType(resourceGPU.getType());
        dto.setBrand(resourceGPU.getBrand());
        dto.setModel(resourceGPU.getModel());
        dto.setGreenEnergyType(resourceGPU.getGreenEnergyType());
        dto.setAvailableHours(resourceGPU.getAvailableHours());
        dto.setKWh(resourceGPU.getKWh());
        dto.setMemberEmail(resourceGPU.getMemberEmail());
        dto.setIsAvailable(resourceGPU.getIsAvailable());
        dto.setAssignedUser(resourceGPU.getAssignedUser());
        dto.setArchitecture(resourceGPU.getArchitecture());
        dto.setVramType(resourceGPU.getVramType());
        dto.setVramSize(resourceGPU.getVramSize());
        dto.setCoreClock(resourceGPU.getCoreClock());
        dto.setBoostClock(resourceGPU.getBoostClock());
        dto.setMemoryClock(resourceGPU.getMemoryClock());
        dto.setTdp(resourceGPU.getTdp());
        dto.setRayTracingSupport(resourceGPU.isRayTracingSupport());
        dto.setDlssSupport(resourceGPU.isDlssSupport());
        dto.setOpenclScore(resourceGPU.getOpenclScore());
        dto.setVulkanScore(resourceGPU.getVulkanScore());
        dto.setCudaScore(resourceGPU.getCudaScore());

        return dto;
    }

    public Resource updateResource(Resource resource, ResourceDTO resourceDTO) {
        Optional.ofNullable(resourceDTO.getName()).ifPresent(resource::setName);
        Optional.ofNullable(resourceDTO.getType()).ifPresent(resource::setType);
        Optional.ofNullable(resourceDTO.getBrand()).ifPresent(resource::setBrand);
        Optional.ofNullable(resourceDTO.getModel()).ifPresent(resource::setModel);
        Optional.ofNullable(resourceDTO.getGreenEnergyType()).ifPresent(resource::setGreenEnergyType);
        Optional.of(resourceDTO.getAvailableHours()).ifPresent(resource::setAvailableHours);
        Optional.of(resourceDTO.getKWh()).ifPresent(resource::setKWh);
        Optional.ofNullable(resourceDTO.getMemberEmail()).ifPresent(resource::setMemberEmail);
        Optional.ofNullable(resourceDTO.getIsAvailable()).ifPresent(resource::setIsAvailable);
        Optional.ofNullable(resourceDTO.getAssignedUser()).ifPresent(resource::setAssignedUser);

        return resource;
    }

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

    public List<Resource> findResources(String name, String type, String greenEnergyType, Integer availableHours, Double kWh, String memberMail, Boolean isAvailable) {
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
        if (availableHours != null) {
            query.addCriteria(Criteria.where("availableHours").is(availableHours));
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
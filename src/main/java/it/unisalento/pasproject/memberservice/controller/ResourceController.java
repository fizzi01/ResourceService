package it.unisalento.pasproject.memberservice.controller;

import it.unisalento.pasproject.memberservice.domain.Resource;
import it.unisalento.pasproject.memberservice.domain.ResourceCPU;
import it.unisalento.pasproject.memberservice.domain.ResourceGPU;
import it.unisalento.pasproject.memberservice.dto.*;
import it.unisalento.pasproject.memberservice.exceptions.ResourceNotFoundException;
import it.unisalento.pasproject.memberservice.repositories.ResourceRepository;
import it.unisalento.pasproject.memberservice.service.ResourceMessageHandler;
import it.unisalento.pasproject.memberservice.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.unisalento.pasproject.memberservice.security.SecurityConstants.ROLE_MEMBRO;

/**
 * ResourceController is a REST controller that provides endpoints for managing resources.
 * It uses the ResourceService for business logic and the ResourceRepository for data access.
 */
@RestController
@RequestMapping("/api/resource")
public class ResourceController {
    final ResourceRepository resourceRepository;

    private final ResourceService resourceService;

    private final ResourceMessageHandler resourceMessageHandler;

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceController.class);

    /**
     * Constructs a new ResourceController with the given ResourceRepository, ResourceService, and ResourceMessageHandler.
     *
     * @param resourceRepository the repository to use for data access
     * @param resourceService the service to use for business logic
     * @param resourceMessageHandler the message handler to use for sending resource messages
     */
    @Autowired
    public ResourceController(ResourceRepository resourceRepository, ResourceService resourceService, ResourceMessageHandler resourceMessageHandler) {
        this.resourceRepository = resourceRepository;
        this.resourceService = resourceService;
        this.resourceMessageHandler = resourceMessageHandler;
    }

    /**
     * Returns all resources.
     *
     * @return a ResourceListDTO containing all resources
     */
    @GetMapping(value="/find/all")
    //@Secured({ROLE_MEMBRO})
    public ResourceListDTO getAllResources() {
        ResourceListDTO resourceListDTO = new ResourceListDTO();
        List<ResourceDTO> list = new ArrayList<>();
        resourceListDTO.setResourcesList(list);

        List<Resource> resources = resourceRepository.findAll();

        for (Resource resource : resources){
            if (resource instanceof ResourceCPU) {
                list.add(resourceService.getResourceCpuDTO((ResourceCPU) resource));
            } else if (resource instanceof ResourceGPU) {
                list.add(resourceService.getResourceGpuDTO((ResourceGPU) resource));
            }
        }

        return resourceListDTO;
    }

    /**
     * Returns resources that match the given filter criteria.
     *
     * @param type the type of the resources to return
     * @param name the name of the resources to return
     * @param greenEnergyType the green energy type of the resources to return
     * @param from the start date of the resources to return
     * @param to the end date of the resources to return
     * @param kWh the kWh of the resources to return
     * @param memberMail the member mail of the resources to return
     * @param isAvailable whether the resources to return should be available
     * @return a ResourceListDTO containing the matching resources
     * @throws ResourceNotFoundException if no resources match the given criteria
     */
    @GetMapping("/find")
    //@Secured(ROLE_MEMBRO)
    public ResourceListDTO getByFilter(@RequestParam() String type,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) String greenEnergyType,
                                       @RequestParam(required = false) LocalDateTime from,
                                       @RequestParam(required = false) LocalDateTime to,
                                       @RequestParam(required = false) Double kWh,
                                       @RequestParam(required = false) String memberMail,
                                       @RequestParam(required = false) Boolean isAvailable) throws ResourceNotFoundException {
        ResourceListDTO resourceListDTO = new ResourceListDTO();
        List<ResourceDTO> list = new ArrayList<>();
        resourceListDTO.setResourcesList(list);

        List<Resource> resources = resourceService.findResources(name, type, greenEnergyType, from, to, kWh, memberMail, isAvailable);

        if (resources.isEmpty())
            throw new ResourceNotFoundException("No resources found with the given criteria.");

        for (Resource resource : resources){
            if (resource instanceof ResourceCPU) {
                list.add(resourceService.getResourceCpuDTO((ResourceCPU) resource));
            } else if (resource instanceof ResourceGPU) {
                list.add(resourceService.getResourceGpuDTO((ResourceGPU) resource));
            }
        }

        return resourceListDTO;
    }

    /**
     * Inserts a new resource.
     *
     * @param newResource the resource to insert
     * @return a ResponseEntity containing the inserted resource
     */
    @PostMapping(value="/insertResource", consumes = MediaType.APPLICATION_JSON_VALUE)
    //@Secured({ROLE_MEMBRO})
    public ResourceDTO insertResource(@RequestBody ResourceDTO newResource) {
        LOGGER.info("New resource arrived: {}", newResource.getAvailability());
        if (newResource instanceof ResourceCpuDTO) {
            ResourceCPU resourceCPU = resourceService.getResourceCPU((ResourceCpuDTO) newResource);

            ScoreDTO scoreDTO = resourceMessageHandler.requestResourceScore(resourceService.getScoreMessageDTO(resourceCPU));

            if (scoreDTO != null) {
                resourceCPU.setSingleCoreScore(scoreDTO.getScore());
                resourceCPU.setMulticoreScore(scoreDTO.getMulticore_score());
            } else {
                throw new ResourceNotFoundException("The resource " + newResource.getName() + " is not present.");
            }

            resourceCPU = resourceRepository.save(resourceCPU);

            resourceMessageHandler.sendNewResourceMessage(resourceService.getResourceMessageDTO(resourceCPU));

            return resourceService.getResourceCpuDTO(resourceCPU);
        } else if (newResource instanceof ResourceGpuDTO) {
            ResourceGPU resourceGPU = resourceService.getResourceGPU((ResourceGpuDTO) newResource);

            ScoreDTO scoreDTO = resourceMessageHandler.requestResourceScore(resourceService.getScoreMessageDTO(resourceGPU));

            if (scoreDTO != null) {
                resourceGPU.setOpenclScore(scoreDTO.getOpencl());
                resourceGPU.setVulkanScore(scoreDTO.getVulkan());
                resourceGPU.setCudaScore(scoreDTO.getCuda());
            } else {
                throw new ResourceNotFoundException("The resource " + newResource.getName() + " is not present.");
            }

            resourceGPU = resourceRepository.save(resourceGPU);

            resourceMessageHandler.sendNewResourceMessage(resourceService.getResourceMessageDTO(resourceGPU));

            return resourceService.getResourceGpuDTO(resourceGPU);
        } else {
            throw new IllegalArgumentException("Invalid resource type: " + newResource.getClass());
        }
    }

    /**
     * Updates a resource.
     *
     * @param resourceToUpdate the resource to update
     * @return the updated resource
     * @throws ResourceNotFoundException if the resource to update does not exist
     */
    @PutMapping(value="/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    //@Secured({ROLE_MEMBRO})
    public ResourceDTO updateResource(@RequestBody ResourceDTO resourceToUpdate) throws ResourceNotFoundException {
        Optional<Resource> resource = resourceRepository.findById(resourceToUpdate.getId());

        if(resource.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found with email: " + resourceToUpdate.getId() + ".");
        }

        Resource retResource = resource.get();

        String oldName = retResource.getName();

        retResource = resourceService.updateResource(retResource, resourceToUpdate);

        if (retResource instanceof ResourceCPU retResourceCPU) {
            ResourceCpuDTO resourceCpuDTO = (ResourceCpuDTO) resourceToUpdate;
            if (!resourceCpuDTO.getName().equals(oldName)) {
                ScoreDTO scoreDTO = resourceMessageHandler.requestResourceScore(resourceService.getScoreMessageDTO(resourceCpuDTO));

                if (scoreDTO != null) {
                    resourceCpuDTO.setSingleCoreScore(scoreDTO.getScore());
                    resourceCpuDTO.setMulticoreScore(scoreDTO.getMulticore_score());
                } else {
                    throw new ResourceNotFoundException("The resource " + resourceToUpdate.getName() + " is not present.");
                }
            }
            retResourceCPU = resourceService.updateResourceCPU(retResourceCPU, resourceCpuDTO);

            retResourceCPU = resourceRepository.save(retResourceCPU);

            resourceMessageHandler.sendUpdateResourceMessage(resourceService.getResourceMessageDTO(retResourceCPU));

            return resourceService.getResourceCpuDTO(retResourceCPU);
        } else if (retResource instanceof ResourceGPU retResourceGPU) {
            ResourceGpuDTO resourceGpuDTO = (ResourceGpuDTO) resourceToUpdate;
            if (!resourceGpuDTO.getName().equals(oldName)) {
                ScoreDTO scoreDTO = resourceMessageHandler.requestResourceScore(resourceService.getScoreMessageDTO(resourceGpuDTO));

                if (scoreDTO != null) {
                    resourceGpuDTO.setOpenclScore(scoreDTO.getOpencl());
                    resourceGpuDTO.setVulkanScore(scoreDTO.getVulkan());
                    resourceGpuDTO.setCudaScore(scoreDTO.getCuda());
                } else {
                    throw new ResourceNotFoundException("The resource " + resourceToUpdate.getName() + " is not present.");
                }
            }
            retResourceGPU = resourceService.updateResourceGPU(retResourceGPU, resourceGpuDTO);

            retResourceGPU = resourceRepository.save(retResourceGPU);

            resourceMessageHandler.sendUpdateResourceMessage(resourceService.getResourceMessageDTO(retResourceGPU));

            return resourceService.getResourceGpuDTO(retResourceGPU);
        } else {
            throw new IllegalArgumentException("Invalid resource type: " + retResource.getClass());
        }
    }

    /**
     * Makes a resource available.
     *
     * @param id the id of the resource to make available
     * @return the updated resource
     * @throws ResourceNotFoundException if the resource to make available does not exist
     */
    @PutMapping(value="/available/{id}")
    @Secured({ROLE_MEMBRO})
    public ResourceDTO makeAvailable(@PathVariable String id) throws ResourceNotFoundException {

        if (resourceRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Resource not found with id: " + id + ".");
        }

        Optional<Resource> resource = resourceRepository.findById(id);

        if(resource.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found with id: " + id + ".");
        }

        Resource retResource = resource.get();
        retResource.setIsAvailable(true);
        retResource = resourceRepository.save(retResource);

        if (retResource instanceof ResourceCPU) {
            resourceMessageHandler.sendUpdateResourceMessage(resourceService.getResourceMessageDTO(retResource));
            return resourceService.getResourceCpuDTO((ResourceCPU) retResource);
        } else if (retResource instanceof ResourceGPU) {
            resourceMessageHandler.sendUpdateResourceMessage(resourceService.getResourceMessageDTO(retResource));
            return resourceService.getResourceGpuDTO((ResourceGPU) retResource);
        } else {
            throw new IllegalArgumentException("Invalid resource type: " + retResource.getClass());
        }
    }

    /**
     * Makes a resource unavailable.
     *
     * @param id the id of the resource to make unavailable
     * @return the updated resource
     * @throws ResourceNotFoundException if the resource to make unavailable does not exist
     */
    @PutMapping(value="/unavailable/{id}")
    @Secured({ROLE_MEMBRO})
    public ResourceDTO makeUnavailable(@PathVariable String id) throws ResourceNotFoundException {

        Optional<Resource> resource = resourceRepository.findById(id);

        if(resource.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found with id: " + id + ".");
        }

        Resource retResource = resource.get();
        retResource.setIsAvailable(false);
        retResource = resourceRepository.save(retResource);

        if (retResource instanceof ResourceCPU) {
            resourceMessageHandler.sendUpdateResourceMessage(resourceService.getResourceMessageDTO(retResource));
            return resourceService.getResourceCpuDTO((ResourceCPU) retResource);
        } else if (retResource instanceof ResourceGPU) {
            resourceMessageHandler.sendUpdateResourceMessage(resourceService.getResourceMessageDTO(retResource));
            return resourceService.getResourceGpuDTO((ResourceGPU) retResource);
        } else {
            throw new IllegalArgumentException("Invalid resource type: " + retResource.getClass());
        }
    }
}
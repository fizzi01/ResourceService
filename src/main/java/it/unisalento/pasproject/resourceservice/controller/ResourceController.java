package it.unisalento.pasproject.resourceservice.controller;

import it.unisalento.pasproject.resourceservice.domain.Resource;
import it.unisalento.pasproject.resourceservice.dto.*;
import it.unisalento.pasproject.resourceservice.exceptions.ExistingResourceException;
import it.unisalento.pasproject.resourceservice.exceptions.ResourceNotFoundException;
import it.unisalento.pasproject.resourceservice.repositories.ResourceRepository;
import it.unisalento.pasproject.resourceservice.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static it.unisalento.pasproject.resourceservice.security.SecurityConstants.ROLE_MEMBRO;

/**
 * ResourceController is a REST controller that provides endpoints for managing resources.
 * It uses the ResourceService for business logic and the ResourceRepository for data access.
 */
@RestController
@RequestMapping("/api/resource")
public class ResourceController {
    final ResourceRepository resourceRepository;

    private final ResourceService resourceService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceController.class);

    /**
     * Constructs a new ResourceController with the given ResourceRepository, ResourceService, and ResourceMessageHandler.
     *
     * @param resourceRepository the repository to use for data access
     * @param resourceService the service to use for business logic
     */
    @Autowired
    public ResourceController(ResourceRepository resourceRepository, ResourceService resourceService) {
        this.resourceRepository = resourceRepository;
        this.resourceService = resourceService;
    }

    /**
     * Returns all resources.
     *
     * @return a ResourceListDTO containing all resources
     */
    @GetMapping(value="/find/all")
    @Secured({ROLE_MEMBRO})
    public ResourceListDTO getAllResources() {
        ResourceListDTO resourceListDTO = new ResourceListDTO();
        List<ResourceDTO> list = new ArrayList<>();
        resourceListDTO.setResourcesList(list);

        List<Resource> resources = resourceRepository.findAll();

        for (Resource resource : resources) {
            list.add(resourceService.getResourceDTO(resource));
            /*if (resource instanceof ResourceCPU) {
                list.add(resourceService.getResourceDTO(resource));
            } else if (resource instanceof ResourceGPU) {
                list.add(resourceService.get((ResourceGPU) resource));
            }*/
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
    @Secured(ROLE_MEMBRO)
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

        for (Resource resource : resources) {
            list.add(resourceService.getResourceDTO(resource));
            /*if (resource instanceof ResourceCPU) {
                list.add(resourceService.getResourceCpuDTO((ResourceCPU) resource));
            } else if (resource instanceof ResourceGPU) {
                list.add(resourceService.getResourceGpuDTO((ResourceGPU) resource));
            }*/
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
    @Secured({ROLE_MEMBRO})
    public ResourceDTO insertResource(@RequestBody ResourceDTO newResource) {
        LOGGER.info("New resource arrived: {}", newResource.getAvailability());

        ResourceDTO resourceDTO = resourceService.insertResource(newResource);

        if (resourceDTO == null) {
            throw new ExistingResourceException("Resource already exists with name: " + newResource.getName() + ", for " + newResource.getMemberEmail() + ".");
        }

        LOGGER.info("New resource inserted: {}", resourceDTO.getId());

        return resourceDTO;
        /*if (newResource instanceof ResourceCpuDTO) {
            ResourceCPU resourceCPU = (ResourceCPU) resourceService.getResource(newResource);

            ScoreDTO scoreDTO = resourceMessageHandler.requestResourceScore(resourceService.getScoreMessageDTO(resourceCPU));

            if (scoreDTO != null) {
                resourceCPU.setSingleCoreScore(scoreDTO.getScore());
                resourceCPU.setMulticoreScore(scoreDTO.getMulticore_score());
            } else {
                throw new ResourceNotFoundException("The resource " + newResource.getName() + " is not present.");
            }

            resourceCPU = resourceRepository.save(resourceCPU);

            resourceMessageHandler.sendNewResourceMessage(resourceService.getResourceMessageDTO(resourceCPU));

            return resourceService.getResourceDTO(resourceCPU);
        } else if (newResource instanceof ResourceGpuDTO) {
            ResourceGPU resourceGPU = (ResourceGPU) resourceService.getResource(newResource);

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

            return resourceService.getResourceDTO(resourceGPU);
        } else {
            throw new IllegalArgumentException("Invalid resource type: " + newResource.getClass());
        }*/
    }

    /**
     * Updates a resource.
     *
     * @param resourceToUpdate the resource to update
     * @return the updated resource
     * @throws ResourceNotFoundException if the resource to update does not exist
     */
    @PutMapping(value="/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_MEMBRO})
    public ResourceDTO updateResource(@RequestBody ResourceDTO resourceToUpdate) throws ResourceNotFoundException {
        LOGGER.info("Resource to update: {}", resourceToUpdate.getId());
        ResourceDTO resourceDTO = resourceService.updateResource(resourceToUpdate);

        if (resourceDTO == null) {
            throw new ResourceNotFoundException("Resource not found with id: " + resourceToUpdate.getId() + ".");
        }

        LOGGER.info("Resource updated: {}", resourceDTO.getId());

        return resourceDTO;
        /*Optional<Resource> resource = resourceRepository.findById(resourceToUpdate.getId());

        if(resource.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found with email: " + resourceToUpdate.getId() + ".");
        }

        Resource retResource = resource.get();

        String oldName = retResource.getName();
        boolean nameChanged = false;

        retResource = resourceService.updateResource(resourceToUpdate);

        if (retResource instanceof ResourceCPU retResourceCPU) {
            ResourceCpuDTO resourceCpuDTO = (ResourceCpuDTO) resourceToUpdate;
            if (!resourceCpuDTO.getName().equals(oldName)) {
                nameChanged = true;
                ScoreDTO scoreDTO = resourceMessageHandler.requestResourceScore(resourceService.getScoreMessageDTO(resourceCpuDTO));

                if (scoreDTO != null) {
                    resourceCpuDTO.setSingleCoreScore(scoreDTO.getScore());
                    resourceCpuDTO.setMulticoreScore(scoreDTO.getMulticore_score());
                } else {
                    throw new ResourceNotFoundException("The resource " + resourceToUpdate.getName() + " is not present.");
                }
            }
            retResourceCPU = (ResourceCPU) resourceService.updateResource(resourceCpuDTO);

            retResourceCPU = resourceRepository.save(retResourceCPU);

            resourceMessageHandler.sendUpdateResourceMessage(resourceService.getResourceMessageDTO(retResourceCPU));

            return resourceService.getResourceDTO(retResourceCPU);
        } else if (retResource instanceof ResourceGPU retResourceGPU) {
            ResourceGpuDTO resourceGpuDTO = (ResourceGpuDTO) resourceToUpdate;
            if (!resourceGpuDTO.getName().equals(oldName)) {
                nameChanged = true;
                ScoreDTO scoreDTO = resourceMessageHandler.requestResourceScore(resourceService.getScoreMessageDTO(resourceGpuDTO));

                if (scoreDTO != null) {
                    resourceGpuDTO.setOpenclScore(scoreDTO.getOpencl());
                    resourceGpuDTO.setVulkanScore(scoreDTO.getVulkan());
                    resourceGpuDTO.setCudaScore(scoreDTO.getCuda());
                } else {
                    throw new ResourceNotFoundException("The resource " + resourceToUpdate.getName() + " is not present.");
                }
            }
            retResourceGPU = (ResourceGPU) resourceService.updateResource(resourceGpuDTO);

            retResourceGPU = resourceRepository.save(retResourceGPU);

            resourceMessageHandler.sendUpdateResourceMessage(resourceService.getResourceMessageDTO(retResourceGPU));

            return resourceService.getResourceDTO(retResourceGPU);
        } else {
            throw new IllegalArgumentException("Invalid resource type: " + retResource.getClass());
        }*/
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
        ResourceDTO resourceDTO = resourceService.updateIsAvailable(id, true);

        if (resourceDTO == null) {
            throw new ResourceNotFoundException("Resource not found with id: " + id + ".");
        }

        return resourceDTO;
        /*if (resourceRepository.findById(id).isEmpty()) {
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
            return resourceService.getResourceDTO(retResource);
        } else if (retResource instanceof ResourceGPU) {
            resourceMessageHandler.sendUpdateResourceMessage(resourceService.getResourceMessageDTO(retResource));
            return resourceService.getResourceDTO(retResource);
        } else {
            throw new IllegalArgumentException("Invalid resource type: " + retResource.getClass());
        }*/
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
        ResourceDTO resourceDTO = resourceService.updateIsAvailable(id, false);

        if (resourceDTO == null) {
            throw new ResourceNotFoundException("Resource not found with id: " + id + ".");
        }

        return resourceDTO;
        /*Optional<Resource> resource = resourceRepository.findById(id);

        if(resource.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found with id: " + id + ".");
        }

        Resource retResource = resource.get();
        retResource.setIsAvailable(false);
        retResource = resourceRepository.save(retResource);

        if (retResource instanceof ResourceCPU) {
            resourceMessageHandler.sendUpdateResourceMessage(resourceService.getResourceMessageDTO(retResource));
            return resourceService.getResourceDTO(retResource);
        } else if (retResource instanceof ResourceGPU) {
            resourceMessageHandler.sendUpdateResourceMessage(resourceService.getResourceMessageDTO(retResource));
            return resourceService.getResourceDTO(retResource);
        } else {
            throw new IllegalArgumentException("Invalid resource type: " + retResource.getClass());
        }*/
    }
}
package it.unisalento.pasproject.resourceservice.controller;

import it.unisalento.pasproject.resourceservice.domain.Resource;
import it.unisalento.pasproject.resourceservice.dto.*;
import it.unisalento.pasproject.resourceservice.exceptions.ExistingResourceException;
import it.unisalento.pasproject.resourceservice.exceptions.ResourceNotFoundException;
import it.unisalento.pasproject.resourceservice.repositories.ResourceRepository;
import it.unisalento.pasproject.resourceservice.service.ResourceQueryFilters;
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
        }

        return resourceListDTO;
    }

    /**
     * Returns resources that match the given filter criteria.
     *
     * @param resourceQueryFilters the type of the resources to return
     * @return a ResourceListDTO containing the matching resources
     * @throws ResourceNotFoundException if no resources match the given criteria
     */
    @GetMapping("/find")
    @Secured(ROLE_MEMBRO)
    public ResourceListDTO getByFilter(@ModelAttribute ResourceQueryFilters resourceQueryFilters) throws ResourceNotFoundException {
        ResourceListDTO resourceListDTO = new ResourceListDTO();
        List<ResourceDTO> list = new ArrayList<>();
        resourceListDTO.setResourcesList(list);

        List<Resource> resources = resourceService.findResources(resourceQueryFilters);

        if (resources.isEmpty())
            throw new ResourceNotFoundException("No resources found with the given criteria.");

        for (Resource resource : resources) {
            list.add(resourceService.getResourceDTO(resource));
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
    }
}
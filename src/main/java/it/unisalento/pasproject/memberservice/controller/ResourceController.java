package it.unisalento.pasproject.memberservice.controller;

import it.unisalento.pasproject.memberservice.domain.Resource;
import it.unisalento.pasproject.memberservice.domain.ResourceCPU;
import it.unisalento.pasproject.memberservice.domain.ResourceGPU;
import it.unisalento.pasproject.memberservice.dto.ResourceCpuDTO;
import it.unisalento.pasproject.memberservice.dto.ResourceDTO;
import it.unisalento.pasproject.memberservice.dto.ResourceGpuDTO;
import it.unisalento.pasproject.memberservice.dto.ResourceListDTO;
import it.unisalento.pasproject.memberservice.exceptions.ResourceNotFoundException;
import it.unisalento.pasproject.memberservice.repositories.ResourceRepository;
import it.unisalento.pasproject.memberservice.service.ResourceMessageHandler;
import it.unisalento.pasproject.memberservice.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {
    final ResourceRepository resourceRepository;

    private final ResourceService resourceService;

    private final ResourceMessageHandler resourceMessageHandler;

    @Autowired
    public ResourceController(ResourceRepository resourceRepository, ResourceService resourceService, ResourceMessageHandler resourceMessageHandler) {
        this.resourceRepository = resourceRepository;
        this.resourceService = resourceService;
        this.resourceMessageHandler = resourceMessageHandler;
    }

    @GetMapping(value="/find/all")
    @Secured({"MEMBRO"})
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

    @GetMapping("/find")
    @Secured("MEMBRO")
    public ResourceListDTO getByFilter(@RequestParam() String type,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) String greenEnergyType,
                                       @RequestParam(required = false) Integer availableHours,
                                       @RequestParam(required = false) Double kWh,
                                       @RequestParam(required = false) String memberMail,
                                       @RequestParam(required = false) Boolean isAvailable) throws ResourceNotFoundException {
        ResourceListDTO resourceListDTO = new ResourceListDTO();
        List<ResourceDTO> list = new ArrayList<>();
        resourceListDTO.setResourcesList(list);

        List<Resource> resources = resourceService.findResources(name, type, greenEnergyType, availableHours, kWh, memberMail, isAvailable);

        if (resources.isEmpty())
            throw new ResourceNotFoundException();

        for (Resource resource : resources){
            if (resource instanceof ResourceCPU) {
                list.add(resourceService.getResourceCpuDTO((ResourceCPU) resource));
            } else if (resource instanceof ResourceGPU) {
                list.add(resourceService.getResourceGpuDTO((ResourceGPU) resource));
            }
        }

        return resourceListDTO;
    }

    @PostMapping(value="/insertResource", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured({"MEMBRO"})
    public ResponseEntity<?> insertResource(@RequestBody ResourceDTO newResource) {
        if (newResource instanceof ResourceCpuDTO) {
            ResourceCPU resourceCPU = resourceService.getResourceCPU((ResourceCpuDTO) newResource);
            resourceCPU = resourceRepository.save(resourceCPU);

            resourceMessageHandler.sendNewResourceMessage(resourceService.getResourceMessageDTO(resourceCPU));

            return ResponseEntity.ok(resourceService.getResourceCpuDTO(resourceCPU));
        } else if (newResource instanceof ResourceGpuDTO) {
            ResourceGPU resourceGPU = resourceService.getResourceGPU((ResourceGpuDTO) newResource);
            resourceGPU = resourceRepository.save(resourceGPU);

            resourceMessageHandler.sendNewResourceMessage(resourceService.getResourceMessageDTO(resourceGPU));

            return ResponseEntity.ok(resourceService.getResourceGpuDTO(resourceGPU));
        } else {
            throw new IllegalArgumentException("Invalid resource type: " + newResource.getClass());
        }
    }

    @PutMapping(value="/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured({"MEMBRO"})
    public ResourceDTO updateResource(@RequestBody ResourceDTO resourceToUpdate) throws ResourceNotFoundException {
        Optional<Resource> resource = resourceRepository.findById(resourceToUpdate.getId());

        if(resource.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        Resource retResource = resource.get();

        retResource = resourceService.updateResource(retResource, resourceToUpdate);

        if (retResource instanceof ResourceCPU retResourceCPU) {
            ResourceCpuDTO resourceCpuDTO = (ResourceCpuDTO) resourceToUpdate;
            retResourceCPU = resourceService.updateResourceCPU(retResourceCPU, resourceCpuDTO);

            retResourceCPU = resourceRepository.save(retResourceCPU);

            resourceMessageHandler.sendUpdateResourceMessage(resourceService.getResourceMessageDTO(retResourceCPU));

            return resourceService.getResourceCpuDTO(retResourceCPU);
        } else if (retResource instanceof ResourceGPU retResourceGPU) {
            ResourceGpuDTO resourceGpuDTO = (ResourceGpuDTO) resourceToUpdate;
            retResourceGPU = resourceService.updateResourceGPU(retResourceGPU, resourceGpuDTO);

            retResourceGPU = resourceRepository.save(retResourceGPU);

            resourceMessageHandler.sendUpdateResourceMessage(resourceService.getResourceMessageDTO(retResourceGPU));

            return resourceService.getResourceGpuDTO(retResourceGPU);
        } else {
            throw new IllegalArgumentException("Invalid resource type: " + retResource.getClass());
        }
    }

    @PutMapping(value="/available/{id}")
    @Secured({"MEMBRO"})
    public ResourceDTO makeAvailable(@PathVariable String id) throws ResourceNotFoundException {

        if (resourceRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException();
        }

        Optional<Resource> resource = resourceRepository.findById(id);

        if(resource.isEmpty()) {
            throw new ResourceNotFoundException();
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

    @PutMapping(value="/unavailable/{id}")
    @Secured({"MEMBRO"})
    public ResourceDTO makeUnavailable(@PathVariable String id) throws ResourceNotFoundException {

        Optional<Resource> resource = resourceRepository.findById(id);

        if(resource.isEmpty()) {
            throw new ResourceNotFoundException();
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
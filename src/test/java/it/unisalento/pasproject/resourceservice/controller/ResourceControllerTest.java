package it.unisalento.pasproject.resourceservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.pasproject.resourceservice.TestSecurityConfig;
import it.unisalento.pasproject.resourceservice.domain.Resource;
import it.unisalento.pasproject.resourceservice.domain.ResourceCPU;
import it.unisalento.pasproject.resourceservice.domain.ResourceGPU;
import it.unisalento.pasproject.resourceservice.domain.ResourceSoC;
import it.unisalento.pasproject.resourceservice.dto.ResourceCpuDTO;
import it.unisalento.pasproject.resourceservice.dto.ResourceDTO;
import it.unisalento.pasproject.resourceservice.dto.ResourceGpuDTO;
import it.unisalento.pasproject.resourceservice.dto.ResourceSoCDTO;
import it.unisalento.pasproject.resourceservice.exceptions.ExistingResourceException;
import it.unisalento.pasproject.resourceservice.exceptions.ResourceNotFoundException;
import it.unisalento.pasproject.resourceservice.repositories.ResourceRepository;
import it.unisalento.pasproject.resourceservice.service.ResourceQueryFilters;
import it.unisalento.pasproject.resourceservice.service.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResourceController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Import(TestSecurityConfig.class)
public class ResourceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResourceRepository resourceRepository;

    @MockBean
    private ResourceService resourceService;

    private List<Resource> resources;

    private List<ResourceDTO> resourceDTOs;

    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @BeforeEach
    void setup() {
        resources = new ArrayList<>();
        resourceDTOs = new ArrayList<>();

        // Mock object for ResourceCPU
        ResourceCPU mockResourceCPU = new ResourceCPU();
        mockResourceCPU.setId("cpu1");
        mockResourceCPU.setName("Intel Core i9");
        mockResourceCPU.setType("cpu");
        mockResourceCPU.setBrand("Intel");
        mockResourceCPU.setModel("i9-9900K");
        mockResourceCPU.setGreenEnergyType("Solar");
        mockResourceCPU.setCountry("USA");
        mockResourceCPU.setRegion("California");
        mockResourceCPU.setCity("San Francisco");
        mockResourceCPU.setKWh(95.0);
        mockResourceCPU.setMemberEmail("user@example.com");
        mockResourceCPU.setStatus(ResourceCPU.Status.AVAILABLE);
        mockResourceCPU.setCurrentTaskId("task1");
        mockResourceCPU.setArchitecture("x86_64");
        mockResourceCPU.setCores(8);
        mockResourceCPU.setThreads(16);
        mockResourceCPU.setBaseFrequency(3.6);
        mockResourceCPU.setMaxFrequency(5.0);
        mockResourceCPU.setCacheSize(16);
        mockResourceCPU.setTdp(95.0);
        mockResourceCPU.setHyperThreading(true);
        mockResourceCPU.setOverclockingSupport(true);
        mockResourceCPU.setSingleCoreScore(600);
        mockResourceCPU.setMulticoreScore(4800);

        // Mock object for ResourceGPU
        ResourceGPU mockResourceGPU = new ResourceGPU();
        mockResourceGPU.setId("gpu1");
        mockResourceGPU.setName("NVIDIA RTX 3080");
        mockResourceGPU.setType("gpu");
        mockResourceGPU.setBrand("NVIDIA");
        mockResourceGPU.setModel("RTX 3080");
        mockResourceGPU.setGreenEnergyType("Wind");
        mockResourceGPU.setCountry("USA");
        mockResourceGPU.setRegion("California");
        mockResourceGPU.setCity("San Francisco");
        mockResourceGPU.setKWh(320.0);
        mockResourceGPU.setMemberEmail("user@example.com");
        mockResourceGPU.setStatus(ResourceGPU.Status.AVAILABLE);
        mockResourceGPU.setCurrentTaskId("task2");
        mockResourceGPU.setArchitecture("Ampere");
        mockResourceGPU.setVramType("GDDR6X");
        mockResourceGPU.setVramSize(10);
        mockResourceGPU.setCoreClock(1.44);
        mockResourceGPU.setBoostClock(1.71);
        mockResourceGPU.setMemoryClock("19 Gbps");
        mockResourceGPU.setTdp(320.0);
        mockResourceGPU.setRayTracingSupport(true);
        mockResourceGPU.setDlssSupport(true);
        mockResourceGPU.setOpenclScore(9000);
        mockResourceGPU.setVulkanScore(9500);
        mockResourceGPU.setCudaScore(10000);

        // Mock object for ResourceSoC
        ResourceSoC mockResourceSoC = new ResourceSoC();
        mockResourceSoC.setId("soc1");
        mockResourceSoC.setName("Apple M1");
        mockResourceSoC.setType("soc");
        mockResourceSoC.setBrand("Apple");
        mockResourceSoC.setModel("M1");
        mockResourceSoC.setGreenEnergyType("Hydro");
        mockResourceSoC.setCountry("USA");
        mockResourceSoC.setRegion("California");
        mockResourceSoC.setCity("Cupertino");
        mockResourceSoC.setKWh(15.0);
        mockResourceSoC.setMemberEmail("user@example.com");
        mockResourceSoC.setStatus(ResourceSoC.Status.AVAILABLE);
        mockResourceSoC.setCurrentTaskId("task3");
        mockResourceSoC.setArchitecture("ARM");
        mockResourceSoC.setCpuCores(8);
        mockResourceSoC.setGpuCores(8);
        mockResourceSoC.setCpuBaseFrequency(3.2);
        mockResourceSoC.setCpuMaxFrequency(3.8);
        mockResourceSoC.setGpuBaseFrequency(1.2);
        mockResourceSoC.setGpuMaxFrequency(1.8);
        mockResourceSoC.setTdp(15.0);
        mockResourceSoC.setSingleCoreScore(1700);
        mockResourceSoC.setMulticoreScore(7500);
        mockResourceSoC.setOpenclScore(8000);
        mockResourceSoC.setVulkanScore(8200);
        mockResourceSoC.setCudaScore(0);

        // Mock object for ResourceCPU
        ResourceCpuDTO resourceCpuDTO = new ResourceCpuDTO();
        resourceCpuDTO.setId("cpu1");
        resourceCpuDTO.setName("Intel Core i9");
        resourceCpuDTO.setType("cpu");
        resourceCpuDTO.setBrand("Intel");
        resourceCpuDTO.setModel("i9-9900K");
        resourceCpuDTO.setGreenEnergyType("Solar");
        resourceCpuDTO.setCountry("USA");
        resourceCpuDTO.setRegion("California");
        resourceCpuDTO.setCity("San Francisco");
        resourceCpuDTO.setKWh(95.0);
        resourceCpuDTO.setMemberEmail("user@example.com");
        resourceCpuDTO.setStatus(ResourceCpuDTO.Status.AVAILABLE);
        resourceCpuDTO.setCurrentTaskId("task1");
        resourceCpuDTO.setArchitecture("x86_64");
        resourceCpuDTO.setCores(8);
        resourceCpuDTO.setThreads(16);
        resourceCpuDTO.setBaseFrequency(3.6);
        resourceCpuDTO.setMaxFrequency(5.0);
        resourceCpuDTO.setCacheSize(16);
        resourceCpuDTO.setTdp(95.0);
        resourceCpuDTO.setHyperThreading(true);
        resourceCpuDTO.setOverclockingSupport(true);
        resourceCpuDTO.setSingleCoreScore(600);
        resourceCpuDTO.setMulticoreScore(4800);

        // Mock object for ResourceGPU
        ResourceGpuDTO resourceGpuDTO = new ResourceGpuDTO();
        resourceGpuDTO.setId("gpu1");
        resourceGpuDTO.setName("NVIDIA RTX 3080");
        resourceGpuDTO.setType("gpu");
        resourceGpuDTO.setBrand("NVIDIA");
        resourceGpuDTO.setModel("RTX 3080");
        resourceGpuDTO.setGreenEnergyType("Wind");
        resourceGpuDTO.setCountry("USA");
        resourceGpuDTO.setRegion("California");
        resourceGpuDTO.setCity("San Francisco");
        resourceGpuDTO.setKWh(320.0);
        resourceGpuDTO.setMemberEmail("user@example.com");
        resourceGpuDTO.setStatus(ResourceCpuDTO.Status.AVAILABLE);
        resourceGpuDTO.setCurrentTaskId("task2");
        resourceGpuDTO.setArchitecture("Ampere");
        resourceGpuDTO.setVramType("GDDR6X");
        resourceGpuDTO.setVramSize(10);
        resourceGpuDTO.setCoreClock(1.44);
        resourceGpuDTO.setBoostClock(1.71);
        resourceGpuDTO.setMemoryClock("19 Gbps");
        resourceGpuDTO.setTdp(320.0);
        resourceGpuDTO.setRayTracingSupport(true);
        resourceGpuDTO.setDlssSupport(true);
        resourceGpuDTO.setOpenclScore(9000);
        resourceGpuDTO.setVulkanScore(9500);
        resourceGpuDTO.setCudaScore(10000);

        // Mock object for ResourceSoC
        ResourceSoCDTO resourceSoCDTO = new ResourceSoCDTO();
        resourceSoCDTO.setId("soc1");
        resourceSoCDTO.setName("Apple M1");
        resourceSoCDTO.setType("soc");
        resourceSoCDTO.setBrand("Apple");
        resourceSoCDTO.setModel("M1");
        resourceSoCDTO.setGreenEnergyType("Hydro");
        resourceSoCDTO.setCountry("USA");
        resourceSoCDTO.setRegion("California");
        resourceSoCDTO.setCity("Cupertino");
        resourceSoCDTO.setKWh(15.0);
        resourceSoCDTO.setMemberEmail("user@example.com");
        resourceSoCDTO.setStatus(ResourceSoCDTO.Status.AVAILABLE);
        resourceSoCDTO.setCurrentTaskId("task3");
        resourceSoCDTO.setArchitecture("ARM");
        resourceSoCDTO.setCpuCores(8);
        resourceSoCDTO.setGpuCores(8);
        resourceSoCDTO.setCpuBaseFrequency(3.2);
        resourceSoCDTO.setCpuMaxFrequency(3.8);
        resourceSoCDTO.setGpuBaseFrequency(1.2);
        resourceSoCDTO.setGpuMaxFrequency(1.8);
        resourceSoCDTO.setTdp(15.0);
        resourceSoCDTO.setSingleCoreScore(1700);
        resourceSoCDTO.setMulticoreScore(7500);
        resourceSoCDTO.setOpenclScore(8000);
        resourceSoCDTO.setVulkanScore(8200);
        resourceSoCDTO.setCudaScore(0);

        resources.add(mockResourceCPU);
        resources.add(mockResourceGPU);
        resources.add(mockResourceSoC);

        resourceDTOs.add(resourceCpuDTO);
        resourceDTOs.add(resourceGpuDTO);
        resourceDTOs.add(resourceSoCDTO);
    }

    @Test
    @WithMockUser(roles = "MEMBRO")
    void getAllResourcesShouldReturnListOfResources() throws Exception {
        when(resourceRepository.findAll()).thenReturn(resources);

        when(resourceService.getResourceDTO(resources.get(0))).thenReturn(resourceDTOs.get(0));
        when(resourceService.getResourceDTO(resources.get(1))).thenReturn(resourceDTOs.get(1));
        when(resourceService.getResourceDTO(resources.get(2))).thenReturn(resourceDTOs.get(2));

        mockMvc.perform(get("/api/resource/find/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourcesList", hasSize(3)))
                .andExpect(jsonPath("$.resourcesList[0].name", is("Intel Core i9")))
                .andExpect(jsonPath("$.resourcesList[1].name", is("NVIDIA RTX 3080")))
                .andExpect(jsonPath("$.resourcesList[2].name", is("Apple M1")));
    }

    @Test
    @WithMockUser(roles = "MEMBRO")
    void getAllResourcesWhenNoResourcesShouldReturnEmptyList() throws Exception {
        given(resourceRepository.findAll()).willReturn(new ArrayList<>());

        mockMvc.perform(get("/api/resource/find/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourcesList", hasSize(0)));
    }

    @Test
    @WithMockUser(roles = "MEMBRO")
    void getByFilterShouldReturnMatchingResources() throws Exception {
        List<Resource> filteredResources = List.of(resources.getFirst());

        when(resourceService.findResources(ArgumentMatchers.any(ResourceQueryFilters.class))).thenReturn(filteredResources);
        when(resourceService.getResourceDTO(ArgumentMatchers.any())).thenReturn(resourceDTOs.getFirst());

        mockMvc.perform(get("/api/resource/find")
                        .param("type", "cpu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourcesList", hasSize(1)))
                .andExpect(jsonPath("$.resourcesList[0].name", is("Intel Core i9")));
    }

    @Test
    @WithMockUser(roles = "MEMBRO")
    void getByFilterWhenNoResourcesMatchShouldReturnEmptyList() throws Exception {
        ResourceQueryFilters filters = new ResourceQueryFilters();
        filters.setType("NonExistentType");
        given(resourceService.findResources(filters)).willReturn(new ArrayList<>());

        mockMvc.perform(get("/api/resource/find")
                        .param("type", "NonExistentType"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourcesList", hasSize(0)));
    }

    @Test
    @WithMockUser(roles = "MEMBRO")
    void insertResourceShouldReturnInsertedResource() throws Exception {
        ResourceDTO resourceSoCDTO = new ResourceSoCDTO();
        resourceSoCDTO.setId("soc2");
        resourceSoCDTO.setName("Apple M2");
        resourceSoCDTO.setType("soc");
        resourceSoCDTO.setBrand("Apple");
        resourceSoCDTO.setModel("M1");
        resourceSoCDTO.setGreenEnergyType("Hydro");
        resourceSoCDTO.setCountry("USA");
        resourceSoCDTO.setRegion("California");
        resourceSoCDTO.setCity("Cupertino");
        resourceSoCDTO.setKWh(15.0);
        resourceSoCDTO.setMemberEmail("user@example.com");
        resourceSoCDTO.setStatus(ResourceSoCDTO.Status.AVAILABLE);
        resourceSoCDTO.setCurrentTaskId("task3");

        when(resourceService.insertResource(ArgumentMatchers.any())).thenReturn(resourceSoCDTO);

        ObjectMapper ObjectMapper = new ObjectMapper();
        String newResourceJson = ObjectMapper.writeValueAsString(resourceSoCDTO);

        mockMvc.perform(post("/api/resource/insertResource")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newResourceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("soc2")))
                .andExpect(jsonPath("$.name", is("Apple M2")))
                .andExpect(jsonPath("$.memberEmail", is("user@example.com")));
    }

    @Test
    @WithMockUser(roles = "MEMBRO")
    void insertResourceWhenResourceAlreadyExistsShouldThrowException() throws Exception {
        given(resourceService.insertResource(ArgumentMatchers.any())).willThrow(new ExistingResourceException(""));

        ObjectMapper ObjectMapper = new ObjectMapper();
        String newResourceJson = ObjectMapper.writeValueAsString(resourceDTOs.getFirst());

        mockMvc.perform(post("/api/resource/insertResource")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newResourceJson))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(roles = "MEMBRO")
    void updateResourceShouldReturnUpdatedResource() throws Exception {
        ResourceDTO resourceToUpdate = new ResourceCpuDTO();
        resourceToUpdate.setId("cpu1");
        resourceToUpdate.setType("cpu");
        resourceToUpdate.setName("Updated Resource");

        given(resourceService.updateResource(ArgumentMatchers.any())).willReturn(resourceToUpdate);

        ObjectMapper objectMapper = new ObjectMapper();
        String resourceJson = objectMapper.writeValueAsString(resourceToUpdate);

        mockMvc.perform(put("/api/resource/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resourceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("cpu1")))
                .andExpect(jsonPath("$.name", is("Updated Resource")));
    }

    @Test
    @WithMockUser(roles = "MEMBRO")
    void updateResourceWhenResourceNotFoundShouldThrowException() throws Exception {
        ResourceDTO resourceToUpdate = new ResourceCpuDTO();
        resourceToUpdate.setId("nonExistentId");
        resourceToUpdate.setType("cpu");
        resourceToUpdate.setName("Non Existent Resource");

        given(resourceService.updateResource(ArgumentMatchers.any())).willThrow(new ResourceNotFoundException(""));

        ObjectMapper objectMapper = new ObjectMapper();
        String resourceJson = objectMapper.writeValueAsString(resourceToUpdate);

        mockMvc.perform(put("/api/resource/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resourceJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "MEMBRO")
    void makeAvailableShouldReturnUpdatedResource() throws Exception {
        ResourceDTO updatedResource = new ResourceCpuDTO();
        updatedResource.setId("cpu1");
        updatedResource.setStatus(ResourceDTO.Status.AVAILABLE);

        given(resourceService.updateStatus("cpu1", Resource.Status.AVAILABLE)).willReturn(updatedResource);

        mockMvc.perform(put("/api/resource/available/cpu1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("cpu1")))
                .andExpect(jsonPath("$.status", is("AVAILABLE")));
    }

    @Test
    @WithMockUser(roles = "MEMBRO")
    void makeAvailableWhenResourceNotFoundShouldThrowException() throws Exception {
        given(resourceService.updateStatus("nonExistentId", Resource.Status.AVAILABLE)).willReturn(null);

        mockMvc.perform(put("/api/resource/available/nonExistentId"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "MEMBRO")
    void makeUnavailableShouldReturnUpdatedResource() throws Exception {
        ResourceDTO updatedResource = new ResourceCpuDTO();
        updatedResource.setId("cpu1");
        updatedResource.setStatus(ResourceDTO.Status.UNAVAILABLE);

        given(resourceService.updateStatus("cpu1", Resource.Status.UNAVAILABLE)).willReturn(updatedResource);

        mockMvc.perform(put("/api/resource/unavailable/cpu1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("cpu1")))
                .andExpect(jsonPath("$.status", is("UNAVAILABLE")));
    }

    @Test
    @WithMockUser(roles = "MEMBRO")
    void makeUnavailableWhenResourceNotFoundShouldThrowException() throws Exception {
        given(resourceService.updateStatus("nonExistentId", Resource.Status.UNAVAILABLE)).willReturn(null);

        mockMvc.perform(put("/api/resource/unavailable/nonExistentId"))
                .andExpect(status().isNotFound());
    }
}

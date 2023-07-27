package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PopulationDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.response.VillageInfo;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.services.VillageInfoService;
import com.example.ludogorieSoft.village.services.VillageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = VillageController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = VillageController.class
                )
        }
)
class VillageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VillageService villageService;
    @MockBean
    private VillageInfoService villageInfoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllVillages() throws Exception {
        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village Name 1");
        villageDTO1.setPopulationDTO(new PopulationDTO());
        villageDTO1.setDateUpload(new Date());
        villageDTO1.setStatus(true);

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setId(2L);
        villageDTO2.setName("Village Name 2");
        villageDTO2.setPopulationDTO(new PopulationDTO());
        villageDTO2.setDateUpload(new Date());
        villageDTO2.setStatus(true);

        List<VillageDTO> villageDTOList = Arrays.asList(villageDTO1, villageDTO2);

        when(villageService.getAllVillages()).thenReturn(villageDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villages")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$.[0].populationDTO").exists())
                .andExpect(jsonPath("$.[0].dateUpload").exists())
                .andExpect(jsonPath("$.[0].status").value(true))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].name").value("Village Name 2"))
                .andExpect(jsonPath("$.[1].populationDTO").exists())
                .andExpect(jsonPath("$.[1].dateUpload").exists())
                .andExpect(jsonPath("$.[1].status").value(true))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetVillageById() throws Exception {
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setId(2L);
        villageDTO.setName("Village Name 2");
        villageDTO.setPopulationDTO(new PopulationDTO());
        villageDTO.setDateUpload(new Date());
        villageDTO.setStatus(false);

        when(villageService.getVillageById(anyLong())).thenReturn(villageDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villages/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Village Name 2"))
                .andExpect(jsonPath("$.populationDTO").exists())
                .andExpect(jsonPath("$.dateUpload").exists())
                .andExpect(jsonPath("$.status").value(false))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testCreateVillage() throws Exception {
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setId(3L);
        villageDTO.setName("Created Village Name");
        villageDTO.setPopulationDTO(new PopulationDTO());
        villageDTO.setDateUpload(new Date());
        villageDTO.setStatus(true);

        when(villageService.createVillage(any(VillageDTO.class))).thenReturn(villageDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/villages")
                        .content("{\"id\": 3}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Created Village Name"))
                .andExpect(jsonPath("$.populationDTO").exists())
                .andExpect(jsonPath("$.dateUpload").exists())
                .andExpect(jsonPath("$.status").value(true))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testUpdateVillage() throws Exception {
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setId(1L);
        villageDTO.setName("Updated Village Name");
        villageDTO.setPopulationDTO(new PopulationDTO());
        villageDTO.setDateUpload(new Date());
        villageDTO.setStatus(false);

        when(villageService.updateVillage(anyLong(), any(VillageDTO.class))).thenReturn(villageDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/villages/{id}", 1)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Village Name"))
                .andExpect(jsonPath("$.populationDTO").exists())
                .andExpect(jsonPath("$.dateUpload").exists())
                .andExpect(jsonPath("$.status").value(false))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetAllVillagesWhenNoneExist() throws Exception {
        when(villageService.getAllVillages()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villages")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }


    @Test
    void testDeleteVillage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/villages/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreateVillageWithNullValues() throws Exception {
        Long villageId = 1L;

        when(villageService.createVillageWhitNullValues()).thenReturn(villageId);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/villages/null")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(villageId))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetVillageInfoById() throws Exception {
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setId(2L);
        villageDTO.setName("Village Name 2");
        villageDTO.setPopulationDTO(new PopulationDTO());
        villageDTO.setDateUpload(new Date());
        villageDTO.setStatus(false);
        VillageInfo villageInfo = new VillageInfo();
        villageInfo.setVillageDTO(villageDTO);
        villageInfo.setEthnicities("няма малцинствени групи");
        villageInfo.setPopulationAssertionResponses(new ArrayList<>());
        villageInfo.setLivingConditionResponses(new ArrayList<>());
        villageInfo.setObjectVillageResponses(new ArrayList<>());
        villageInfo.setAnswersQuestionResponses(new ArrayList<>());
        when(villageInfoService.getVillageInfoByVillageId(anyLong())).thenReturn(villageInfo);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villages/info/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.villageDTO.id").value(2))
                .andExpect(jsonPath("$.villageDTO.name").value("Village Name 2"))
                .andExpect(jsonPath("$.villageDTO.populationDTO").exists())
                .andExpect(jsonPath("$.villageDTO.dateUpload").exists())
                .andExpect(jsonPath("$.villageDTO.status").value(false))
                .andExpect(jsonPath("$.ethnicities").value(villageInfo.getEthnicities()))
                .andExpect(jsonPath("$.populationAssertionResponses").value(villageInfo.getPopulationAssertionResponses()))
                .andExpect(jsonPath("$.livingConditionResponses").value(villageInfo.getLivingConditionResponses()))
                .andExpect(jsonPath("$.objectVillageResponses").value(villageInfo.getObjectVillageResponses()))
                .andExpect(jsonPath("$.answersQuestionResponses").value(villageInfo.getAnswersQuestionResponses()))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

}

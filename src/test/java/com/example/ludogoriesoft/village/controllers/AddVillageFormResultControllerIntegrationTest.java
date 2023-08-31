package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.AddVillageFormResult;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.PopulationDTO;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.enums.Foreigners;
import com.example.ludogorieSoft.village.enums.NumberOfPopulation;
import com.example.ludogorieSoft.village.enums.Residents;
import com.example.ludogorieSoft.village.services.AddVillageFormResultService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AddVillageFormResultController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = AddVillageFormResultController.class
                )
        }
)
@AutoConfigureMockMvc(addFilters = false)
class AddVillageFormResultControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddVillageFormResultService addVillageFormResultService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAddVillageForResult() throws Exception {
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setId(1L);
        villageDTO.setName("Test Village");

        PopulationDTO populationDTO = new PopulationDTO();
        populationDTO.setNumberOfPopulation(NumberOfPopulation.UP_TO_10_PEOPLE);
        populationDTO.setResidents(Residents.FROM_21_TO_30_PERCENT);
        populationDTO.setChildren(Children.BELOW_10);
        populationDTO.setForeigners(Foreigners.YES);

        String groundCategoryName = "Ground Category 1";

        AddVillageFormResult inputFormResult = new AddVillageFormResult();
        inputFormResult.setVillageDTO(villageDTO);
        inputFormResult.setPopulationDTO(populationDTO);
        inputFormResult.setGroundCategoryIds(new ArrayList<>());

        AddVillageFormResult createdFormResult = new AddVillageFormResult();
        createdFormResult.setVillageDTO(villageDTO);
        createdFormResult.setPopulationDTO(populationDTO);
        createdFormResult.setGroundCategoryIds(new ArrayList<>());
        when(addVillageFormResultService.create(any(AddVillageFormResult.class))).thenReturn(createdFormResult);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/addVillageForm")
                        .content(asJsonString(inputFormResult))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.villageDTO.name").value("Test Village"))
                .andExpect(jsonPath("$.populationDTO.numberOfPopulation").value("UP_TO_10_PEOPLE"))
                .andExpect(jsonPath("$.populationDTO.residents").value("FROM_21_TO_30_PERCENT"))
                .andExpect(jsonPath("$.populationDTO.children").value("BELOW_10"))
                .andExpect(jsonPath("$.populationDTO.foreigners").value("YES"))
                .andExpect(jsonPath("$.groundCategoryIds").value(new ArrayList<>()))
                .andReturn();
    }

    private static String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
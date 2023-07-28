package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillagePopulationAssertionDTO;
import com.example.ludogorieSoft.village.enums.Consents;
import com.example.ludogorieSoft.village.services.VillagePopulationAssertionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.ludogorieSoft.village.enums.Consents.*;
import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = VillagePopulationAssertionController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = VillagePopulationAssertionController.class
                )
        }
)
class VillagePopulationAssertionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VillagePopulationAssertionService villagePopulationAssertionService;

    @Test
    void getAllVillagePopulationAssertionsShouldReturnListOfAssertions() throws Exception {
        VillagePopulationAssertionDTO assertion1 = new VillagePopulationAssertionDTO();
        assertion1.setId(1L);
        assertion1.setVillageId(1L);
        assertion1.setPopulatedAssertionId(1L);
        assertion1.setAnswer(COMPLETELY_AGREED);

        VillagePopulationAssertionDTO assertion2 = new VillagePopulationAssertionDTO();
        assertion2.setId(2L);
        assertion2.setVillageId(2L);
        assertion2.setPopulatedAssertionId(2L);
        assertion2.setAnswer(COMPLETELY_AGREED);

        List<VillagePopulationAssertionDTO> assertions = Arrays.asList(assertion1, assertion2);

        when(villagePopulationAssertionService.getAllVillagePopulationAssertion())
                .thenReturn(assertions);

        mockMvc.perform(get("/api/v1/villagePopulationAssertions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].villageId", is(1)))
                .andExpect(jsonPath("$[0].populatedAssertionId", is(1)))
                .andExpect(jsonPath("$[0].answer", is(COMPLETELY_AGREED.toString())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].villageId", is(2)))
                .andExpect(jsonPath("$[1].populatedAssertionId", is(2)))
                .andExpect(jsonPath("$[1].answer", is(COMPLETELY_AGREED.toString())));
    }

    @Test
    void getVillagePopulationAssertionByIdShouldReturnAssertion() throws Exception {
        VillagePopulationAssertionDTO assertion = new VillagePopulationAssertionDTO();
        assertion.setId(1L);
        assertion.setVillageId(1L);
        assertion.setPopulatedAssertionId(1L);
        assertion.setAnswer(DISAGREE);

        when(villagePopulationAssertionService.getByID(1L))
                .thenReturn(assertion);

        mockMvc.perform(get("/api/v1/villagePopulationAssertions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.villageId", is(1)))
                .andExpect(jsonPath("$.populatedAssertionId", is(1)))
                .andExpect(jsonPath("$.answer", is(DISAGREE.toString())));
    }

    @Test
    void updateVillagePopulationAssertionByIdShouldReturnUpdatedAssertion() throws Exception {
        VillagePopulationAssertionDTO assertion = new VillagePopulationAssertionDTO();
        assertion.setId(1L);
        assertion.setVillageId(1L);
        assertion.setPopulatedAssertionId(1L);
        assertion.setAnswer(DISAGREE);

        when(villagePopulationAssertionService.updateVillagePopulationAssertion(1L, assertion))
                .thenReturn(assertion);

        mockMvc.perform(put("/api/v1/villagePopulationAssertions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"villageId\": 1, \"populatedAssertionId\": 1, \"answer\": \"DISAGREE\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.villageId", is(1)))
                .andExpect(jsonPath("$.populatedAssertionId", is(1)))
                .andExpect(jsonPath("$.answer", is(DISAGREE.toString())));
    }

    @Test
    void createVillagePopulationAssertionShouldReturnCreatedAssertion() throws Exception {
        VillagePopulationAssertionDTO assertion = new VillagePopulationAssertionDTO();
        assertion.setId(1L);
        assertion.setVillageId(1L);
        assertion.setPopulatedAssertionId(1L);
        assertion.setAnswer(COMPLETELY_AGREED);

        when(villagePopulationAssertionService.createVillagePopulationAssertionDTO(assertion))
                .thenReturn(assertion);

        mockMvc.perform(post("/api/v1/villagePopulationAssertions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"villageId\": 1, \"populatedAssertionId\": 1, \"answer\": \"COMPLETELY_AGREED\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteVillagePopulationAssertionByIdShouldReturnNoContentWhenDeleted() throws Exception {
        when(villagePopulationAssertionService.deleteVillagePopulationAssertion(1L))
                .thenReturn(1);

        mockMvc.perform(delete("/api/v1/villagePopulationAssertions/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllVillagePopulationAssertionsWhenNoneExist() throws Exception {
        when(villagePopulationAssertionService.getAllVillagePopulationAssertion()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villagePopulationAssertions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }

    @Test
    void deleteVillagePopulationAssertionByIdShouldReturnNotFoundWhenNotDeleted() throws Exception {
        when(villagePopulationAssertionService.deleteVillagePopulationAssertion(1L))
                .thenReturn(0);

        mockMvc.perform(delete("/api/v1/villagePopulationAssertions/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetVillagePopulationAssertionByVillageIdValidVillageIDWithAssertions() throws Exception {
        Long villageId = 1L;

        VillagePopulationAssertionService villagePopulationAssertionService = mock(VillagePopulationAssertionService.class);
        List<VillagePopulationAssertionDTO> populationAssertions = new ArrayList<>();
        populationAssertions.add(new VillagePopulationAssertionDTO(1L, 1L, 1L, Consents.COMPLETELY_AGREED,true,now()));
        populationAssertions.add(new VillagePopulationAssertionDTO(2L, 1L, 2L, Consents.DISAGREE,true,now()));

        when(villagePopulationAssertionService.getVillagePopulationAssertionByVillageId(villageId))
                .thenReturn(populationAssertions);

        VillagePopulationAssertionController villagePopulationAssertionController = new VillagePopulationAssertionController(villagePopulationAssertionService);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(villagePopulationAssertionController).build();

        ResultMatcher expectedStatus = status().isOk();
        ResultMatcher expectedContentType = content().contentType(MediaType.APPLICATION_JSON);
        ResultMatcher expectedBody = jsonPath("$", hasSize(2));

        mockMvc.perform(get("/api/v1/villagePopulationAssertions/village/{id}", villageId))
                .andExpect(expectedStatus)
                .andExpect(expectedContentType)
                .andExpect(expectedBody);

        verify(villagePopulationAssertionService).getVillagePopulationAssertionByVillageId(villageId);
    }


}

package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.UserSearchDataDTO;
import com.example.ludogorieSoft.village.enums.ConstructionType;
import com.example.ludogorieSoft.village.enums.PropertyTransferType;
import com.example.ludogorieSoft.village.enums.PropertyType;
import com.example.ludogorieSoft.village.services.UserSearchDataService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = UserSearchDataController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = UserSearchDataController.class
                )
        }
)
class UserSearchDataControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserSearchDataService userSearchDataService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserSearchData() throws Exception {
        UserSearchDataDTO userSearchDataDTO = new UserSearchDataDTO();
        userSearchDataDTO.setId(1L);
        userSearchDataDTO.setSearchName("Example Search");
        userSearchDataDTO.setVillageName("Village Example");
        userSearchDataDTO.setRegionName("Region Example");
        userSearchDataDTO.setPropertyTransferType(PropertyTransferType.SALE);
        userSearchDataDTO.setPropertyTypes(List.of(PropertyType.HOUSE));
        userSearchDataDTO.setConstructionTypes(List.of(ConstructionType.BRICKS));
        userSearchDataDTO.setMinPrice(BigDecimal.valueOf(100000));
        userSearchDataDTO.setMaxPrice(BigDecimal.valueOf(300000));

        when(userSearchDataService.createUserSearchData(any(UserSearchDataDTO.class))).thenReturn(userSearchDataDTO);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/user-search-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"searchName\": \"Example Search\", \"villageName\": \"Village Example\", \"regionName\": \"Region Example\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.searchName").value("Example Search"))
                .andExpect(jsonPath("$.villageName").value("Village Example"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetAllUserSearchDataDTOsForUser() throws Exception {
        UserSearchDataDTO userSearchDataDTO1 = new UserSearchDataDTO();
        userSearchDataDTO1.setId(1L);
        userSearchDataDTO1.setSearchName("Search 1");

        UserSearchDataDTO userSearchDataDTO2 = new UserSearchDataDTO();
        userSearchDataDTO2.setId(2L);
        userSearchDataDTO2.setSearchName("Search 2");

        when(userSearchDataService.getAllUserSearchDataDTOsForUser(anyLong())).thenReturn(List.of(userSearchDataDTO1, userSearchDataDTO2));

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/user-search-data/get-all-for-user")
                        .param("alternativeUserId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].searchName").value("Search 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].searchName").value("Search 2"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testSoftDeleteUserSearchDataById() throws Exception {
        Long searchDataId = 1L;
        when(userSearchDataService.softDeleteUserSearchDataById(anyLong())).thenReturn("Successfully deleted");

        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/user-search-data/{id}", searchDataId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Successfully deleted"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }
}

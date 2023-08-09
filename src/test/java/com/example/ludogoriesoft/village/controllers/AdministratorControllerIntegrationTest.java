package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.request.AdministratorRequest;
import com.example.ludogorieSoft.village.dtos.response.VillageInfo;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.services.AdminVillageService;
import com.example.ludogorieSoft.village.services.AdministratorService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = AdministratorController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = AdministratorController.class
                )
        }
)
class AdministratorControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdministratorService administratorService;
    @MockBean
    private AdminVillageService adminVillageService;
    @MockBean
    private VillageInfoService villageInfoService;

    @MockBean
    private VillageService villageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAdministrators_shouldReturnListOfAdministrators() throws Exception {
        AdministratorDTO administratorDTO1 = new AdministratorDTO();
        administratorDTO1.setId(1L);
        administratorDTO1.setUsername("username1");
        AdministratorDTO administratorDTO2 = new AdministratorDTO();
        administratorDTO2.setId(2L);
        administratorDTO2.setUsername("username2");

        List<AdministratorDTO> administratorDTOList = Arrays.asList(administratorDTO1, administratorDTO2);

        when(administratorService.getAllAdministrators()).thenReturn(administratorDTOList);

        mockMvc.perform(get("/api/v1/admins")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("username1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].username").value("username2"));

        verify(administratorService, times(1)).getAllAdministrators();
    }

    @Test
    void getAdministratorById_shouldReturnAdministrator() throws Exception {
        AdministratorDTO administratorDTO1 = new AdministratorDTO();
        administratorDTO1.setId(1L);
        administratorDTO1.setUsername("username1");

        when(administratorService.getAdministratorById(1L)).thenReturn(administratorDTO1);
        mockMvc.perform(get("/api/v1/admins/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("username1"));

        verify(administratorService, times(1)).getAdministratorById(1L);
    }

    @Test
    void createAdministrator_shouldReturnCreatedAdministrator() throws Exception {
        AdministratorDTO createdAdministrator = new AdministratorDTO();
        createdAdministrator.setId(1L);
        createdAdministrator.setUsername("username");
        when(administratorService.createAdministrator(any(AdministratorRequest.class))).thenReturn(createdAdministrator);
        AdministratorRequest request = new AdministratorRequest();
        request.setUsername("username");

        mockMvc.perform(post("/api/v1/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("username"));

        verify(administratorService, times(1)).createAdministrator(request);
    }

    @Test
    void updateAdministrator_shouldReturnUpdatedAdministrator() throws Exception {
        AdministratorRequest request = new AdministratorRequest();
        request.setId(1L);
        request.setUsername("username");
        AdministratorDTO administratorDTO = new AdministratorDTO();
        administratorDTO.setId(1L);
        administratorDTO.setUsername("username");
        when(administratorService.updateAdministrator(1L, request)).thenReturn(administratorDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/admins/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1,\"username\":\"username\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("username"));

        verify(administratorService, times(1)).updateAdministrator(eq(1L), any(AdministratorRequest.class));
    }

    @Test
    void deleteAdministratorById_shouldReturnSuccessMessage() throws Exception {
        Long adminId = 1L;
        mockMvc.perform(delete("/api/v1/admins/{id}", adminId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Administrator with id: 1 has been deleted successfully!!"));

        verify(administratorService, times(1)).deleteAdministratorById(1L);
    }

    @Test
    void getAllVillages_shouldReturnListOfVillageResponses() throws Exception {
        VillageResponse villageResponse1 = new VillageResponse();
        villageResponse1.setId(1L);
        villageResponse1.setName("village1");
        VillageResponse villageResponse2 = new VillageResponse();
        villageResponse2.setId(2L);
        villageResponse2.setName("village2");
        List<VillageResponse> villageResponses = Arrays.asList(villageResponse1, villageResponse2);
        when(villageService.getAllVillagesWithAdmin()).thenReturn(villageResponses);
        mockMvc.perform(get("/api/v1/admins/village")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("village1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("village2"));

        verify(villageService, times(1)).getAllVillagesWithAdmin();
    }

    @Test
    void testDeleteVillageById() throws Exception {
        Long villageId = 1L;
        doNothing().when(villageService).deleteVillage(villageId);

        mockMvc.perform(delete("/api/v1/admins/village-delete/{villageId}", villageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Village with id: " + villageId + " has been deleted successfully!!"));
    }

    @Test
    void testChangeVillageStatus() throws Exception {
        String answerDate = "2023-08-10";
        Long villageId = 1L;
        VillageDTO villageDTO = new VillageDTO();

        when(villageService.getVillageById(villageId)).thenReturn(villageDTO);
        when(villageService.updateVillageStatus(villageId, villageDTO)).thenReturn(villageDTO);

        mockMvc.perform(post("/api/v1/admins/approve/{villageId}", villageId)
                        .param("villageId", String.valueOf(villageId))
                        .param("answerDate", answerDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Status of village with ID: " + villageId + " changed successfully!!!"));
    }

    @Test
    void testApproveVillageResponse() throws Exception {
        Long villageId = 1L;
        String answerDate = "2023-08-10";

        VillageDTO villageDTO = new VillageDTO();

        when(villageService.getVillageById(villageId)).thenReturn(villageDTO);

        mockMvc.perform(post("/api/v1/admins/approve/{villageId}", villageId)
                        .param("villageId", String.valueOf(villageId))
                        .param("answerDate", answerDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Status of village with ID: " + villageId + " changed successfully!!!"));

        verify(villageService).getVillageById(villageId);
        verify(villageService).updateVillageStatus(villageId, villageDTO);
        verify(adminVillageService).updateVillageStatusAndVillageResponsesStatus(villageId, answerDate);
    }

    @Test
    void testFindVillageById() throws Exception {
        Long villageId = 1L;

        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setId(1L);

        when(villageService.getVillageById(villageId)).thenReturn(villageDTO);

        mockMvc.perform(get("/api/v1/admins/update/{villageId}", villageId))
                .andExpect(status().isOk());

        verify(villageService).getVillageById(villageId);
    }

    @Test
    void testFindUnapprovedVillageResponseByVillageId() throws Exception {
        List<VillageResponse> villageResponses = new ArrayList<>(); // Create your mock responses
        when(adminVillageService.getUnapprovedVillageResponsesWithSortedAnswers(false)).thenReturn(villageResponses);

        mockMvc.perform(get("/api/v1/admins/update"))
                .andExpect(status().isOk());

        verify(adminVillageService).getUnapprovedVillageResponsesWithSortedAnswers(false);
    }

    @Test
    void testRejectVillageResponse() throws Exception {
        Long villageId = 1L;
        String answerDate = "2023-08-10"; // Adjust the date as needed

        mockMvc.perform(post("/api/v1/admins/reject/{villageId}", villageId)
                        .param("villageId", String.valueOf(villageId))
                        .param("answerDate", answerDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Response of village with ID: " + villageId + " rejected successfully!!!"));

        verify(adminVillageService).rejectVillageResponses(villageId, answerDate);
    }

    @Test
    void testGetVillageInfoById() throws Exception {
        Long villageId = 1L;
        String answerDate = "2023-08-10";
        boolean status = true;

        VillageInfo villageInfo = new VillageInfo();
        when(villageInfoService.getVillageInfoByVillageId(villageId, status, answerDate)).thenReturn(villageInfo);

        mockMvc.perform(get("/api/v1/admins/info/{villageId}", villageId)
                        .param("villageId", String.valueOf(villageId))
                        .param("answerDate", answerDate)
                        .param("status", String.valueOf(status)))
                .andExpect(status().isOk());

        verify(villageInfoService).getVillageInfoByVillageId(villageId, status, answerDate);
    }

}

package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.response.VillageInfo;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.services.AdminVillageService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = AdminFunctionController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = AdminFunctionController.class
                )
        }
)
class AdminFunctionControllerTest {
    @Autowired
    private MockMvc mockMvc;
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
    void testDeleteVillageById() throws Exception {
        Long villageId = 1L;
        doNothing().when(villageService).deleteVillage(villageId);

        mockMvc.perform(delete("/api/v1/admins/functions/village-delete/{villageId}", villageId)
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

        mockMvc.perform(post("/api/v1/admins/functions/approve/{villageId}", villageId)
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

        mockMvc.perform(post("/api/v1/admins/functions/approve/{villageId}", villageId)
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
    void testFindUnapprovedVillageResponseByVillageId() throws Exception {
        List<VillageResponse> villageResponses = new ArrayList<>(); // Create your mock responses
        when(adminVillageService.getUnapprovedVillageResponsesWithSortedAnswers(false)).thenReturn(villageResponses);

        mockMvc.perform(get("/api/v1/admins/functions/update"))
                .andExpect(status().isOk());

        verify(adminVillageService).getUnapprovedVillageResponsesWithSortedAnswers(false);
    }

    @Test
    void testRejectVillageResponse() throws Exception {
        Long villageId = 1L;
        String answerDate = "2023-08-10"; // Adjust the date as needed

        mockMvc.perform(post("/api/v1/admins/functions/reject/{villageId}", villageId)
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

        mockMvc.perform(get("/api/v1/admins/functions/info/{villageId}", villageId)
                        .param("villageId", String.valueOf(villageId))
                        .param("answerDate", answerDate)
                        .param("status", String.valueOf(status)))
                .andExpect(status().isOk());

        verify(villageInfoService).getVillageInfoByVillageId(villageId, status, answerDate);
    }

}
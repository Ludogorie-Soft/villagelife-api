package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.VillageVideoDTO;
import com.example.ludogorieSoft.village.dtos.response.VillageInfo;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.services.*;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AdminVillageService adminVillageService;
    @MockBean
    private VillageInfoService villageInfoService;

    @MockBean
    private VillageService villageService;
    @MockBean
    private VillageImageService villageImageService;
    @MockBean
    private VillageVideoService villageVideoService;

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
        List<VillageResponse> villageResponses = new ArrayList<>();
        when(adminVillageService.getUnapprovedVillageResponsesWithSortedAnswers(false)).thenReturn(villageResponses);

        mockMvc.perform(get("/api/v1/admins/functions/toApprove"))
                .andExpect(status().isOk());

        verify(adminVillageService).getUnapprovedVillageResponsesWithSortedAnswers(false);
    }

    @Test
    void testRejectVillageResponse() throws Exception {
        Long villageId = 1L;
        String answerDate = "2023-08-10";

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

    @Test
    void testGetVillagesWithRejectedResponses() throws Exception {
        List<VillageResponse> sampleResponses = new ArrayList<>();
        sampleResponses.add(new VillageResponse());
        sampleResponses.add(new VillageResponse());

        when(adminVillageService.getRejectedVillageResponsesWithSortedAnswers(false)).thenReturn(sampleResponses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admins/functions/getRejected")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(sampleResponses)));

        verify(adminVillageService).getRejectedVillageResponsesWithSortedAnswers(false);
    }

    @Test
    void testSaveVideos() throws Exception {
        Long villageId = 1L;
        List<String> videoUrls = List.of("https://www.youtube.com/watch?v=VIDEO_ID");

        mockMvc.perform(post("/api/v1/admins/functions/video")
                        .param("villageId", String.valueOf(villageId))
                        .param("videoUrl", videoUrls.toArray(new String[0]))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated())
                .andExpect(content().string("Video successfully saved"))
                .andDo(print());

        verify(villageVideoService, times(1)).createVideoPats(villageId, videoUrls);
    }

    @Test
    void testGetAllVideosByVillageId() throws Exception {
        long villageId = 1;
        List<VillageVideoDTO> videoDTOs = List.of(new VillageVideoDTO());

        when(villageVideoService.findVideosByVillageIdAndDateDeletedIsNull(villageId)).thenReturn(videoDTOs);

        mockMvc.perform(get("/api/v1/admins/functions/videos/{villageId}", villageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists())
                .andDo(print());

        verify(villageVideoService, times(1)).findVideosByVillageIdAndDateDeletedIsNull(villageId);
    }

    @Test
    void testGetAllDeletedVideosByVillageId() throws Exception {
        Long villageId = 1L;
        List<VillageVideoDTO> videoDTOs = Collections.singletonList(new VillageVideoDTO());

        when(villageVideoService.findDeletedVideosByVillageIdAndDateDeletedIsNotNull(villageId)).thenReturn(videoDTOs);

        mockMvc.perform(get("/api/v1/admins/functions/deleted-videos/{villageId}", villageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists())
                .andDo(print());

        verify(villageVideoService, times(1)).findDeletedVideosByVillageIdAndDateDeletedIsNotNull(villageId);
    }
    @Test
    void testDeleteVideoByVideoId() throws Exception {
        Long videoId = 1L;

        when(villageVideoService.deleteVideoById(videoId)).thenReturn("Video deleted");

        mockMvc.perform(delete("/api/v1/admins/functions/video-delete/{videoId}", videoId))
                .andExpect(status().isOk())
                .andExpect(content().string("Video deleted"))
                .andDo(print());

        verify(villageVideoService, times(1)).deleteVideoById(videoId);
    }

    @Test
    void testResumeVideoByVideoId() throws Exception {
        Long videoId = 1L;

        when(villageVideoService.resumeVideoById(videoId)).thenReturn("Video resumed");

        mockMvc.perform(put("/api/v1/admins/functions/resume-video/{videoId}", videoId))
                .andExpect(status().isOk())
                .andExpect(content().string("Video resumed"))
                .andDo(print());

        verify(villageVideoService, times(1)).resumeVideoById(videoId);
    }

    @Test
    void testRejectVideoByVideoId() throws Exception {
        Long videoId = 1L;

        when(villageVideoService.rejectVideoById(videoId)).thenReturn("Video rejected");

        mockMvc.perform(put("/api/v1/admins/functions/reject-video/{videoId}", videoId))
                .andExpect(status().isOk())
                .andExpect(content().string("Video rejected"))
                .andDo(print());

        verify(villageVideoService, times(1)).rejectVideoById(videoId);
    }
}
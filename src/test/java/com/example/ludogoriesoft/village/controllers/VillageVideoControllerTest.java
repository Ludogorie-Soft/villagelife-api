package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageVideoDTO;
import com.example.ludogorieSoft.village.services.VillageVideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = VillageVideoController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = VillageVideoController.class
                )
        }
)
class VillageVideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VillageVideoService villageVideoService;

    private static final Long VILLAGE_ID = 1L;

    @Test
    void testGetAllVideosByVillageId() throws Exception {
        List<VillageVideoDTO> villageVideoDTOList = List.of(getVillageVideoDTO());

        when(villageVideoService.getAllVideosByVillageId(VILLAGE_ID)).thenReturn(villageVideoDTOList);

        mockMvc.perform(get("/api/v1/villageVideos/village/{villageId}", VILLAGE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L));

        verify(villageVideoService, times(1)).getAllVideosByVillageId(VILLAGE_ID);
    }

    @Test
    void testGetAllApprovedVideosByVillageId() throws Exception {
        List<VillageVideoDTO> villageVideoDTOList = List.of(getVillageVideoDTO());

        when(villageVideoService.getAllApprovedVideosByVillageId(VILLAGE_ID)).thenReturn(villageVideoDTOList);

        mockMvc.perform(get("/api/v1/villageVideos/approved/village/{villageId}", VILLAGE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L));

        verify(villageVideoService, times(1)).getAllApprovedVideosByVillageId(VILLAGE_ID);
    }

    private VillageVideoDTO getVillageVideoDTO() {
        VillageVideoDTO villageVideoDTO = new VillageVideoDTO();
        villageVideoDTO.setId(2L);
        villageVideoDTO.setUrl("https://www.youtube.com/watch?v=VIDEO_ID");
        villageVideoDTO.setStatus(true);
        villageVideoDTO.setDateUpload(LocalDateTime.now());
        return villageVideoDTO;
    }
}
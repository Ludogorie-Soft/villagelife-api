package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageVideoDTO;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageVideo;
import com.example.ludogorieSoft.village.repositories.VillageVideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VillageVideoServiceTest {
    private static final long VILLAGE_ID = 1;
    private static final List<String> VIDEO_LIST = List.of(
            "https://www.youtube.com/watch?v=VIDEO_ID1",
            "https://www.youtube.com/watch?v=VIDEO_ID2"
    );

    @Mock
    private VillageVideoRepository villageVideoRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private VillageService villageService;
    @InjectMocks
    private VillageVideoService villageVideoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllVideosByVillageId() {
        VillageVideoDTO villageVideoDTO = getVillageVideoDTO();
        villageVideoDTO.setStatus(true);
        VillageVideo villageVideo = getVillageVideo();

        when(villageVideoRepository.findByVillageId(VILLAGE_ID)).thenReturn(List.of(villageVideo));
        when(modelMapper.map(villageVideo, VillageVideoDTO.class)).thenReturn(getVillageVideoDTO());

        List<VillageVideoDTO> villageVideoDTOS = villageVideoService.getAllVideosByVillageId(VILLAGE_ID);

        verify(villageVideoRepository, times(1)).findByVillageId(VILLAGE_ID);
        assertEquals(2L, villageVideoDTOS.get(0).getId());
    }

    @Test
    void testGetAllApprovedVideosByVillageId() {
        VillageVideoDTO villageVideoDTO = getVillageVideoDTO();
        villageVideoDTO.setStatus(true);
        VillageVideo villageVideo = getVillageVideo();

        when(villageVideoRepository.findByVillageId(VILLAGE_ID)).thenReturn(List.of(villageVideo));
        when(modelMapper.map(villageVideo, VillageVideoDTO.class)).thenReturn(villageVideoDTO);

        List<VillageVideoDTO> villageVideoDTOS = villageVideoService.getAllVideosByVillageId(VILLAGE_ID);

        verify(villageVideoRepository, times(1)).findByVillageId(VILLAGE_ID);
        assertEquals(2L, villageVideoDTOS.get(0).getId());
        assertEquals(true, villageVideoDTOS.get(0).getStatus());
    }

    @Test
    void testCreateVideoPats() {
        when(villageService.checkVillage(VILLAGE_ID)).thenReturn(getVillageVideo().getVillage());

        villageVideoService.createVideoPats(VILLAGE_ID, VIDEO_LIST);

        verify(villageService, times(1)).checkVillage(VILLAGE_ID);
        verify(villageVideoRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testFindVideosByVillageIdAndDateDeletedIsNull() {
        VillageVideoDTO villageVideoDTO = getVillageVideoDTO();
        villageVideoDTO.setStatus(true);
        VillageVideo villageVideo = getVillageVideo();

        when(villageVideoRepository.findByVillageIdAndDateDeletedIsNull(VILLAGE_ID)).thenReturn(List.of(villageVideo));
        when(modelMapper.map(villageVideo, VillageVideoDTO.class)).thenReturn(villageVideoDTO);

        List<VillageVideoDTO> villageVideoDTOS = villageVideoService.findVideosByVillageIdAndDateDeletedIsNull(VILLAGE_ID);

        verify(villageVideoRepository, times(1)).findByVillageIdAndDateDeletedIsNull(VILLAGE_ID);
        assertNull(villageVideoDTOS.get(0).getDateDeleted());
    }

    @Test
    void testFindDeletedVideosByVillageIdAndDateDeletedIsNotNull() {
        VillageVideoDTO villageVideoDTO = getVillageVideoDTO();
        villageVideoDTO.setDateDeleted(LocalDateTime.now());
        VillageVideo villageVideo = getVillageVideo();

        when(villageVideoRepository.findDeletedVideosByVillageIdAndDateDeletedIsNotNull(VILLAGE_ID)).thenReturn(List.of(villageVideo));
        when(modelMapper.map(villageVideo, VillageVideoDTO.class)).thenReturn(villageVideoDTO);

        List<VillageVideoDTO> villageVideoDTOS = villageVideoService.findDeletedVideosByVillageIdAndDateDeletedIsNotNull(VILLAGE_ID);

        verify(villageVideoRepository, times(1)).findDeletedVideosByVillageIdAndDateDeletedIsNotNull(VILLAGE_ID);
        assertNotNull(villageVideoDTOS.get(0).getDateDeleted());
    }

    @Test
    void deleteVideoById() {
        when(villageVideoRepository.findById(VILLAGE_ID)).thenReturn(Optional.of(getVillageVideo()));

        String villageId = villageVideoService.deleteVideoById(VILLAGE_ID);

        verify(villageVideoRepository, times(1)).deleteById(VILLAGE_ID);
        assertEquals("1", villageId);
    }

    @Test
    void testResumeVideoById() {
        VillageVideo villageVideo = getVillageVideo();
        villageVideo.setDateDeleted(LocalDateTime.of(2024, 5, 22, 12, 24));
        when(villageVideoRepository.findById(VILLAGE_ID)).thenReturn(Optional.of(villageVideo));

        String villageId = villageVideoService.resumeVideoById(VILLAGE_ID);

        verify(villageVideoRepository, times(1)).save(villageVideo);
        assertEquals("1", villageId);
    }

    @Test
    void rejectVideoById() {
        VillageVideo villageVideo = getVillageVideo();
        villageVideo.setDateDeleted(null);
        when(villageVideoRepository.findById(VILLAGE_ID)).thenReturn(Optional.of(villageVideo));

        String villageId = villageVideoService.rejectVideoById(VILLAGE_ID);

        verify(villageVideoRepository, times(1)).save(villageVideo);
        assertNotNull(villageVideo.getDateDeleted());
        assertEquals("1", villageId);
    }

    private VillageVideo getVillageVideo() {
        Village village = new Village();
        village.setId(1L);

        VillageVideo villageVideo = new VillageVideo();
        villageVideo.setVillage(village);
        villageVideo.setStatus(true);
        villageVideo.setId(2L);
        villageVideo.setUrl("https://www.youtube.com/watch?v=VIDEO_ID1");
        return villageVideo;
    }

    private VillageVideoDTO getVillageVideoDTO() {
        VillageVideoDTO villageVideoDTO = new VillageVideoDTO();
        villageVideoDTO.setId(2L);
        villageVideoDTO.setVillageId(1L);
        villageVideoDTO.setStatus(true);
        villageVideoDTO.setUrl("https://www.youtube.com/watch?v=VIDEO_ID1");
        return villageVideoDTO;
    }
}
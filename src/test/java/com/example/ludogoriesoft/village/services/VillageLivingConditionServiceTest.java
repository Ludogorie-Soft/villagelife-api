package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageLivingConditionDTO;
import com.example.ludogorieSoft.village.enums.Consents;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.LivingConditionRepository;
import com.example.ludogorieSoft.village.repositories.VillageLivingConditionRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

class VillageLivingConditionServiceTest {
    @Mock
    private VillageLivingConditionRepository villageLivingConditionRepository;
    @Mock
    private VillageService villageService;
    @Mock
    private VillageRepository villageRepository;
    @Mock
    private LivingConditionService livingConditionService;
    @Mock
    private LivingConditionRepository livingConditionRepository;
    @Mock
    private ModelMapper modelMapper;
    @Captor
    private ArgumentCaptor<List<VillageLivingConditions>> livingConditionListCaptor;

    @InjectMocks
    private VillageLivingConditionService villageLivingConditionService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void updateVillageLivingConditionShouldReturnUpdatedVillageLivingConditionDTOWhenFound() {
        Long id = 1L;
        VillageLivingConditionDTO villageLivingConditionDTO = new VillageLivingConditionDTO();
        villageLivingConditionDTO.setVillageId(2L);
        villageLivingConditionDTO.setLivingConditionId(3L);
        villageLivingConditionDTO.setConsents(Consents.COMPLETELY_AGREED);

        VillageLivingConditions existingVillageLivingCondition = new VillageLivingConditions();
        existingVillageLivingCondition.setId(id);

        Village village = new Village();
        village.setId(2L);
        LivingCondition livingCondition = new LivingCondition();
        livingCondition.setId(3L);

        when(villageLivingConditionRepository.findById(id)).thenReturn(Optional.of(existingVillageLivingCondition));
        when(villageService.checkVillage(villageLivingConditionDTO.getVillageId())).thenReturn(village);
        when(livingConditionService.checkLivingCondition(villageLivingConditionDTO.getLivingConditionId())).thenReturn(livingCondition);
        when(villageLivingConditionRepository.save(existingVillageLivingCondition)).thenReturn(existingVillageLivingCondition);
        when(villageLivingConditionService.toDTO(existingVillageLivingCondition)).thenReturn(villageLivingConditionDTO);

        existingVillageLivingCondition.setVillage(villageService.checkVillage(2L));
        existingVillageLivingCondition.setLivingCondition(livingConditionService.checkLivingCondition(3L));
        VillageLivingConditionDTO result = villageLivingConditionService.updateVillageLivingCondition(id, villageLivingConditionDTO);

        verify(villageLivingConditionRepository, times(1)).findById(id);
        verify(villageService, times(2)).checkVillage(villageLivingConditionDTO.getVillageId());
        verify(livingConditionService, times(2)).checkLivingCondition(villageLivingConditionDTO.getLivingConditionId());
        verify(villageLivingConditionRepository, times(1)).save(existingVillageLivingCondition);
        Assertions.assertEquals(villageLivingConditionDTO, result);
        Assertions.assertEquals(village, existingVillageLivingCondition.getVillage());
        Assertions.assertEquals(livingCondition, existingVillageLivingCondition.getLivingCondition());
        Assertions.assertEquals(villageLivingConditionDTO.getConsents(), existingVillageLivingCondition.getConsents());
    }

    @Test
    void updateVillageLivingConditionShouldThrowExceptionWhenNotFound() {
        Long id = 1L;
        VillageLivingConditionDTO villageLivingConditionDTO = new VillageLivingConditionDTO();

        when(villageLivingConditionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageLivingConditionService.updateVillageLivingCondition(id, villageLivingConditionDTO));
        verify(villageLivingConditionRepository, times(1)).findById(id);
        verifyNoMoreInteractions(villageService, livingConditionService, villageLivingConditionRepository);
    }

    @Test
    void testUpdateVillageLivingConditionStatus() {
        Long villageId = 1L;
        String localDateTime = "2023-08-10T00:00:00";
        boolean status = true;

        Village village = new Village();
        village.setId(1L);

        VillageLivingConditions livingCondition = new VillageLivingConditions();
        livingCondition.setId(1L);
        livingCondition.setVillage(village);

        List<VillageLivingConditions> livingConditionList = new ArrayList<>();
        livingConditionList.add(livingCondition);

        when(villageLivingConditionRepository.findByVillageIdAndVillageStatusAndDateUpload(villageId, status, localDateTime))
                .thenReturn(livingConditionList);

        when(villageService.checkVillage(anyLong())).thenReturn(village);

        villageLivingConditionService.updateVillageLivingConditionStatus(villageId, status, localDateTime);

        verify(villageService, times(1)).checkVillage(villageId);
        verify(villageLivingConditionRepository, times(1)).saveAll(livingConditionListCaptor.capture());

        List<VillageLivingConditions> capturedList = livingConditionListCaptor.getValue();
        Assertions.assertEquals(1, capturedList.size());
        VillageLivingConditions capturedLivingCondition = capturedList.get(0);
        Assertions.assertTrue(capturedLivingCondition.getVillageStatus());
    }

    @Test
    void testRejectVillageLivingConditionResponse() {
        Long villageId = 1L;
        String responseDate = "2023-08-10T00:00:00";
        LocalDateTime deleteDate = LocalDateTime.now();
        boolean status = false;

        Village village = new Village();
        village.setId(1L);

        VillageLivingConditions livingCondition = new VillageLivingConditions();
        livingCondition.setId(1L);
        livingCondition.setVillage(village);

        List<VillageLivingConditions> livingConditionList = new ArrayList<>();
        livingConditionList.add(livingCondition);

        when(villageLivingConditionRepository.findByVillageIdAndVillageStatusAndDateUpload(villageId, status, responseDate))
                .thenReturn(livingConditionList);

        villageLivingConditionService.rejectVillageLivingConditionResponse(villageId, status, responseDate, deleteDate);

        verify(villageService, times(1)).checkVillage(villageId);
        verify(villageLivingConditionRepository, times(1)).saveAll(livingConditionListCaptor.capture());

        List<VillageLivingConditions> capturedList = livingConditionListCaptor.getValue();
        Assertions.assertEquals(1, capturedList.size());
        VillageLivingConditions capturedLivingCondition = capturedList.get(0);
        Assertions.assertEquals(deleteDate, capturedLivingCondition.getDateDeleted());
    }
}

package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageLivingConditionDTO;
import com.example.ludogorieSoft.village.enums.Consents;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.LivingCondition;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageLivingConditions;
import com.example.ludogorieSoft.village.repositories.VillageLivingConditionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

class VillageLivingConditionServiceTest {
    @Mock
    private VillageLivingConditionRepository villageLivingConditionRepository;
    @Mock
    private VillageService villageService;
    @Mock
    private LivingConditionService livingConditionService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private VillageLivingConditionService villageLivingConditionService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getAllVillageLivingConditionsShouldReturnListOfVillageLivingConditionDTOs() {
        VillageLivingConditions villageLivingCondition1 = new VillageLivingConditions();
        VillageLivingConditions villageLivingCondition2 = new VillageLivingConditions();
        List<VillageLivingConditions> villageLivingConditions = new ArrayList<>();
        villageLivingConditions.add(villageLivingCondition1);
        villageLivingConditions.add(villageLivingCondition2);

        when(villageLivingConditionRepository.findAll()).thenReturn(villageLivingConditions);


        List<VillageLivingConditionDTO> result = villageLivingConditionService.getAllVillageLivingConditions();

        verify(villageLivingConditionRepository, times(1)).findAll();
        Assertions.assertEquals(villageLivingConditions.size(), result.size());
    }

    @Test
    void getAllVillageLivingConditionsShouldReturnEmptyListWhenNoVillageLivingConditions() {
        List<VillageLivingConditions> villageLivingConditions = new ArrayList<>();
        when(villageLivingConditionRepository.findAll()).thenReturn(villageLivingConditions);
        List<VillageLivingConditionDTO> result = villageLivingConditionService.getAllVillageLivingConditions();
        verify(villageLivingConditionRepository, times(1)).findAll();
        Assertions.assertEquals(0, result.size());
    }
    @Test
    void getByIDShouldReturnVillageLivingConditionDTOWhenFound() {
        Long id = 1L;
        VillageLivingConditions villageLivingCondition = new VillageLivingConditions();
        villageLivingCondition.setId(id);
        VillageLivingConditionDTO villageLivingConditionDTO = new VillageLivingConditionDTO();
        villageLivingConditionDTO.setId(id);

        when(villageLivingConditionRepository.findById(id)).thenReturn(Optional.of(villageLivingCondition));
        when(villageLivingConditionService.toDTO(villageLivingCondition)).thenReturn(villageLivingConditionDTO);

        VillageLivingConditionDTO result = villageLivingConditionService.getByID(id);
        verify(villageLivingConditionRepository, times(1)).findById(id);
        Assertions.assertEquals(villageLivingConditionDTO, result);
    }

    @Test
    void getByIDShouldThrowExceptionWhenNotFound() {
        Long id = 1L;
        when(villageLivingConditionRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ApiRequestException.class, () -> villageLivingConditionService.getByID(id));
        verify(villageLivingConditionRepository, times(1)).findById(id);
    }
    @Test
    void createVillageLivingConditionShouldReturnCreatedVillageLivingConditionDTO() {
        VillageLivingConditionDTO villageLivingConditionDTO = new VillageLivingConditionDTO();
        villageLivingConditionDTO.setVillageId(1L);
        villageLivingConditionDTO.setLivingConditionId(2L);
        villageLivingConditionDTO.setConsents(Consents.CANT_DECIDE);

        Village village = new Village();
        village.setId(1L);
        LivingCondition livingCondition = new LivingCondition();
        livingCondition.setId(2L);
        VillageLivingConditions villageLivingCondition = new VillageLivingConditions();

        when(villageService.checkVillage(villageLivingConditionDTO.getVillageId())).thenReturn(village);
        when(livingConditionService.checkLivingCondition(villageLivingConditionDTO.getLivingConditionId())).thenReturn(livingCondition);
        when(villageLivingConditionRepository.save(villageLivingCondition)).thenReturn(villageLivingCondition);
        when(villageLivingConditionService.toDTO(villageLivingCondition)).thenReturn(villageLivingConditionDTO);

        villageLivingCondition.setVillage(villageService.checkVillage(1L));
        villageLivingCondition.setLivingCondition(livingConditionService.checkLivingCondition(2L));
        villageLivingCondition.setConsents(Consents.CANT_DECIDE);
        VillageLivingConditionDTO result = villageLivingConditionService.createVillageLivingCondition(villageLivingConditionDTO);

        verify(villageService, times(2)).checkVillage(villageLivingConditionDTO.getVillageId());
        verify(livingConditionService, times(2)).checkLivingCondition(villageLivingConditionDTO.getLivingConditionId());
        verify(villageLivingConditionRepository, times(1)).save(villageLivingCondition);
        Assertions.assertEquals(villageLivingConditionDTO, result);
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
    void deleteVillageLivingConditionsShouldReturn1WhenDeleted() {
        Long id = 1L;
        int result = villageLivingConditionService.deleteVillageLivingConditions(id);
        verify(villageLivingConditionRepository, times(1)).deleteById(id);
        Assertions.assertEquals(1, result);
    }

    @Test
    void deleteVillageLivingConditionsShouldReturn0WhenNotFound() {
        Long id = 1L;
        doThrow(EmptyResultDataAccessException.class).when(villageLivingConditionRepository).deleteById(id);
        int result = villageLivingConditionService.deleteVillageLivingConditions(id);
        verify(villageLivingConditionRepository, times(1)).deleteById(id);
        Assertions.assertEquals(0, result);
    }
}

package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.LandscapeDTO;
import com.example.ludogorieSoft.village.dtos.LivingConditionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Landscape;
import com.example.ludogorieSoft.village.model.LivingCondition;
import com.example.ludogorieSoft.village.repositories.LivingConditionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class LivingConditionServiceTest {
    @Mock
    private LivingConditionRepository livingConditionRepository;

    @InjectMocks
    private LivingConditionService livingConditionService;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllLivingConditions() {
        LivingCondition livingCondition1 = new LivingCondition(1L, "Condition 1");
        LivingCondition livingCondition2 = new LivingCondition(2L, "Condition 2");

        List<LivingCondition> livingConditions = new ArrayList<>();
        livingConditions.add(livingCondition1);
        livingConditions.add(livingCondition2);

        LivingConditionDTO livingConditionDTO1 = new LivingConditionDTO(1L, "Condition 1");
        LivingConditionDTO livingConditionDTO2 = new LivingConditionDTO(2L, "Condition 2");

        List<LivingConditionDTO> expectedDTOs = new ArrayList<>();
        expectedDTOs.add(livingConditionDTO1);
        expectedDTOs.add(livingConditionDTO2);

        when(livingConditionRepository.findAll()).thenReturn(livingConditions);
        when(modelMapper.map(livingCondition1, LivingConditionDTO.class)).thenReturn(livingConditionDTO1);
        when(modelMapper.map(livingCondition2, LivingConditionDTO.class)).thenReturn(livingConditionDTO2);

        List<LivingConditionDTO> result = livingConditionService.getAllLivingConditions();

        verify(livingConditionRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(livingCondition1, LivingConditionDTO.class);
        verify(modelMapper, times(1)).map(livingCondition2, LivingConditionDTO.class);
        Assertions.assertEquals(expectedDTOs, result);
    }
    @Test
    public void testGetAllLivingConditionsWhenNoLivingConditionsExist() {
        List<LivingCondition> livingConditions = new ArrayList<>();
        List<LivingConditionDTO> expectedDTOs = new ArrayList<>();

        when(livingConditionRepository.findAll()).thenReturn(livingConditions);

        List<LivingConditionDTO> result = livingConditionService.getAllLivingConditions();

        verify(livingConditionRepository, times(1)).findAll();
        Assertions.assertEquals(expectedDTOs, result);
    }
    @Test
    public void testGetLivingConditionByIdWithExistingId() {
        Long livingConditionId = 1L;
        LivingCondition livingCondition = new LivingCondition(livingConditionId, "Condition");
        LivingConditionDTO expectedDTO = new LivingConditionDTO(livingConditionId, "Condition");

        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.of(livingCondition));
        when(modelMapper.map(livingCondition, LivingConditionDTO.class)).thenReturn(expectedDTO);

        LivingConditionDTO result = livingConditionService.getLivingConditionById(livingConditionId);

        verify(livingConditionRepository, times(1)).findById(livingConditionId);
        Assertions.assertEquals(expectedDTO, result);
    }

    @Test
    public void testGetLivingConditionByIdWithNonExistingId() {
        Long livingConditionId = 1L;
        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> livingConditionService.getLivingConditionById(livingConditionId));
        verify(livingConditionRepository, times(1)).findById(livingConditionId);
        verify(modelMapper, never()).map(any(Landscape.class), eq(LandscapeDTO.class));
    }

    @Test
    public void testCreateLivingConditionWithUniqueCondition() {
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO(1L, "Condition");
        when(livingConditionRepository.existsByLivingCondition(livingConditionDTO.getLivingCondition())).thenReturn(false);
        LivingConditionDTO result = livingConditionService.createLivingCondition(livingConditionDTO);
        verify(livingConditionRepository, times(1)).existsByLivingCondition(livingConditionDTO.getLivingCondition());
        verify(livingConditionRepository, times(1)).save(any(LivingCondition.class));
        Assertions.assertEquals(livingConditionDTO, result);
    }

    @Test
    public void testCreateLivingConditionWithDuplicateCondition() {
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO(1L, "Condition");
        when(livingConditionRepository.existsByLivingCondition(livingConditionDTO.getLivingCondition())).thenReturn(true);
        Assertions.assertThrows(ApiRequestException.class, () -> livingConditionService.createLivingCondition(livingConditionDTO));
        verify(livingConditionRepository, times(1)).existsByLivingCondition(livingConditionDTO.getLivingCondition());
        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
    }
    @Test
    public void testUpdateLivingConditionWithExistingIdAndNonExistingConditionName() {
        Long livingConditionId = 123L;
        LivingCondition livingCondition = new LivingCondition();
        livingCondition.setLivingCondition("Updated Condition");
        LivingCondition existingLivingCondition = new LivingCondition();
        existingLivingCondition.setLivingCondition("Old Condition");
        LivingConditionDTO expectedDTO = livingConditionService.livingConditionToLivingConditionDTO(livingCondition);

        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.of(existingLivingCondition));
        when(livingConditionRepository.existsByLivingCondition(livingCondition.getLivingCondition())).thenReturn(false);
        when(livingConditionRepository.save(any(LivingCondition.class))).thenReturn(livingCondition);

        LivingConditionDTO result = livingConditionService.updateLivingCondition(livingConditionId, livingCondition);

        verify(livingConditionRepository, times(1)).findById(livingConditionId);
        verify(livingConditionRepository, times(1)).existsByLivingCondition(livingCondition.getLivingCondition());
        verify(livingConditionRepository, times(1)).save(any(LivingCondition.class));
        Assertions.assertEquals(expectedDTO, result);
    }

    @Test
    public void testUpdateLivingConditionWithNonExistingId() {
        Long livingConditionId = 1L;
        LivingCondition livingCondition = new LivingCondition(livingConditionId, "Condition");

        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> livingConditionService.updateLivingCondition(livingConditionId, livingCondition));
        verify(livingConditionRepository, times(1)).findById(livingConditionId);
        verify(livingConditionRepository, never()).existsByLivingCondition(anyString());
        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
    }

    @Test
    public void testUpdateLivingConditionWithExistingIdAndDuplicateCondition() {
        Long livingConditionId = 1L;
        LivingCondition livingCondition = new LivingCondition(livingConditionId, "Condition");
        LivingCondition existingLivingCondition = new LivingCondition(livingConditionId, "Existing Condition");

        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.of(existingLivingCondition));
        when(livingConditionRepository.existsByLivingCondition(livingCondition.getLivingCondition())).thenReturn(true);

        Assertions.assertThrows(ApiRequestException.class, () -> livingConditionService.updateLivingCondition(livingConditionId, livingCondition));
        verify(livingConditionRepository, times(1)).findById(livingConditionId);
        verify(livingConditionRepository, times(1)).existsByLivingCondition(livingCondition.getLivingCondition());
        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
    }

    @Test
    public void testDeleteLivingConditionWithExistingId() {
        Long livingConditionId = 123L;
        LivingCondition livingCondition = new LivingCondition();
        livingCondition.setId(livingConditionId);

        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.of(livingCondition));
        livingConditionService.deleteLivingCondition(livingConditionId);

        verify(livingConditionRepository, times(1)).findById(livingConditionId);
        verify(livingConditionRepository, times(1)).delete(livingCondition);
    }

    @Test
    public void testDeleteLivingConditionWithNonExistingId() {
        Long livingConditionId = 123L;
        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ApiRequestException.class, () -> livingConditionService.deleteLivingCondition(livingConditionId));
        verify(livingConditionRepository, times(1)).findById(livingConditionId);
        verify(livingConditionRepository, never()).delete(any(LivingCondition.class));
    }
}

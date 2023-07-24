package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.LandscapeDTO;
import com.example.ludogorieSoft.village.dtos.LivingConditionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Landscape;
import com.example.ludogorieSoft.village.model.LivingCondition;
import com.example.ludogorieSoft.village.repositories.LivingConditionRepository;
import com.example.ludogorieSoft.village.repositories.VillageLivingConditionRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LivingConditionServiceTest {
    @Mock
    private LivingConditionRepository livingConditionRepository;

    @InjectMocks
    private LivingConditionService livingConditionService;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLivingConditions() {
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
    void testGetAllLivingConditionsWhenNoLivingConditionsExist() {
        List<LivingCondition> livingConditions = new ArrayList<>();
        List<LivingConditionDTO> expectedDTOs = new ArrayList<>();

        when(livingConditionRepository.findAll()).thenReturn(livingConditions);

        List<LivingConditionDTO> result = livingConditionService.getAllLivingConditions();

        verify(livingConditionRepository, times(1)).findAll();
        Assertions.assertEquals(expectedDTOs, result);
    }

    @Test
    void testGetLivingConditionByIdWithExistingId() {
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
    void testGetLivingConditionByIdWithNonExistingId() {
        Long livingConditionId = 1L;
        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> livingConditionService.getLivingConditionById(livingConditionId));
        verify(livingConditionRepository, times(1)).findById(livingConditionId);
        verify(modelMapper, never()).map(any(Landscape.class), eq(LandscapeDTO.class));
    }

    @Test
    void testCreateLivingCondition() {
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setLivingConditionName("Test Condition");
        LivingCondition livingCondition = new LivingCondition();
        livingCondition.setLivingConditionName("Test Condition");

        when(livingConditionRepository.existsByLivingConditionName("Test Condition")).thenReturn(false);
        when(modelMapper.map(livingCondition, LivingConditionDTO.class)).thenReturn(livingConditionDTO);
        when(livingConditionRepository.save(any(LivingCondition.class))).thenReturn(livingCondition);

        LivingConditionDTO result = livingConditionService.createLivingCondition(livingConditionDTO);

        assertNotNull(result);
        assertEquals("Test Condition", result.getLivingConditionName());
        verify(livingConditionRepository, times(1)).existsByLivingConditionName("Test Condition");
        verify(livingConditionRepository, times(1)).save(any(LivingCondition.class));
    }

    @Test
    void testCreateLivingConditionExistingCondition() {
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setLivingConditionName("Test Condition");

        when(livingConditionRepository.existsByLivingConditionName("Test Condition")).thenReturn(true);

        assertThrows(ApiRequestException.class, () -> livingConditionService.createLivingCondition(livingConditionDTO));

        verify(livingConditionRepository, times(1)).existsByLivingConditionName("Test Condition");
        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
    }

    @Test
    void testUpdateLivingCondition() {
        Long id = 1L;
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setLivingConditionName("Updated Condition");
        LivingCondition existingLivingCondition = new LivingCondition();
        existingLivingCondition.setId(id);
        existingLivingCondition.setLivingConditionName("Old Condition");

        Optional<LivingCondition> optionalLivingCondition = Optional.of(existingLivingCondition);

        when(livingConditionRepository.findById(id)).thenReturn(optionalLivingCondition);
        when(livingConditionRepository.existsByLivingConditionName("Updated Condition")).thenReturn(false);
        when(livingConditionRepository.save(any(LivingCondition.class))).thenReturn(existingLivingCondition);
        when(modelMapper.map(existingLivingCondition, LivingConditionDTO.class)).thenReturn(livingConditionDTO);

        LivingConditionDTO result = livingConditionService.updateLivingCondition(id, livingConditionDTO);

        assertNotNull(result);
        assertEquals("Updated Condition", result.getLivingConditionName());
        verify(livingConditionRepository, times(1)).findById(id);
        verify(livingConditionRepository, times(1)).existsByLivingConditionName("Updated Condition");
        verify(livingConditionRepository, times(1)).save(any(LivingCondition.class));
    }

    @Test
    void testUpdateLivingConditionInvalidId() {
        Long id = 1L;
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setLivingConditionName("Updated Condition");

        when(livingConditionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> livingConditionService.updateLivingCondition(id, livingConditionDTO));

        verify(livingConditionRepository, times(1)).findById(id);
        verify(livingConditionRepository, never()).existsByLivingConditionName(anyString());
        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
    }

    @Test
    void testUpdateLivingConditionExistingCondition() {
        Long id = 1L;
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setLivingConditionName("Updated Condition");
        LivingCondition existingLivingCondition = new LivingCondition();
        existingLivingCondition.setId(id);
        existingLivingCondition.setLivingConditionName("Old Condition");

        Optional<LivingCondition> optionalLivingCondition = Optional.of(existingLivingCondition);

        when(livingConditionRepository.findById(id)).thenReturn(optionalLivingCondition);
        when(livingConditionRepository.existsByLivingConditionName("Updated Condition")).thenReturn(true);

        assertThrows(ApiRequestException.class, () -> livingConditionService.updateLivingCondition(id, livingConditionDTO));

        verify(livingConditionRepository, times(1)).findById(id);
        verify(livingConditionRepository, times(1)).existsByLivingConditionName("Updated Condition");
        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
    }

    @Test
    void testDeleteLivingConditionWithExistingId() {
        Long livingConditionId = 123L;
        LivingCondition livingCondition = new LivingCondition();
        livingCondition.setId(livingConditionId);

        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.of(livingCondition));
        livingConditionService.deleteLivingCondition(livingConditionId);

        verify(livingConditionRepository, times(1)).findById(livingConditionId);
        verify(livingConditionRepository, times(1)).delete(livingCondition);
    }

    @Test
    void testDeleteLivingConditionWithNonExistingId() {
        Long livingConditionId = 123L;
        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ApiRequestException.class, () -> livingConditionService.deleteLivingCondition(livingConditionId));
        verify(livingConditionRepository, times(1)).findById(livingConditionId);
        verify(livingConditionRepository, never()).delete(any(LivingCondition.class));
    }
    @Test
    void checkLivingConditionShouldReturnExistingLivingCondition() {
        Long livingConditionId = 1L;
        LivingCondition existingLivingCondition = new LivingCondition();
        existingLivingCondition.setId(livingConditionId);
        Optional<LivingCondition> optionalLivingCondition = Optional.of(existingLivingCondition);

        when(livingConditionRepository.findById(livingConditionId)).thenReturn(optionalLivingCondition);

        LivingCondition result = livingConditionService.checkLivingCondition(livingConditionId);

        verify(livingConditionRepository, times(1)).findById(livingConditionId);
        assertEquals(existingLivingCondition, result);
    }

    @Test
    void checkLivingConditionShouldThrowExceptionWhenLivingConditionNotFound() {
        Long livingConditionId = 1L;
        Optional<LivingCondition> optionalLivingCondition = Optional.empty();

        when(livingConditionRepository.findById(livingConditionId)).thenReturn(optionalLivingCondition);

        Assertions.assertThrows(ApiRequestException.class, () -> livingConditionService.checkLivingCondition(livingConditionId));

        verify(livingConditionRepository, times(1)).findById(livingConditionId);
    }


    @Test
    void testCreateLivingConditionWithBlankName() {
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setLivingConditionName("");

        assertThrows(ApiRequestException.class, () -> livingConditionService.createLivingCondition(livingConditionDTO));
        verify(livingConditionRepository, never()).existsByLivingConditionName(anyString());
        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
    }

    @Test
    void testUpdateLivingConditionWithInvalidData() {
        Long id = 1L;
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setLivingConditionName("");
        assertThrows(ApiRequestException.class, () -> livingConditionService.updateLivingCondition(id, livingConditionDTO));
        verify(livingConditionRepository, times(1)).findById(id);
        verify(livingConditionRepository, never()).existsByLivingConditionName(anyString());
        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
    }


    @Test
    void testUpdateLivingConditionWithNullData() {
        Long id = 1L;

        assertThrows(ApiRequestException.class, () -> livingConditionService.updateLivingCondition(id, null));
        verify(livingConditionRepository, times(1)).findById(id);
        verify(livingConditionRepository, never()).existsByLivingConditionName(anyString());
        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
    }


}

//class LivingConditionServiceTest {
//    @Mock
//    private LivingConditionRepository livingConditionRepository;
//
//    @InjectMocks
//    private LivingConditionService livingConditionService;
//    @Mock
//    private ModelMapper modelMapper;
//    @Mock
//    private VillageLivingConditionRepository villageLivingConditionRepository;
//
//    @BeforeEach
//    public void setup() {
//        livingConditionRepository = mock(LivingConditionRepository.class);
//        modelMapper = mock(ModelMapper.class);
//        livingConditionService = new LivingConditionService(livingConditionRepository, modelMapper, villageLivingConditionRepository);
//    }
//
//    @Test
//    void testGetAllLivingConditions() {
//        LivingCondition livingCondition1 = new LivingCondition(1L, "Condition 1");
//        LivingCondition livingCondition2 = new LivingCondition(2L, "Condition 2");
//
//        List<LivingCondition> livingConditions = new ArrayList<>();
//        livingConditions.add(livingCondition1);
//        livingConditions.add(livingCondition2);
//
//        LivingConditionDTO livingConditionDTO1 = new LivingConditionDTO(1L, "Condition 1");
//        LivingConditionDTO livingConditionDTO2 = new LivingConditionDTO(2L, "Condition 2");
//
//        List<LivingConditionDTO> expectedDTOs = new ArrayList<>();
//        expectedDTOs.add(livingConditionDTO1);
//        expectedDTOs.add(livingConditionDTO2);
//
//        when(livingConditionRepository.findAll()).thenReturn(livingConditions);
//        when(modelMapper.map(livingCondition1, LivingConditionDTO.class)).thenReturn(livingConditionDTO1);
//        when(modelMapper.map(livingCondition2, LivingConditionDTO.class)).thenReturn(livingConditionDTO2);
//
//        List<LivingConditionDTO> result = livingConditionService.getAllLivingConditions();
//
//        verify(livingConditionRepository, times(1)).findAll();
//        verify(modelMapper, times(1)).map(livingCondition1, LivingConditionDTO.class);
//        verify(modelMapper, times(1)).map(livingCondition2, LivingConditionDTO.class);
//        Assertions.assertEquals(expectedDTOs, result);
//    }
//
//    @Test
//    void testGetAllLivingConditionsWhenNoLivingConditionsExist() {
//        List<LivingCondition> livingConditions = new ArrayList<>();
//        List<LivingConditionDTO> expectedDTOs = new ArrayList<>();
//
//        when(livingConditionRepository.findAll()).thenReturn(livingConditions);
//
//        List<LivingConditionDTO> result = livingConditionService.getAllLivingConditions();
//
//        verify(livingConditionRepository, times(1)).findAll();
//        Assertions.assertEquals(expectedDTOs, result);
//    }
//
//    @Test
//    void testGetLivingConditionByIdWithExistingId() {
//        Long livingConditionId = 1L;
//        LivingCondition livingCondition = new LivingCondition(livingConditionId, "Condition");
//        LivingConditionDTO expectedDTO = new LivingConditionDTO(livingConditionId, "Condition");
//
//        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.of(livingCondition));
//        when(modelMapper.map(livingCondition, LivingConditionDTO.class)).thenReturn(expectedDTO);
//
//        LivingConditionDTO result = livingConditionService.getLivingConditionById(livingConditionId);
//
//        verify(livingConditionRepository, times(1)).findById(livingConditionId);
//        Assertions.assertEquals(expectedDTO, result);
//    }
//
//    @Test
//    void testGetLivingConditionByIdWithNonExistingId() {
//        Long livingConditionId = 1L;
//        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(ApiRequestException.class, () -> livingConditionService.getLivingConditionById(livingConditionId));
//        verify(livingConditionRepository, times(1)).findById(livingConditionId);
//        verify(modelMapper, never()).map(any(Landscape.class), eq(LandscapeDTO.class));
//    }
//
//    @Test
//    void testCreateLivingCondition() {
//        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
//        livingConditionDTO.setLivingConditionName("Test Condition");
//        LivingCondition livingCondition = new LivingCondition();
//        livingCondition.setLivingConditionName("Test Condition");
//
//        when(livingConditionRepository.existsByLivingConditionName("Test Condition")).thenReturn(false);
//        when(modelMapper.map(livingCondition, LivingConditionDTO.class)).thenReturn(livingConditionDTO);
//        when(livingConditionRepository.save(any(LivingCondition.class))).thenReturn(livingCondition);
//
//        LivingConditionDTO result = livingConditionService.createLivingCondition(livingConditionDTO);
//
//        assertNotNull(result);
//        assertEquals("Test Condition", result.getLivingConditionName());
//        verify(livingConditionRepository, times(1)).existsByLivingConditionName("Test Condition");
//        verify(livingConditionRepository, times(1)).save(any(LivingCondition.class));
//    }
//
//    @Test
//    void testCreateLivingConditionExistingCondition() {
//        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
//        livingConditionDTO.setLivingConditionName("Test Condition");
//
//        when(livingConditionRepository.existsByLivingConditionName("Test Condition")).thenReturn(true);
//
//        assertThrows(ApiRequestException.class, () -> {
//            livingConditionService.createLivingCondition(livingConditionDTO);
//        });
//
//        verify(livingConditionRepository, times(1)).existsByLivingConditionName("Test Condition");
//        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
//    }
//
//    @Test
//    void testUpdateLivingCondition() {
//        Long id = 1L;
//        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
//        livingConditionDTO.setLivingConditionName("Updated Condition");
//        LivingCondition existingLivingCondition = new LivingCondition();
//        existingLivingCondition.setId(id);
//        existingLivingCondition.setLivingConditionName("Old Condition");
//
//        Optional<LivingCondition> optionalLivingCondition = Optional.of(existingLivingCondition);
//
//        when(livingConditionRepository.findById(id)).thenReturn(optionalLivingCondition);
//        when(livingConditionRepository.existsByLivingConditionName("Updated Condition")).thenReturn(false);
//        when(livingConditionRepository.save(any(LivingCondition.class))).thenReturn(existingLivingCondition);
//        when(modelMapper.map(existingLivingCondition, LivingConditionDTO.class)).thenReturn(livingConditionDTO);
//
//        LivingConditionDTO result = livingConditionService.updateLivingCondition(id, livingConditionDTO);
//
//        assertNotNull(result);
//        assertEquals("Updated Condition", result.getLivingConditionName());
//        verify(livingConditionRepository, times(1)).findById(id);
//        verify(livingConditionRepository, times(1)).existsByLivingConditionName("Updated Condition");
//        verify(livingConditionRepository, times(1)).save(any(LivingCondition.class));
//    }
//
//    @Test
//    void testUpdateLivingConditionInvalidId() {
//        Long id = 1L;
//        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
//        livingConditionDTO.setLivingConditionName("Updated Condition");
//
//        when(livingConditionRepository.findById(id)).thenReturn(Optional.empty());
//
//        assertThrows(ApiRequestException.class, () -> {
//            livingConditionService.updateLivingCondition(id, livingConditionDTO);
//        });
//
//        verify(livingConditionRepository, times(1)).findById(id);
//        verify(livingConditionRepository, never()).existsByLivingConditionName(anyString());
//        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
//    }
//
//    @Test
//    void testUpdateLivingConditionExistingCondition() {
//        Long id = 1L;
//        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
//        livingConditionDTO.setLivingConditionName("Updated Condition");
//        LivingCondition existingLivingCondition = new LivingCondition();
//        existingLivingCondition.setId(id);
//        existingLivingCondition.setLivingConditionName("Old Condition");
//
//        Optional<LivingCondition> optionalLivingCondition = Optional.of(existingLivingCondition);
//
//        when(livingConditionRepository.findById(id)).thenReturn(optionalLivingCondition);
//        when(livingConditionRepository.existsByLivingConditionName("Updated Condition")).thenReturn(true);
//
//        assertThrows(ApiRequestException.class, () -> {
//            livingConditionService.updateLivingCondition(id, livingConditionDTO);
//        });
//
//        verify(livingConditionRepository, times(1)).findById(id);
//        verify(livingConditionRepository, times(1)).existsByLivingConditionName("Updated Condition");
//        verify(livingConditionRepository, never()).save(any(LivingCondition.class));
//    }
//
//    @Test
//    void testDeleteLivingConditionWithExistingId() {
//        Long livingConditionId = 123L;
//        LivingCondition livingCondition = new LivingCondition();
//        livingCondition.setId(livingConditionId);
//
//        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.of(livingCondition));
//        livingConditionService.deleteLivingCondition(livingConditionId);
//
//        verify(livingConditionRepository, times(1)).findById(livingConditionId);
//        verify(livingConditionRepository, times(1)).delete(livingCondition);
//    }
//
//    @Test
//    void testDeleteLivingConditionWithNonExistingId() {
//        Long livingConditionId = 123L;
//        when(livingConditionRepository.findById(livingConditionId)).thenReturn(Optional.empty());
//        Assertions.assertThrows(ApiRequestException.class, () -> livingConditionService.deleteLivingCondition(livingConditionId));
//        verify(livingConditionRepository, times(1)).findById(livingConditionId);
//        verify(livingConditionRepository, never()).delete(any(LivingCondition.class));
//    }
//    @Test
//    void checkLivingConditionShouldReturnExistingLivingCondition() {
//        Long livingConditionId = 1L;
//        LivingCondition existingLivingCondition = new LivingCondition();
//        existingLivingCondition.setId(livingConditionId);
//        Optional<LivingCondition> optionalLivingCondition = Optional.of(existingLivingCondition);
//
//        when(livingConditionRepository.findById(livingConditionId)).thenReturn(optionalLivingCondition);
//
//        LivingCondition result = livingConditionService.checkLivingCondition(livingConditionId);
//
//        verify(livingConditionRepository, times(1)).findById(livingConditionId);
//        assertEquals(existingLivingCondition, result);
//    }
//
//    @Test
//    void checkLivingConditionShouldThrowExceptionWhenLivingConditionNotFound() {
//        Long livingConditionId = 1L;
//        Optional<LivingCondition> optionalLivingCondition = Optional.empty();
//
//        when(livingConditionRepository.findById(livingConditionId)).thenReturn(optionalLivingCondition);
//
//        Assertions.assertThrows(ApiRequestException.class, () -> livingConditionService.checkLivingCondition(livingConditionId));
//
//        verify(livingConditionRepository, times(1)).findById(livingConditionId);
//    }
//}
//
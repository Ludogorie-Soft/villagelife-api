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
    void getVillagePopulationAssertionByVillageIdEcoValue_ShouldCalculateAverage() {
        Long villageId = 1L;
        Village village = new Village();
        village.setId(villageId);
        boolean status = true;

        List<VillageLivingConditions> villageLivingConditionsList = new ArrayList<>();
        villageLivingConditionsList.add(new VillageLivingConditions(1L, village, new LivingCondition(1L, "Condition 1"), Consents.DISAGREE, true, now(), now())); // 0
        villageLivingConditionsList.add(new VillageLivingConditions(2L, village, new LivingCondition(2L, "Condition 2"), Consents.DISAGREE, true, now(), now())); // 0
        villageLivingConditionsList.add(new VillageLivingConditions(3L, village, new LivingCondition(3L, "Condition 3"), Consents.DISAGREE, true, now(), now())); // 0
        villageLivingConditionsList.add(new VillageLivingConditions(4L, village, new LivingCondition(4L, "Condition 4"), Consents.DISAGREE, true, now(), now())); // 0


        when(villageLivingConditionRepository.findAll()).thenReturn(villageLivingConditionsList);

        double result = villageLivingConditionService.getVillageLivingConditionByVillageIdEcoValue(villageId);

        Assertions.assertEquals(0, result, 0.01);

        verify(villageLivingConditionRepository, times(1)).findAll();
    }


    @Test
    void getVillagePopulationAssertionByVillageIdDelinquencyValue_ShouldReturnValue() {
        Long villageId = 1L;
        Village village = new Village();
        village.setId(villageId);
        boolean status = true;
        VillageLivingConditionRepository villageLivingConditionRepository = mock(VillageLivingConditionRepository.class);

        List<VillageLivingConditions> villageLivingConditionsList = new ArrayList<>();
        villageLivingConditionsList.add(new VillageLivingConditions(1L, village, new LivingCondition(1L, "в селото няма престъпност"), Consents.DISAGREE, true, now(), now())); // 0
        villageLivingConditionsList.add(new VillageLivingConditions(2L, village, new LivingCondition(1L, "Some other condition"), Consents.DISAGREE, true, now(), now())); // not relevant
        villageLivingConditionsList.add(new VillageLivingConditions(3L, village, new LivingCondition(1L, "Some other condition"), Consents.DISAGREE, true, now(), now())); // not relevant


        when(villageLivingConditionRepository.findAll()).thenReturn(villageLivingConditionsList);
        VillageLivingConditionService villageLivingConditionService = new VillageLivingConditionService(villageLivingConditionRepository, villageService, livingConditionService, modelMapper);

        double result = villageLivingConditionService.getVillageLivingConditionByVillageIdDelinquencyValue(villageId);

        Assertions.assertEquals(80, result, 0.01);

        verify(villageLivingConditionRepository, times(1)).findAll();
    }

    @Test
    void testGetVillageLivingConditionByVillageIdDelinquencyValueShouldReturnValue() {
        Long villageId = 1L;
        Village village = new Village();
        village.setId(villageId);

        List<VillageLivingConditions> villageLivingConditionsList = new ArrayList<>();

        villageLivingConditionsList.add(new VillageLivingConditions(1L, village, new LivingCondition(1L, "Some other condition"), Consents.DISAGREE, true, now(), now()));
        villageLivingConditionsList.add(new VillageLivingConditions(2L, village, new LivingCondition(1L, "Some other condition"), Consents.DISAGREE, true, now(), now()));
        villageLivingConditionsList.add(new VillageLivingConditions(3L, village, new LivingCondition(1L, "Some other condition"), Consents.DISAGREE, true, now(), now()));

        when(villageLivingConditionRepository.findAll()).thenReturn(villageLivingConditionsList);

        double result = villageLivingConditionService.getVillageLivingConditionByVillageIdDelinquencyValue(villageId);
        Assertions.assertEquals(50, result, 0.01);

        verify(villageLivingConditionRepository, times(1)).findAll();
    }


    @Test
    void getVillagePopulationAssertionByVillageIdDelinquencyValue_ShouldReturnExpectedValue() {
        Long villageId = 1L;
        Village village = new Village();
        village.setId(villageId);
        boolean status = true;

        VillageLivingConditionRepository villageLivingConditionRepository = mock(VillageLivingConditionRepository.class);

        List<VillageLivingConditions> villageLivingConditionsList = new ArrayList<>();
        villageLivingConditionsList.add(new VillageLivingConditions(1L, village, new LivingCondition(1L, "Some other condition"), Consents.DISAGREE, true, now(), now())); // not relevant
        villageLivingConditionsList.add(new VillageLivingConditions(2L, village, new LivingCondition(1L, "Some other condition"), Consents.DISAGREE, true, now(), now())); // not relevant
        villageLivingConditionsList.add(new VillageLivingConditions(3L, village, new LivingCondition(1L, "Some other condition"), Consents.DISAGREE, true, now(), now())); // not relevant


        when(villageLivingConditionRepository.findAll()).thenReturn(villageLivingConditionsList);

        VillageLivingConditionService villageLivingConditionService = new VillageLivingConditionService(villageLivingConditionRepository, villageService, livingConditionService, modelMapper);

        double result = villageLivingConditionService.getVillageLivingConditionByVillageIdDelinquencyValue(villageId);

        assertEquals(50, result, 0.01);

        verify(villageLivingConditionRepository, times(1)).findAll();
    }

    @Test
    void getVillagePopulationAssertionByVillageIdValueShouldCalculateAverage() {
        Long villageId = 1L;
        Village village = new Village();
        village.setId(villageId);

        VillageLivingConditionRepository villageLivingConditionRepository = mock(VillageLivingConditionRepository.class);
        List<VillageLivingConditions> villageLivingConditionsList = new ArrayList<>();

        villageLivingConditionsList.add(new VillageLivingConditions(1L, village, new LivingCondition(1L, "Name"), Consents.DISAGREE, true, LocalDateTime.now(), now()));
        villageLivingConditionsList.add(new VillageLivingConditions(1L, village, new LivingCondition(1L, "Name"), Consents.CANT_DECIDE, true, LocalDateTime.now(), now()));
        villageLivingConditionsList.add(new VillageLivingConditions(1L, village, new LivingCondition(1L, "Name"), Consents.CANT_DECIDE, true, LocalDateTime.now(), now()));
        villageLivingConditionsList.add(new VillageLivingConditions(1L, village, new LivingCondition(1L, "Name"), Consents.COMPLETELY_AGREED, true, LocalDateTime.now(), now()));

        when(villageLivingConditionRepository.findAll()).thenReturn(villageLivingConditionsList);

        VillageLivingConditionService villageLivingConditionService = new VillageLivingConditionService(villageLivingConditionRepository, villageService, livingConditionService, modelMapper);
        double result = villageLivingConditionService.getVillageLivingConditionByVillageIdValue(villageId);

        Assertions.assertEquals(60, result, 0.01);
        verify(villageLivingConditionRepository, times(1)).findAll();
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


    @Test
    void testGetVillageLivingConditionByVillageIdWithValidId() {
        Long villageId = 1L;

        VillageLivingConditions condition1 = new VillageLivingConditions();
        condition1.setId(1L);
        Village village1 = new Village(1L, "Village1", new Region(1L, "Region1"),

                LocalDateTime.now(), true, new Administrator(), LocalDateTime.now(), null, null, null, null, null, null, null, null, null, 0);
        condition1.setVillage(village1);


        VillageLivingConditions condition2 = new VillageLivingConditions();
        condition2.setId(2L);
        Village village2 = new Village(2L, "Village2", new Region(1L, "Region1"),

                 LocalDateTime.now(), true, new Administrator(), LocalDateTime.now(), null, null, null, null, null, null, null, null, null, 0);
        condition2.setVillage(village2);

        List<VillageLivingConditions> villageLivingConditionsList = List.of(condition1, condition2);

        when(villageLivingConditionRepository.findAll()).thenReturn(villageLivingConditionsList);

        VillageLivingConditionDTO dto1 = new VillageLivingConditionDTO();
        VillageLivingConditionDTO dto2 = new VillageLivingConditionDTO();

        when(modelMapper.map(any(VillageLivingConditions.class), eq(VillageLivingConditionDTO.class)))
                .thenReturn(dto1, dto2);

        List<VillageLivingConditionDTO> result = villageLivingConditionService.getVillageLivingConditionByVillageId(villageId);

        verify(villageLivingConditionRepository, times(1)).findAll();

        verify(modelMapper, times(1)).map(condition1, VillageLivingConditionDTO.class);
        verify(modelMapper, times(0)).map(condition2, VillageLivingConditionDTO.class);

    }

    @Test
    void testGetVillageLivingConditionByVillageIdWithNullId() {
        VillageLivingConditions condition1 = new VillageLivingConditions();
        condition1.setId(1L);

        VillageLivingConditions condition2 = new VillageLivingConditions();
        condition2.setId(2L);

        List<VillageLivingConditions> villageLivingConditionsList = List.of(condition1, condition2);

        when(villageLivingConditionRepository.findAll()).thenReturn(villageLivingConditionsList);

        VillageLivingConditionDTO dto1 = new VillageLivingConditionDTO();
        VillageLivingConditionDTO dto2 = new VillageLivingConditionDTO();

        when(modelMapper.map(any(VillageLivingConditions.class), eq(VillageLivingConditionDTO.class)))
                .thenReturn(dto1, dto2);

        List<VillageLivingConditionDTO> result = villageLivingConditionService.getVillageLivingConditionByVillageId(null);

        verify(villageLivingConditionRepository, times(1)).findAll();

        verify(modelMapper, times(1)).map(condition1, VillageLivingConditionDTO.class);
        verify(modelMapper, times(1)).map(condition2, VillageLivingConditionDTO.class);

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

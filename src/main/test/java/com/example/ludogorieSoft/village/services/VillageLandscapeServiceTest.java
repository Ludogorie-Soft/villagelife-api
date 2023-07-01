package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageLandscapeDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Landscape;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageLandscape;
import com.example.ludogorieSoft.village.repositories.VillageLandscapeRepository;
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

class VillageLandscapeServiceTest {
    @Mock
    private VillageLandscapeRepository villageLandscapeRepository;
    @Mock
    private VillageService villageService;
    @Mock
    private LandscapeService landscapeService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private VillageLandscapeService villageLandscapeService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getAllVillageLandscapesShouldReturnAllLandscapesAsDTOs() {
        List<VillageLandscape> villageLandscapes = new ArrayList<>();
        villageLandscapes.add(new VillageLandscape());
        villageLandscapes.add(new VillageLandscape());
        when(villageLandscapeRepository.findAll()).thenReturn(villageLandscapes);

        List<VillageLandscapeDTO> result = villageLandscapeService.getAllVillageLandscapes();

        verify(villageLandscapeRepository, times(1)).findAll();
        Assertions.assertEquals(villageLandscapes.size(), result.size());
    }

    @Test
    void getAllVillageLandscapesShouldReturnEmptyListWhenNoLandscapesExist() {
        when(villageLandscapeRepository.findAll()).thenReturn(new ArrayList<>());

        List<VillageLandscapeDTO> result = villageLandscapeService.getAllVillageLandscapes();

        verify(villageLandscapeRepository, times(1)).findAll();
        Assertions.assertEquals(0, result.size());
    }
    @Test
    void createVillageLandscapeShouldSaveVillageLandscapeAndReturnDTO() {
        VillageLandscapeDTO villageLandscapeDTO = new VillageLandscapeDTO();
        villageLandscapeDTO.setVillageId(1L);
        villageLandscapeDTO.setLandscapeId(2L);

        VillageLandscape villageLandscape = new VillageLandscape();
        Village village = new Village();
        Landscape landscape = new Landscape();

        when(villageService.checkVillage(anyLong())).thenReturn(village);
        when(landscapeService.checkLandscape(anyLong())).thenReturn(landscape);
        when(villageLandscapeRepository.save(any(VillageLandscape.class))).thenReturn(villageLandscape);
        when(villageLandscapeService.toDTO(villageLandscape)).thenReturn(villageLandscapeDTO);

        villageLandscape.setVillage(villageService.checkVillage(1L));
        villageLandscape.setLandscape(landscapeService.checkLandscape(2L));
        VillageLandscapeDTO result = villageLandscapeService.createVillageLandscape(villageLandscapeDTO);

        verify(villageService, times(2)).checkVillage(anyLong());
        verify(landscapeService, times(2)).checkLandscape(anyLong());
        verify(villageLandscapeRepository, times(1)).save(any(VillageLandscape.class));
        Assertions.assertEquals(villageLandscapeDTO, result);
    }
    @Test
    void getVillageLandscapeByIdShouldReturnVillageLandscapeDTOWhenFound() {
        Long id = 1L;
        VillageLandscape villageLandscape = new VillageLandscape();
        villageLandscape.setId(id);

        VillageLandscapeDTO villageLandscapeDTO = new VillageLandscapeDTO();
        villageLandscapeDTO.setId(id);

        when(villageLandscapeRepository.findById(id)).thenReturn(Optional.of(villageLandscape));
        when(villageLandscapeService.toDTO(villageLandscape)).thenReturn(villageLandscapeDTO);

        VillageLandscapeDTO result = villageLandscapeService.getVillageLandscapeById(id);

        verify(villageLandscapeRepository, times(1)).findById(id);
        Assertions.assertEquals(villageLandscape.getId(), result.getId());
    }

    @Test
    void getVillageLandscapeByIdShouldThrowExceptionWhenNotFound() {
        Long id = 1L;
        when(villageLandscapeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageLandscapeService.getVillageLandscapeById(id));
        verify(villageLandscapeRepository, times(1)).findById(id);
    }
    @Test
    void deleteVillageLandscapeByIdShouldReturnOneWhenDeletedSuccessfully() {
        Long id = 1L;
        doNothing().when(villageLandscapeRepository).deleteById(id);

        int result = villageLandscapeService.deleteVillageLandscapeById(id);

        verify(villageLandscapeRepository, times(1)).deleteById(id);
        Assertions.assertEquals(1, result);
    }

    @Test
    void deleteVillageLandscapeByIdShouldReturnZeroWhenIdNotFound() {
        Long id = 1L;
        doThrow(EmptyResultDataAccessException.class).when(villageLandscapeRepository).deleteById(id);

        int result = villageLandscapeService.deleteVillageLandscapeById(id);

        verify(villageLandscapeRepository, times(1)).deleteById(id);
        Assertions.assertEquals(0, result);
    }

    @Test
    void updateVillageLandscapeShouldReturnUpdatedVillageLandscapeDTOWhenFound() {
        Long id = 1L;
        VillageLandscapeDTO villageLandscapeDTO = new VillageLandscapeDTO();
        villageLandscapeDTO.setId(id);
        villageLandscapeDTO.setVillageId(2L);
        villageLandscapeDTO.setLandscapeId(3L);

        VillageLandscape foundVillageLandscape = new VillageLandscape();
        foundVillageLandscape.setId(id);
        Village village = new Village();
        Landscape landscape = new Landscape();

        when(villageLandscapeRepository.findById(id)).thenReturn(Optional.of(foundVillageLandscape));
        when(villageService.checkVillage(villageLandscapeDTO.getVillageId())).thenReturn(village);
        when(landscapeService.checkLandscape(villageLandscapeDTO.getLandscapeId())).thenReturn(landscape);
        when(villageLandscapeRepository.save(foundVillageLandscape)).thenReturn(foundVillageLandscape);
        when(villageLandscapeService.toDTO(foundVillageLandscape)).thenReturn(villageLandscapeDTO);

        foundVillageLandscape.setVillage(villageService.checkVillage(2L));
        foundVillageLandscape.setLandscape(landscapeService.checkLandscape(3L));

        VillageLandscapeDTO result = villageLandscapeService.updateVillageLandscape(id, villageLandscapeDTO);

        verify(villageLandscapeRepository, times(1)).findById(id);
        verify(villageService, times(2)).checkVillage(villageLandscapeDTO.getVillageId());
        verify(landscapeService, times(2)).checkLandscape(villageLandscapeDTO.getLandscapeId());
        verify(villageLandscapeRepository, times(1)).save(foundVillageLandscape);
        Assertions.assertEquals(villageLandscapeDTO, result);
    }

    @Test
    void updateVillageLandscapeShouldThrowExceptionWhenNotFound() {
        Long id = 1L;
        VillageLandscapeDTO villageLandscapeDTO = new VillageLandscapeDTO();
        villageLandscapeDTO.setVillageId(2L);
        villageLandscapeDTO.setLandscapeId(3L);

        when(villageLandscapeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageLandscapeService.updateVillageLandscape(id, villageLandscapeDTO));
        verify(villageLandscapeRepository, times(1)).findById(id);
        verify(villageService, never()).checkVillage(anyLong());
        verify(landscapeService, never()).checkLandscape(anyLong());
        verify(villageLandscapeRepository, never()).save(any(VillageLandscape.class));
    }

}

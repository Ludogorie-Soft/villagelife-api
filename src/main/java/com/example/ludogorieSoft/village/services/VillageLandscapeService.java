package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageLandscapeDTO;
import com.example.ludogorieSoft.village.model.GroundCategory;
import com.example.ludogorieSoft.village.model.Landscape;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageLandscape;
import com.example.ludogorieSoft.village.repositories.LandscapeRepository;
import com.example.ludogorieSoft.village.repositories.VillageLandscapeRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VillageLandscapeService {
    private final VillageLandscapeRepository villageLandscapeRepository;
    private final VillageRepository villageRepository;
    private final LandscapeRepository landscapeRepository;
    private final VillageService villageService;
    private final LandscapeService landscapeService;
    private final ModelMapper modelMapper;

    public VillageLandscapeDTO toDTO(VillageLandscape villageLandscape) {
        return modelMapper.map(villageLandscape, VillageLandscapeDTO.class);
    }

    public List<VillageLandscapeDTO> getAllVillageLandscapes() {
        List <VillageLandscape> villageLandscapes = villageLandscapeRepository.findAll();
        return villageLandscapes
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public VillageLandscapeDTO createVillageLandscape(VillageLandscapeDTO villageLandscapeDTO) {
        VillageLandscape villageLandscape = new VillageLandscape();

        Village village = villageService.checkVillage(villageLandscapeDTO.getVillageId());
        villageLandscape.setVillage(village);

        Landscape landscape = landscapeService.checkLandscape(villageLandscapeDTO.getLandscapeId());
        villageLandscape.setLandscape(landscape);

        villageLandscapeRepository.save(villageLandscape);
        return toDTO(villageLandscape);
    }

    public VillageLandscapeDTO getVillageLandscapeById(Long id) {
        Optional<VillageLandscape> villageLandscape = villageLandscapeRepository.findById(id);
        if (villageLandscape.isEmpty()) {
            throw new ApiRequestException("Village Landscape not found");
        }
        return toDTO(villageLandscape.get());
    }

    public int deleteVillageLandscapeById(Long id) {
        try {
            villageLandscapeRepository.deleteById(id);
            return 1;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public VillageLandscapeDTO updateVillageLandscape(Long id, VillageLandscapeDTO villageLandscapeDTO) {
        Optional<VillageLandscape> foundVillageLandscape = villageLandscapeRepository.findById(id);
        if (foundVillageLandscape.isEmpty()) {
            throw new ApiRequestException("Village Landscape not found");
        }
        Village village = villageService.checkVillage(villageLandscapeDTO.getVillageId());
        foundVillageLandscape.get().setVillage(village);

        Landscape landscape = landscapeService.checkLandscape(villageLandscapeDTO.getLandscapeId());
        foundVillageLandscape.get().setLandscape(landscape);

        villageLandscapeRepository.save(foundVillageLandscape.get());
        return toDTO(foundVillageLandscape.get());
    }



}

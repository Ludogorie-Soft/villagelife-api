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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VillageLandscapeService {
    private final VillageLandscapeRepository villageLandscapeRepository;
    private final VillageRepository villageRepository;
    private final LandscapeRepository landscapeRepository;
    private final ModelMapper modelMapper;

    public VillageLandscapeDTO toDTO(VillageLandscape villageLandscape) {
        return modelMapper.map(villageLandscape, VillageLandscapeDTO.class);
    }

    public List<VillageLandscapeDTO> getAllVillageLandscapes() {
        List <VillageLandscape> villageLandscapes = villageLandscapeRepository.findAll();
        return villageLandscapes
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public VillageLandscapeDTO createVillageLandscape(VillageLandscapeDTO villageLandscapeDTO) {
        VillageLandscape villageLandscape = new VillageLandscape();
        Optional<Village> village = villageRepository.findById(villageLandscapeDTO.getVillageId());
        if (village.isPresent()){
            villageLandscape.setVillage(village.get());
        }else {
            throw new ApiRequestException("Village not found");
        }
        Optional<Landscape> landscape = landscapeRepository.findById(villageLandscapeDTO.getLandscapeId());
        if (landscape.isPresent()){
            villageLandscape.setLandscape(landscape.get());
        }else {
            throw new ApiRequestException("Landscape not found");
        }
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
        Optional<Village> village = villageRepository.findById(villageLandscapeDTO.getVillageId());
        if (village.isPresent()){
            foundVillageLandscape.get().setVillage(village.get());
        }else {
            throw new ApiRequestException("Village not found");
        }
        Optional<Landscape> landscape = landscapeRepository.findById(villageLandscapeDTO.getLandscapeId());
        if (landscape.isPresent()){
            foundVillageLandscape.get().setLandscape(landscape.get());
        }else {
            throw new ApiRequestException("Landscape not found");
        }
        villageLandscapeRepository.save(foundVillageLandscape.get());
        return toDTO(foundVillageLandscape.get());
    }



}

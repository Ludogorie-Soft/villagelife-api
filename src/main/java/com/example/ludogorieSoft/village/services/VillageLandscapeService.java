package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageLandscapeDTO;
import com.example.ludogorieSoft.village.model.VillageLandscape;
import com.example.ludogorieSoft.village.repositories.VillageLandscapeRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
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

    public VillageLandscapeDTO createVillageLandscape(VillageLandscape villageLandscape) {
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

    public VillageLandscapeDTO updateVillageLandscape(Long id, VillageLandscape villageLandscape) {
        Optional<VillageLandscape> foundVillageLandscape = villageLandscapeRepository.findById(id);
        if (foundVillageLandscape.isEmpty()) {
            throw new ApiRequestException("Village Landscape not found");
        }
        foundVillageLandscape.get().setId(id);
        foundVillageLandscape.get().setLandscape(villageLandscape.getLandscape());
        foundVillageLandscape.get().setVillage(villageLandscape.getVillage());
        villageLandscapeRepository.save(foundVillageLandscape.get());
        return toDTO(foundVillageLandscape.get());
    }



}

package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.LandscapeDTO;
import com.example.ludogorieSoft.village.model.Landscape;
import com.example.ludogorieSoft.village.repositories.LandscapeRepository;
import com.example.ludogorieSoft.village.exceptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LandscapeService {
    private final LandscapeRepository landscapeRepository;
    private final ModelMapper modelMapper;
    public LandscapeDTO landscapeToLandscapeDTO(Landscape landscape) {
        return modelMapper.map(landscape, LandscapeDTO.class);
    }

    public List<LandscapeDTO> getAllLandscapes() {
        List<Landscape> landscapes = landscapeRepository.findAll();
        return landscapes
                .stream()
                .map(this::landscapeToLandscapeDTO)
                .toList();
    }

    public LandscapeDTO getLandscapeById(Long id) {
        Optional<Landscape> optionalLandscape = landscapeRepository.findById(id);
        if (optionalLandscape.isEmpty()) {
            throw new ApiRequestException("Landscape with id: " + id + " Not Found");
        }
        return landscapeToLandscapeDTO(optionalLandscape.get());
    }

    public LandscapeDTO createLandscape(LandscapeDTO landscapeDTO) {
        if (StringUtils.isBlank(landscapeDTO.getLandscapeName())) {
            throw new ApiRequestException("Landscape is blank");
        }
        if (landscapeRepository.existsByLandscapeName(landscapeDTO.getLandscapeName())) {
            throw new ApiRequestException("Landscape with name: " + landscapeDTO.getLandscapeName() + " already exists");
        }
        Landscape landscape = new Landscape();
        landscape.setLandscapeName(landscapeDTO.getLandscapeName());
        landscapeRepository.save(landscape);
        return landscapeDTO;
    }


    public LandscapeDTO updateLandscape(Long id, LandscapeDTO landscapeDTO) {
        Optional<Landscape> foundLandscape = landscapeRepository.findById(id);
        Landscape landscape = foundLandscape.orElseThrow(() -> new ApiRequestException("Landscape not found"));

        if (landscapeDTO == null || landscapeDTO.getLandscapeName() == null || landscapeDTO.getLandscapeName().isEmpty()) {
            throw new ApiRequestException("Invalid Landscape data");
        }

        String newLandscapeName = landscapeDTO.getLandscapeName();
        if (!newLandscapeName.equals(landscape.getLandscapeName())) {
            if (landscapeRepository.existsByLandscapeName(newLandscapeName)) {
                throw new ApiRequestException("Landscape with name: " + newLandscapeName + " already exists");
            }
            landscape.setLandscapeName(newLandscapeName);
            landscapeRepository.save(landscape);
        }

        return landscapeToLandscapeDTO(landscape);
    }

    public void deleteLandscape(Long id) {
        Optional<Landscape> landscape = landscapeRepository.findById(id);
        if (landscape.isEmpty()) {
            throw new ApiRequestException("Landscape not found for id " + id);
        }
        landscapeRepository.delete(landscape.get());
    }

    public Landscape checkLandscape(Long id) {
        Optional<Landscape> landscape = landscapeRepository.findById(id);
        if (landscape.isPresent()){
            return landscape.get();
        }else {
            throw new ApiRequestException("Landscape not found");
        }
    }

}

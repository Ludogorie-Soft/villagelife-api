package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.RegionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Region;
import com.example.ludogorieSoft.village.repositories.RegionRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;
    private final ModelMapper modelMapper;
    private static final String ERROR_MESSAGE = "Region not found";

    public RegionDTO regionToRegionDTO(Region region) {
        return modelMapper.map(region, RegionDTO.class);
    }

    public Region checkRegion(Long id) {
        Optional<Region> region = regionRepository.findById(id);
        if (region.isPresent()) {
            return region.get();
        } else {
            throw new ApiRequestException(ERROR_MESSAGE);
        }
    }

    public List<RegionDTO> getAllRegions() {
        List<Region> regions = regionRepository.findAllByOrderByIdAsc();
        return regions
                .stream()
                .map(this::regionToRegionDTO)
                .toList();
    }

    public RegionDTO getRegionById(Long id) {
        Optional<Region> region = regionRepository.findById(id);
        if (region.isEmpty()) {
            throw new ApiRequestException(ERROR_MESSAGE);
        }
        return regionToRegionDTO(region.get());
    }

    public RegionDTO createRegion(RegionDTO regionDTO) {
        if (StringUtils.isBlank(regionDTO.getRegionName())) {
            throw new ApiRequestException("Region name is blank");
        }
        if (regionRepository.existsByRegionName(regionDTO.getRegionName())) {
            throw new ApiRequestException("Region with name: " + regionDTO.getRegionName() + " already exists");
        }
        Region region = new Region();
        region.setRegionName(regionDTO.getRegionName());
        regionRepository.save(region);
        return regionDTO;
    }

    public RegionDTO updateRegion(Long id, RegionDTO regionDTO) {
        Optional<Region> findRegion = regionRepository.findById(id);
        Region region = findRegion.orElseThrow(() -> new ApiRequestException(ERROR_MESSAGE));

        if (regionDTO == null || regionDTO.getRegionName() == null || regionDTO.getRegionName().isEmpty()) {
            throw new ApiRequestException("Invalid region data");
        }

        String newRegionName = regionDTO.getRegionName();
        if (!newRegionName.equals(region.getRegionName())) {
            if (regionRepository.existsByRegionName(newRegionName)) {
                throw new ApiRequestException("Region: " + newRegionName + " already exists");
            }
            region.setRegionName(newRegionName);
            regionRepository.save(region);
        }

        return regionToRegionDTO(region);
    }

    public void deleteRegionById(Long id) {
        if (regionRepository.existsById(id)) {
            regionRepository.deleteById(id);
        } else {
            throw new ApiRequestException("Region with id: " + id + " not found");
        }
    }


    public RegionDTO findRegionByName(String name) {
        Region region = regionRepository.findByRegionName(name);
        if (region == null) {
            throw new ApiRequestException(ERROR_MESSAGE);
        }
        return regionToRegionDTO(region);
    }


}

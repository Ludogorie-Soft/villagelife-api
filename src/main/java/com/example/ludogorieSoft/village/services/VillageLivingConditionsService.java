package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageLivingConditionsDTO;
import com.example.ludogorieSoft.village.model.VillageLivingConditions;
import com.example.ludogorieSoft.village.repositories.VillageLivingConditionsRepository;
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
public class VillageLivingConditionsService {
    private final VillageLivingConditionsRepository livingConditionsRepository;
    private  final ModelMapper modelMapper;
    public VillageLivingConditionsDTO toDTO(VillageLivingConditions forMap){return modelMapper.map(forMap,VillageLivingConditionsDTO.class); }

    public List<VillageLivingConditionsDTO> getAllVillageLivingConditions() {
        List<VillageLivingConditions> villageLivingConditions = livingConditionsRepository.findAll();
        return villageLivingConditions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public VillageLivingConditionsDTO createVillageLivingConditionsDTO(VillageLivingConditions villageLivingConditions) {
        livingConditionsRepository.save(villageLivingConditions);
        return toDTO(villageLivingConditions);
    }

    public VillageLivingConditionsDTO getByID(Long id) {
        Optional<VillageLivingConditions> optionalVillageLivingConditions = livingConditionsRepository.findById(id);
        if (optionalVillageLivingConditions.isEmpty()) {
            throw new ApiRequestException("VillageLivingConditions Not Found");
        }
        return toDTO(optionalVillageLivingConditions.get());
    }

    public int deleteVillageLivingConditions(Long id) {
        try {
            livingConditionsRepository.deleteById(id);
            return 1;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }
}

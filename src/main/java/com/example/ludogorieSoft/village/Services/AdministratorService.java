package com.example.ludogorieSoft.village.Services;


import com.example.ludogorieSoft.village.DTOs.AdministratorDTO;
import com.example.ludogorieSoft.village.Model.Administrator;
import com.example.ludogorieSoft.village.Repositories.AdministratorRepository;
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
public class AdministratorService {
    private final AdministratorRepository administratorRepository;
    private final ModelMapper modelMapper;
    public AdministratorDTO administratorToAdministratorDTO(Administrator administrator){
        return modelMapper.map(administrator, AdministratorDTO.class);
    }
    public List<AdministratorDTO> getAllAdministrators() {
        List<Administrator> administrators = administratorRepository.findAll();
        return administrators
                .stream()
                .map(this::administratorToAdministratorDTO)
                .collect(Collectors.toList());
    }
    public AdministratorDTO createAdministrator(Administrator administrator) {
        administratorRepository.save(administrator);
        return administratorToAdministratorDTO(administrator);
    }
    public AdministratorDTO getAdministratorById(Long id) {
        Optional<Administrator> administrator = administratorRepository.findById(id);
        if (administrator.isEmpty()) {
            throw new ApiRequestException("Administrator not found");
        }
        return administratorToAdministratorDTO(administrator.get());
    }
    public int deleteAdministratorById(Long id) {
        try {
            administratorRepository.deleteById(id);
            return 1;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }
    public AdministratorDTO updateAdministrator(Long id, Administrator administrator) {
        Optional<Administrator> foundAdministrator = administratorRepository.findById(id);
        if (foundAdministrator.isEmpty()) {
            throw new ApiRequestException("Administrator not found");
        }
        foundAdministrator.get().setFullName(administrator.getFullName());
        foundAdministrator.get().setEmail(administrator.getEmail());
        foundAdministrator.get().setUsername(administrator.getUsername());
        foundAdministrator.get().setPassword(administrator.getPassword());
        foundAdministrator.get().setMobile(administrator.getMobile());
        foundAdministrator.get().setCreatedAt(administrator.getCreatedAt());
        administratorRepository.save(foundAdministrator.get());
        return administratorToAdministratorDTO(foundAdministrator.get());
    }
}

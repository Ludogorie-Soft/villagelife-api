package com.example.ludogoriesoft.village.services;


import com.example.ludogoriesoft.village.dtos.AdministratorDTO;
import com.example.ludogoriesoft.village.dtos.AdministratorRequest;
import com.example.ludogoriesoft.village.exeptions.ApiRequestException;
import com.example.ludogoriesoft.village.model.Administrator;
import com.example.ludogoriesoft.village.repositories.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdministratorService {
    private final AdministratorRepository administratorRepository;
    private final ModelMapper modelMapper;
    public AdministratorDTO administratorToAdministratorDTO(Administrator administrator){
        return modelMapper.map(administrator, AdministratorDTO.class);
    }
    public Administrator administratorRequestToAdministrator(AdministratorRequest administratorRequest){
        return modelMapper.map(administratorRequest, Administrator.class);
    }
    public List<AdministratorDTO> getAllAdministrators() {
        List<Administrator> administrators = administratorRepository.findAll();
        return administrators
                .stream()
                .map(this::administratorToAdministratorDTO)
                .toList();
    }
    public AdministratorDTO createAdministrator(AdministratorRequest administratorRequest) {
        if (administratorRepository.existsByUsername(administratorRequest.getUsername())) {
            throw new ApiRequestException("Administrator already exists");
        }
        Administrator administrator = administratorRequestToAdministrator(administratorRequest);
        String hashedPassword = BCrypt.hashpw(administrator.getPassword(), BCrypt.gensalt());
        administrator.setPassword(hashedPassword);
        administratorRepository.save(administrator);
        administrator = administratorRepository.findByUsername(administratorRequest.getUsername());
        return administratorToAdministratorDTO(administrator);
    }
    public AdministratorDTO getAdministratorById(Long id) {
        Optional<Administrator> administrator = administratorRepository.findById(id);
        if (administrator.isEmpty()) {
            throw new ApiRequestException("Administrator not found");
        }
        return administratorToAdministratorDTO(administrator.get());
    }
    public void deleteAdministratorById(Long id) {
        if (administratorRepository.existsById(id)) {
            administratorRepository.deleteById(id);
        } else {
            throw new ApiRequestException("Administrator with id " + id + " not found");
        }
    }
    public AdministratorDTO updateAdministrator(Long id, AdministratorRequest administratorRequest) {
        Optional<Administrator> foundAdministrator = administratorRepository.findById(id);
        if (foundAdministrator.isEmpty()) {
            throw new ApiRequestException("Administrator not found");
        }
        foundAdministrator.get().setFullName(administratorRequest.getFullName());
        foundAdministrator.get().setEmail(administratorRequest.getEmail());
        foundAdministrator.get().setUsername(administratorRequest.getUsername());
        foundAdministrator.get().setPassword(administratorRequest.getPassword());
        foundAdministrator.get().setMobile(administratorRequest.getMobile());
        foundAdministrator.get().setCreatedAt(foundAdministrator.get().getCreatedAt());
        administratorRepository.save(foundAdministrator.get());
        return administratorToAdministratorDTO(foundAdministrator.get());
    }

    public Administrator findAdminByUsername(String username){
        Administrator administrator =  administratorRepository.findByUsername(username);
        return administrator; //administratorToAdministratorDTO(administrator);
    }

}

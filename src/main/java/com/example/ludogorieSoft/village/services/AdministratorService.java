package com.example.ludogorieSoft.village.services;


import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.request.AdministratorRequest;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Administrator;
import com.example.ludogorieSoft.village.model.Region;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.AdministratorRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AdministratorService {
    private final AdministratorRepository administratorRepository;
    private final ModelMapper modelMapper;
    private final VillageRepository villageRepository;

    public AdministratorDTO administratorToAdministratorDTO(Administrator administrator) {
        return modelMapper.map(administrator, AdministratorDTO.class);
    }

    public Administrator administratorRequestToAdministrator(AdministratorRequest administratorRequest) {
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
        if (administratorRequest.getPassword() == null || administratorRequest.getPassword().isEmpty() ) {

            foundAdministrator.get().setPassword(foundAdministrator.get().getPassword());
        } else {

            foundAdministrator.get().setPassword(BCrypt.hashpw(administratorRequest.getPassword(), BCrypt.gensalt()));
        }
        foundAdministrator.get().setMobile(administratorRequest.getMobile());
        foundAdministrator.get().setCreatedAt(administratorRequest.getCreatedAt());
        foundAdministrator.get().setRole(administratorRequest.getRole());

        administratorRepository.save(foundAdministrator.get());
        return administratorToAdministratorDTO(foundAdministrator.get());
    }

    public Administrator findAdminByUsername(String username) {
        return administratorRepository.findByUsername(username);
    }


    public List<VillageResponse> getAllVillagesWithPopulation() {
        List<Object[]> results = villageRepository.findAllVillagesWithPopulation();

        List<VillageResponse> villageResponses = new ArrayList<>();
        for (Object[] result : results) {
            Village village = (Village) result[0];
            Region region = (Region) result[1];
            Administrator administrator = (Administrator) result[2];

            VillageResponse response = new VillageResponse();
            response.setId(village.getId());
            response.setName(village.getName());
            response.setRegion(region);
            response.setDateUpload(village.getDateUpload());

            if (administrator != null) {
                response.setAdmin(administrator);
                response.setDateApproved(village.getDateApproved());
            } else {
                response.setAdmin(null);
                response.setDateApproved(null);
            }

            villageResponses.add(response);
        }

        return villageResponses;
    }

}

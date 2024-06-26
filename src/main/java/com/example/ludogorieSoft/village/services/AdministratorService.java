package com.example.ludogorieSoft.village.services;


import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.request.AdministratorRequest;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Administrator;
import com.example.ludogorieSoft.village.repositories.AdministratorRepository;
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

    public AdministratorDTO findAdminByUsername(String username) {
        Administrator administrator = administratorRepository.findByUsername(username);
        return administratorToAdministratorDTO(administrator);
    }

}

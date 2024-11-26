package com.example.ludogorieSoft.village.services;


import com.example.ludogorieSoft.village.dtos.AlternativeUserDTO;
import com.example.ludogorieSoft.village.dtos.BusinessCardDTO;
import com.example.ludogorieSoft.village.dtos.request.AdministratorRequest;
import com.example.ludogorieSoft.village.enums.Role;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.AlternativeUser;
import com.example.ludogorieSoft.village.repositories.AlternativeUserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AdministratorService {
    private final AlternativeUserRepository alternativeUserRepository;
    private final ModelMapper modelMapper;

    public AlternativeUser administratorDTOToAdministrator(AlternativeUserDTO alternativeUserDTO) {
        return modelMapper.map(alternativeUserDTO, AlternativeUser.class);
    }
    public AlternativeUserDTO administratorToAdministratorDTO(AlternativeUser alternativeUser) {
        AlternativeUserDTO alternativeUserDTO = modelMapper.map(alternativeUser, AlternativeUserDTO.class);
        if(alternativeUserDTO.getRole() != Role.USER && alternativeUserDTO.getRole() != Role.ADMIN) {
            alternativeUserDTO.setBusinessCardDTO(modelMapper.map(alternativeUser.getBusinessCard(), BusinessCardDTO.class));
        }
        return alternativeUserDTO;
    }

    public AlternativeUser administratorRequestToAdministrator(AdministratorRequest administratorRequest) {
        return modelMapper.map(administratorRequest, AlternativeUser.class);
    }

    public List<AlternativeUserDTO> getAllAdministrators() {
        List<AlternativeUser> alternativeUsers = alternativeUserRepository.findAll();
        return alternativeUsers
                .stream()
                .map(this::administratorToAdministratorDTO)
                .toList();
    }

    public AlternativeUserDTO getAdministratorById(Long id) {
        Optional<AlternativeUser> administrator = alternativeUserRepository.findById(id);
        if (administrator.isEmpty()) {
            throw new ApiRequestException("Administrator not found");
        }
        return administratorToAdministratorDTO(administrator.get());
    }

    public void deleteAdministratorById(Long id) {
        if (alternativeUserRepository.existsById(id)) {
            alternativeUserRepository.deleteById(id);
        } else {
            throw new ApiRequestException("Administrator with id " + id + " not found");
        }
    }

    public AlternativeUserDTO updateAdministrator(Long id, AdministratorRequest administratorRequest) {
        Optional<AlternativeUser> foundAdministrator = alternativeUserRepository.findById(id);

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

        alternativeUserRepository.save(foundAdministrator.get());
        return administratorToAdministratorDTO(foundAdministrator.get());
    }

    public AlternativeUserDTO findAdminByUsername(String username) {
        AlternativeUser alternativeUser = alternativeUserRepository.findByUsername(username);
        return administratorToAdministratorDTO(alternativeUser);
    }

}

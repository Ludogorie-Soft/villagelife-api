package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.AlternativeUserDTO;
import com.example.ludogorieSoft.village.dtos.UserSearchDataDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Region;
import com.example.ludogorieSoft.village.model.UserSearchData;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.RegionRepository;
import com.example.ludogorieSoft.village.repositories.UserSearchDataRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSearchDataService {
    private final UserSearchDataRepository userSearchDataRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;
    private final VillageRepository villageRepository;
    private final RegionRepository regionRepository;

    public UserSearchData userSearchDataDTOToUserSearchData(UserSearchDataDTO userSearchDataDTO) {
        return modelMapper.map(userSearchDataDTO, UserSearchData.class);
    }

    public UserSearchDataDTO userSearchDataToUserSearchDataDTO(UserSearchData userSearchData) {
        return modelMapper.map(userSearchData, UserSearchDataDTO.class);
    }

    public UserSearchDataDTO createUserSearchData(UserSearchDataDTO userSearchDataDTO) {
        AlternativeUserDTO alternativeUserDTO = authService.getAdministratorInfo();
        userSearchDataDTO.setAlternativeUserDTO(alternativeUserDTO);
        checkUserSearchDataValidations(userSearchDataDTO);
        UserSearchData userSearchData = userSearchDataRepository.save(userSearchDataDTOToUserSearchData(userSearchDataDTO));
        return userSearchDataToUserSearchDataDTO(userSearchData);
    }

    public List<UserSearchDataDTO> getAllUserSearchDataDTOsForUser(Long id) {
        List<UserSearchData> userSearchDataList = userSearchDataRepository.findAllByAlternativeUserId(id);
        return userSearchDataList
                .stream()
                .map(this::userSearchDataToUserSearchDataDTO)
                .toList();
    }

    private void checkUserSearchDataValidations(UserSearchDataDTO userSearchDataDTO) {
        if (userSearchDataDTO.getSearchName().isBlank()) throw new ApiRequestException("Search name is required!");
        if (userSearchDataDTO.getSearchName().length() > 50) throw new ApiRequestException("Search name can not be more than 50 signs long!");
        checkSearchNameForAlternativeUser(userSearchDataDTO);
        checkBuiltUpAreaValidations(userSearchDataDTO);
        checkYardAreaValidations(userSearchDataDTO);
        checkRoomsCountValidations(userSearchDataDTO);
        checkBathroomsCountValidations(userSearchDataDTO);
        checkConstructionYearValidations(userSearchDataDTO);
        checkPriceValidations(userSearchDataDTO);
        checkSearchForRegion(userSearchDataDTO);
        checkSearchForVillage(userSearchDataDTO);
    }

    private void checkSearchNameForAlternativeUser(UserSearchDataDTO userSearchDataDTO) {
        UserSearchData userSearchData = userSearchDataRepository.findBySearchNameAndAlternativeUserId(userSearchDataDTO.getSearchName(),
                userSearchDataDTO.getAlternativeUserDTO().getId());
        if (userSearchData != null) throw new ApiRequestException("User search data with the same search name and user already exists!");
    }

    private void checkBuiltUpAreaValidations(UserSearchDataDTO userSearchDataDTO) {
        if (userSearchDataDTO.getMinBuiltUpArea() != null && userSearchDataDTO.getMinBuiltUpArea() < 0) throw new ApiRequestException("Minimal built up area can not be less than 0!");
        if (userSearchDataDTO.getMaxBuiltUpArea() != null && userSearchDataDTO.getMaxBuiltUpArea() < 0) throw new ApiRequestException("Maximal built up area can not be less than 0!");
        if (userSearchDataDTO.getMinBuiltUpArea() != null && userSearchDataDTO.getMaxBuiltUpArea() != null
                && userSearchDataDTO.getMinBuiltUpArea() > userSearchDataDTO.getMaxBuiltUpArea())
            throw new ApiRequestException("Minimal built up area can not be more than maximal built up area!");
    }

    private void checkYardAreaValidations(UserSearchDataDTO userSearchDataDTO) {
        if (userSearchDataDTO.getMinYardArea() != null && userSearchDataDTO.getMinYardArea() < 0) throw new ApiRequestException("Minimal yard area can not be less than 0!");
        if (userSearchDataDTO.getMaxYardArea() != null && userSearchDataDTO.getMaxYardArea() < 0) throw new ApiRequestException("Maximal yard area can not be less than 0!");
        if (userSearchDataDTO.getMinYardArea() != null && userSearchDataDTO.getMaxYardArea() != null
                && userSearchDataDTO.getMinYardArea() > userSearchDataDTO.getMaxYardArea())
            throw new ApiRequestException("Minimal yard area can not be more than maximal yard area!");
    }

    private void checkRoomsCountValidations(UserSearchDataDTO userSearchDataDTO) {
        if (userSearchDataDTO.getMinRoomsCount() != null && userSearchDataDTO.getMinRoomsCount() < 0) throw new ApiRequestException("Minimal room count can not be less than 0!");
        if (userSearchDataDTO.getMaxRoomsCount() != null && userSearchDataDTO.getMaxRoomsCount() < 0) throw new ApiRequestException("Maximal room count can not be less than 0!");
        if (userSearchDataDTO.getMinRoomsCount() != null && userSearchDataDTO.getMaxRoomsCount() != null
                && userSearchDataDTO.getMinRoomsCount() > userSearchDataDTO.getMaxRoomsCount())
            throw new ApiRequestException("Minimal room count can not be more than maximal room count!");
    }

    private void checkBathroomsCountValidations(UserSearchDataDTO userSearchDataDTO) {
        if (userSearchDataDTO.getMinBathroomsCount() != null && userSearchDataDTO.getMinBathroomsCount() < 0) throw new ApiRequestException("Minimal bathroom count can not be less than 0!");
        if (userSearchDataDTO.getMaxBathroomsCount() != null && userSearchDataDTO.getMaxBathroomsCount() < 0) throw new ApiRequestException("Maximal bathroom count can not be less than 0!");
        if (userSearchDataDTO.getMinBathroomsCount() != null && userSearchDataDTO.getMaxBathroomsCount() != null
                && userSearchDataDTO.getMinBathroomsCount() > userSearchDataDTO.getMaxBathroomsCount())
            throw new ApiRequestException("Minimal bathroom count can not be more than maximal bathroom count!");
    }

    private void checkConstructionYearValidations(UserSearchDataDTO userSearchDataDTO) {
        if (userSearchDataDTO.getMinConstructionYear() != null && userSearchDataDTO.getMinConstructionYear() < 0) throw new ApiRequestException("Minimal construction year can not be less than 0!");
        if (userSearchDataDTO.getMaxConstructionYear() != null && userSearchDataDTO.getMaxConstructionYear() < 0) throw new ApiRequestException("Maximal construction year can not be less than 0!");
        if (userSearchDataDTO.getMinConstructionYear() != null && userSearchDataDTO.getMaxConstructionYear() != null
                && userSearchDataDTO.getMinConstructionYear() > userSearchDataDTO.getMaxConstructionYear())
            throw new ApiRequestException("Minimal construction year can not be more than maximal construction year!");
    }

    private void checkPriceValidations(UserSearchDataDTO userSearchDataDTO) {
        BigDecimal zero = BigDecimal.valueOf(0.0);
        if (userSearchDataDTO.getMinPrice() != null && userSearchDataDTO.getMinPrice().compareTo(zero) < 0) throw new ApiRequestException("Minimal price can not be less than 0!");
        if (userSearchDataDTO.getMaxPrice() != null && userSearchDataDTO.getMaxPrice().compareTo(zero) < 0) throw new ApiRequestException("Maximal price can not be less than 0!");
        if (userSearchDataDTO.getMinPrice() != null && userSearchDataDTO.getMaxPrice() != null
                && userSearchDataDTO.getMinPrice().compareTo(userSearchDataDTO.getMaxPrice()) > 0)
            throw new ApiRequestException("Minimal price can not be more than maximal price!");
    }

    private void checkSearchForVillage(UserSearchDataDTO userSearchDataDTO) {
        if (userSearchDataDTO.getVillageName() == null) return;
        Village village = villageRepository.findSingleVillageByNameAndRegionName(userSearchDataDTO.getVillageName(), userSearchDataDTO.getRegionName());
        if (village == null) throw new ApiRequestException("Village not found!");
    }

    private void checkSearchForRegion(UserSearchDataDTO userSearchDataDTO) {
        if (userSearchDataDTO.getRegionName() == null) return;
        Region region = regionRepository.findByRegionName(userSearchDataDTO.getRegionName());
        if (region == null) throw new ApiRequestException("Region not found!");
    }
}

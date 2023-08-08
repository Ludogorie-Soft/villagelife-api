package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.config.DatabaseUtils;
import com.example.ludogorieSoft.village.dtos.VillageGroundCategoryDTO;
import com.example.ludogorieSoft.village.model.GroundCategory;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageGroundCategory;
import com.example.ludogorieSoft.village.repositories.VillageGroundCategoryRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class VillageGroundCategoryService {

    private final VillageGroundCategoryRepository villageGroundCategoryRepository;
    private final ModelMapper modelMapper;
    private final VillageService villageService;
    private final GroundCategoryService groundCategoryService;

    public VillageGroundCategoryDTO toDTO(VillageGroundCategory forMap) {
        return modelMapper.map(forMap, VillageGroundCategoryDTO.class);
    }

    public List<VillageGroundCategoryDTO> getAllVillageGroundCategories() {
        List<VillageGroundCategory> villageGroundCategories = villageGroundCategoryRepository.findAll();
        return villageGroundCategories.stream()
                .map(this::toDTO)
                .toList();
    }

    public VillageGroundCategoryDTO getByID(Long id) {
        Optional<VillageGroundCategory> optionalVillageGroundCategory = villageGroundCategoryRepository.findById(id);
        if (optionalVillageGroundCategory.isEmpty()) {
            throw new ApiRequestException("Village Ground Category with id: " + id + " Not Found");
        }
        return toDTO(optionalVillageGroundCategory.get());
    }

    public VillageGroundCategoryDTO createVillageGroundCategoryDTO(VillageGroundCategoryDTO villageGroundCategoryDTO) {
        VillageGroundCategory villageGroundCategory = new VillageGroundCategory();

        Village village = villageService.checkVillage(villageGroundCategoryDTO.getVillageId());
        villageGroundCategory.setVillage(village);

        GroundCategory groundCategory = groundCategoryService.checkGroundCategory(villageGroundCategoryDTO.getGroundCategoryId());
        villageGroundCategory.setGroundCategory(groundCategory);

        villageGroundCategory.setVillageStatus(villageGroundCategoryDTO.getStatus());
        villageGroundCategory.setDateUpload(villageGroundCategoryDTO.getDateUpload());
        villageGroundCategoryRepository.save(villageGroundCategory);
        return toDTO(villageGroundCategory);
    }

    public VillageGroundCategoryDTO updateVillageGroundCategory(Long id, VillageGroundCategoryDTO villageGroundCategoryDTO) {
        Optional<VillageGroundCategory> foundVillageGroundCategory = villageGroundCategoryRepository.findById(id);
        if (foundVillageGroundCategory.isEmpty()) {
            throw new ApiRequestException("Village Ground Category Not Found");
        }

        Village village = villageService.checkVillage(villageGroundCategoryDTO.getVillageId());
        foundVillageGroundCategory.get().setVillage(village);

        GroundCategory groundCategory = groundCategoryService.checkGroundCategory(villageGroundCategoryDTO.getGroundCategoryId());
        foundVillageGroundCategory.get().setGroundCategory(groundCategory);

        villageGroundCategoryRepository.save(foundVillageGroundCategory.get());
        return toDTO(foundVillageGroundCategory.get());
    }

    public int deleteVillageGroundCategory(Long id) {
        try {
            villageGroundCategoryRepository.deleteById(id);
            return 1;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public VillageGroundCategoryDTO findVillageGroundCategoryDTOByVillageId(Long villageId){
        VillageGroundCategory villageGroundCategory = villageGroundCategoryRepository.findByVillageId(villageId);
        if(villageGroundCategory == null){
            throw new ApiRequestException("Village ground category with village id " + villageId + " not found");
        }
        return toDTO(villageGroundCategory);
    }


    public void updateVillageGroundCategories(Long villageId, Long groundCategoryId) {
        try (Connection connection = DatabaseUtils.getConnection()) {
            if (villageId != null && groundCategoryId != null) {
                executeUpdate(connection, villageId, groundCategoryId);
            } else {
                throw new IllegalArgumentException("VillageGroundCategory or its IDs are null.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void executeUpdate(Connection connection, Long villageId, Long groundCategoryId) throws SQLException {
        String sqlQuery = "UPDATE village_ground_categories " +
                "SET ground_category_id = ? " +
                "WHERE village_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setLong(1, groundCategoryId);
            statement.setLong(2, villageId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean isVillageExists(Long villageId) {
        boolean exists = false;

        try (Connection connection = DatabaseUtils.getConnection()) {
            exists = checkVillageExists(connection, villageId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return exists;
    }

    private boolean checkVillageExists(Connection connection, Long villageId) throws SQLException {
        String sqlQuery = "SELECT COUNT(*) FROM village_ground_categories WHERE village_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setLong(1, villageId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}

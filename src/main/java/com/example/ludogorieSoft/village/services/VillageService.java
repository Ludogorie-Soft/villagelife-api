package com.example.ludogoriesoft.village.services;

import com.example.ludogoriesoft.village.dtos.VillageDTO;
import com.example.ludogoriesoft.village.model.Population;
import com.example.ludogoriesoft.village.model.Village;
import com.example.ludogoriesoft.village.repositories.VillageRepository;
import com.example.ludogoriesoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VillageService {

    private final VillageRepository villageRepository;
    private final ModelMapper modelMapper;
    private final PopulationService populationService;
    private final String errorMessage1="Village with id ";
    private final String errorMessage2=" not found  ";


    public VillageDTO villageToVillageDTO(Village village){
        return modelMapper.map(village, VillageDTO.class);
    }

    public List<VillageDTO> getAllVillages() {
        List<Village> villages = villageRepository.findAll();
        return villages.stream()
                .map(this::villageToVillageDTO)
                .toList();
    }


    public VillageDTO getVillageById(Long id) {
        Optional<Village> optionalVillage = villageRepository.findById(id);
        if (optionalVillage.isPresent()) {
            return villageToVillageDTO(optionalVillage.get());
        } else {
            throw new ApiRequestException(errorMessage1 + id + errorMessage2);
        }
    }


    //public VillageDTO createVillage(VillageDTO villageDTO) {
    //    Village village = new Village();
    //    village.setPopulation(modelMapper.map(villageDTO.getPopulationDTO(), Population.class));
    //    village.setName(villageDTO.getName());
    //    //Village village = modelMapper.map(villageDTO, Village.class);
    //    villageRepository.save(village);
    //    return modelMapper.map(village, VillageDTO.class);
    //}
    public VillageDTO createVillage(VillageDTO villageDTO) {
        Village village = new Village();
//        village.setPopulation(modelMapper.map(villageDTO.getPopulationDTO(), Population.class));
        village.setName(villageDTO.getName());
        Village savedVillage = villageRepository.save(village);
        villageDTO.setId(savedVillage.getId());
        return modelMapper.map(village, VillageDTO.class);
    }


    public VillageDTO updateVillage(Long id, VillageDTO villageDTO) {
        Optional<Village> optionalVillage = villageRepository.findById(id);
        if (optionalVillage.isPresent()) {
            Village village = optionalVillage.get();
            village.setName(villageDTO.getName());
            //village.setPopulation(villageDTO.getPopulation());
//            village.setPopulation(modelMapper.map(villageDTO.getPopulationDTO(), Population.class));
            villageRepository.save(village);
            return modelMapper.map(village, VillageDTO.class);
        } else {
            throw new ApiRequestException(errorMessage1 + id + errorMessage2);
        }
    }


    public void deleteVillage(Long id) {
        if (villageRepository.existsById(id)) {
            villageRepository.deleteById(id);
        } else {
            throw new ApiRequestException(errorMessage1 + id + errorMessage2);
        }
    }
    public Village checkVillage(Long id){
        Optional<Village> village = villageRepository.findById(id);
        if (village.isPresent()){
            return village.get();
        }else {
            throw new ApiRequestException("Village not found");
        }
    }

}

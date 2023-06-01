package com.example.ludogorieSoft.village.Services;

import com.example.ludogorieSoft.village.DTOs.AdministratorDTO;
import com.example.ludogorieSoft.village.DTOs.VillageAnswerQuestionDTO;
import com.example.ludogorieSoft.village.Model.Administrator;
import com.example.ludogorieSoft.village.Model.VillageAnswerQuestion;
import com.example.ludogorieSoft.village.Repositories.VillageAnswerQuestionRepository;
import com.example.ludogorieSoft.village.Repositories.VillageLandscapeRepository;
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
public class VillageAnswerQuestionService {
    private final VillageAnswerQuestionRepository villageAnswerQuestionRepository;
    private final ModelMapper modelMapper;
    public VillageAnswerQuestionDTO toDTO(VillageAnswerQuestion villageAnswerQuestion){
        return modelMapper.map(villageAnswerQuestion, VillageAnswerQuestionDTO.class);}


    public List<VillageAnswerQuestionDTO> getAllVillageAnswerQuestions() {
        List<VillageAnswerQuestion> villageAnswerQuestions = villageAnswerQuestionRepository.findAll();
        return villageAnswerQuestions
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public VillageAnswerQuestionDTO createVillageAnswerQuestion(VillageAnswerQuestion villageAnswerQuestion) {
        villageAnswerQuestionRepository.save(villageAnswerQuestion);
        return toDTO(villageAnswerQuestion);
    }

    public VillageAnswerQuestionDTO getVillageAnswerQuestionById(Long id) {
        Optional<VillageAnswerQuestion> villageAnswerQuestion = villageAnswerQuestionRepository.findById(id);
        if (villageAnswerQuestion.isEmpty()) {
            throw new ApiRequestException("VillageAnswerQuestion not found");
        }
        return toDTO(villageAnswerQuestion.get());
    }

    public int deleteVillageAnswerQuestionById(Long id) {
        try {
            villageAnswerQuestionRepository.deleteById(id);
            return 1;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public VillageAnswerQuestionDTO updateVillageAnswerQuestion(Long id, VillageAnswerQuestion villageAnswerQuestion) {
        Optional<VillageAnswerQuestion> foundVillageAnswerQuestion = villageAnswerQuestionRepository.findById(id);
        if (foundVillageAnswerQuestion.isEmpty()) {
            throw new ApiRequestException("VillageAnswerQuestion not found");
        }
        foundVillageAnswerQuestion.get().setId(villageAnswerQuestion.getId());
        foundVillageAnswerQuestion.get().setQuestionsID(villageAnswerQuestion.getQuestionsID());
        foundVillageAnswerQuestion.get().setAnswer(villageAnswerQuestion.getAnswer());
        foundVillageAnswerQuestion.get().setVillageID(villageAnswerQuestion.getVillageID());
        villageAnswerQuestionRepository.save(foundVillageAnswerQuestion.get());
        return toDTO(foundVillageAnswerQuestion.get());
    }

}

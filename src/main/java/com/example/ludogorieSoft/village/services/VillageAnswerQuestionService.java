package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageAnswerQuestionDTO;
import com.example.ludogorieSoft.village.model.Question;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageAnswerQuestion;
import com.example.ludogorieSoft.village.repositories.QuestionRepository;
import com.example.ludogorieSoft.village.repositories.VillageAnswerQuestionRepository;
import com.example.ludogorieSoft.village.exceptions.ApiRequestException;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VillageAnswerQuestionService {
    private final VillageAnswerQuestionRepository villageAnswerQuestionRepository;
    private final VillageRepository villageRepository;
    private final QuestionRepository questionRepository;
    private final ModelMapper modelMapper;
    private final VillageService villageService;
    private final QuestionService questionService;

    public VillageAnswerQuestionDTO toDTO(VillageAnswerQuestion villageAnswerQuestion) {
        return modelMapper.map(villageAnswerQuestion, VillageAnswerQuestionDTO.class);
    }


    public List<VillageAnswerQuestionDTO> getAllVillageAnswerQuestions() {
        List<VillageAnswerQuestion> villageAnswerQuestions = villageAnswerQuestionRepository.findAll();
        return villageAnswerQuestions
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public VillageAnswerQuestionDTO createVillageAnswerQuestion(VillageAnswerQuestionDTO villageAnswerQuestionDTO) {
        VillageAnswerQuestion villageAnswerQuestion = new VillageAnswerQuestion();

        Village village = villageService.checkVillage(villageAnswerQuestionDTO.getVillageId());
        villageAnswerQuestion.setVillage(village);

        Question question = questionService.checkQuestion(villageAnswerQuestionDTO.getQuestionId());
        villageAnswerQuestion.setQuestion(question);

        villageAnswerQuestion.setAnswer(villageAnswerQuestionDTO.getAnswer());
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

    public VillageAnswerQuestionDTO updateVillageAnswerQuestion(Long id, VillageAnswerQuestionDTO villageAnswerQuestionDTO) {
        Optional<VillageAnswerQuestion> foundVillageAnswerQuestion = villageAnswerQuestionRepository.findById(id);
        if (foundVillageAnswerQuestion.isEmpty()) {
            throw new ApiRequestException("VillageAnswerQuestion not found");
        }
        Village village = villageService.checkVillage(villageAnswerQuestionDTO.getVillageId());
        foundVillageAnswerQuestion.get().setVillage(village);

        Question question = questionService.checkQuestion(villageAnswerQuestionDTO.getQuestionId());
        foundVillageAnswerQuestion.get().setQuestion(question);

        foundVillageAnswerQuestion.get().setAnswer(villageAnswerQuestionDTO.getAnswer());
        villageAnswerQuestionRepository.save(foundVillageAnswerQuestion.get());
        return toDTO(foundVillageAnswerQuestion.get());
    }

    public List<VillageAnswerQuestionDTO> getVillageAnswerQuestionByVillageId(Long id) {
        List<VillageAnswerQuestion> villageAnswerQuestionList = villageAnswerQuestionRepository.findAll();

        if (id != null) {
            villageAnswerQuestionList = villageAnswerQuestionList.stream()
                    .filter(assertion -> id.equals(assertion.getVillage().getId()))
                    .toList();
        }


        villageAnswerQuestionList = villageAnswerQuestionList.stream()
                .filter(assertion -> assertion.getAnswer() != null && !assertion.getAnswer().equals(""))
                .toList();

        return villageAnswerQuestionList.stream()
                .map(this::toDTO)
                .toList();
    }
}

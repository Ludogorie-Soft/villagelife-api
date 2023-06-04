package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageAnswerQuestionDTO;
import com.example.ludogorieSoft.village.model.Question;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageAnswerQuestion;
import com.example.ludogorieSoft.village.repositories.QuestionRepository;
import com.example.ludogorieSoft.village.repositories.VillageAnswerQuestionRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
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
    public VillageAnswerQuestionDTO toDTO(VillageAnswerQuestion villageAnswerQuestion){
        return modelMapper.map(villageAnswerQuestion, VillageAnswerQuestionDTO.class);}


    public List<VillageAnswerQuestionDTO> getAllVillageAnswerQuestions() {
        List<VillageAnswerQuestion> villageAnswerQuestions = villageAnswerQuestionRepository.findAll();
        return villageAnswerQuestions
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public VillageAnswerQuestionDTO createVillageAnswerQuestion(VillageAnswerQuestionDTO villageAnswerQuestionDTO) {
        VillageAnswerQuestion villageAnswerQuestion = new VillageAnswerQuestion();
        Optional<Village> village = villageRepository.findById(villageAnswerQuestionDTO.getVillageId());
        if (village.isPresent()){
            villageAnswerQuestion.setVillage(village.get());
        }else {
            throw new ApiRequestException("Village not found");
        }
        Optional<Question> question = questionRepository.findById(villageAnswerQuestionDTO.getQuestionId());
        if (question.isPresent()){
            villageAnswerQuestion.setQuestion(question.get());
        }else {
            throw new ApiRequestException("Question not found");
        }
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
        Optional<Village> village = villageRepository.findById(villageAnswerQuestionDTO.getVillageId());
        if (village.isPresent()){
            foundVillageAnswerQuestion.get().setVillage(village.get());
        }else {
            throw new ApiRequestException("Village not found");
        }
        Optional<Question> question = questionRepository.findById(villageAnswerQuestionDTO.getQuestionId());
        if (question.isPresent()){
            foundVillageAnswerQuestion.get().setQuestion(question.get());
        }else {
            throw new ApiRequestException("Question not found");
        }
        foundVillageAnswerQuestion.get().setAnswer(villageAnswerQuestionDTO.getAnswer());
        villageAnswerQuestionRepository.save(foundVillageAnswerQuestion.get());
        return toDTO(foundVillageAnswerQuestion.get());
    }
}

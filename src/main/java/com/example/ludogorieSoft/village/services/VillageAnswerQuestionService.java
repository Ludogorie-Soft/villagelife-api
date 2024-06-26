package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageAnswerQuestionDTO;
import com.example.ludogorieSoft.village.dtos.response.AnswersQuestionResponse;
import com.example.ludogorieSoft.village.model.Question;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageAnswerQuestion;
import com.example.ludogorieSoft.village.repositories.VillageAnswerQuestionRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class VillageAnswerQuestionService {
    private final VillageAnswerQuestionRepository villageAnswerQuestionRepository;
    private final VillageRepository villageRepository;
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

        villageAnswerQuestion.setAnswer(villageAnswerQuestionDTO.getAnswer().trim());
        villageAnswerQuestion.setVillageStatus(villageAnswerQuestionDTO.getStatus());
        villageAnswerQuestion.setDateUpload(villageAnswerQuestionDTO.getDateUpload());
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

    public List<AnswersQuestionResponse> getAnswersQuestionResponsesByVillageId(Long villageId, boolean status, String date) {
        List<VillageAnswerQuestion> villageAnswerQuestions;
        if(status){
            villageAnswerQuestions = villageAnswerQuestionRepository.findByVillageIdAndVillageStatus(villageId, true);

        }else {
            villageAnswerQuestions = villageAnswerQuestionRepository.findByVillageIdAndVillageStatusAndDateUpload(villageId, status,date);
        }
        Map<String, List<String>> questionToAnswersMap = groupAnswersByQuestion(villageAnswerQuestions);
        return createAnswersQuestionResponses(questionToAnswersMap);
    }

    public Map<String, List<String>> groupAnswersByQuestion(List<VillageAnswerQuestion> villageAnswerQuestions) {
        Map<String, List<String>> questionToAnswersMap = new HashMap<>();
        for (VillageAnswerQuestion villageAnswerQuestion : villageAnswerQuestions) {
            String questionName = villageAnswerQuestion.getQuestion().getInformMessage();
            String answer = villageAnswerQuestion.getAnswer();

            questionToAnswersMap.computeIfAbsent(questionName, k -> new ArrayList<>()).add(answer);
        }
        return questionToAnswersMap;
    }

    public List<AnswersQuestionResponse> createAnswersQuestionResponses(Map<String, List<String>> questionToAnswersMap) {
        List<AnswersQuestionResponse> answersQuestionResponses = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : questionToAnswersMap.entrySet()) {
            AnswersQuestionResponse response = new AnswersQuestionResponse();
            response.setQuestion(entry.getKey());
            response.setAnswers(entry.getValue());
            answersQuestionResponses.add(response);
        }
        return answersQuestionResponses;
    }
    
    public boolean existsByVillageIdAndQuestionIdAndAnswer(Long villageId, Long questionId, String answer){
        return villageAnswerQuestionRepository.existsByVillageIdAndQuestionIdAndAnswer(villageId, questionId, answer);
    }

    public void updateVillageAnswerQuestionStatus(Long id, boolean status, String localDateTime) {
        List<VillageAnswerQuestion> villageAnswerQuestions = villageAnswerQuestionRepository.findByVillageIdAndVillageStatusAndDateUpload(id, status, localDateTime);

        List<VillageAnswerQuestion> villa = new ArrayList<>();

        if (!villageAnswerQuestions.isEmpty()) {
            for (VillageAnswerQuestion vill : villageAnswerQuestions) {
                Village village = villageService.checkVillage(vill.getVillage().getId());
                vill.setVillage(village);
                vill.setVillageStatus(true);
                vill.setDateDeleted(null);
                villa.add(vill);
            }
            villageAnswerQuestionRepository.saveAll(villa);
        }
    }
    public void rejectVillageAnswerQuestionResponse(Long id, boolean status, String responseDate, LocalDateTime dateDelete) {
        List<VillageAnswerQuestion> villageAnswerQuestions = villageAnswerQuestionRepository.findByVillageIdAndVillageStatusAndDateUpload(
                id, status, responseDate
        );
        List<VillageAnswerQuestion> villa = new ArrayList<>();

        if (!villageAnswerQuestions.isEmpty()) {
            for (VillageAnswerQuestion vill : villageAnswerQuestions) {
                Village village = villageService.checkVillage(vill.getVillage().getId());
                vill.setVillage(village);
                vill.setDateDeleted(dateDelete);
                villa.add(vill);
            }
            villageAnswerQuestionRepository.saveAll(villa);
        }
    }
    public List<Object[]> findVillageNameAndAnswerByQuestionName(String questionName){
        return villageAnswerQuestionRepository.findVillageNameAndAnswerByQuestionName(questionName);
    }
}

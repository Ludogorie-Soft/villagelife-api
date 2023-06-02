package com.example.ludogorieSoft.village.Services;

import com.example.ludogorieSoft.village.DTOs.QuestionsDTO;
import com.example.ludogorieSoft.village.Model.Questions;
import com.example.ludogorieSoft.village.Repositories.QuestionsRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuestionsService {

    private final QuestionsRepository questionsRepository;
    private final ModelMapper modelMapper;

    private QuestionsDTO convertToDTO(Questions question) {
        return modelMapper.map(question, QuestionsDTO.class);
    }
    public List<QuestionsDTO> getAllQuestions() {
        List<Questions> questionsList = questionsRepository.findAll();
        return questionsList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public QuestionsDTO createQuestion(QuestionsDTO questionDTO) {
        Questions question = new Questions();
        question.setQuestion(questionDTO.getQuestion());

        Questions createdQuestion = questionsRepository.save(question);

        return mapQuestionToDTO(createdQuestion);
    }

    private QuestionsDTO mapQuestionToDTO(Questions question) {
        QuestionsDTO questionDTO = new QuestionsDTO();
        questionDTO.setId(question.getId());
        questionDTO.setQuestion(question.getQuestion());

        return questionDTO;
    }


    public QuestionsDTO getQuestionById(Long id) {
        Optional<Questions> questions = questionsRepository.findById(id);
        if (questions.isEmpty()) {
            throw new ApiRequestException("Question with id: " + id + " not found");
        }
        return convertToDTO(questions.get());
    }


    public QuestionsDTO updateQuestion(Long id, QuestionsDTO questionDTO) {
        Questions existingQuestion = questionsRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Question with id: " + id + " not found"));

        existingQuestion.setQuestion(questionDTO.getQuestion());

        Questions updatedQuestion = questionsRepository.save(existingQuestion);
        return modelMapper.map(updatedQuestion, QuestionsDTO.class);
    }

    public void deleteQuestion(Long id) {
        if (!questionsRepository.existsById(id)) {
            throw new ApiRequestException("Question with id: " + id + " not found");
        }

        questionsRepository.deleteById(id);
    }



}

package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.QuestionsDTO;
import com.example.ludogorieSoft.village.model.Questions;
import com.example.ludogorieSoft.village.repositories.QuestionsRepository;
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
public class QuestionService {
    private QuestionsRepository questionRepository;
    private final ModelMapper modelMapper;

    public QuestionsDTO questionToQuestionDTO(Questions question) {
        return modelMapper.map(question, QuestionsDTO.class);
    }

    public List<QuestionsDTO> getAllQuestions() {
        List<Questions> questions = questionRepository.findAll();
        return questions.stream()
                .map(this::questionToQuestionDTO)
                .collect(Collectors.toList());
    }

    public QuestionsDTO createQuestion(Questions question) {
        questionRepository.save(question);
        return questionToQuestionDTO(question);
    }

    public QuestionsDTO getQuestionById(Long id) {
        Optional<Questions> question = questionRepository.findById(id);
        if (question.isEmpty()) {
            throw new ApiRequestException("This question not found");
        }
        return questionToQuestionDTO(question.get());
    }

    public int deleteQuestionById(Long id) {
        try {
            questionRepository.deleteById(id);
            return 1;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public QuestionsDTO updateQuestion(Long id, Questions question) {
        Optional<Questions> findQuestion = questionRepository.findById(id);
        if (findQuestion.isEmpty()) {
            throw new ApiRequestException("Question not found");
        }
        findQuestion.get().setQuestion(question.getQuestion());
        questionRepository.save(findQuestion.get());
        return questionToQuestionDTO(findQuestion.get());
    }
}
package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.QuestionDTO;
import com.example.ludogorieSoft.village.model.Question;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.QuestionRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QuestionService {
    private QuestionRepository questionRepository;
    private final ModelMapper modelMapper;

    public QuestionDTO questionToQuestionDTO(Question question) {
        return modelMapper.map(question, QuestionDTO.class);
    }

    public List<QuestionDTO> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream()
                .map(this::questionToQuestionDTO)
                .toList();
    }

    public QuestionDTO createQuestion(Question question) {
        if (questionRepository.existsByQuestionName(question.getQuestionName())) {
            throw new ApiRequestException("Question: " + question.getQuestionName() + " already exists");
        }
        questionRepository.save(question);
        return questionToQuestionDTO(question);
    }

    public QuestionDTO getQuestionById(Long id) {
        Optional<Question> question = questionRepository.findById(id);
        if (question.isEmpty()) {
            throw new ApiRequestException("This question not found");
        }
        return questionToQuestionDTO(question.get());
    }

    public void deleteQuestionById(Long id) {
        Optional<Question> question = questionRepository.findById(id);
        if (question.isEmpty()) {
            throw new ApiRequestException("Question not found for id " + id);
        }
        questionRepository.delete(question.get());
    }

    public QuestionDTO updateQuestion(Long id, Question question) {
        Optional<Question> findQuestion = questionRepository.findById(id);
        if (findQuestion.isEmpty()) {
            throw new ApiRequestException("Question not found");
        }
        if (questionRepository.existsByQuestionName(question.getQuestionName())) {
            throw new ApiRequestException("Question: " + question.getQuestionName() + " already exists");
        }
        findQuestion.get().setQuestionName(question.getQuestionName());
        questionRepository.save(findQuestion.get());
        return questionToQuestionDTO(findQuestion.get());
    }

    public Question checkQuestion(Long id) {
        Optional<Question> question = questionRepository.findById(id);
        if (question.isPresent()){
            return question.get();
        }else {
            throw new ApiRequestException("Question not found");
        }
    }
}
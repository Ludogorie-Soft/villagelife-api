package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.QuestionDTO;
import com.example.ludogorieSoft.village.model.Question;
import com.example.ludogorieSoft.village.repositories.QuestionRepository;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
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
        List<Question> questions = questionRepository.findAllByOrderByIdAsc();
        return questions.stream()
                .map(this::questionToQuestionDTO)
                .toList();
    }


    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getQuestion())) {
            throw new ApiRequestException("Question is blank");
        }
        if (questionRepository.existsByQuestionName(questionDTO.getQuestion())) {
            throw new ApiRequestException("Question: " + questionDTO.getQuestion() + " already exists");
        }
        Question question = new Question();
        question.setQuestionName(questionDTO.getQuestion());
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


    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO) {
        Optional<Question> findQuestion = questionRepository.findById(id);
        Question question = findQuestion.orElseThrow(() -> new ApiRequestException("Question not found"));

        if (questionDTO == null || questionDTO.getQuestion() == null || questionDTO.getQuestion().isEmpty()) {
            throw new ApiRequestException("Invalid question data");
        }

        String newQuestionName = questionDTO.getQuestion();
        if (!newQuestionName.equals(question.getQuestionName())) {
            if (questionRepository.existsByQuestionName(newQuestionName)) {
                throw new ApiRequestException("Question: " + newQuestionName + " already exists");
            }
            question.setQuestionName(newQuestionName);
            questionRepository.save(question);
        }

        return questionToQuestionDTO(question);
    }


    public Question checkQuestion(Long id) {
        Optional<Question> question = questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new ApiRequestException("Question not found");
        }
    }
}

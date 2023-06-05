package com.example.ludogorieSoft.village.services_tests;

import com.example.ludogorieSoft.village.dtos.QuestionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Question;
import com.example.ludogorieSoft.village.repositories.QuestionRepository;
import com.example.ludogorieSoft.village.services.QuestionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertThrows;

class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllQuestionsWithQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question());
        questions.add(new Question());
        when(questionRepository.findAll()).thenReturn(questions);

        List<QuestionDTO> result = questionService.getAllQuestions();

        verify(questionRepository, times(1)).findAll();
        Assertions.assertEquals(questions.size(), result.size());
    }

    @Test
    void testGetAllQuestionsWithNoQuestions() {
        List<Question> questions = new ArrayList<>();
        when(questionRepository.findAll()).thenReturn(questions);

        List<QuestionDTO> result = questionService.getAllQuestions();

        verify(questionRepository, times(1)).findAll();
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testGetQuestionByIdWithExistingId() {
        Long questionId = 123L;
        Question question = new Question();
        question.setId(questionId);
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(questionId);

        Optional<Question> optionalQuestion = Optional.of(question);
        when(questionRepository.findById(questionId)).thenReturn(optionalQuestion);
        when(modelMapper.map(question, QuestionDTO.class)).thenReturn(questionDTO);

        QuestionDTO result = questionService.getQuestionById(questionId);


        verify(questionRepository, times(1)).findById(questionId);
        Assertions.assertEquals(questionService.questionToQuestionDTO(question), result);
    }

    @Test
    void testGetQuestionByIdWithNonExistingId() {
        Long questionId = 123L;

        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> {
            questionService.getQuestionById(questionId);
        });

        verify(questionRepository, times(1)).findById(questionId);
    }

    @Test
    void testDeleteQuestionByIdWithExistingId() {
        Long questionId = 123L;
        Question question = new Question();
        question.setId(questionId);

        Optional<Question> optionalQuestion = Optional.of(question);
        when(questionRepository.findById(questionId)).thenReturn(optionalQuestion);

        questionService.deleteQuestionById(questionId);

        verify(questionRepository, times(1)).findById(questionId);
        verify(questionRepository, times(1)).delete(question);
    }

    @Test
    void testDeleteQuestionByIdWithNonExistingId() {
        Long questionId = 123L;

        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> {
            questionService.deleteQuestionById(questionId);
        });

        verify(questionRepository, times(1)).findById(questionId);
        verify(questionRepository, never()).delete(any(Question.class));
    }


    @Test
     void testCreateQuestionThrowsExceptionWhenQuestionExists() {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuestion("Test Question");

        when(questionRepository.existsByQuestionName(questionDTO.getQuestion())).thenReturn(true);

        Assertions.assertThrows(ApiRequestException.class, () -> {
            questionService.createQuestion(questionDTO);
        });

        verify(questionRepository, times(1)).existsByQuestionName(questionDTO.getQuestion());
        verifyNoMoreInteractions(questionRepository);
        verifyNoInteractions(modelMapper);
    }


    @Test
     void testUpdateQuestionThrowsExceptionWhenQuestionNotFound() {
        Long id = 1L;
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuestion("Updated Question");

        when(questionRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> {
            questionService.updateQuestion(id, questionDTO);
        });

        verify(questionRepository, times(1)).findById(id);
        verifyNoMoreInteractions(questionRepository);
        verifyNoInteractions(modelMapper);
    }

    @Test
     void testUpdateQuestionThrowsExceptionWhenQuestionExists() {
        Long id = 1L;
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuestion("Updated Question");

        Question question = new Question();
        question.setId(id);
        question.setQuestionName("Original Question");

        Optional<Question> optionalQuestion = Optional.of(question);

        when(questionRepository.findById(id)).thenReturn(optionalQuestion);
        when(questionRepository.existsByQuestionName(questionDTO.getQuestion())).thenReturn(true);

        Assertions.assertThrows(ApiRequestException.class, () -> {
            questionService.updateQuestion(id, questionDTO);
        });

        verify(questionRepository, times(1)).findById(id);
        verify(questionRepository, times(1)).existsByQuestionName(questionDTO.getQuestion());
        verifyNoMoreInteractions(questionRepository);
        verifyNoInteractions(modelMapper);
    }

//    @Test
//    void createQuestionWhenQuestionDoesNotExistShouldCreateQuestion() {
//        QuestionDTO questionDTO = new QuestionDTO();
//        questionDTO.setQuestion("What is your name?");
//
//        when(questionRepository.existsByQuestionName(questionDTO.getQuestion())).thenReturn(false);
//
//        QuestionDTO result = questionService.createQuestion(questionDTO);
//
//        assertNotNull(result);
//        Assertions.assertEquals(questionDTO.getQuestion(), result.getQuestion());
//    }


    @Test
     void createQuestion_WhenQuestionExists_ShouldThrowApiRequestException() {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuestion("What is your name?");

        when(questionRepository.existsByQuestionName(questionDTO.getQuestion())).thenReturn(true);

        assertThrows(ApiRequestException.class, () -> {
            questionService.createQuestion(questionDTO);
        });
    }



    @Test
     void updateQuestion_WhenQuestionIsNull_ShouldThrowApiRequestException() {
        Long questionId = 1L;
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuestion(null);

        assertThrows(ApiRequestException.class, () -> {
            questionService.updateQuestion(questionId, questionDTO);
        });
    }




}

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

import static org.mockito.Mockito.*;

public class QuestionServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetAllQuestionsWithQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question());
        questions.add(new Question());
        when(questionRepository.findAll()).thenReturn(questions);

        List<QuestionDTO> result = questionService.getAllQuestions();

        verify(questionRepository, times(1)).findAll();
        Assertions.assertEquals(questions.size(), result.size());
    }

    @Test
    public void testGetAllQuestionsWithNoQuestions() {
        List<Question> questions = new ArrayList<>();
        when(questionRepository.findAll()).thenReturn(questions);

        List<QuestionDTO> result = questionService.getAllQuestions();

        verify(questionRepository, times(1)).findAll();
        Assertions.assertEquals(0, result.size());
    }
    @Test
    public void testCreateQuestionWithNonExistingQuestion() {
        Question question = new Question();
        question.setQuestionName("New Question");
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuestion("New Question");

        when(questionRepository.existsByQuestionName(question.getQuestionName())).thenReturn(false);
        when(questionRepository.save(any(Question.class))).thenReturn(question);
        when(modelMapper.map(question, QuestionDTO.class)).thenReturn(questionDTO);

        QuestionDTO result = questionService.createQuestion(question);

        verify(questionRepository, times(1)).existsByQuestionName(question.getQuestionName());
        verify(questionRepository, times(1)).save(question);
        Assertions.assertEquals(questionService.questionToQuestionDTO(question), result);
    }

    @Test
    public void testCreateQuestionWithExistingQuestion() {
        Question question = new Question();
        question.setQuestionName("Existing Question");

        when(questionRepository.existsByQuestionName(question.getQuestionName())).thenReturn(true);

        Assertions.assertThrows(ApiRequestException.class, () -> {
            questionService.createQuestion(question);
        });

        verify(questionRepository, times(1)).existsByQuestionName(question.getQuestionName());
        verify(questionRepository, times(0)).save(any(Question.class));
    }
    @Test
    public void testGetQuestionByIdWithExistingId() {
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
    public void testGetQuestionByIdWithNonExistingId() {
        Long questionId = 123L;

        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> {
            questionService.getQuestionById(questionId);
        });

        verify(questionRepository, times(1)).findById(questionId);
    }
    @Test
    public void testDeleteQuestionByIdWithExistingId() {
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
    public void testDeleteQuestionByIdWithNonExistingId() {
        Long questionId = 123L;

        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> {
            questionService.deleteQuestionById(questionId);
        });

        verify(questionRepository, times(1)).findById(questionId);
        verify(questionRepository, never()).delete(any(Question.class));
    }
    @Test
    public void testUpdateQuestionWithExistingIdAndNonExistingQuestion() {
        Long questionId = 123L;
        Question question = new Question();
        question.setId(questionId);
        question.setQuestionName("Updated Question");

        Optional<Question> optionalQuestion = Optional.of(new Question());
        optionalQuestion.get().setQuestionName("Old Question");

        when(questionRepository.findById(questionId)).thenReturn(optionalQuestion);
        when(questionRepository.existsByQuestionName(question.getQuestionName())).thenReturn(false);
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        QuestionDTO result = questionService.updateQuestion(questionId, question);

        verify(questionRepository, times(1)).findById(questionId);
        verify(questionRepository, times(1)).existsByQuestionName(question.getQuestionName());
        verify(questionRepository, times(1)).save(any(Question.class));
        Assertions.assertEquals(questionService.questionToQuestionDTO(question), result);
    }

    @Test
    public void testUpdateQuestionWithNonExistingId() {
        Long questionId = 123L;
        Question question = new Question();
        question.setId(questionId);
        question.setQuestionName("Updated Question");

        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiRequestException.class, () -> {
            questionService.updateQuestion(questionId, question);
        });

        verify(questionRepository, times(1)).findById(questionId);
        verify(questionRepository, never()).existsByQuestionName(anyString());
        verify(questionRepository, never()).save(any(Question.class));
    }

    @Test
    public void testUpdateQuestionWithExistingQuestion() {
        Long questionId = 123L;
        Question question = new Question();
        question.setId(questionId);
        question.setQuestionName("Updated Question");

        Optional<Question> optionalQuestion = Optional.of(new Question());
        optionalQuestion.get().setQuestionName("Existing Question");

        when(questionRepository.findById(questionId)).thenReturn(optionalQuestion);
        when(questionRepository.existsByQuestionName(question.getQuestionName())).thenReturn(true);

        Assertions.assertThrows(ApiRequestException.class, () -> {
            questionService.updateQuestion(questionId, question);
        });

        verify(questionRepository, times(1)).findById(questionId);
        verify(questionRepository, times(1)).existsByQuestionName(question.getQuestionName());
        verify(questionRepository, never()).save(any(Question.class));
    }
}

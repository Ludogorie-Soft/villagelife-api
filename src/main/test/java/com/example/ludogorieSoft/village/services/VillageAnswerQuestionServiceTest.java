package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageAnswerQuestionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Question;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageAnswerQuestion;
import com.example.ludogorieSoft.village.repositories.QuestionRepository;
import com.example.ludogorieSoft.village.repositories.VillageAnswerQuestionRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VillageAnswerQuestionServiceTest {

    @Mock
    private VillageAnswerQuestionRepository villageAnswerQuestionRepository;
    @Mock
    private VillageRepository villageRepository;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private VillageService villageService;
    @Mock
    private QuestionService questionService;
    @InjectMocks
    private VillageAnswerQuestionService villageAnswerQuestionService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testCreateVillageAnswerQuestion_ValidInput_ReturnsDTO() {
//        // Arrange
//        VillageAnswerQuestionDTO inputDTO = new VillageAnswerQuestionDTO();
//        inputDTO.setVillageId(1L);
//        inputDTO.setQuestionId(1L);
//        inputDTO.setAnswer("Sample answer");
//
//        Village village = new Village();
//        when(villageService.checkVillage(1L)).thenReturn(village);
//
//        Question question = new Question();
//        when(questionService.checkQuestion(1L)).thenReturn(question);
//
//        VillageAnswerQuestion savedVillageAnswerQuestion = new VillageAnswerQuestion();
//        when(villageAnswerQuestionRepository.save(any(VillageAnswerQuestion.class))).thenReturn(savedVillageAnswerQuestion);
//
//        VillageAnswerQuestionDTO expectedDTO = new VillageAnswerQuestionDTO();
//        expectedDTO.setId(savedVillageAnswerQuestion.getId());
//        expectedDTO.setVillageId(village.getId());
//        expectedDTO.setQuestionId(question.getId());
//        expectedDTO.setAnswer(inputDTO.getAnswer());
//
//        // Act
//        VillageAnswerQuestionDTO resultDTO = villageAnswerQuestionService.createVillageAnswerQuestion(inputDTO);
//
//        // Assert
//        assertNotNull(resultDTO);
//        assertEquals(expectedDTO.getId(), resultDTO.getId());
//        assertEquals(expectedDTO.getVillageId(), resultDTO.getVillageId());
//        assertEquals(expectedDTO.getQuestionId(), resultDTO.getQuestionId());
//        assertEquals(expectedDTO.getAnswer(), resultDTO.getAnswer());
//
//        verify(villageService, times(1)).checkVillage(1L);
//        verify(questionService, times(1)).checkQuestion(1L);
//        verify(villageAnswerQuestionRepository, times(1)).save(any(VillageAnswerQuestion.class));
//    }



    @Test
    void testCreateVillageAnswerQuestionInvalidVillageThrowsException() {
        VillageAnswerQuestionDTO inputDTO = new VillageAnswerQuestionDTO();
        inputDTO.setVillageId(1L);
        inputDTO.setQuestionId(1L);
        inputDTO.setAnswer("Sample answer");

        when(villageService.checkVillage(1L)).thenThrow(new ApiRequestException("Village not found"));

        assertThrows(ApiRequestException.class, () -> villageAnswerQuestionService.createVillageAnswerQuestion(inputDTO));

        verify(villageService, times(1)).checkVillage(1L);
        verify(questionService, never()).checkQuestion(anyLong());
        verify(villageAnswerQuestionRepository, never()).save(any(VillageAnswerQuestion.class));
    }

    @Test
    void testCreateVillageAnswerQuestionInvalidQuestionThrowsException() {
        VillageAnswerQuestionDTO inputDTO = new VillageAnswerQuestionDTO();
        inputDTO.setVillageId(1L);
        inputDTO.setQuestionId(1L);
        inputDTO.setAnswer("Sample answer");

        Village village = new Village();
        when(villageService.checkVillage(1L)).thenReturn(village);

        when(questionService.checkQuestion(1L)).thenThrow(new ApiRequestException("Question not found"));

        assertThrows(ApiRequestException.class, () -> villageAnswerQuestionService.createVillageAnswerQuestion(inputDTO));

        verify(villageService, times(1)).checkVillage(1L);
        verify(questionService, times(1)).checkQuestion(1L);
        verify(villageAnswerQuestionRepository, never()).save(any(VillageAnswerQuestion.class));
    }


    @Test
    void getAllVillageAnswerQuestionsShouldReturnListOfVillageAnswerQuestionDTOs() {
        List<VillageAnswerQuestion> villageAnswerQuestions = new ArrayList<>();
        villageAnswerQuestions.add(new VillageAnswerQuestion());
        villageAnswerQuestions.add(new VillageAnswerQuestion());
        when(villageAnswerQuestionRepository.findAll()).thenReturn(villageAnswerQuestions);

        List<VillageAnswerQuestionDTO> result = villageAnswerQuestionService.getAllVillageAnswerQuestions();

        assertEquals(villageAnswerQuestions.size(), result.size());
    }


    @Test
     void createVillageAnswerQuestionWhenInvalidVillageIdShouldThrowApiRequestException() {
        VillageAnswerQuestionDTO villageAnswerQuestionDTO = new VillageAnswerQuestionDTO();
        villageAnswerQuestionDTO.setVillageId(1L);
        villageAnswerQuestionDTO.setQuestionId(2L);
        villageAnswerQuestionDTO.setAnswer("Answer");

        when(villageService.checkVillage(1L)).thenThrow(new ApiRequestException("Village not found"));

        assertThrows(ApiRequestException.class, () -> villageAnswerQuestionService.createVillageAnswerQuestion(villageAnswerQuestionDTO));
    }

    @Test
     void createVillageAnswerQuestionWhenInvalidQuestionIdShouldThrowApiRequestException() {
        VillageAnswerQuestionDTO villageAnswerQuestionDTO = new VillageAnswerQuestionDTO();
        villageAnswerQuestionDTO.setVillageId(1L);
        villageAnswerQuestionDTO.setQuestionId(2L);
        villageAnswerQuestionDTO.setAnswer("Answer");

        Village village = new Village();
        when(villageService.checkVillage(1L)).thenReturn(village);

        when(questionService.checkQuestion(2L)).thenThrow(new ApiRequestException("Question not found"));

        assertThrows(ApiRequestException.class, () -> villageAnswerQuestionService.createVillageAnswerQuestion(villageAnswerQuestionDTO));
    }

    @Test
     void getVillageAnswerQuestionByIdWhenValidIdShouldReturnVillageAnswerQuestionDTO() {
        Long id = 1L;
        VillageAnswerQuestion villageAnswerQuestion = new VillageAnswerQuestion();
        villageAnswerQuestion.setId(id);
        when(villageAnswerQuestionRepository.findById(id)).thenReturn(Optional.of(villageAnswerQuestion));

        VillageAnswerQuestionDTO result = villageAnswerQuestionService.getVillageAnswerQuestionById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(villageAnswerQuestionRepository).findById(id);
    }

    @Test
     void getVillageAnswerQuestionByIdWhenInvalidIdShouldThrowApiRequestException() {
        Long id = 1L;
        when(villageAnswerQuestionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageAnswerQuestionService.getVillageAnswerQuestionById(id));
    }


    @Test
     void deleteVillageAnswerQuestionByIdWhenValidIdShouldReturn1() {
        Long id = 1L;

        int result = villageAnswerQuestionService.deleteVillageAnswerQuestionById(id);

        assertEquals(1, result);
        verify(villageAnswerQuestionRepository).deleteById(id);
    }

    @Test
     void deleteVillageAnswerQuestionByIdWhenInvalidIdShouldReturn0() {
        Long id = 1L;
        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(villageAnswerQuestionRepository).deleteById(id);

        int result = villageAnswerQuestionService.deleteVillageAnswerQuestionById(id);

        assertEquals(0, result);
    }


    @Test
     void updateVillageAnswerQuestionWhenInvalidIdShouldThrowApiRequestException() {
        Long id = 1L;
        VillageAnswerQuestionDTO villageAnswerQuestionDTO = new VillageAnswerQuestionDTO();
        villageAnswerQuestionDTO.setVillageId(1L);
        villageAnswerQuestionDTO.setQuestionId(2L);
        villageAnswerQuestionDTO.setAnswer("Answer");

        when(villageAnswerQuestionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageAnswerQuestionService.updateVillageAnswerQuestion(id, villageAnswerQuestionDTO));
    }
}

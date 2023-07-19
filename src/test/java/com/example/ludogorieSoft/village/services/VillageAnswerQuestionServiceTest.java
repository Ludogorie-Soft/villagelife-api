package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageAnswerQuestionDTO;
import com.example.ludogorieSoft.village.exceptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.QuestionRepository;
import com.example.ludogorieSoft.village.repositories.VillageAnswerQuestionRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
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

    @Test
    void testCreateVillageAnswerQuestionWithValidInputThenReturnsDTO() {
        VillageAnswerQuestionDTO inputDTO = new VillageAnswerQuestionDTO();
        inputDTO.setVillageId(1L);
        inputDTO.setQuestionId(1L);
        inputDTO.setAnswer("Sample answer");
        VillageAnswerQuestion savedVillageAnswerQuestion = new VillageAnswerQuestion();

        Village village = new Village();
        village.setId(1L);
        when(villageService.checkVillage(1L)).thenReturn(village);
        savedVillageAnswerQuestion.setVillage(villageService.checkVillage(1L));

        Question question = new Question();
        question.setId(1L);
        when(questionService.checkQuestion(1L)).thenReturn(question);
        savedVillageAnswerQuestion.setQuestion(questionService.checkQuestion(1L));
        savedVillageAnswerQuestion.setAnswer(inputDTO.getAnswer());

        when(villageAnswerQuestionRepository.save(any(VillageAnswerQuestion.class))).thenReturn(savedVillageAnswerQuestion);
        when(villageAnswerQuestionService.toDTO(savedVillageAnswerQuestion)).thenReturn(inputDTO);

        VillageAnswerQuestionDTO expectedDTO = new VillageAnswerQuestionDTO();
        expectedDTO.setId(savedVillageAnswerQuestion.getId());
        expectedDTO.setVillageId(savedVillageAnswerQuestion.getVillage().getId());
        expectedDTO.setQuestionId(savedVillageAnswerQuestion.getQuestion().getId());
        expectedDTO.setAnswer(inputDTO.getAnswer());

        VillageAnswerQuestionDTO resultDTO = villageAnswerQuestionService.createVillageAnswerQuestion(inputDTO);

        assertNotNull(resultDTO);
        assertEquals(expectedDTO.getId(), resultDTO.getId());
        assertEquals(expectedDTO.getVillageId(), resultDTO.getVillageId());
        assertEquals(expectedDTO.getQuestionId(), resultDTO.getQuestionId());
        assertEquals(expectedDTO.getAnswer(), resultDTO.getAnswer());

        verify(villageService, times(2)).checkVillage(1L);
        verify(questionService, times(2)).checkQuestion(1L);
        verify(villageAnswerQuestionRepository, times(1)).save(any(VillageAnswerQuestion.class));
    }



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
        VillageAnswerQuestionDTO villageAnswerQuestionDTO = new VillageAnswerQuestionDTO();
        villageAnswerQuestionDTO.setId(id);

        when(villageAnswerQuestionRepository.findById(id)).thenReturn(Optional.of(villageAnswerQuestion));
        when(villageAnswerQuestionService.toDTO(villageAnswerQuestion)).thenReturn(villageAnswerQuestionDTO);

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


    @Test
    void testGetVillageAnswerQuestionByVillageIdWithNullVillageIdReturnsAllDTOs() {
        Long villageId = null;

        VillageAnswerQuestion villageAnswerQuestion1 = new VillageAnswerQuestion();
        VillageAnswerQuestion villageAnswerQuestion2 = new VillageAnswerQuestion();

        villageAnswerQuestion1.setVillage(new Village(1L, "Village1", new Region(1L, "Region1"), 1000, new Population(), LocalDateTime.now(), true, new Administrator(), null, null, null));
        villageAnswerQuestion1.setAnswer("Answer1");

        villageAnswerQuestion2.setVillage(new Village(2L, "Village1", new Region(1L, "Region1"), 1000, new Population(), LocalDateTime.now(), true, new Administrator(), null, null, null));
        villageAnswerQuestion2.setAnswer("Answer2");

        List<VillageAnswerQuestion> villageAnswerQuestions = List.of(villageAnswerQuestion1, villageAnswerQuestion2);

        when(villageAnswerQuestionRepository.findAll()).thenReturn(villageAnswerQuestions);

        List<VillageAnswerQuestionDTO> result = villageAnswerQuestionService.getVillageAnswerQuestionByVillageId(villageId);

        assertEquals(2, result.size());
    }


    @Test
    void testGetVillageAnswerQuestionByVillageIdWithValidVillageIdFiltersProperly() {
        Long villageId = 1L;

        VillageAnswerQuestion villageAnswerQuestion1 = new VillageAnswerQuestion();
        villageAnswerQuestion1.setVillage(new Village(1L, "Village1", new Region(1L, "Region1"), 1000, new Population(), LocalDateTime.now(), true, new Administrator(), null, null, null));
        villageAnswerQuestion1.setAnswer("Answer1");

        VillageAnswerQuestion villageAnswerQuestion2 = new VillageAnswerQuestion();
        villageAnswerQuestion2.setVillage(new Village(2L, "Village2", new Region(2L, "Region2"), 2000, new Population(), LocalDateTime.now(), true, new Administrator(), null, null, null));
        villageAnswerQuestion2.setAnswer("Answer2");

        List<VillageAnswerQuestion> villageAnswerQuestions = List.of(villageAnswerQuestion1, villageAnswerQuestion2);

        when(villageAnswerQuestionRepository.findAll()).thenReturn(villageAnswerQuestions);

        List<VillageAnswerQuestionDTO> result = villageAnswerQuestionService.getVillageAnswerQuestionByVillageId(villageId);

        assertEquals(1, result.size());
    }

    @Test
    void testGetVillageAnswerQuestionByVillageIdWithValidVillageIdNoMatches() {
        Long villageId = 3L;

        VillageAnswerQuestion villageAnswerQuestion1 = new VillageAnswerQuestion();
        villageAnswerQuestion1.setVillage(new Village(1L, "Village1", new Region(1L, "Region1"), 1000, new Population(), LocalDateTime.now(), true, new Administrator(), null, null, null));
        villageAnswerQuestion1.setAnswer("Answer1");

        VillageAnswerQuestion villageAnswerQuestion2 = new VillageAnswerQuestion();
        villageAnswerQuestion2.setVillage(new Village(2L, "Village2", new Region(2L, "Region2"), 2000, new Population(), LocalDateTime.now(), true, new Administrator(), null, null, null));
        villageAnswerQuestion2.setAnswer("Answer2");

        List<VillageAnswerQuestion> villageAnswerQuestions = List.of(villageAnswerQuestion1, villageAnswerQuestion2);

        when(villageAnswerQuestionRepository.findAll()).thenReturn(villageAnswerQuestions);

        List<VillageAnswerQuestionDTO> result = villageAnswerQuestionService.getVillageAnswerQuestionByVillageId(villageId);

        assertEquals(0, result.size());
    }


    @Test
    void testGetVillageAnswerQuestionByVillageIdWithNullVillageId() {
        Long villageId = null;

        VillageAnswerQuestion villageAnswerQuestion1 = new VillageAnswerQuestion();
        villageAnswerQuestion1.setVillage(new Village(1L, "Village1", new Region(1L, "Region1"), 1000, new Population(), LocalDateTime.now(), true, new Administrator(), null, null, null));
        villageAnswerQuestion1.setAnswer("Answer1");

        VillageAnswerQuestion villageAnswerQuestion2 = new VillageAnswerQuestion();
        villageAnswerQuestion2.setVillage(new Village(2L, "Village2", new Region(2L, "Region2"), 2000, new Population(), LocalDateTime.now(), true, new Administrator(), null, null, null));
        villageAnswerQuestion2.setAnswer("Answer2");

        List<VillageAnswerQuestion> villageAnswerQuestions = List.of(villageAnswerQuestion1, villageAnswerQuestion2);

        when(villageAnswerQuestionRepository.findAll()).thenReturn(villageAnswerQuestions);

        List<VillageAnswerQuestionDTO> result = villageAnswerQuestionService.getVillageAnswerQuestionByVillageId(villageId);

        assertEquals(2, result.size());
    }


    @Test
    void testUpdateVillageAnswerQuestionWithInvalidId() {
        Long id = 1L;
        VillageAnswerQuestionDTO inputDTO = new VillageAnswerQuestionDTO();
        inputDTO.setVillageId(1L);
        inputDTO.setQuestionId(1L);
        inputDTO.setAnswer("Updated answer");

        when(villageAnswerQuestionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageAnswerQuestionService.updateVillageAnswerQuestion(id, inputDTO));

        verify(villageService, never()).checkVillage(anyLong());
        verify(questionService, never()).checkQuestion(anyLong());
        verify(villageAnswerQuestionRepository, times(1)).findById(id);
        verify(villageAnswerQuestionRepository, never()).save(any(VillageAnswerQuestion.class));
    }


    @Test
    void testDeleteVillageAnswerQuestionByIdWithValidId() {
        Long id = 1L;
        int expectedResult = 1;

        int result = villageAnswerQuestionService.deleteVillageAnswerQuestionById(id);

        assertEquals(expectedResult, result);
        verify(villageAnswerQuestionRepository, times(1)).deleteById(id);
    }


    @Test
    void testDeleteVillageAnswerQuestionByIdWithInvalidId() {
        Long id = 1L;
        int expectedResult = 0;

        doThrow(new EmptyResultDataAccessException(1)).when(villageAnswerQuestionRepository).deleteById(id);

        int result = villageAnswerQuestionService.deleteVillageAnswerQuestionById(id);

        assertEquals(expectedResult, result);
        verify(villageAnswerQuestionRepository, times(1)).deleteById(id);
    }


    @Test
    void testGetVillageAnswerQuestionByInvalidVillageId() {
        Long villageId = 999L;

        VillageAnswerQuestion villageAnswerQuestion1 = new VillageAnswerQuestion();
        villageAnswerQuestion1.setVillage(new Village(1L, "Village1", new Region(1L, "Region1"), 1000, new Population(), LocalDateTime.now(), true, new Administrator(), null, null, null));
        villageAnswerQuestion1.setAnswer("Answer1");

        VillageAnswerQuestion villageAnswerQuestion2 = new VillageAnswerQuestion();
        villageAnswerQuestion2.setVillage(new Village(2L, "Village2", new Region(2L, "Region2"), 2000, new Population(), LocalDateTime.now(), true, new Administrator(), null, null, null));
        villageAnswerQuestion2.setAnswer("Answer2");

        List<VillageAnswerQuestion> villageAnswerQuestions = List.of(villageAnswerQuestion1, villageAnswerQuestion2);

        when(villageAnswerQuestionRepository.findAll()).thenReturn(villageAnswerQuestions);

        List<VillageAnswerQuestionDTO> result = villageAnswerQuestionService.getVillageAnswerQuestionByVillageId(villageId);

        assertTrue(result.isEmpty());
    }


    @Test
    void testGetVillageAnswerQuestionByEmptyVillageId() {
        Long villageId = null;

        VillageAnswerQuestion villageAnswerQuestion1 = new VillageAnswerQuestion();
        villageAnswerQuestion1.setVillage(new Village(1L, "Village1", new Region(1L, "Region1"), 1000, new Population(), LocalDateTime.now(), true, new Administrator(), null, null, null));
        villageAnswerQuestion1.setAnswer("Answer1");

        VillageAnswerQuestion villageAnswerQuestion2 = new VillageAnswerQuestion();
        villageAnswerQuestion2.setVillage(new Village(2L, "Village2", new Region(2L, "Region2"), 2000, new Population(), LocalDateTime.now(), true, new Administrator(), null, null, null));
        villageAnswerQuestion2.setAnswer("Answer2");

        List<VillageAnswerQuestion> villageAnswerQuestions = List.of(villageAnswerQuestion1, villageAnswerQuestion2);

        when(villageAnswerQuestionRepository.findAll()).thenReturn(villageAnswerQuestions);

        List<VillageAnswerQuestionDTO> result = villageAnswerQuestionService.getVillageAnswerQuestionByVillageId(villageId);

        assertEquals(2, result.size());
    }



    @Test
    void testCreateVillageAnswerQuestionWithInvalidVillageAndQuestionIds() {
        VillageAnswerQuestionDTO inputDTO = new VillageAnswerQuestionDTO();
        inputDTO.setVillageId(1L);
        inputDTO.setQuestionId(1L);
        inputDTO.setAnswer("Sample answer");

        when(villageService.checkVillage(1L)).thenThrow(new ApiRequestException("Village not found"));
        when(questionService.checkQuestion(1L)).thenThrow(new ApiRequestException("Question not found"));

        assertThrows(ApiRequestException.class, () -> villageAnswerQuestionService.createVillageAnswerQuestion(inputDTO));

        verify(villageService, times(1)).checkVillage(1L);
        verify(villageAnswerQuestionRepository, never()).save(any(VillageAnswerQuestion.class));
    }


    @Test
    void testGetAllVillageAnswerQuestionsEmptyList() {
        when(villageAnswerQuestionRepository.findAll()).thenReturn(new ArrayList<>());

        List<VillageAnswerQuestionDTO> result = villageAnswerQuestionService.getAllVillageAnswerQuestions();

        assertTrue(result.isEmpty());
    }








}

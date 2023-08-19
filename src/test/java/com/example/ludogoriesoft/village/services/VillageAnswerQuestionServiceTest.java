package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageAnswerQuestionDTO;
import com.example.ludogorieSoft.village.dtos.response.AnswersQuestionResponse;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Question;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageAnswerQuestion;
import com.example.ludogorieSoft.village.repositories.VillageAnswerQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VillageAnswerQuestionServiceTest {

    @Mock
    private VillageAnswerQuestionRepository villageAnswerQuestionRepository;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private VillageService villageService;
    @Mock
    private QuestionService questionService;
    @Captor
    private ArgumentCaptor<List<VillageAnswerQuestion>> answerQuestionListCaptor;
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
    void testGetVillageAnswerQuestionByVillageId() {
        Village village = new Village();
        village.setId(1L);

        VillageAnswerQuestion villageAnswerQuestion1 = new VillageAnswerQuestion();
        villageAnswerQuestion1.setAnswer("Answer 1");
        villageAnswerQuestion1.setVillage(village);

        VillageAnswerQuestion villageAnswerQuestion2 = new VillageAnswerQuestion();
        villageAnswerQuestion2.setAnswer("Answer 2");
        villageAnswerQuestion2.setVillage(village);

        VillageAnswerQuestionDTO villageAnswerQuestionDTO1 = new VillageAnswerQuestionDTO();
        villageAnswerQuestionDTO1.setAnswer("Answer 1");

        VillageAnswerQuestionDTO villageAnswerQuestionDTO2 = new VillageAnswerQuestionDTO();
        villageAnswerQuestionDTO2.setAnswer("Answer 2");

        when(villageAnswerQuestionRepository.findAll()).thenReturn(List.of(villageAnswerQuestion1, villageAnswerQuestion2));
        when(villageAnswerQuestionService.toDTO(villageAnswerQuestion1)).thenReturn(villageAnswerQuestionDTO1);
        when(villageAnswerQuestionService.toDTO(villageAnswerQuestion2)).thenReturn(villageAnswerQuestionDTO2);

        List<VillageAnswerQuestionDTO> result = villageAnswerQuestionService.getVillageAnswerQuestionByVillageId(1L);

        assertEquals(2, result.size());
        assertEquals("Answer 1", result.get(0).getAnswer());
        assertEquals("Answer 2", result.get(1).getAnswer());
    }


    @Test
    void testUpdateVillageAnswerQuestionWhenInvalidVillageIdShouldThrowApiRequestException() {
        Long id = 1L;
        VillageAnswerQuestionDTO inputDTO = new VillageAnswerQuestionDTO();
        inputDTO.setVillageId(2L);
        inputDTO.setQuestionId(3L);
        inputDTO.setAnswer("Updated answer");

        VillageAnswerQuestion existingVillageAnswerQuestion = new VillageAnswerQuestion();
        existingVillageAnswerQuestion.setId(id);

        when(villageAnswerQuestionRepository.findById(id)).thenReturn(Optional.of(existingVillageAnswerQuestion));
        when(villageService.checkVillage(2L)).thenThrow(new ApiRequestException("Village not found"));

        assertThrows(ApiRequestException.class, () -> villageAnswerQuestionService.updateVillageAnswerQuestion(id, inputDTO));

        verify(villageAnswerQuestionRepository, times(1)).findById(id);
        verify(villageService, times(1)).checkVillage(2L);
        verify(questionService, never()).checkQuestion(anyLong());
        verify(villageAnswerQuestionRepository, never()).save(any(VillageAnswerQuestion.class));
    }

    @Test
    void testUpdateVillageAnswerQuestionWhenInvalidQuestionIdShouldThrowApiRequestException() {
        Long id = 1L;
        VillageAnswerQuestionDTO inputDTO = new VillageAnswerQuestionDTO();
        inputDTO.setVillageId(2L);
        inputDTO.setQuestionId(3L);
        inputDTO.setAnswer("Updated answer");

        VillageAnswerQuestion existingVillageAnswerQuestion = new VillageAnswerQuestion();
        existingVillageAnswerQuestion.setId(id);

        Village village = new Village();
        village.setId(2L);
        when(villageService.checkVillage(2L)).thenReturn(village);
        existingVillageAnswerQuestion.setVillage(village);

        when(villageAnswerQuestionRepository.findById(id)).thenReturn(Optional.of(existingVillageAnswerQuestion));
        when(questionService.checkQuestion(3L)).thenThrow(new ApiRequestException("Question not found"));

        assertThrows(ApiRequestException.class, () -> villageAnswerQuestionService.updateVillageAnswerQuestion(id, inputDTO));

        verify(villageAnswerQuestionRepository, times(1)).findById(id);
        verify(villageService, times(1)).checkVillage(2L);
        verify(questionService, times(1)).checkQuestion(3L);
        verify(villageAnswerQuestionRepository, never()).save(any(VillageAnswerQuestion.class));
    }

    @Test
    void testUpdateVillageAnswerQuestionWhenNotFoundShouldThrowApiRequestException() {
        Long id = 1L;
        VillageAnswerQuestionDTO inputDTO = new VillageAnswerQuestionDTO();
        inputDTO.setVillageId(2L);
        inputDTO.setQuestionId(3L);
        inputDTO.setAnswer("Updated answer");

        when(villageAnswerQuestionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageAnswerQuestionService.updateVillageAnswerQuestion(id, inputDTO));

        verify(villageAnswerQuestionRepository, times(1)).findById(id);
        verify(villageService, never()).checkVillage(anyLong());
        verify(questionService, never()).checkQuestion(anyLong());
        verify(villageAnswerQuestionRepository, never()).save(any(VillageAnswerQuestion.class));
    }

    @Test
    void testUpdateVillageAnswerQuestionWithInvalidId() {
        Long id = 1L;
        VillageAnswerQuestionDTO inputDTO = new VillageAnswerQuestionDTO();
        inputDTO.setVillageId(2L);
        inputDTO.setQuestionId(3L);
        inputDTO.setAnswer("Updated answer");

        when(villageAnswerQuestionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageAnswerQuestionService.updateVillageAnswerQuestion(id, inputDTO));

        verify(villageService, never()).checkVillage(anyLong());
        verify(questionService, never()).checkQuestion(anyLong());
        verify(villageAnswerQuestionRepository, never()).save(any(VillageAnswerQuestion.class));
    }

    @Test
    void testUpdateVillageAnswerQuestionWithInvalidVillageId() {
        Long id = 1L;
        VillageAnswerQuestionDTO inputDTO = new VillageAnswerQuestionDTO();
        inputDTO.setVillageId(999L);
        inputDTO.setQuestionId(3L);
        inputDTO.setAnswer("Updated answer");

        when(villageAnswerQuestionRepository.findById(id)).thenReturn(Optional.of(new VillageAnswerQuestion()));
        when(villageService.checkVillage(anyLong())).thenThrow(new ApiRequestException("Invalid VillageId"));

        assertThrows(ApiRequestException.class, () -> villageAnswerQuestionService.updateVillageAnswerQuestion(id, inputDTO));

        verify(questionService, never()).checkQuestion(anyLong());
        verify(villageAnswerQuestionRepository, never()).save(any(VillageAnswerQuestion.class));
    }

    @Test
    void testUpdateVillageAnswerQuestionWithInvalidQuestionId() {
        Long id = 1L;
        VillageAnswerQuestionDTO inputDTO = new VillageAnswerQuestionDTO();
        inputDTO.setVillageId(2L);
        inputDTO.setQuestionId(999L);
        inputDTO.setAnswer("Updated answer");

        when(villageAnswerQuestionRepository.findById(id)).thenReturn(Optional.of(new VillageAnswerQuestion()));
        when(villageService.checkVillage(anyLong())).thenReturn(new Village());
        when(questionService.checkQuestion(anyLong())).thenThrow(new ApiRequestException("Invalid QuestionId"));

        assertThrows(ApiRequestException.class, () -> villageAnswerQuestionService.updateVillageAnswerQuestion(id, inputDTO));

        verify(villageAnswerQuestionRepository, never()).save(any(VillageAnswerQuestion.class));
    }


    @Test
    void testUpdateVillageAnswerQuestion() {
        Long villageAnswerQuestionId = 1L;
        Long villageId = 10L;
        Long questionId = 20L;
        String answer = "Test Answer";

        VillageAnswerQuestionDTO villageAnswerQuestionDTO = new VillageAnswerQuestionDTO();
        villageAnswerQuestionDTO.setVillageId(villageId);
        villageAnswerQuestionDTO.setQuestionId(questionId);
        villageAnswerQuestionDTO.setAnswer(answer);

        VillageAnswerQuestion foundVillageAnswerQuestion = new VillageAnswerQuestion();
        foundVillageAnswerQuestion.setId(villageAnswerQuestionId);

        when(villageAnswerQuestionRepository.findById(villageAnswerQuestionId)).thenReturn(Optional.of(foundVillageAnswerQuestion));
        when(villageService.checkVillage(villageId)).thenReturn(new Village());
        when(questionService.checkQuestion(questionId)).thenReturn(new Question());
        when(villageAnswerQuestionRepository.save(foundVillageAnswerQuestion)).thenReturn(foundVillageAnswerQuestion);

        VillageAnswerQuestionDTO result = villageAnswerQuestionService.updateVillageAnswerQuestion(villageAnswerQuestionId, villageAnswerQuestionDTO);

        verify(villageAnswerQuestionRepository, times(1)).findById(villageAnswerQuestionId);
        verify(villageService, times(1)).checkVillage(villageId);
        verify(questionService, times(1)).checkQuestion(questionId);
        verify(villageAnswerQuestionRepository, times(1)).save(foundVillageAnswerQuestion);
    }


    @Test
    void testExistsByVillageIdAndQuestionIdAndAnswerWhenExistsThenReturnsTrue() {
        Long villageId = 1L;
        Long questionId = 2L;
        String answer = "Yes";

        when(villageAnswerQuestionRepository.existsByVillageIdAndQuestionIdAndAnswer(villageId, questionId, answer)).thenReturn(true);

        boolean result = villageAnswerQuestionService.existsByVillageIdAndQuestionIdAndAnswer(villageId, questionId, answer);

        assertTrue(result);
        verify(villageAnswerQuestionRepository, times(1)).existsByVillageIdAndQuestionIdAndAnswer(villageId, questionId, answer);
    }

    @Test
    void testExistsByVillageIdAndQuestionIdAndAnswerWhenNotExistsThenReturnsFalse() {
        Long villageId = 1L;
        Long questionId = 2L;
        String answer = "No";

        when(villageAnswerQuestionRepository.existsByVillageIdAndQuestionIdAndAnswer(villageId, questionId, answer)).thenReturn(false);

        boolean result = villageAnswerQuestionService.existsByVillageIdAndQuestionIdAndAnswer(villageId, questionId, answer);

        assertFalse(result);
        verify(villageAnswerQuestionRepository, times(1)).existsByVillageIdAndQuestionIdAndAnswer(villageId, questionId, answer);
    }

    @Test
    void testGetAnswersQuestionResponsesByVillageId() {
        Long villageId = 1L;
        Village village = new Village();
        village.setId(villageId);
        Question question1 = new Question();
        question1.setQuestionName("Question 1");
        question1.setId(1L);
        Question question2 = new Question();
        question2.setQuestionName("Question 2");
        question2.setId(2L);
        VillageAnswerQuestion answerQuestion1 = new VillageAnswerQuestion(1L, village, question1, "Answer 1", true, LocalDateTime.now(),null);
        VillageAnswerQuestion answerQuestion2 = new VillageAnswerQuestion(2L, village, question1, "Answer 2", true, LocalDateTime.now(),null);
        VillageAnswerQuestion answerQuestion3 = new VillageAnswerQuestion(3L, village, question2, "Answer 3", true, LocalDateTime.now(),null);

        List<VillageAnswerQuestion> villageAnswerQuestions = new ArrayList<>();
        villageAnswerQuestions.add(answerQuestion1);
        villageAnswerQuestions.add(answerQuestion2);
        villageAnswerQuestions.add(answerQuestion3);

        when(villageAnswerQuestionRepository.findByVillageIdAndVillageStatus(villageId, true)).thenReturn(villageAnswerQuestions);

        List<AnswersQuestionResponse> result = villageAnswerQuestionService.getAnswersQuestionResponsesByVillageId(villageId,true,null);

        assertEquals(2, result.size());

        AnswersQuestionResponse response1 = result.get(0);
        assertEquals("Question 1", response1.getQuestion());
        assertEquals(2, response1.getAnswers().size());
        assertEquals("Answer 1", response1.getAnswers().get(0));
        assertEquals("Answer 2", response1.getAnswers().get(1));

        AnswersQuestionResponse response2 = result.get(1);
        assertEquals("Question 2", response2.getQuestion());
        assertEquals(1, response2.getAnswers().size());
        assertEquals("Answer 3", response2.getAnswers().get(0));
    }

    @Test
    void testGroupAnswersByQuestion() {
        Village village1 = new Village();
        village1.setId(1L);
        VillageAnswerQuestion answerQuestion1 = new VillageAnswerQuestion(1L, village1, new Question(1L, "Question 1"), "Answer 1", true, LocalDateTime.now(),null);
        VillageAnswerQuestion answerQuestion2 = new VillageAnswerQuestion(2L, village1, new Question(1L,"Question 1"), "Answer 2", true, LocalDateTime.now(),null);
        VillageAnswerQuestion answerQuestion3 = new VillageAnswerQuestion(3L, village1, new Question(2L,"Question 2"), "Answer 3", true, LocalDateTime.now(),null);
        VillageAnswerQuestion answerQuestion4 = new VillageAnswerQuestion(4L, village1, new Question(2L,"Question 2"), "Answer 4", true, LocalDateTime.now(),null);

        List<VillageAnswerQuestion> villageAnswerQuestions = new ArrayList<>();
        villageAnswerQuestions.add(answerQuestion1);
        villageAnswerQuestions.add(answerQuestion2);
        villageAnswerQuestions.add(answerQuestion3);
        villageAnswerQuestions.add(answerQuestion4);

        Map<String, List<String>> result = villageAnswerQuestionService.groupAnswersByQuestion(villageAnswerQuestions);

        assertEquals(2, result.size());
        assertEquals(List.of("Answer 1", "Answer 2"), result.get("Question 1"));
        assertEquals(List.of("Answer 3", "Answer 4"), result.get("Question 2"));
    }

    @Test
    void testCreateAnswersQuestionResponses() {
        Map<String, List<String>> questionToAnswersMap = new HashMap<>();
        List<String> answers1 = List.of("Answer 1", "Answer 2");
        List<String> answers2 = List.of("Answer 3");
        questionToAnswersMap.put("Question 1", answers1);
        questionToAnswersMap.put("Question 2", answers2);


        List<AnswersQuestionResponse> result = villageAnswerQuestionService.createAnswersQuestionResponses(questionToAnswersMap);

        assertEquals(2, result.size());

        AnswersQuestionResponse response1 = result.get(0);
        assertEquals("Question 1", response1.getQuestion());
        assertEquals(2, response1.getAnswers().size());
        assertEquals("Answer 1", response1.getAnswers().get(0));
        assertEquals("Answer 2", response1.getAnswers().get(1));

        AnswersQuestionResponse response2 = result.get(1);
        assertEquals("Question 2", response2.getQuestion());
        assertEquals(1, response2.getAnswers().size());
        assertEquals("Answer 3", response2.getAnswers().get(0));
    }

    @Test
    void testUpdateVillageAnswerQuestionStatus() {
        Long villageId = 1L;
        String localDateTime = "2023-08-10T00:00:00";
        boolean status = true;

        Village village = new Village();
        village.setId(villageId);

        VillageAnswerQuestion answerQuestion = new VillageAnswerQuestion();
        answerQuestion.setVillage(village);

        List<VillageAnswerQuestion> answerQuestions = new ArrayList<>();
        answerQuestions.add(answerQuestion);

        when(villageAnswerQuestionRepository.findByVillageIdAndVillageStatusAndDateUpload(
                villageId, status, localDateTime
        )).thenReturn(answerQuestions);

        villageAnswerQuestionService.updateVillageAnswerQuestionStatus(villageId, status, localDateTime);

        verify(villageService, times(1)).checkVillage(villageId);
        verify(villageAnswerQuestionRepository).saveAll(answerQuestionListCaptor.capture());

    }

    @Test
    void testRejectVillageAnswerQuestionResponse() {
        Long villageId = 1L;
        String responseDate = "2023-08-10T00:00:00";
        LocalDateTime deleteDate = LocalDateTime.now();

        Village village = new Village();
        village.setId(villageId);

        VillageAnswerQuestion answerQuestion = new VillageAnswerQuestion();
        answerQuestion.setVillage(village);

        List<VillageAnswerQuestion> answerQuestions = new ArrayList<>();
        answerQuestions.add(answerQuestion);

        when(villageAnswerQuestionRepository.findByVillageIdAndVillageStatusAndDateUpload(
                villageId, true, responseDate
        )).thenReturn(answerQuestions);

        villageAnswerQuestionService.rejectVillageAnswerQuestionResponse(villageId, true, responseDate, deleteDate);

        verify(villageService, times(1)).checkVillage(villageId);
        verify(villageAnswerQuestionRepository).saveAll(answerQuestionListCaptor.capture());

    }
}

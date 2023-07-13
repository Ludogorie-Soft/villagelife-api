package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.QuestionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiExceptionHandler;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.services.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = QuestionController.class
        , useDefaultFilters = false
        , includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = QuestionController.class
        ), @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = ApiExceptionHandler.class
)
}
)
class QuestionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllQuestions() throws Exception {
        QuestionDTO questionDTO1 = new QuestionDTO();
        questionDTO1.setId(1L);
        questionDTO1.setQuestion("Question 1");
        QuestionDTO questionDTO2 = new QuestionDTO();
        questionDTO2.setId(2L);
        questionDTO2.setQuestion("Question 2");
        List<QuestionDTO> landscapeDTOList = Arrays.asList(questionDTO1, questionDTO2);

        when(questionService.getAllQuestions()).thenReturn(landscapeDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].question").value("Question 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].question").value("Question 2"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetQuestionById() throws Exception {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(1L);
        questionDTO.setQuestion("Question 1");

        when(questionService.getQuestionById(anyLong())).thenReturn(questionDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/questions/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question").value("Question 1"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testCreateQuestion() throws Exception {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(1L);
        questionDTO.setQuestion("New Question");

        when(questionService.createQuestion(any(QuestionDTO.class))).thenReturn(questionDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/questions")
                        .content("{\"id\": 1, \"question\": \"New Question\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question").value("New Question"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testUpdateQuestion() throws Exception {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(1L);
        questionDTO.setQuestion("Updated Question");

        when(questionService.updateQuestion(anyLong(), any(QuestionDTO.class))).thenReturn(questionDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/questions/{id}", 1)
                        .content("{\"id\": 1, \"question\": \"Updated Question\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question").value("Updated Question"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testDeleteQuestionById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/questions/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Question with id: 1 has been deleted successfully!!"));
    }

    @Test
    void testGetAllQuestionsWhenNoQuestionExist() throws Exception {
        when(questionService.getAllQuestions()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }


    @Test
    void testGetQuestionByIdWhenQuestionDoesNotExist() throws Exception {
        Long invalidId = 100000L;

        when(questionService.getQuestionById(invalidId))
                .thenThrow(new ApiRequestException("Question with id: " + invalidId + " Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/questions/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Question with id: " + invalidId + " Not Found"))
                .andReturn();
    }

    @Test
    void testShouldNotCreateQuestionWithBlankQuestion() throws Exception {
        String blankQuestion = "";

        doThrow(new ApiRequestException("Question is blank"))
                .when(questionService).createQuestion(any(QuestionDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/questions")
                        .content("{\"id\": 1, \"question\": \"" + blankQuestion + "\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Question is blank"))
                .andReturn();
    }


    @Test
    void testShouldNotUpdateQuestionWithInvalidRequestBody() throws Exception {
        String blankQuestion = "";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/questions/{id}", 1)
                        .content("{\"id\": 1, \"question\": }" + blankQuestion)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    @Test
    void testDeleteQuestionByIdWhenQuestionIdDoesNotExist() throws Exception {
        Long invalidId = 100L;

        doThrow(new ApiRequestException("Question not found for id " + invalidId))
                .when(questionService).deleteQuestionById(invalidId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/questions/{id}", invalidId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Question not found for id " + invalidId))
                .andReturn();
    }

}

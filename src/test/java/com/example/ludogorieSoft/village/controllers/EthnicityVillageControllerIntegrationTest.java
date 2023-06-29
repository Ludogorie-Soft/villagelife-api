package com.example.ludogorieSoft.village.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class EthnicityVillageControllerIntegrationTest {
//    @MockBean
//    private EthnicityVillageService ethnicityVillageService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void getAllEthnicityVillagesShouldReturnListOfEthnicityVillages() throws Exception {
//        EthnicityVillageDTO ethnicity1 = new EthnicityVillageDTO();
//        ethnicity1.setId(1L);
//        ethnicity1.setVillageId(1L);
//        ethnicity1.setEthnicityId(1L);
//
//        EthnicityVillageDTO ethnicity2 = new EthnicityVillageDTO();
//        ethnicity2.setId(2L);
//        ethnicity2.setVillageId(2L);
//        ethnicity2.setEthnicityId(2L);
//
//        List<EthnicityVillageDTO> ethnicityVillages = Arrays.asList(ethnicity1, ethnicity2);
//
//        when(ethnicityVillageService.getAllEthnicityVillages()).thenReturn(ethnicityVillages);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageEthnicities")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].villageId").value(1L))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ethnicityId").value(1L))
//
//                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[1].villageId").value(2L))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[1].ethnicityId").value(2L));
//    }
//
//    @Test
//    void getEthnicityVillageByIdWithValidIdShouldReturnEthnicityVillage() throws Exception {
//        // Arrange
//        Long id = 1L;
//
//        EthnicityVillageDTO ethnicityVillage = new EthnicityVillageDTO();
//        ethnicityVillage.setId(id);
//        ethnicityVillage.setVillageId(1L);
//        ethnicityVillage.setEthnicityId(1L);
//
//        when(ethnicityVillageService.getEthnicityVillageById(id)).thenReturn(ethnicityVillage);
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageEthnicities/{id}", id))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.villageId").value(1L))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.ethnicityId").value(1L));
//    }
//
//    @Test
//    void getEthnicityVillageByIdWithInvalidIdShouldReturnNotFound() throws Exception {
//        Long id = 1L;
//
//        when(ethnicityVillageService.getEthnicityVillageById(id)).thenThrow(new ApiRequestException("Ethnicity in Village with id " + id + " not found"));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageEthnicities/{id}", id))
//                .andExpect(status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Ethnicity in Village with id " + id + " not found"));
//    }
//
//    @Test
//    void createEthnicityVillageShouldReturnCreatedEthnicityVillage() throws Exception {
//        EthnicityVillageDTO newEthnicity = new EthnicityVillageDTO();
//        newEthnicity.setId(1L);
//        newEthnicity.setVillageId(1L);
//        newEthnicity.setEthnicityId(1L);
//
//        EthnicityVillageDTO createdEthnicity = new EthnicityVillageDTO();
//        createdEthnicity.setId(1L);
//        createdEthnicity.setVillageId(1L);
//        createdEthnicity.setEthnicityId(1L);
//
//        when(ethnicityVillageService.createEthnicityVillage(newEthnicity)).thenReturn(createdEthnicity);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/villageEthnicities")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(newEthnicity)))
//                .andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/api/v1/villageEthnicities/1"));
//    }
//
//
//    @Test
//    void updateEthnicityVillageByIdWithValidIdShouldReturnUpdatedEthnicityVillage() throws Exception {
//        EthnicityVillageDTO existingEthnicity = new EthnicityVillageDTO();
//        existingEthnicity.setId(1L);
//        existingEthnicity.setVillageId(1L);
//        existingEthnicity.setEthnicityId(1L);
//
//        EthnicityVillageDTO updatedEthnicity = new EthnicityVillageDTO();
//        updatedEthnicity.setId(1L);
//        updatedEthnicity.setVillageId(1L);
//        updatedEthnicity.setEthnicityId(1L);
//
//        when(ethnicityVillageService.updateEthnicityVillageById(1L, updatedEthnicity)).thenReturn(updatedEthnicity);
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/villageEthnicities/{id}", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(updatedEthnicity)))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.villageId").value(1L))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.ethnicityId").value(1L));
//    }
//
//
//    @Test
//    void deleteEthnicityVillageByIdWithValidIdShouldReturnSuccessMessage() throws Exception {
//        Long id = 1L;
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/villageEthnicities/{id}", id))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("Ethnicity in Village with id: " + id + " has been deleted successfully!!"));
//    }
//
//    @Test
//    void testGetAllEthnicityVillagesWhenNoneExist() throws Exception {
//        when(ethnicityVillageService.getAllEthnicityVillages()).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageEthnicities")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(0))
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$").isEmpty())
//                .andReturn();
//    }
//
//    @Test
//    void deleteEthnicityVillageByIdWithInvalidIdShouldReturnNotFound() throws Exception {
//        Long id = 1L;
//
//        doThrow(new ApiRequestException("Ethnicity in Village with id " + id + " not found")).when(ethnicityVillageService).deleteEthnicityVillageById(id);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/villageEthnicities/{id}", id))
//                .andExpect(status().isNotFound())
//                .andExpect(MockMvcResultMatchers.content().string("Ethnicity in Village with id " + id + " not found"));
//    }
//
//    private String asJsonString(Object obj) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.writeValueAsString(obj);
//    }
}

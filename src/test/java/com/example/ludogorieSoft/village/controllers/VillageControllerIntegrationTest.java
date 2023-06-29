package com.example.ludogorieSoft.village.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

//@WebMvcTest(VillageController.class)
@AutoConfigureMockMvc
class VillageControllerIntegrationTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private VillageService villageService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAllVillages() throws Exception {
//        VillageDTO villageDTO1 = new VillageDTO();
//        villageDTO1.setId(1L);
//        villageDTO1.setName("Village Name 1");
//        villageDTO1.setDateUpload(new Date());
//        villageDTO1.setStatus(true);
//
//        VillageDTO villageDTO2 = new VillageDTO();
//        villageDTO2.setId(2L);
//        villageDTO2.setName("Village Name 2");
//        villageDTO2.setDateUpload(new Date());
//        villageDTO2.setStatus(true);
//
//        List<VillageDTO> villageDTOList = Arrays.asList(villageDTO1, villageDTO2);
//
//        when(villageService.getAllVillages()).thenReturn(villageDTOList);
//
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villages")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].id").value(1))
//                .andExpect(jsonPath("$.[0].name").value("Village Name 1"))
//                .andExpect(jsonPath("$.[0].population").exists())
//                .andExpect(jsonPath("$.[0].dateUpload").exists())
//                .andExpect(jsonPath("$.[0].status").value(true))
//                .andExpect(jsonPath("$.[1].id").value(2))
//                .andExpect(jsonPath("$.[1].name").value("Village Name 2"))
//                .andExpect(jsonPath("$.[1].population").exists())
//                .andExpect(jsonPath("$.[1].dateUpload").exists())
//                .andExpect(jsonPath("$.[1].status").value(true))
//                .andReturn();
//
//        String response = mvcResult.getResponse().getContentAsString();
//        assertNotNull(response);
//    }
//
//    @Test
//    void testGetVillageById() throws Exception {
//        VillageDTO villageDTO = new VillageDTO();
//        villageDTO.setId(2L);
//        villageDTO.setName("Village Name 2");
//        villageDTO.setDateUpload(new Date());
//        villageDTO.setStatus(false);
//
//        when(villageService.getVillageById(anyLong())).thenReturn(villageDTO);
//
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villages/{id}", 2)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(2))
//                .andExpect(jsonPath("$.name").value("Village Name 2"))
//                .andExpect(jsonPath("$.population").exists())
//                .andExpect(jsonPath("$.dateUpload").exists())
//                .andExpect(jsonPath("$.status").value(false))
//                .andReturn();
//
//        String response = mvcResult.getResponse().getContentAsString();
//        assertNotNull(response);
//    }
//
//    @Test
//    void testCreateVillage() throws Exception {
//        VillageDTO villageDTO = new VillageDTO();
//        villageDTO.setId(3L);
//        villageDTO.setName("Created Village Name");
//        villageDTO.setDateUpload(new Date());
//        villageDTO.setStatus(true);
//
//        when(villageService.createVillage(any(VillageDTO.class))).thenReturn(villageDTO);
//
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/villages")
//                        .content("{\"id\": 3}")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(3))
//                .andExpect(jsonPath("$.name").value("Created Village Name"))
//                .andExpect(jsonPath("$.population").exists())
//                .andExpect(jsonPath("$.dateUpload").exists())
//                .andExpect(jsonPath("$.status").value(true))
//                .andReturn();
//
//        String response = mvcResult.getResponse().getContentAsString();
//        assertNotNull(response);
//    }
//
//    @Test
//    void testUpdateVillage() throws Exception {
//        VillageDTO villageDTO = new VillageDTO();
//        villageDTO.setId(1L);
//        villageDTO.setName("Updated Village Name");
//        villageDTO.setDateUpload(new Date());
//        villageDTO.setStatus(false);
//
//        when(villageService.updateVillage(anyLong(), any(VillageDTO.class))).thenReturn(villageDTO);
//
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/villages/{id}", 1)
//                        .content("{\"id\": 1}")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("Updated Village Name"))
//                .andExpect(jsonPath("$.population").exists())
//                .andExpect(jsonPath("$.dateUpload").exists())
//                .andExpect(jsonPath("$.status").value(false))
//                .andReturn();
//
//        String response = mvcResult.getResponse().getContentAsString();
//        assertNotNull(response);
//    }
//
//    @Test
//    void testGetAllVillagesWhenNoneExist() throws Exception {
//        when(villageService.getAllVillages()).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villages")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(0))
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$").isEmpty())
//                .andReturn();
//    }
//
//
//    @Test
//    void testDeleteVillage() throws Exception {
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/villages/{id}", 1))
//                .andExpect(status().isNoContent());
//    }
}

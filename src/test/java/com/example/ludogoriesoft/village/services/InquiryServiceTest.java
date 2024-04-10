package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.InquiryDTO;
import com.example.ludogorieSoft.village.enums.InquiryType;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Inquiry;
import com.example.ludogorieSoft.village.model.Region;
import com.example.ludogorieSoft.village.model.Village;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import com.example.ludogorieSoft.village.repositories.InquiryRepository;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

class InquiryServiceTest {
    @Mock
    EmailSenderService emailSenderService;
    @Mock
    InquiryRepository inquiryRepository;
    @Mock
    VillageService villageService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    InquiryService inquiryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInquiryWhenSuccessful() {
        InquiryDTO inquiryDTO = new InquiryDTO(1L, "John Doe", "johndoe@example.com", "Hello, this is a test inquiry message.", "1234567890", 1L, InquiryType.JOB_OPPORTUNITIES);

        Village village = new Village();
        village.setId(1L);
        village.setName("Test Village");
        village.setRegion(new Region(1L, "Test Region"));

        when(villageService.checkVillage(1L)).thenReturn(village);

        Inquiry inquiry = new Inquiry(null, inquiryDTO.getUserName(), inquiryDTO.getEmail(), inquiryDTO.getUserMessage(), inquiryDTO.getMobile(), village, inquiryDTO.getInquiryType());
        when(inquiryRepository.save(any(Inquiry.class))).thenReturn(inquiry);

        InquiryDTO result = inquiryService.createInquiry(inquiryDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(inquiryDTO.getUserName(), result.getUserName());
        Assertions.assertEquals(inquiryDTO.getEmail(), result.getEmail());
        Assertions.assertEquals(inquiryDTO.getUserMessage(), result.getUserMessage());

        verify(inquiryRepository).save(any(Inquiry.class));
        verify(emailSenderService).sendEmail(inquiryDTO.getEmail(), "<html><body><table></table></body></html>","VillageLife - \u0412\u044a\u0437\u043c\u043e\u0436\u043d\u043e\u0441\u0442\u0438 \u0437\u0430 \u0440\u0430\u0431\u043e\u0442\u0430");
    }


    @Test
    void testCreateInquiryErrorCreatingInquiry() {
        InquiryDTO inquiryDTO = new InquiryDTO();
        inquiryDTO.setUserName("John Doe");
        inquiryDTO.setEmail("johndoe@example.com");
        inquiryDTO.setUserMessage("Hello, this is a test inquiry message.");
        inquiryDTO.setMobile("1234567890");
        inquiryDTO.setVillageId(1L);
        inquiryDTO.setInquiryType(InquiryType.LOOKING_FOR_BETTER_INDICATORS);

        when(villageService.checkVillage(1L)).thenReturn(new Village());

        doThrow(new RuntimeException("Error creating inquiry")).when(inquiryRepository).save(any(Inquiry.class));

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            inquiryService.createInquiry(inquiryDTO);
        });

        Assertions.assertEquals("Error creating inquiry", exception.getMessage());

        verify(inquiryRepository).save(any(Inquiry.class));
        verify(emailSenderService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testGetAllInquiriesWithExistingInquiries() {
        Inquiry inquiry1 = new Inquiry();
        inquiry1.setId(1L);
        inquiry1.setUserMessage("Inquiry 1");

        Inquiry inquiry2 = new Inquiry();
        inquiry2.setId(2L);
        inquiry2.setUserMessage("Inquiry 2");

        InquiryDTO inquiryDTO1 = new InquiryDTO();
        inquiryDTO1.setId(1L);
        inquiryDTO1.setUserMessage("Inquiry 1");

        InquiryDTO inquiryDTO2 = new InquiryDTO();
        inquiryDTO2.setId(2L);
        inquiryDTO2.setUserMessage("Inquiry 2");

        List<Inquiry> inquiries = new ArrayList<>();
        inquiries.add(inquiry1);
        inquiries.add(inquiry2);

        List<InquiryDTO> expectedInquiries = new ArrayList<>();
        expectedInquiries.add(inquiryDTO1);
        expectedInquiries.add(inquiryDTO2);

        when(inquiryRepository.findAll()).thenReturn(inquiries);
        when(modelMapper.map(inquiry1, InquiryDTO.class)).thenReturn(inquiryDTO1);
        when(modelMapper.map(inquiry2, InquiryDTO.class)).thenReturn(inquiryDTO2);

        List<InquiryDTO> result = inquiryService.getAllInquiries();

        verify(inquiryRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(inquiry1, InquiryDTO.class);
        verify(modelMapper, times(1)).map(inquiry2, InquiryDTO.class);
        Assertions.assertEquals(expectedInquiries, result);
    }

    @Test
    void testGetAllInquiriesWithNoInquiries() {
        List<Inquiry> inquiries = new ArrayList<>();
        List<InquiryDTO> expectedInquiries = new ArrayList<>();

        when(inquiryRepository.findAll()).thenReturn(inquiries);
        List<InquiryDTO> result = inquiryService.getAllInquiries();

        verify(inquiryRepository, times(1)).findAll();
        verify(modelMapper, never()).map(any(Inquiry.class), eq(InquiryDTO.class));
        Assertions.assertEquals(expectedInquiries, result);
    }

}

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

class InquiryServiceTest {
    @Mock
    EmailSenderService emailSenderService;
    @Mock
    InquiryRepository inquiryRepository;
    @Mock
    VillageService villageService;
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
}

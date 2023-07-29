package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.InquiryDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Inquiry;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.InquiryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final ModelMapper modelMapper;
    private final EmailSenderService emailSenderService;
    private VillageService villageService;
    public InquiryDTO inquiryToInquiryDTO(Inquiry inquiry){
        return modelMapper.map(inquiry, InquiryDTO.class);
    }

    public InquiryDTO createInquiry(InquiryDTO inquiryDTO) {
        try {
            Village village = villageService.checkVillage(inquiryDTO.getVillageId());
            Inquiry inquiry = new Inquiry(null, inquiryDTO.getUserName(), inquiryDTO.getEmail(), inquiryDTO.getUserMessage(), inquiryDTO.getMobile(), village, inquiryDTO.getInquiryType());
            inquiryRepository.save(inquiry);

            emailSenderService.sendEmail(
                    inquiry.getEmail(),
                    "Име на потребител: " + inquiry.getUserName() +
                            "\nТелефон: " + inquiry.getMobile() +
                            "\nEmail: " + inquiry.getEmail() +
                            "\nТип: " + inquiry.getInquiryType().getName() +
                            "\nЗапитване или заявка за село " + village.getName() + " (област " + village.getRegion().getRegionName() + "): " +
                            inquiry.getUserMessage(),
                    "VillageLife - " + inquiry.getInquiryType().getName());
            return inquiryDTO;
        } catch (Exception e) {
            throw new ApiRequestException("Error creating inquiry");
        }
    }
}

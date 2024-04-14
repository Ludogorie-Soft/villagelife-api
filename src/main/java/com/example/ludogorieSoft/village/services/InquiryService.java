package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.InquiryDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Inquiry;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.InquiryRepository;
import com.example.ludogorieSoft.village.utils.TimestampUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final EmailSenderService emailSenderService;
    private VillageService villageService;
    private final ModelMapper modelMapper;
    public InquiryDTO inquiryToInquiryDTO(Inquiry inquiry){
        return modelMapper.map(inquiry, InquiryDTO.class);
    }

    public InquiryDTO createInquiry(InquiryDTO inquiryDTO) {
        try {
            Village village = villageService.checkVillage(inquiryDTO.getVillageId());
            Inquiry inquiry = new Inquiry(null, inquiryDTO.getUserName().trim(), inquiryDTO.getEmail().trim(), inquiryDTO.getUserMessage().trim(), inquiryDTO.getMobile().trim(), village, inquiryDTO.getInquiryType(), TimestampUtils.getCurrentTimestamp());
            inquiryRepository.save(inquiry);
            String emailBody = createInquiryEmailBody(inquiry);
            emailSenderService.sendEmail(inquiry.getEmail(), emailBody, "VillageLife - " + inquiry.getInquiryType().getName());
            return inquiryDTO;
        } catch (Exception e) {
            throw new ApiRequestException("Error creating inquiry");
        }
    }
    public String createInquiryEmailBody(Inquiry inquiry){
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("<html><body><table>");
        emailSenderService.addTableRow(emailBody, "Име на потребител", inquiry.getUserName());
        emailSenderService.addTableRow(emailBody, "Телефон", inquiry.getMobile());
        emailSenderService.addTableRow(emailBody, "Email", inquiry.getEmail());
        emailSenderService.addTableRow(emailBody, "Тип", inquiry.getInquiryType().getName());
        emailSenderService.addTableRow(emailBody, "Запитване или заявка за село " + inquiry.getVillage().getName() + " (област " + inquiry.getVillage().getRegion().getRegionName() + ")", inquiry.getUserMessage());
        emailBody.append("</table></body></html>");
        return emailBody.toString();
    }
    public List<InquiryDTO> getAllInquiries() {
        return inquiryRepository.findAll()
                .stream()
                .map(this::inquiryToInquiryDTO)
                .toList();
    }
}

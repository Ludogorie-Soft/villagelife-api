package com.example.ludogorieSoft.village.dtos;

import com.example.ludogorieSoft.village.enums.Consents;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VillagePopulationAssertionDTO {

        private Long id;
        private Long villageId;
        private Long populatedAssertionId;
        private Consents answer;
        private Boolean status;
        private LocalDateTime dateUpload;
}

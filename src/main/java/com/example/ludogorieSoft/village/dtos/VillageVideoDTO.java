package com.example.ludogorieSoft.village.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VillageVideoDTO {
        private Long id;
        private Long villageId;
        private String url;
        private Boolean villageStatus;
        private LocalDateTime dateUpload;
        private LocalDateTime dateDeleted;
        private UserDTO userDTO;
}

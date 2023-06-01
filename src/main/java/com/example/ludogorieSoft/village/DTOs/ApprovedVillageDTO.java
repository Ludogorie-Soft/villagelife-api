package com.example.ludogorieSoft.village.DTOs;

import com.example.ludogorieSoft.village.Model.Administrator;
import com.example.ludogorieSoft.village.Model.Village;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovedVillageDTO {

    private Long id;
    private Village village;
    private Administrator admin;
    private Date dateApproved;
}

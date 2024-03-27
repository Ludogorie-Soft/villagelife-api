package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.services.VillageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/filter")
@AllArgsConstructor
public class FilterController {
    private VillageService villageSearchService;

    @GetMapping("/searchVillages")
    public ResponseEntity<Page<VillageDTO>> searchVillagesByCriteria(
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "name", required = false) String villageName,
            @RequestParam(value = "objectAroundVillageDTOS", required = false) List<String> objectAroundVillageDTOS,
            @RequestParam(value = "livingConditionDTOS", required = false) List<String> livingConditionDTOS,
            @RequestParam(value = "children", required = false) String children,
            @PageableDefault(size = 6, sort = "name") Pageable pageable
    ) {
        Children childrenEnum;
        if (children != null) {
            childrenEnum = Children.valueOf(children);
        } else {
            childrenEnum = null;
        }
        Page<VillageDTO> villages = villageSearchService.getSearchVillages2(region, villageName, objectAroundVillageDTOS, livingConditionDTOS, childrenEnum, pageable);
        return ResponseEntity.ok(villages);
    }
}

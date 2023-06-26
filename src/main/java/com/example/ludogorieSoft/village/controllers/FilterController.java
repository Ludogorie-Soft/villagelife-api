package com.example.ludogoriesoft.village.controllers;

import com.example.ludogoriesoft.village.dtos.VillageDTO;
import com.example.ludogoriesoft.village.enums.Children;
import com.example.ludogoriesoft.village.services.VillageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/filter")
public class FilterController {
//test
    @Autowired
    private VillageService villageSearchService;


    @GetMapping("/byName")
    public ResponseEntity<List<VillageDTO>> getVillageByName(@RequestParam("name") String name) {
        List<VillageDTO> villages = villageSearchService.getAllSearchVillages(name);
        return ResponseEntity.ok(villages);
    }

    @GetMapping("/byRegion")
    public ResponseEntity<List<VillageDTO>> getVillageByRegion(@RequestParam("region") String region) {
        List<VillageDTO> villages = villageSearchService.getAllSearchVillagesByRegionName(region);
        return ResponseEntity.ok(villages);
    }

    @GetMapping("/searchAll")
    public ResponseEntity<List<VillageDTO>> getVillageByNameAndRegion(@RequestParam String region, @RequestParam String keyword) {
        return ResponseEntity.ok(villageSearchService.getAllSearchVillagesByNameAndRegionName(region, keyword));
    }


    @GetMapping("/searchVillages")
    public ResponseEntity<List<VillageDTO>> searchVillagesByCriteria(
            @RequestParam("objectAroundVillageDTOS") List<String> objectAroundVillageDTOS,
            @RequestParam("livingConditionDTOS") List<String> livingConditionDTOS,
            @RequestParam("children") String children
    ) {
        Children childrenEnum = Children.valueOf(children);
        List<VillageDTO> villages = villageSearchService.getSearchVillages(objectAroundVillageDTOS, livingConditionDTOS, childrenEnum);
        return ResponseEntity.ok(villages);
    }


    @GetMapping("/searchVillagesByLivingConditionAndChildren")
    public ResponseEntity<List<VillageDTO>> searchVillagesByLivingConditionAndChildren(
            @RequestParam("livingConditionDTOS") List<String> livingConditionDTOS,
            @RequestParam("children") String children
    ) {
        Children childrenEnum = Children.valueOf(children);
        List<VillageDTO> villages = villageSearchService.getSearchVillagesByLivingConditionAndChildren(livingConditionDTOS, childrenEnum);
        return ResponseEntity.ok(villages);
    }


    @GetMapping("/searchVillagesByObjectAndChildren")
    public ResponseEntity<List<VillageDTO>> searchVillagesByObjectAndChildren(
            @RequestParam("objectAroundVillageDTOS") List<String> objectAroundVillageDTOS,
            @RequestParam("children") String children
    ) {
        Children childrenEnum = Children.valueOf(children);
        List<VillageDTO> villages = villageSearchService.getSearchVillagesByObjectAndChildren(objectAroundVillageDTOS, childrenEnum);
        return ResponseEntity.ok(villages);
    }


    @GetMapping("/searchVillagesByObjectAndLivingCondition")
    public ResponseEntity<List<VillageDTO>> searchVillagesByObjectAndLivingCondition(
            @RequestParam("objectAroundVillageDTOS") List<String> objectAroundVillageDTOS,
            @RequestParam("livingConditionDTOS") List<String> livingConditionDTOS) {
        List<VillageDTO> villages = villageSearchService.getSearchVillagesByObjectAndLivingCondition(objectAroundVillageDTOS, livingConditionDTOS);
        return ResponseEntity.ok(villages);
    }

}
package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.services.VillageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("api/v1/filter")
public class FilterController {

    @Autowired
    private VillageService villageSearchService;


    @GetMapping
    public ResponseEntity<List<VillageDTO>> getAllApprovedVillages() {
        List<VillageDTO> approvedVillages = villageSearchService.getAllApprovedVillages();
        return ResponseEntity.ok(approvedVillages);
    }

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
    public ResponseEntity<List<VillageDTO>> getVillageByNameAndRegion(@RequestParam String region, @RequestParam String keyword, @RequestParam(required = false) String sort) {
        List<VillageDTO> villages = villageSearchService.getAllSearchVillagesByNameAndRegionName(region, keyword);

        if ("nameAsc".equals(sort)) {
            villages.sort(Comparator.comparing(VillageDTO::getName));
        } else if ("nameDesc".equals(sort)) {
            villages.sort(Comparator.comparing(VillageDTO::getName).reversed());
        }
        return ResponseEntity.ok(villages);
    }

    @GetMapping("/searchVillages")
    public ResponseEntity<List<VillageDTO>> searchVillagesByCriteria(
            @RequestParam("objectAroundVillageDTOS") List<String> objectAroundVillageDTOS,
            @RequestParam("livingConditionDTOS") List<String> livingConditionDTOS,
            @RequestParam("children") String children,
            @RequestParam(required = false) String sort
    ) {
        Children childrenEnum = Children.valueOf(children);
        List<VillageDTO> villages = villageSearchService.getSearchVillages(objectAroundVillageDTOS, livingConditionDTOS, childrenEnum);

        if ("nameAsc".equals(sort)) {
            villages.sort(Comparator.comparing(VillageDTO::getName));
        } else if ("nameDesc".equals(sort)) {
            villages.sort(Comparator.comparing(VillageDTO::getName).reversed());
        }
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


    @GetMapping("/searchVillagesByChildrenCount")
    public ResponseEntity<List<VillageDTO>> searchVillagesByChildrenCount(@RequestParam("children") String children) {
        Children childrenEnum = Children.valueOf(children);
        List<VillageDTO> villages = villageSearchService.getSearchVillagesByChildrenCount(childrenEnum);
        return ResponseEntity.ok(villages);
    }


    @GetMapping("/searchVillagesByObject")
    public ResponseEntity<List<VillageDTO>> searchVillagesByObject(
            @RequestParam("objectAroundVillageDTOS") List<String> objectAroundVillageDTOS) {
        List<VillageDTO> villages = villageSearchService.getSearchVillagesByObject(objectAroundVillageDTOS);
        return ResponseEntity.ok(villages);
    }


    @GetMapping("/searchVillagesByLivingCondition")
    public ResponseEntity<List<VillageDTO>> searchVillagesByLivingCondition(@RequestParam("livingConditionDTOS") List<String> livingConditionDTOS) {
        List<VillageDTO> villages = villageSearchService.getSearchVillagesByLivingCondition(livingConditionDTOS);
        return ResponseEntity.ok(villages);
    }

}

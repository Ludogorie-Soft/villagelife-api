package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.services.VillageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/filter")
public class FilterController {

    @Autowired
    private VillageService villageSearchService;


    @GetMapping("/{page}")
    public ResponseEntity<List<VillageDTO>> getAllApprovedVillages(@PathVariable("page") int page, @RequestParam(required = false) String sort) {
        List<VillageDTO> approvedVillages = villageSearchService.getAllApprovedVillages(page, 6, sort).getContent();
        return ResponseEntity.ok(approvedVillages);
    }

    @GetMapping("/{page}/elementsCount")
    public ResponseEntity<Long> getAllApprovedVillagesElementsCount(@PathVariable("page") int page) {
        Long count = villageSearchService.getAllApprovedVillages(page, 6, "").getTotalElements();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/byName/{page}")
    public ResponseEntity<List<VillageDTO>> getVillageByName(@PathVariable("page") int page, @RequestParam("name") String name, @RequestParam(required = false) String sort) {
        List<VillageDTO> villages = villageSearchService.getAllSearchVillages(name, page, 6, sort).getContent();
        return ResponseEntity.ok(villages);
    }

    @GetMapping("/byName/{page}/elementsCount")
    public ResponseEntity<Long> getVillageByNameElementsCount(@PathVariable("page") int page, @RequestParam("name") String name) {
        Long count = villageSearchService.getAllSearchVillages(name, page, 6, "").getTotalElements();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/byRegion/{page}")
    public ResponseEntity<List<VillageDTO>> getVillageByRegion(@PathVariable("page") int page, @RequestParam("region") String region, @RequestParam(required = false) String sort) {
        List<VillageDTO> villages = villageSearchService.getAllSearchVillagesByRegionName(region, page, 6, sort).getContent();
        return ResponseEntity.ok(villages);
    }

    @GetMapping("/byRegion/{page}/elementsCount")
    public ResponseEntity<Long> getVillageByRegionElementsCount(@PathVariable("page") int page, @RequestParam("region") String region) {
        Long count = villageSearchService.getAllSearchVillagesByRegionName(region, page, 6, "").getTotalElements();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/searchAll/{page}")
    public ResponseEntity<List<VillageDTO>> getVillageByNameAndRegion(@PathVariable("page") int page, @RequestParam String region, @RequestParam String keyword, @RequestParam(required = false) String sort) {
        List<VillageDTO> villages = villageSearchService.getAllSearchVillagesByNameAndRegionName(region, keyword, page, 6, sort).getContent();
        return ResponseEntity.ok(villages);
    }

    @GetMapping("/searchAll/{page}/elementsCount")
    public ResponseEntity<Long> getVillageByNameAndRegionElementsCount(@PathVariable("page") int page, @RequestParam String region, @RequestParam String keyword) {
        Long count = villageSearchService.getAllSearchVillagesByNameAndRegionName(region, keyword, page, 6, "").getTotalElements();
        return ResponseEntity.ok(count);
    }

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

    @GetMapping("/searchVillages/{page}/elementsCount")
    public ResponseEntity<Long> searchVillagesByCriteriaElementsCount(
            @PathVariable("page") int page,
            @RequestParam("objectAroundVillageDTOS") List<String> objectAroundVillageDTOS,
            @RequestParam("livingConditionDTOS") List<String> livingConditionDTOS,
            @RequestParam("children") String children
    ) {
        Children childrenEnum = Children.valueOf(children);
        Long count = villageSearchService.getSearchVillages(objectAroundVillageDTOS, livingConditionDTOS, childrenEnum, page, 6, "").getTotalElements();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/searchVillagesByLivingConditionAndChildren/{page}")
    public ResponseEntity<List<VillageDTO>> searchVillagesByLivingConditionAndChildren(
            @PathVariable("page") int page,
            @RequestParam("livingConditionDTOS") List<String> livingConditionDTOS,
            @RequestParam("children") String children,
            @RequestParam(required = false) String sort
    ) {
        Children childrenEnum = Children.valueOf(children);
        List<VillageDTO> villages = villageSearchService.getSearchVillagesByLivingConditionAndChildren(livingConditionDTOS, childrenEnum, page, 6, sort).getContent();
        return ResponseEntity.ok(villages);
    }

    @GetMapping("/searchVillagesByLivingConditionAndChildren/{page}/elementsCount")
    public ResponseEntity<Long> searchVillagesByLivingConditionAndChildrenElementsCount(
            @PathVariable("page") int page,
            @RequestParam("livingConditionDTOS") List<String> livingConditionDTOS,
            @RequestParam("children") String children
    ) {
        Children childrenEnum = Children.valueOf(children);
        Long count = villageSearchService.getSearchVillagesByLivingConditionAndChildren(livingConditionDTOS, childrenEnum, page, 6, "").getTotalElements();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/searchVillagesByObjectAndChildren/{page}")
    public ResponseEntity<List<VillageDTO>> searchVillagesByObjectAndChildren(
            @PathVariable("page") int page,
            @RequestParam("objectAroundVillageDTOS") List<String> objectAroundVillageDTOS,
            @RequestParam("children") String children,
            @RequestParam(required = false) String sort
    ) {
        Children childrenEnum = Children.valueOf(children);
        List<VillageDTO> villages = villageSearchService.getSearchVillagesByObjectAndChildren(objectAroundVillageDTOS, childrenEnum, page, 6, sort).getContent();
        return ResponseEntity.ok(villages);
    }

    @GetMapping("/searchVillagesByObjectAndChildren/{page}/elementsCount")
    public ResponseEntity<Long> searchVillagesByObjectAndChildrenElementsCount(
            @PathVariable("page") int page,
            @RequestParam("objectAroundVillageDTOS") List<String> objectAroundVillageDTOS,
            @RequestParam("children") String children
    ) {
        Children childrenEnum = Children.valueOf(children);
        Long count = villageSearchService.getSearchVillagesByObjectAndChildren(objectAroundVillageDTOS, childrenEnum, page, 6, "").getTotalElements();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/searchVillagesByObjectAndLivingCondition/{page}")
    public ResponseEntity<List<VillageDTO>> searchVillagesByObjectAndLivingCondition(
            @PathVariable("page") int page,
            @RequestParam("objectAroundVillageDTOS") List<String> objectAroundVillageDTOS,
            @RequestParam("livingConditionDTOS") List<String> livingConditionDTOS,
            @RequestParam(required = false) String sort) {
        List<VillageDTO> villages = villageSearchService.getSearchVillagesByObjectAndLivingCondition(objectAroundVillageDTOS, livingConditionDTOS, page, 6, sort).getContent();
        return ResponseEntity.ok(villages);
    }

    @GetMapping("/searchVillagesByObjectAndLivingCondition/{page}/elementsCount")
    public ResponseEntity<Long> searchVillagesByObjectAndLivingConditionElementsCount(
            @PathVariable("page") int page,
            @RequestParam("objectAroundVillageDTOS") List<String> objectAroundVillageDTOS,
            @RequestParam("livingConditionDTOS") List<String> livingConditionDTOS) {
        Long count = villageSearchService.getSearchVillagesByObjectAndLivingCondition(objectAroundVillageDTOS, livingConditionDTOS, page, 6, "").getTotalElements();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/searchVillagesByChildrenCount/{page}")
    public ResponseEntity<List<VillageDTO>> searchVillagesByChildrenCount(@PathVariable("page") int page, @RequestParam("children") String children, @RequestParam(required = false) String sort) {
        Children childrenEnum = Children.valueOf(children);
        List<VillageDTO> villages = villageSearchService.getSearchVillagesByChildrenCount(childrenEnum, page, 6, sort).getContent();
        return ResponseEntity.ok(villages);
    }

    @GetMapping("/searchVillagesByChildrenCount/{page}/elementsCount")
    public ResponseEntity<Long> searchVillagesByChildrenCountElementsCount(@PathVariable("page") int page, @RequestParam("children") String children) {
        Children childrenEnum = Children.valueOf(children);
        Long count = villageSearchService.getSearchVillagesByChildrenCount(childrenEnum, page, 6, "").getTotalElements();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/searchVillagesByObject/{page}")
    public ResponseEntity<List<VillageDTO>> searchVillagesByObject(
            @PathVariable("page") int page, @RequestParam("objectAroundVillageDTOS") List<String> objectAroundVillageDTOS, @RequestParam(required = false) String sort) {
        List<VillageDTO> villages = villageSearchService.getSearchVillagesByObject(objectAroundVillageDTOS, page, 6, sort).getContent();
        return ResponseEntity.ok(villages);
    }

    @GetMapping("/searchVillagesByObject/{page}/elementsCount")
    public ResponseEntity<Long> searchVillagesByObjectElementsCount(
            @PathVariable("page") int page, @RequestParam("objectAroundVillageDTOS") List<String> objectAroundVillageDTOS) {
        Long count = villageSearchService.getSearchVillagesByObject(objectAroundVillageDTOS, page, 6, "").getTotalElements();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/searchVillagesByLivingCondition/{page}")
    public ResponseEntity<List<VillageDTO>> searchVillagesByLivingCondition(@PathVariable("page") int page, @RequestParam("livingConditionDTOS") List<String> livingConditionDTOS, @RequestParam(required = false) String sort) {
        List<VillageDTO> villages = villageSearchService.getSearchVillagesByLivingCondition(livingConditionDTOS, page, 6, sort).getContent();
        return ResponseEntity.ok(villages);
    }

    @GetMapping("/searchVillagesByLivingCondition/{page}/elementsCount")
    public ResponseEntity<Long> searchVillagesByLivingConditionElementsCount(@PathVariable("page") int page, @RequestParam("livingConditionDTOS") List<String> livingConditionDTOS) {
        Long count = villageSearchService.getSearchVillagesByLivingCondition(livingConditionDTOS, page, 6, "").getTotalElements();
        return ResponseEntity.ok(count);
    }
}

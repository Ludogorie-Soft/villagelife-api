package com.example.ludogoriesoft.village.advancedSearch;

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

    @Autowired
    private VillageService villageSearchService;

    @GetMapping("/{keyword}")
    public ResponseEntity<List<VillageDTO>> getVillageByName(@PathVariable(name = "keyword") String name) {
        return ResponseEntity.ok(villageSearchService.getAllSearchVillages(name));
    }


//    @GetMapping("/searchVillages")
//    public ResponseEntity<List<VillageDTO>> searchVillagesByCriteria(
//            @RequestParam List<String> objectAroundVillageDTOS,
//            @RequestParam List<String> livingConditionDTOS,
//            @RequestParam Children children
//    ) {
//        List<VillageDTO> villages = villageSearchService.getSearchVillages(objectAroundVillageDTOS, livingConditionDTOS, children.getEnumValue());
//        return ResponseEntity.ok(villages);
//    }


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


}
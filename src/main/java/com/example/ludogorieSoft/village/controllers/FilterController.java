package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.services.PropertyService;
import com.example.ludogorieSoft.village.services.VillageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/filter")
@RequiredArgsConstructor
public class FilterController {
    private final PropertyService propertyService;
    private final VillageService villageSearchService;

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

    @GetMapping("/searchProperties")
    public ResponseEntity<Page<PropertyDTO>> searchPropertiesByCriteria(
            @RequestParam(value = "propertyTypes", required = false) List<String> propertyTypes,
            @RequestParam(value = "propertyTransferType", required = false) String propertyTransferType,
            @RequestParam(value = "minBuiltUpArea", required = false) Double minBuiltUpArea,
            @RequestParam(value = "maxBuiltUpArea", required = false) Double maxBuiltUpArea,
            @RequestParam(value = "minYardArea", required = false) Double minYardArea,
            @RequestParam(value = "maxYardArea", required = false) Double maxYardArea,
            @RequestParam(value = "minRoomsCount", required = false) Short minRoomsCount,
            @RequestParam(value = "maxRoomsCount", required = false) Short maxRoomsCount,
            @RequestParam(value = "minBathroomsCount", required = false) Short minBathroomsCount,
            @RequestParam(value = "maxBathroomsCount", required = false) Short maxBathroomsCount,
            @RequestParam(value = "heating", required = false) List<String> heating,
            @RequestParam(value = "constructionTypes", required = false) List<String> constructionTypes,
            @RequestParam(value = "propertyConditions", required = false) List<String> propertyConditions,
            @RequestParam(value = "minConstructionYear", required = false) Short minConstructionYear,
            @RequestParam(value = "maxConstructionYear", required = false) Short maxConstructionYear,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "ownershipTypes", required = false) List<String> ownershipTypes,
            @RequestParam(value = "villageName", required = false) String villageName,
            @RequestParam(value = "regionName", required = false) String regionName,
            @PageableDefault(size = 6, sort = "createdAt") Pageable pageable
    ) {
        Page<PropertyDTO> properties = propertyService.getSearchProperties(propertyTypes, propertyTransferType,
                minBuiltUpArea, maxBuiltUpArea, minYardArea, maxYardArea, minRoomsCount, maxRoomsCount, minBathroomsCount,
                maxBathroomsCount, heating, constructionTypes, propertyConditions, minConstructionYear, maxConstructionYear, minPrice,
                maxPrice, ownershipTypes, villageName, regionName, pageable);
        return ResponseEntity.ok(properties);
    }

    @PutMapping("/increment-seen-in-results")
    public ResponseEntity<Void> incrementSearchPropertiesSeenInResults(
            @RequestParam(value = "propertyTypes", required = false) List<String> propertyTypes,
            @RequestParam(value = "propertyTransferType", required = false) String propertyTransferType,
            @RequestParam(value = "minBuiltUpArea", required = false) Double minBuiltUpArea,
            @RequestParam(value = "maxBuiltUpArea", required = false) Double maxBuiltUpArea,
            @RequestParam(value = "minYardArea", required = false) Double minYardArea,
            @RequestParam(value = "maxYardArea", required = false) Double maxYardArea,
            @RequestParam(value = "minRoomsCount", required = false) Short minRoomsCount,
            @RequestParam(value = "maxRoomsCount", required = false) Short maxRoomsCount,
            @RequestParam(value = "minBathroomsCount", required = false) Short minBathroomsCount,
            @RequestParam(value = "maxBathroomsCount", required = false) Short maxBathroomsCount,
            @RequestParam(value = "heating", required = false) List<String> heating,
            @RequestParam(value = "constructionTypes", required = false) List<String> constructionTypes,
            @RequestParam(value = "propertyConditions", required = false) List<String> propertyConditions,
            @RequestParam(value = "minConstructionYear", required = false) Short minConstructionYear,
            @RequestParam(value = "maxConstructionYear", required = false) Short maxConstructionYear,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "ownershipTypes", required = false) List<String> ownershipTypes,
            @RequestParam(value = "villageName", required = false) String villageName,
            @RequestParam(value = "regionName", required = false) String regionName
    ) {
        propertyService.incrementSearchPropertiesSeenInResults(propertyTypes, propertyTransferType,
                minBuiltUpArea, maxBuiltUpArea, minYardArea, maxYardArea, minRoomsCount, maxRoomsCount, minBathroomsCount,
                maxBathroomsCount, heating, constructionTypes, propertyConditions, minConstructionYear, maxConstructionYear, minPrice,
                maxPrice, ownershipTypes, villageName, regionName);
        return ResponseEntity.noContent().build();
    }
}

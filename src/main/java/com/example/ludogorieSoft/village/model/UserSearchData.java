package com.example.ludogorieSoft.village.model;

import com.example.ludogorieSoft.village.enums.ConstructionType;
import com.example.ludogorieSoft.village.enums.OwnershipType;
import com.example.ludogorieSoft.village.enums.PropertyTransferType;
import com.example.ludogorieSoft.village.enums.PropertyType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_search_data")
public class UserSearchData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "alternative_user_id", nullable = false)
    private AlternativeUser alternativeUser;

    @NotBlank
    @Size(max = 50)
    private String searchName;

    @ManyToOne
    private Village village;

    @ElementCollection(targetClass = PropertyType.class)
    @CollectionTable(name = "user_search_property_types", joinColumns = @JoinColumn(name = "user_search_data_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", columnDefinition = "enum('PLOT','AGRICULTURAL_LAND','HOUSE','VILLA','FLOOR_OF_A_HOUSE','BUSINESS_PROPERTY','APARTMENT')")
    private List<PropertyType> propertyTypes;

    @Column(name = "property_transfer_type",columnDefinition="enum('SALE','RENT')")
    @Enumerated(EnumType.STRING)
    private PropertyTransferType propertyTransferType;

    @Min(0)
    private Double minBuiltUpArea;

    @Min(0)
    private Double maxBuiltUpArea;

    @Min(0)
    private Double minYardArea;

    @Min(0)
    private Double maxYardArea;

    @Min(0)
    private Short minRoomsCount;

    @Min(0)
    private Short maxRoomsCount;

    @Min(0)
    private Short minBathroomsCount;

    @Min(0)
    private Short maxBathroomsCount;

    @ElementCollection
    @CollectionTable(name = "heating_options", joinColumns = @JoinColumn(name = "user_search_data_id"))
    @Column(name = "heating")
    private List<String> heating;

    @ElementCollection(targetClass = ConstructionType.class)
    @CollectionTable(name = "user_search_construction_types", joinColumns = @JoinColumn(name = "user_search_data_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "construction_type", columnDefinition = "enum('BRICKS','PANEL','WOOD')")
    private List<ConstructionType> constructionTypes;

    private Short minConstructionYear;

    private Short maxConstructionYear;

    @Min(0)
    private BigDecimal minPrice;

    @Min(0)
    private BigDecimal maxPrice;

    @ElementCollection(targetClass = OwnershipType.class)
    @CollectionTable(name = "user_search_ownership_types", joinColumns = @JoinColumn(name = "user_search_data_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "ownership_type", columnDefinition = "enum('INDIVIDUAL','AGENCY','BUILDER','INVESTOR')")
    private List<OwnershipType> ownershipTypes;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime deletedAt;
}



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
    private Village village;

    @Column(name = "property_type",columnDefinition="enum('PLOT','AGRICULTURAL_LAND','HOUSE','VILLA','FLOOR_OF_A_HOUSE','BUSINESS_PROPERTY','APARTMENT')")
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

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
    private short minRoomsCount;

    @Min(0)
    private short maxRoomsCount;

    @Min(0)
    private short minBathroomsCount;

    @Min(0)
    private short maxBathroomsCount;

    @ElementCollection
    @CollectionTable(name = "heating_options", joinColumns = @JoinColumn(name = "user_search_data_id"))
    @Column(name = "heating")
    private List<String> heating;

    @Column(name = "construction_type",columnDefinition="enum('BRICKS','PANEL','WOOD')")
    @Enumerated(EnumType.STRING)
    private ConstructionType constructionType;

    private short minConstructionYear;

    private short maxConstructionYear;

    @Column(name = "extras", columnDefinition = "TEXT")
    private String extras;

    @Min(0)
    private BigDecimal minPrice;

    @Min(0)
    private BigDecimal maxPrice;

    @Column(name = "ownership_type",columnDefinition="enum('INDIVIDUAL','AGENCY','BUILDER','INVESTOR')")
    @Enumerated(EnumType.STRING)
    private OwnershipType ownershipType;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime deletedAt;

}



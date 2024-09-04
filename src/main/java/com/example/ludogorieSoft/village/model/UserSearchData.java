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
import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @Enumerated(EnumType.STRING)
    private PropertyTransferType propertyTransferType;

    @Min(0)
    private int minBuiltUpArea;

    @Min(0)
    private int maxBuiltUpArea;

    @Min(0)
    private int minYardArea;

    @Min(0)
    private int maxYardArea;

    @Min(0)
    private short minRoomsCount;

    @Min(0)
    private short maxRoomsCount;

    @Min(0)
    private short minBathroomsCount;

    @Min(0)
    private short maxBathroomsCount;

    private String heating;

    @Enumerated(EnumType.STRING)
    private ConstructionType constructionType;

    private short minConstructionYear;

    private short maxConstructionYear;

    @OneToOne
    private PropertyUser propertyUser;

    private String extras;

    @Min(0)
    private int minPrice;

    @Min(0)
    private int maxPrice;

    @Enumerated(EnumType.STRING)
    private OwnershipType ownershipType;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime deletedAt;

}



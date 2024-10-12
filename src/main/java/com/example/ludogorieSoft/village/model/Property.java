package com.example.ludogorieSoft.village.model;

import com.example.ludogorieSoft.village.enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "properties")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "village_id")
    private Village village;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private PropertyUser propertyUser;

    @Column(name = "property_type",columnDefinition="enum('PLOT','AGRICULTURAL_LAND','HOUSE','VILLA','FLOOR_OF_A_HOUSE','BUSINESS_PROPERTY','APARTMENT')")
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @Column(name = "property_transfer_type",columnDefinition="enum('SALE','RENT')")
    @Enumerated(EnumType.STRING)
    private PropertyTransferType propertyTransferType;

    @Min(0)
    private BigDecimal price;

    @Length(min = 10, message = "Phone number should be at least 10 numbers long!")
    private String phoneNumber;

    @Min(0)
    @NotNull
    private Double buildUpArea;

    @Min(0)
    @NotNull
    private Double yardArea;

    @Min(0)
    @NotNull
    private int roomsCount;

    @Min(0)
    @NotNull
    private int bathroomsCount;

    @ElementCollection
    @CollectionTable(name = "property_heating", joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "heating")
    private List<String> heating;

//    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PropertyImage> propertyImages;

    private String imageUrl;

    @Column(name = "construction_type",columnDefinition="enum('BRICKS','PANEL','WOOD')")
    @Enumerated(EnumType.STRING)
    private ConstructionType constructionType;

    private int constructionYear;

    @Column(name = "extras", columnDefinition = "TEXT")
    private String extras;

    @OneToOne
    @JoinColumn(name = "stats_id")
    private PropertyStats propertyStats;

    @Column(name = "ownership_type",columnDefinition="enum('INDIVIDUAL','AGENCY','BUILDER','INVESTOR')")
    @Enumerated(EnumType.STRING)
    private OwnershipType ownershipType;

    @Column(name = "property_condition",columnDefinition="enum('NEW','EXCELLENT','GOOD','FAIR', 'POOR', 'UNDER_CONSTRUCTION', 'DAMAGED', 'RENOVATION_REQUIRED')")
    @Enumerated(EnumType.STRING)
    private PropertyCondition propertyCondition;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime updatedAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime deactivatedAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime deletedAt;

}

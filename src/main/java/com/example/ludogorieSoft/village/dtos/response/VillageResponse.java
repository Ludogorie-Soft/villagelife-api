package com.example.ludogorieSoft.village.dtos.response;

import com.example.ludogorieSoft.village.model.Administrator;
import com.example.ludogorieSoft.village.model.Population;
import com.example.ludogorieSoft.village.model.Region;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VillageResponse {

    private Long id;

    private String name;

    private Region region;

    private int populationCount;

    private Population population;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime dateUpload;

    private Boolean status;

    private Administrator admin;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime dateApproved;

}

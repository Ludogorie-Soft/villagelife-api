package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageImageDTO;
import com.example.ludogorieSoft.village.dtos.VillageVideoDTO;
import com.example.ludogorieSoft.village.model.VillageImage;
import com.example.ludogorieSoft.village.model.VillageVideo;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VillageVideoService {
    private final ModelMapper modelMapper;
    public VillageVideo villageVideoDTOToVillageVideo(VillageVideoDTO villageVideoDTO) {
        return modelMapper.map(villageVideoDTO, VillageVideo.class);
    }

    public VillageVideoDTO villageVideoToVillageVideoDTO(VillageVideo villageVideo) {
        return modelMapper.map(villageVideo, VillageVideoDTO.class);
    }
}

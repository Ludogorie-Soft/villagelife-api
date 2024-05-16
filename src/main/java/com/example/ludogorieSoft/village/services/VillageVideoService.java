package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageVideoDTO;
import com.example.ludogorieSoft.village.model.VillageVideo;
import com.example.ludogorieSoft.village.repositories.VillageVideoRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VillageVideoService {
    private final ModelMapper modelMapper;
    private final VillageVideoRepository villageVideoRepository;

    public VillageVideo villageVideoDTOToVillageVideo(VillageVideoDTO villageVideoDTO) {
        return modelMapper.map(villageVideoDTO, VillageVideo.class);
    }

    public VillageVideoDTO villageVideoToVillageVideoDTO(VillageVideo villageVideo) {
        return modelMapper.map(villageVideo, VillageVideoDTO.class);
    }

    public void createVideoPats(List<VillageVideoDTO> videoList) {
        List<VillageVideo> villageVideos = videoList.stream()
                .map(this::villageVideoDTOToVillageVideo)
                .toList();
        villageVideoRepository.saveAll(villageVideos);
        System.out.println("success");
    }
}

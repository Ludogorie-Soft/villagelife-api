package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageVideoDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageVideo;
import com.example.ludogorieSoft.village.repositories.VillageVideoRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
public class VillageVideoService {
    private final ModelMapper modelMapper;
    private final VillageVideoRepository villageVideoRepository;
    private final VillageService villageService;

    public VillageVideo villageVideoDTOToVillageVideo(VillageVideoDTO villageVideoDTO) {
        return modelMapper.map(villageVideoDTO, VillageVideo.class);
    }

    public VillageVideoDTO villageVideoToVillageVideoDTO(VillageVideo villageVideo) {
        return modelMapper.map(villageVideo, VillageVideoDTO.class);
    }

    public List<VillageVideoDTO> getAllVideosByVillageId(Long villageId) {
        List<VillageVideo> videos = villageVideoRepository.findByVillageId(villageId);
        return videos
                .stream()
                .map(this::villageVideoToVillageVideoDTO)
                .toList();
    }

    public List<VillageVideoDTO> getAllApprovedVideosByVillageId(Long villageId) {
        List<VillageVideo> videos = villageVideoRepository.findByVillageIdAndStatusTrue(villageId);
        return videos
                .stream()
                .map(this::villageVideoToVillageVideoDTO)
                .toList();
    }

    public void createVideoPats(Long villageId, List<String> videoList) {
        Village village = findVillageById(villageId);
        List<VillageVideo> villageVideoList = videoList.stream()
                .map(videoUrl -> {
                    VillageVideo video = new VillageVideo();
                    video.setVillage(village);
                    video.setUrl(videoUrl);
                    video.setStatus(true);
                    video.setDateUpload(now());
                    return video;
                })
                .toList();

        villageVideoRepository.saveAll(villageVideoList);
    }

    public List<VillageVideoDTO> findVideosByVillageIdAndDateDeletedIsNull(long villageId) {
        findVillageById(villageId);
        List<VillageVideo> videos = villageVideoRepository.findByVillageIdAndDateDeletedIsNull(villageId);
        return videos
                .stream()
                .map(this::villageVideoToVillageVideoDTO)
                .toList();
    }

    public List<VillageVideoDTO> findDeletedVideosByVillageIdAndDateDeletedIsNotNull(Long villageId) {
        List<VillageVideo> videos = villageVideoRepository.findDeletedVideosByVillageIdAndDateDeletedIsNotNull(villageId);
        return videos
                .stream()
                .map(this::villageVideoToVillageVideoDTO)
                .toList();
    }

    public String deleteVideoById(Long videoId) {
        VillageVideo villageVideo = getVillageVideoById(videoId);
        villageVideoRepository.deleteById(videoId);
        return villageVideo.getVillage().getId().toString();
    }

    public String resumeVideoById(Long videoId) {
        VillageVideo villageVideo = getVillageVideoById(videoId);
        villageVideo.setDateDeleted(null);
        villageVideoRepository.save(villageVideo);
        return villageVideo.getVillage().getId().toString();
    }

    public String rejectVideoById(Long videoId) {
        VillageVideo villageVideo = getVillageVideoById(videoId);
        villageVideo.setDateDeleted(now());
        villageVideoRepository.save(villageVideo);
        return villageVideo.getVillage().getId().toString();
    }

    private Village findVillageById(long villageId) {
        return villageService.checkVillage(villageId);
    }

    private VillageVideo getVillageVideoById(Long videoId) {
        Optional<VillageVideo> villageVideo = villageVideoRepository.findById(videoId);
        if (villageVideo.isPresent()) {
            return villageVideo.get();
        } else {
            throw new ApiRequestException("Video with ID: " + videoId + " not found");
        }
    }
}

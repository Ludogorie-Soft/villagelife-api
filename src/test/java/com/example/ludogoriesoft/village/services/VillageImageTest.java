package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageImage;
import org.junit.jupiter.api.Test;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;


class VillageImageTest {

    @Test
    void testVillageImageConstructor() {
        Long id = 1L;
        Village village = new Village();
        String imageName = "image.jpg";
        boolean status = true;

        VillageImage villageImage = new VillageImage(id, village, imageName, true, now(), now());

        assertEquals(id, villageImage.getId());
        assertEquals(village, villageImage.getVillage());
        assertEquals(imageName, villageImage.getImageName());
    }

    @Test
    void testVillageImageGettersAndSetters() {
        VillageImage villageImage = new VillageImage();

        Long id = 1L;
        Village village = new Village();
        String imageName = "image.jpg";
        villageImage.setId(id);
        villageImage.setVillage(village);
        villageImage.setImageName(imageName);

        assertEquals(id, villageImage.getId());
        assertEquals(village, villageImage.getVillage());
        assertEquals(imageName, villageImage.getImageName());
    }
}


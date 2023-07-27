package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageImage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.time.LocalDateTime.now;


class VillageImageTest {

    @Test
    void testVillageImageConstructor() {
        Long id = 1L;
        Village village = new Village();
        String imageName = "image.jpg";
        boolean status = true;

<<<<<<< HEAD
        VillageImage villageImage = new VillageImage(id, village, imageName,true,now());
=======
        VillageImage villageImage = new VillageImage(id, village, imageName, status,null);
>>>>>>> 6bdbc72 (add migrations and new fields, and fix some tests)

        Assertions.assertEquals(id, villageImage.getId());
        Assertions.assertEquals(village, villageImage.getVillage());
        Assertions.assertEquals(imageName, villageImage.getImageName());
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

        Assertions.assertEquals(id, villageImage.getId());
        Assertions.assertEquals(village, villageImage.getVillage());
        Assertions.assertEquals(imageName, villageImage.getImageName());
    }
}


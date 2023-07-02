package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageImage;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class VillageImageTest {

    @Test
    void testVillageImageConstructor() {
        // Define the test input
        Long id = 1L;
        Village village = new Village();
        String imageName = "image.jpg";

        // Create a new VillageImage instance
        VillageImage villageImage = new VillageImage(id, village, imageName);

        // Verify the properties
        assertEquals(id, villageImage.getId());
        assertEquals(village, villageImage.getVillage());
        assertEquals(imageName, villageImage.getImageName());
    }

    @Test
    void testVillageImageGettersAndSetters() {
        // Create a new VillageImage instance
        VillageImage villageImage = new VillageImage();

        // Set the properties
        Long id = 1L;
        Village village = new Village();
        String imageName = "image.jpg";
        villageImage.setId(id);
        villageImage.setVillage(village);
        villageImage.setImageName(imageName);

        // Verify the properties
        assertEquals(id, villageImage.getId());
        assertEquals(village, villageImage.getVillage());
        assertEquals(imageName, villageImage.getImageName());
    }
}


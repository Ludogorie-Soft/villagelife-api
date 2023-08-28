package com.example.ludogorieSoft.village.enums;

import com.example.ludogorieSoft.village.enums.Residents;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResidentsTest {
    @Test
    void testGetByValueAsNumberWhenValidValue() {
        int valueAsNumber = 2;
        Residents expectedResidents = Residents.FROM_2_TO_5_PERCENT;

        Residents result = Residents.getByValueAsNumber(valueAsNumber);

        assertEquals(expectedResidents, result);
    }

    @Test
    void testGetByValueAsNumberWhenInvalidValue() {
        int valueAsNumber = 7;

        assertThrows(ApiRequestException.class, () -> {
            Residents.getByValueAsNumber(valueAsNumber);
        });
    }
}

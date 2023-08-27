package com.example.ludogoriesoft.village.enums;

import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChildrenTest {
    @Test
    void testGetByValueAsNumberWhenValidValue() {
        int valueAsNumber = 2;
        Children expectedChild = Children.FROM_11_TO_20;

        Children result = Children.getByValueAsNumber(valueAsNumber);

        assertEquals(expectedChild, result);
    }

    @Test
    void testGetByValueAsNumberWhenInvalidValue() {
        int valueAsNumber = 5;

        assertThrows(ApiRequestException.class, () -> {
            Children.getByValueAsNumber(valueAsNumber);
        });
    }
}

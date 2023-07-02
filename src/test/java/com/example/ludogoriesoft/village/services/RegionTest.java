package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.model.Region;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class RegionTest {


    @Test
    void hasEntityAnnotation() {
        // Arrange
        Class<?> regionClass = Region.class;
        boolean hasEntityAnnotation = regionClass.isAnnotationPresent(Entity.class);

        // Assert
        assertTrue(hasEntityAnnotation);
    }

    @Test
    void hasTableAnnotationWithCorrectName() {
        // Arrange
        Class<?> regionClass = Region.class;
        Table tableAnnotation = regionClass.getAnnotation(Table.class);

        // Assert
        assertNotNull(tableAnnotation);
        assertEquals("regions", tableAnnotation.name());
    }

    @Test
    void hasAllArgsConstructor() {
        // Arrange
        Class<?> regionClass = Region.class;
        boolean hasAllArgsConstructor = false;
        try {
            regionClass.getDeclaredConstructor(Long.class, String.class);
            hasAllArgsConstructor = true;
        } catch (NoSuchMethodException e) {
            // Ignore
        }

        // Assert
        assertTrue(hasAllArgsConstructor);
    }

    @Test
    void hasNoArgsConstructor() {
        // Arrange
        Class<?> regionClass = Region.class;
        boolean hasNoArgsConstructor = false;
        try {
            regionClass.getDeclaredConstructor();
            hasNoArgsConstructor = true;
        } catch (NoSuchMethodException e) {
            // Ignore
        }

        // Assert
        assertTrue(hasNoArgsConstructor);
    }

    @Test
    void idFieldHasIdAnnotation() throws NoSuchFieldException {
        // Arrange
        Field idField = Region.class.getDeclaredField("id");
        Id idAnnotation = idField.getAnnotation(Id.class);

        // Assert
        assertNotNull(idAnnotation);
    }

    @Test
    void idFieldHasGeneratedValueAnnotationWithIdentityStrategy() throws NoSuchFieldException {
        // Arrange
        Field idField = Region.class.getDeclaredField("id");
        GeneratedValue generatedValueAnnotation = idField.getAnnotation(GeneratedValue.class);

        // Assert
        assertNotNull(generatedValueAnnotation);
        assertEquals(GenerationType.IDENTITY, generatedValueAnnotation.strategy());
    }


    @Test
    void regionNameFieldHasNotBlankAnnotation() throws NoSuchFieldException {
        // Arrange
        Field regionNameField = Region.class.getDeclaredField("regionName");
        NotBlank notBlankAnnotation = regionNameField.getAnnotation(NotBlank.class);

        // Assert
        assertNotNull(notBlankAnnotation);
    }


    @Test
    void validateNotBlankRegionName_ValidValue_NoValidationErrors() {
        // Arrange
        Region region = new Region();
        region.setRegionName("Europe");

        // Validate
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Region>> violations = validator.validateProperty(region, "regionName");

        // Assert
        assertEquals(0, violations.size());
    }

    @Test
    void validateNotBlankRegionName_NullValue_ValidationErrors() {
        // Arrange
        Region region = new Region();
        region.setRegionName(null);

        // Validate
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Region>> violations = validator.validateProperty(region, "regionName");

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<Region> violation = violations.iterator().next();
        assertEquals("must not be blank", violation.getMessage());
    }
}

package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.model.Region;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

class RegionTest {


    @Test
    void hasEntityAnnotation() {
        Class<?> regionClass = Region.class;
        boolean hasEntityAnnotation = regionClass.isAnnotationPresent(Entity.class);

        Assertions.assertTrue(hasEntityAnnotation);
    }

    @Test
    void hasTableAnnotationWithCorrectName() {
        Class<?> regionClass = Region.class;
        Table tableAnnotation = regionClass.getAnnotation(Table.class);

        assertNotNull(tableAnnotation);
        Assertions.assertEquals("regions", tableAnnotation.name());
    }

    @Test
    void hasAllArgsConstructor() {
        Class<?> regionClass = Region.class;
        boolean hasAllArgsConstructor = false;
        try {
            // Adjusted to match the new constructor with three parameters
            regionClass.getDeclaredConstructor(Long.class, String.class, String.class);
            hasAllArgsConstructor = true;
        } catch (NoSuchMethodException e) {
            // Constructor does not exist
        }

        Assertions.assertTrue(hasAllArgsConstructor);
    }


    @Test
    void hasNoArgsConstructor() {
        Class<?> regionClass = Region.class;
        boolean hasNoArgsConstructor = false;
        try {
            regionClass.getDeclaredConstructor();
            hasNoArgsConstructor = true;
        } catch (NoSuchMethodException e) {
        }

        Assertions.assertTrue(hasNoArgsConstructor);
    }

    @Test
    void idFieldHasIdAnnotation() throws NoSuchFieldException {
        Field idField = Region.class.getDeclaredField("id");
        Id idAnnotation = idField.getAnnotation(Id.class);

        assertNotNull(idAnnotation);
    }

    @Test
    void idFieldHasGeneratedValueAnnotationWithIdentityStrategy() throws NoSuchFieldException {
        Field idField = Region.class.getDeclaredField("id");
        GeneratedValue generatedValueAnnotation = idField.getAnnotation(GeneratedValue.class);

        assertNotNull(generatedValueAnnotation);
        Assertions.assertEquals(GenerationType.IDENTITY, generatedValueAnnotation.strategy());
    }


    @Test
    void regionNameFieldHasNotBlankAnnotation() throws NoSuchFieldException {
        Field regionNameField = Region.class.getDeclaredField("regionName");
        NotBlank notBlankAnnotation = regionNameField.getAnnotation(NotBlank.class);

        assertNotNull(notBlankAnnotation);
    }


    @Test
    void validateNotBlankRegionNameValidValueNoValidationErrors() {
        Region region = new Region();
        region.setRegionName("Europe");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Region>> violations = validator.validateProperty(region, "regionName");

        Assertions.assertEquals(0, violations.size());
    }

    @Test
    void validateNotBlankRegionNameNullValueValidationErrors() {
        Region region = new Region();
        region.setRegionName(null);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Region>> violations = validator.validateProperty(region, "regionName");

        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Region> violation = violations.iterator().next();
        Assertions.assertEquals("must not be blank", violation.getMessage());
    }
}

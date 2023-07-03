package com.example.ludogorieSoft.village.config;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTransformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AppConfigTest {

    @Autowired
    private ModelMapper modelMapper;


    @Test
    void testModelMapperBeanNotNull() {
        assertThat(modelMapper).isNotNull();
    }

    @Test
    void testModelMapperConfiguration() {
        Configuration configuration = modelMapper.getConfiguration();

        assertThat(configuration).isNotNull();
        assertThat(configuration.isFieldMatchingEnabled()).isFalse();
        assertThat(configuration.isAmbiguityIgnored()).isFalse();
    }


    @Test
    void testModelMapperNameTransformers() {
        Configuration configuration = modelMapper.getConfiguration();

        assertThat(configuration.getSourceNameTransformer()).isEqualTo(NameTransformers.JAVABEANS_ACCESSOR);
        assertThat(configuration.getDestinationNameTransformer()).isEqualTo(NameTransformers.JAVABEANS_MUTATOR);
    }

    @Test
    void testModelMapperNotNull() {
        assertNotNull(modelMapper);
    }

    @Test
    void testModelMapperMatchingStrategy() {
        Configuration configuration = modelMapper.getConfiguration();

        assertThat(configuration.getMatchingStrategy()).isEqualTo(MatchingStrategies.STANDARD);
    }

    @Test
    void testModelMapperSkipNullEnabled() {
        Configuration configuration = modelMapper.getConfiguration();

        assertThat(configuration.isSkipNullEnabled()).isFalse();
    }

    @Test
    void testModelMapperDeepCopyEnabled() {
        Configuration configuration = modelMapper.getConfiguration();

        assertThat(configuration.isDeepCopyEnabled()).isFalse();
    }



}

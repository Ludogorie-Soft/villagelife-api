package com.example.ludogorieSoft.village.config;

import com.example.ludogorieSoft.village.authorization.JwtAuthenticationEntryPoint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.minio.MinioClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.time.format.DateTimeFormatter;

@Configuration
public class AppConfig {
    @Value("${space.bucket.origin.url}")
    private String spaceBucketOriginUrl;
    @Value("${digital.ocean.access.key}")
    private String digitalOceanAccessKey;
    @Value("${digital.ocean.secret.key}")
    private String digitalOceanSecretKey;
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Jackson2ObjectMapperBuilder builder =
                new Jackson2ObjectMapperBuilder()
                        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .serializers(new LocalDateTimeSerializer(formatter))
                        .serializationInclusion(JsonInclude.Include.NON_NULL);
        return new MappingJackson2HttpMessageConverter(builder.build());
    }
    @Bean
    public JwtAuthenticationEntryPoint myJwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }
    @Bean
    public MinioClient minioClient (){
        return MinioClient.builder().endpoint(spaceBucketOriginUrl)
                .region("fra1")
                .credentials(digitalOceanAccessKey ,digitalOceanSecretKey).build();
    }
}

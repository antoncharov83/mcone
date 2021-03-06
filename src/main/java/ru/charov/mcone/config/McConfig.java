package ru.charov.mcone.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.GsonMessageConverter;

import java.time.LocalDateTime;

@Configuration
public class McConfig {

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    @Bean("mapper")
    @Primary
    public Gson getMapper() {
        return new GsonBuilder()
                .setDateFormat(DATETIME_FORMAT)
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
    }

    @Bean
    public GsonMessageConverter getConverter() {
        return new GsonMessageConverter(getMapper());
    }
}

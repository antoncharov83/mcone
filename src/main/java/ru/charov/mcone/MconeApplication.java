package ru.charov.mcone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

@SpringBootApplication(exclude = { JacksonAutoConfiguration.class })
public class MconeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MconeApplication.class, args);
    }

}

package com.gmail.kirilllapitsky.mailing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringBootConfig {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootConfig.class, args);
    }
}
package com.example.junguniv_bb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class JungunivBbApplication {

    public static void main(String[] args) {
        SpringApplication.run(JungunivBbApplication.class, args);
    }

}

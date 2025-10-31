package com.studyplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudyPlannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyPlannerApplication.class, args);
    }
}

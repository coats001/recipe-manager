package org.amoscoats.recipemanager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Main Spring Boot application class. */
@Slf4j
@SpringBootApplication
public class RecipeManagerApplication {

  /** Main method to start the Spring Boot application. */
  public static void main(String[] args) {
    log.info("Starting Recipe Manager Application...");
    SpringApplication.run(RecipeManagerApplication.class, args);
    log.info("Recipe Manager Application started successfully");
  }
}

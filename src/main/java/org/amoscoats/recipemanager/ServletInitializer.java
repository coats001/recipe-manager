package org.amoscoats.recipemanager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/** Servlet initializer for WAR deployment. */
@Slf4j
public class ServletInitializer extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    log.info("Configuring Spring Boot Servlet Initializer for WAR deployment");
    return application.sources(RecipeManagerApplication.class);
  }
}

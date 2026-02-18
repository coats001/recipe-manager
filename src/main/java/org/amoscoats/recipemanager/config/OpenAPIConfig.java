package org.amoscoats.recipemanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration for OpenAPI documentation. */
@Configuration
public class OpenAPIConfig {

  /**
   * Creates the OpenAPI bean for Swagger documentation.
   *
   * @return configured OpenAPI instance
   */
  @Bean
  public OpenAPI recipeManagerOpenApi() {
    Server devServer = new Server();
    devServer.setUrl("http://localhost:8080");
    devServer.setDescription("Development Server");

    Server prodServer = new Server();
    prodServer.setUrl("https://api.recipe-manager.example.com");
    prodServer.setDescription("Production Server");

    Contact contact = new Contact();
    contact.setEmail("support@recipe-manager.example.com");
    contact.setName("Recipe Manager Support");
    contact.setUrl("https://recipe-manager.example.com");

    License mitLicense =
        new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

    Info info =
        new Info()
            .title("Recipe Manager API")
            .version("1.0.0")
            .contact(contact)
            .description(
                "REST API for managing favorite recipes with advanced filtering capabilities. "
                    + "Supports CRUD operations and filtering by vegetarian status, servings, "
                    + "ingredients, and text search within instructions.")
            .termsOfService("https://recipe-manager.example.com/terms")
            .license(mitLicense);

    return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
  }
}

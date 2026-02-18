package org.amoscoats.recipemanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OpenAPIConfig Tests")
class OpenAPIConfigTest {

  private OpenAPIConfig openAPIConfig;

  @BeforeEach
  void setUp() {
    openAPIConfig = new OpenAPIConfig();
  }

  @Test
  @DisplayName("Should create OpenAPI configuration bean")
  void shouldCreateOpenAPIBean() {
    // When
    OpenAPI openAPI = openAPIConfig.recipeManagerOpenApi();

    // Then
    assertThat(openAPI).isNotNull();
  }

  @Test
  @DisplayName("Should configure API info correctly")
  void shouldConfigureApiInfo() {
    // When
    OpenAPI openAPI = openAPIConfig.recipeManagerOpenApi();
    Info info = openAPI.getInfo();

    // Then
    assertThat(info).isNotNull();
    assertThat(info.getTitle()).isEqualTo("Recipe Manager API");
    assertThat(info.getVersion()).isEqualTo("1.0.0");
    assertThat(info.getDescription()).contains("REST API for managing favorite recipes");
    assertThat(info.getDescription()).contains("advanced filtering capabilities");
    assertThat(info.getTermsOfService()).isEqualTo("https://recipe-manager.example.com/terms");
  }

  @Test
  @DisplayName("Should configure contact information")
  void shouldConfigureContactInfo() {
    // When
    OpenAPI openAPI = openAPIConfig.recipeManagerOpenApi();
    Contact contact = openAPI.getInfo().getContact();

    // Then
    assertThat(contact).isNotNull();
    assertThat(contact.getName()).isEqualTo("Recipe Manager Support");
    assertThat(contact.getEmail()).isEqualTo("support@recipe-manager.example.com");
    assertThat(contact.getUrl()).isEqualTo("https://recipe-manager.example.com");
  }

  @Test
  @DisplayName("Should configure license information")
  void shouldConfigureLicenseInfo() {
    // When
    OpenAPI openAPI = openAPIConfig.recipeManagerOpenApi();
    License license = openAPI.getInfo().getLicense();

    // Then
    assertThat(license).isNotNull();
    assertThat(license.getName()).isEqualTo("MIT License");
    assertThat(license.getUrl()).isEqualTo("https://choosealicense.com/licenses/mit/");
  }

  @Test
  @DisplayName("Should configure development server")
  void shouldConfigureDevelopmentServer() {
    // When
    OpenAPI openAPI = openAPIConfig.recipeManagerOpenApi();

    // Then
    assertThat(openAPI.getServers()).hasSize(2);
    Server devServer = openAPI.getServers().get(0);
    assertThat(devServer.getUrl()).isEqualTo("http://localhost:8080");
    assertThat(devServer.getDescription()).isEqualTo("Development Server");
  }

  @Test
  @DisplayName("Should configure production server")
  void shouldConfigureProductionServer() {
    // When
    OpenAPI openAPI = openAPIConfig.recipeManagerOpenApi();

    // Then
    assertThat(openAPI.getServers()).hasSize(2);
    Server prodServer = openAPI.getServers().get(1);
    assertThat(prodServer.getUrl()).isEqualTo("https://api.recipe-manager.example.com");
    assertThat(prodServer.getDescription()).isEqualTo("Production Server");
  }

  @Test
  @DisplayName("Should have both development and production servers")
  void shouldHaveBothServers() {
    // When
    OpenAPI openAPI = openAPIConfig.recipeManagerOpenApi();

    // Then
    assertThat(openAPI.getServers()).isNotNull();
    assertThat(openAPI.getServers()).hasSize(2);
  }

  @Test
  @DisplayName("Should have complete API description")
  void shouldHaveCompleteDescription() {
    // When
    OpenAPI openAPI = openAPIConfig.recipeManagerOpenApi();
    String description = openAPI.getInfo().getDescription();

    // Then
    assertThat(description).contains("REST API");
    assertThat(description).contains("managing favorite recipes");
    assertThat(description).contains("advanced filtering");
    assertThat(description).contains("CRUD operations");
    assertThat(description).contains("vegetarian status");
    assertThat(description).contains("servings");
    assertThat(description).contains("ingredients");
    assertThat(description).contains("text search");
    assertThat(description).contains("instructions");
  }
}

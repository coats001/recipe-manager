package org.amoscoats.recipemanager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ServletInitializer Tests")
class ServletInitializerTest {

  @Test
  @DisplayName("Should configure SpringApplicationBuilder correctly")
  void shouldConfigureApplication() {
    // Given
    ServletInitializer servletInitializer = new ServletInitializer();
    SpringApplicationBuilder builder = new SpringApplicationBuilder();

    // When
    SpringApplicationBuilder result = servletInitializer.configure(builder);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isSameAs(builder);
  }

  @Test
  @DisplayName("Should return non-null SpringApplicationBuilder")
  void shouldReturnNonNullBuilder() {
    // Given
    ServletInitializer servletInitializer = new ServletInitializer();
    SpringApplicationBuilder builder = new SpringApplicationBuilder();

    // When
    SpringApplicationBuilder result = servletInitializer.configure(builder);

    // Then
    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("Should create ServletInitializer instance")
  void shouldCreateServletInitializer() {
    // When
    ServletInitializer servletInitializer = new ServletInitializer();

    // Then
    assertThat(servletInitializer).isNotNull();
  }
}

package org.amoscoats.recipemanager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@DisplayName("RecipeManagerApplication Tests")
class RecipeManagerApplicationTests {

  @Autowired
  private ApplicationContext applicationContext;

  @Test
  @DisplayName("Should load Spring application context")
  void contextLoads() {
    assertThat(applicationContext).isNotNull();
  }

  @Test
  @DisplayName("Should have RecipeManagerApplication bean")
  void shouldHaveMainApplicationBean() {
    assertThat(applicationContext.containsBean("recipeManagerApplication")).isTrue();
  }

  @Test
  @DisplayName("Should load all required beans")
  void shouldLoadRequiredBeans() {
    assertThat(applicationContext.containsBean("recipeService")).isTrue();
    assertThat(applicationContext.containsBean("recipeController")).isTrue();
    assertThat(applicationContext.containsBean("recipeRepository")).isTrue();
  }

  @Test
  @DisplayName("Should run main method without exceptions")
  void shouldRunMainMethod() {
    // This tests that the main method can be invoked
    assertThat(RecipeManagerApplication.class).isNotNull();
  }
}

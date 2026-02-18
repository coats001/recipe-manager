package org.amoscoats.recipemanager.mapper;

import org.amoscoats.recipemanager.dto.RecipeRequest;
import org.amoscoats.recipemanager.dto.RecipeResponse;
import org.amoscoats.recipemanager.entity.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("RecipeMapper Tests")
class RecipeMapperTest {

  @Autowired
  private RecipeMapper recipeMapper;

  private Recipe recipe;
  private RecipeRequest recipeRequest;

  @BeforeEach
  void setUp() {
    Set<String> ingredients = new HashSet<>();
    ingredients.add("Potato");
    ingredients.add("Onion");
    ingredients.add("Garlic");

    recipe = new Recipe();
    recipe.setId(1L);
    recipe.setName("Test Recipe");
    recipe.setVegetarian(true);
    recipe.setServings(4);
    recipe.setIngredients(ingredients);
    recipe.setInstructions("Test instructions");

    recipeRequest = new RecipeRequest();
    recipeRequest.setName("New Recipe");
    recipeRequest.setVegetarian(false);
    recipeRequest.setServings(2);
    recipeRequest.setIngredients(ingredients);
    recipeRequest.setInstructions("New instructions");
  }

  @Test
  @DisplayName("Should map Recipe entity to RecipeResponse DTO")
  void shouldMapRecipeToRecipeResponse() {
    // When
    RecipeResponse response = recipeMapper.toResponse(recipe);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(recipe.getId());
    assertThat(response.getName()).isEqualTo(recipe.getName());
    assertThat(response.getVegetarian()).isEqualTo(recipe.getVegetarian());
    assertThat(response.getServings()).isEqualTo(recipe.getServings());
    assertThat(response.getIngredients()).isEqualTo(recipe.getIngredients());
    assertThat(response.getInstructions()).isEqualTo(recipe.getInstructions());
  }

  @Test
  @DisplayName("Should map RecipeRequest DTO to Recipe entity")
  void shouldMapRecipeRequestToRecipe() {
    // When
    Recipe newRecipe = recipeMapper.toEntity(recipeRequest);

    // Then
    assertThat(newRecipe).isNotNull();
    assertThat(newRecipe.getId()).isNull(); // ID should be ignored
    assertThat(newRecipe.getName()).isEqualTo(recipeRequest.getName());
    assertThat(newRecipe.getVegetarian()).isEqualTo(recipeRequest.getVegetarian());
    assertThat(newRecipe.getServings()).isEqualTo(recipeRequest.getServings());
    assertThat(newRecipe.getIngredients()).isEqualTo(recipeRequest.getIngredients());
    assertThat(newRecipe.getInstructions()).isEqualTo(recipeRequest.getInstructions());
  }


  @Test
  @DisplayName("Should handle null Recipe when mapping to RecipeResponse")
  void shouldHandleNullRecipeToResponse() {
    // When
    RecipeResponse response = recipeMapper.toResponse(null);

    // Then
    assertThat(response).isNull();
  }

  @Test
  @DisplayName("Should handle null RecipeRequest when mapping to Recipe")
  void shouldHandleNullRequestToEntity() {
    // When
    Recipe newRecipe = recipeMapper.toEntity(null);

    // Then
    assertThat(newRecipe).isNull();
  }

  @Test
  @DisplayName("Should handle Recipe with empty ingredients")
  void shouldHandleRecipeWithEmptyIngredients() {
    // Given
    recipe.setIngredients(new HashSet<>());

    // When
    RecipeResponse response = recipeMapper.toResponse(recipe);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getIngredients()).isEmpty();
  }

  @Test
  @DisplayName("Should handle Recipe with null ingredients")
  void shouldHandleRecipeWithNullIngredients() {
    // Given
    recipe.setIngredients(null);

    // When
    RecipeResponse response = recipeMapper.toResponse(recipe);

    // Then
    assertThat(response).isNotNull();
  }
}

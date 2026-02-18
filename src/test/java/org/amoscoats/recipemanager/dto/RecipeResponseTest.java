package org.amoscoats.recipemanager.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RecipeResponse DTO Unit Tests")
class RecipeResponseTest {

    @Test
    @DisplayName("Should create RecipeResponse with all fields")
    void shouldCreateRecipeResponseWithAllFields() {
        Set<String> ingredients = Set.of("ingredient1", "ingredient2");
        RecipeResponse response = new RecipeResponse(
                1L,
                "Test Recipe",
                true,
                4,
                "Test instructions",
                ingredients
        );

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Test Recipe");
        assertThat(response.getVegetarian()).isTrue();
        assertThat(response.getServings()).isEqualTo(4);
        assertThat(response.getInstructions()).isEqualTo("Test instructions");
        assertThat(response.getIngredients()).hasSize(2);
    }

    @Test
    @DisplayName("Should create RecipeResponse with default constructor")
    void shouldCreateRecipeResponseWithDefaultConstructor() {
        RecipeResponse response = new RecipeResponse();
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Should set and get id")
    void shouldSetAndGetId() {
        RecipeResponse response = new RecipeResponse();
        response.setId(1L);
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should set and get name")
    void shouldSetAndGetName() {
        RecipeResponse response = new RecipeResponse();
        response.setName("Test Recipe");
        assertThat(response.getName()).isEqualTo("Test Recipe");
    }

    @Test
    @DisplayName("Should set and get vegetarian")
    void shouldSetAndGetVegetarian() {
        RecipeResponse response = new RecipeResponse();
        response.setVegetarian(true);
        assertThat(response.getVegetarian()).isTrue();
    }

    @Test
    @DisplayName("Should set and get servings")
    void shouldSetAndGetServings() {
        RecipeResponse response = new RecipeResponse();
        response.setServings(4);
        assertThat(response.getServings()).isEqualTo(4);
    }

    @Test
    @DisplayName("Should set and get instructions")
    void shouldSetAndGetInstructions() {
        RecipeResponse response = new RecipeResponse();
        response.setInstructions("Test instructions");
        assertThat(response.getInstructions()).isEqualTo("Test instructions");
    }

    @Test
    @DisplayName("Should set and get ingredients")
    void shouldSetAndGetIngredients() {
        RecipeResponse response = new RecipeResponse();
        Set<String> ingredients = Set.of("ingredient1", "ingredient2");
        response.setIngredients(ingredients);
        assertThat(response.getIngredients()).hasSize(2);
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void shouldTestEqualsAndHashCode() {
        Set<String> ingredients = Set.of("ingredient1");
        RecipeResponse response1 = new RecipeResponse(1L, "Recipe", true, 4, "Instructions", ingredients);
        RecipeResponse response2 = new RecipeResponse(1L, "Recipe", true, 4, "Instructions", ingredients);
        RecipeResponse response3 = new RecipeResponse(2L, "Other", false, 2, "Other", Set.of("other"));

        assertThat(response1).isEqualTo(response2);
        assertThat(response1).isNotEqualTo(response3);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    @DisplayName("Should test toString")
    void shouldTestToString() {
        RecipeResponse response = new RecipeResponse(1L, "Recipe", true, 4, "Instructions", Set.of("ing1"));
        String toString = response.toString();
        assertThat(toString).contains("Recipe");
        assertThat(toString).contains("1");
        assertThat(toString).contains("true");
        assertThat(toString).contains("4");
    }
}

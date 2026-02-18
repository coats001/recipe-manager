package org.amoscoats.recipemanager.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RecipeRequest DTO Unit Tests")
class RecipeRequestTest {

    @Test
    @DisplayName("Should create RecipeRequest with all fields")
    void shouldCreateRecipeRequestWithAllFields() {
        Set<String> ingredients = Set.of("ingredient1", "ingredient2");
        RecipeRequest request = new RecipeRequest(
                "Test Recipe",
                true,
                4,
                "Test instructions",
                ingredients
        );

        assertThat(request.getName()).isEqualTo("Test Recipe");
        assertThat(request.getVegetarian()).isTrue();
        assertThat(request.getServings()).isEqualTo(4);
        assertThat(request.getInstructions()).isEqualTo("Test instructions");
        assertThat(request.getIngredients()).hasSize(2);
    }

    @Test
    @DisplayName("Should create RecipeRequest with default constructor")
    void shouldCreateRecipeRequestWithDefaultConstructor() {
        RecipeRequest request = new RecipeRequest();
        assertThat(request).isNotNull();
    }

    @Test
    @DisplayName("Should set and get name")
    void shouldSetAndGetName() {
        RecipeRequest request = new RecipeRequest();
        request.setName("Test Recipe");
        assertThat(request.getName()).isEqualTo("Test Recipe");
    }

    @Test
    @DisplayName("Should set and get vegetarian")
    void shouldSetAndGetVegetarian() {
        RecipeRequest request = new RecipeRequest();
        request.setVegetarian(true);
        assertThat(request.getVegetarian()).isTrue();
    }

    @Test
    @DisplayName("Should set and get servings")
    void shouldSetAndGetServings() {
        RecipeRequest request = new RecipeRequest();
        request.setServings(4);
        assertThat(request.getServings()).isEqualTo(4);
    }

    @Test
    @DisplayName("Should set and get instructions")
    void shouldSetAndGetInstructions() {
        RecipeRequest request = new RecipeRequest();
        request.setInstructions("Test instructions");
        assertThat(request.getInstructions()).isEqualTo("Test instructions");
    }

    @Test
    @DisplayName("Should set and get ingredients")
    void shouldSetAndGetIngredients() {
        RecipeRequest request = new RecipeRequest();
        Set<String> ingredients = Set.of("ingredient1", "ingredient2");
        request.setIngredients(ingredients);
        assertThat(request.getIngredients()).hasSize(2);
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void shouldTestEqualsAndHashCode() {
        Set<String> ingredients = Set.of("ingredient1");
        RecipeRequest request1 = new RecipeRequest("Recipe", true, 4, "Instructions", ingredients);
        RecipeRequest request2 = new RecipeRequest("Recipe", true, 4, "Instructions", ingredients);
        RecipeRequest request3 = new RecipeRequest("Other", false, 2, "Other", Set.of("other"));

        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    @DisplayName("Should test toString")
    void shouldTestToString() {
        RecipeRequest request = new RecipeRequest("Recipe", true, 4, "Instructions", Set.of("ing1"));
        String toString = request.toString();
        assertThat(toString).contains("Recipe");
        assertThat(toString).contains("true");
        assertThat(toString).contains("4");
    }
}

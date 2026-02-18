package org.amoscoats.recipemanager.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Recipe Entity Unit Tests")
class RecipeTest {

    @Test
    @DisplayName("Should create recipe with default constructor")
    void shouldCreateRecipeWithDefaultConstructor() {
        Recipe recipe = new Recipe();
        assertThat(recipe).isNotNull();
        assertThat(recipe.getIngredients()).isNotNull();
    }

    @Test
    @DisplayName("Should create recipe with all arguments constructor")
    void shouldCreateRecipeWithAllArgumentsConstructor() {
        Set<String> ingredients = Set.of("ingredient1", "ingredient2");
        Recipe recipe = new Recipe(1L, "Test Recipe", true, 4, "Instructions", ingredients);

        assertThat(recipe.getId()).isEqualTo(1L);
        assertThat(recipe.getName()).isEqualTo("Test Recipe");
        assertThat(recipe.getVegetarian()).isTrue();
        assertThat(recipe.getServings()).isEqualTo(4);
        assertThat(recipe.getInstructions()).isEqualTo("Instructions");
        assertThat(recipe.getIngredients()).hasSize(2);
    }

    @Test
    @DisplayName("Should set and get id")
    void shouldSetAndGetId() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        assertThat(recipe.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should set and get name")
    void shouldSetAndGetName() {
        Recipe recipe = new Recipe();
        recipe.setName("Test Recipe");
        assertThat(recipe.getName()).isEqualTo("Test Recipe");
    }

    @Test
    @DisplayName("Should set and get vegetarian")
    void shouldSetAndGetVegetarian() {
        Recipe recipe = new Recipe();
        recipe.setVegetarian(true);
        assertThat(recipe.getVegetarian()).isTrue();

        recipe.setVegetarian(false);
        assertThat(recipe.getVegetarian()).isFalse();
    }

    @Test
    @DisplayName("Should set and get servings")
    void shouldSetAndGetServings() {
        Recipe recipe = new Recipe();
        recipe.setServings(4);
        assertThat(recipe.getServings()).isEqualTo(4);
    }

    @Test
    @DisplayName("Should set and get instructions")
    void shouldSetAndGetInstructions() {
        Recipe recipe = new Recipe();
        recipe.setInstructions("Test instructions");
        assertThat(recipe.getInstructions()).isEqualTo("Test instructions");
    }

    @Test
    @DisplayName("Should set and get ingredients")
    void shouldSetAndGetIngredients() {
        Recipe recipe = new Recipe();
        Set<String> ingredients = Set.of("ingredient1", "ingredient2", "ingredient3");
        recipe.setIngredients(ingredients);
        assertThat(recipe.getIngredients()).hasSize(3);
        assertThat(recipe.getIngredients()).containsExactlyInAnyOrder("ingredient1", "ingredient2", "ingredient3");
    }

    @Test
    @DisplayName("Should handle empty ingredients set")
    void shouldHandleEmptyIngredientsSet() {
        Recipe recipe = new Recipe();
        recipe.setIngredients(new HashSet<>());
        assertThat(recipe.getIngredients()).isEmpty();
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void shouldTestEqualsAndHashCode() {
        Set<String> ingredients = Set.of("ingredient1");
        Recipe recipe1 = new Recipe(1L, "Recipe", true, 4, "Instructions", ingredients);
        Recipe recipe2 = new Recipe(1L, "Recipe", true, 4, "Instructions", ingredients);
        Recipe recipe3 = new Recipe(2L, "Other Recipe", false, 2, "Other", Set.of("ingredient2"));

        assertThat(recipe1).isEqualTo(recipe2);
        assertThat(recipe1).isNotEqualTo(recipe3);
        assertThat(recipe1.hashCode()).isEqualTo(recipe2.hashCode());
    }

    @Test
    @DisplayName("Should test toString")
    void shouldTestToString() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Test Recipe");
        recipe.setVegetarian(true);
        recipe.setServings(4);

        String toString = recipe.toString();
        assertThat(toString).contains("Test Recipe");
        assertThat(toString).contains("1");
        assertThat(toString).contains("true");
        assertThat(toString).contains("4");
    }
}

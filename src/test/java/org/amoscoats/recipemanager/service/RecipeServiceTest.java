package org.amoscoats.recipemanager.service;

import org.amoscoats.recipemanager.dto.RecipeRequest;
import org.amoscoats.recipemanager.dto.RecipeResponse;
import org.amoscoats.recipemanager.entity.Recipe;
import org.amoscoats.recipemanager.mapper.RecipeMapper;
import org.amoscoats.recipemanager.repository.RecipeRepository;
import org.amoscoats.recipemanager.specification.RecipeSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecipeService Unit Tests")
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeService recipeService;

    private RecipeRequest recipeRequest;
    private Recipe recipe;
    private RecipeResponse recipeResponse;

    @BeforeEach
    void setUp() {
        recipeRequest = new RecipeRequest(
                "Test Recipe",
                true,
                4,
                "Test instructions",
                Set.of("ingredient1", "ingredient2")
        );

        recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Test Recipe");
        recipe.setVegetarian(true);
        recipe.setServings(4);
        recipe.setInstructions("Test instructions");
        recipe.setIngredients(Set.of("ingredient1", "ingredient2"));

        recipeResponse = new RecipeResponse(
                1L,
                "Test Recipe",
                true,
                4,
                "Test instructions",
                Set.of("ingredient1", "ingredient2")
        );
    }

    @Test
    @DisplayName("Should create recipe successfully")
    void shouldCreateRecipe() {
        // Given
        when(recipeMapper.toEntity(recipeRequest)).thenReturn(recipe);
        when(recipeRepository.save(recipe)).thenReturn(recipe);
        when(recipeMapper.toResponse(recipe)).thenReturn(recipeResponse);

        // When
        RecipeResponse result = recipeService.createRecipe(recipeRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Recipe");
        assertThat(result.getVegetarian()).isTrue();
        assertThat(result.getServings()).isEqualTo(4);

        verify(recipeMapper).toEntity(recipeRequest);
        verify(recipeRepository).save(recipe);
        verify(recipeMapper).toResponse(recipe);
    }

    @Test
    @DisplayName("Should update recipe successfully")
    void shouldUpdateRecipe() {
        // Given
        Long recipeId = 1L;
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(recipe)).thenReturn(recipe);
        when(recipeMapper.toResponse(recipe)).thenReturn(recipeResponse);

        // When
        RecipeResponse result = recipeService.updateRecipe(recipeId, recipeRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(recipeRepository).findById(recipeId);
        verify(recipeRepository).save(recipe);
        verify(recipeMapper).toResponse(recipe);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent recipe")
    void shouldThrowExceptionWhenUpdatingNonExistentRecipe() {
        // Given
        Long recipeId = 999L;
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> recipeService.updateRecipe(recipeId, recipeRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Recipe not found with id: 999");

        verify(recipeRepository).findById(recipeId);
        verify(recipeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete recipe successfully")
    void shouldDeleteRecipe() {
        // Given
        Long recipeId = 1L;
        when(recipeRepository.existsById(recipeId)).thenReturn(true);
        doNothing().when(recipeRepository).deleteById(recipeId);

        // When
        recipeService.deleteRecipe(recipeId);

        // Then
        verify(recipeRepository).existsById(recipeId);
        verify(recipeRepository).deleteById(recipeId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent recipe")
    void shouldThrowExceptionWhenDeletingNonExistentRecipe() {
        // Given
        Long recipeId = 999L;
        when(recipeRepository.existsById(recipeId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> recipeService.deleteRecipe(recipeId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Recipe not found with id: 999");

        verify(recipeRepository).existsById(recipeId);
        verify(recipeRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should get recipe by ID successfully")
    void shouldGetRecipeById() {
        // Given
        Long recipeId = 1L;
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        when(recipeMapper.toResponse(recipe)).thenReturn(recipeResponse);

        // When
        RecipeResponse result = recipeService.getRecipeById(recipeId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Recipe");

        verify(recipeRepository).findById(recipeId);
        verify(recipeMapper).toResponse(recipe);
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent recipe by ID")
    void shouldThrowExceptionWhenGettingNonExistentRecipeById() {
        // Given
        Long recipeId = 999L;
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> recipeService.getRecipeById(recipeId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Recipe not found with id: 999");

        verify(recipeRepository).findById(recipeId);
        verify(recipeMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Should get all recipes successfully")
    void shouldGetAllRecipes() {
        // Given
        Recipe recipe2 = new Recipe();
        recipe2.setId(2L);
        recipe2.setName("Recipe 2");

        RecipeResponse response2 = new RecipeResponse(
                2L, "Recipe 2", false, 2, "Instructions", Set.of("ingredient3")
        );

        List<Recipe> recipes = List.of(recipe, recipe2);
        when(recipeRepository.findAll()).thenReturn(recipes);
        when(recipeMapper.toResponse(recipe)).thenReturn(recipeResponse);
        when(recipeMapper.toResponse(recipe2)).thenReturn(response2);

        // When
        List<RecipeResponse> result = recipeService.getAllRecipes();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);

        verify(recipeRepository).findAll();
        verify(recipeMapper, times(2)).toResponse(any(Recipe.class));
    }

    @Test
    @DisplayName("Should return empty list when no recipes exist")
    void shouldReturnEmptyListWhenNoRecipesExist() {
        // Given
        when(recipeRepository.findAll()).thenReturn(List.of());

        // When
        List<RecipeResponse> result = recipeService.getAllRecipes();

        // Then
        assertThat(result).isEmpty();

        verify(recipeRepository).findAll();
        verify(recipeMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Should filter recipes successfully")
    void shouldFilterRecipes() {
        // Given
        Boolean vegetarian = true;
        Integer servings = 4;
        Set<String> includeIngredients = Set.of("potatoes");
        Set<String> excludeIngredients = Set.of("salmon");
        String searchText = "oven";

        List<Recipe> filteredRecipes = List.of(recipe);
        when(recipeRepository.findAll(any(Specification.class))).thenReturn(filteredRecipes);
        when(recipeMapper.toResponse(recipe)).thenReturn(recipeResponse);

        // When
        List<RecipeResponse> result = recipeService.filterRecipes(
                vegetarian, servings, includeIngredients, excludeIngredients, searchText
        );

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);

        verify(recipeRepository).findAll(any(Specification.class));
        verify(recipeMapper).toResponse(recipe);
    }

    @Test
    @DisplayName("Should filter recipes with null parameters")
    void shouldFilterRecipesWithNullParameters() {
        // Given
        List<Recipe> filteredRecipes = List.of(recipe);
        when(recipeRepository.findAll(any(Specification.class))).thenReturn(filteredRecipes);
        when(recipeMapper.toResponse(recipe)).thenReturn(recipeResponse);

        // When
        List<RecipeResponse> result = recipeService.filterRecipes(
                null, null, null, null, null
        );

        // Then
        assertThat(result).hasSize(1);

        verify(recipeRepository).findAll(any(Specification.class));
        verify(recipeMapper).toResponse(recipe);
    }

    @Test
    @DisplayName("Should filter recipes with empty result")
    void shouldFilterRecipesWithEmptyResult() {
        // Given
        when(recipeRepository.findAll(any(Specification.class))).thenReturn(List.of());

        // When
        List<RecipeResponse> result = recipeService.filterRecipes(
                true, 10, Set.of("ingredient"), Set.of(), "text"
        );

        // Then
        assertThat(result).isEmpty();

        verify(recipeRepository).findAll(any(Specification.class));
        verify(recipeMapper, never()).toResponse(any());
    }
}

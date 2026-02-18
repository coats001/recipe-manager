package org.amoscoats.recipemanager.controller;

import org.amoscoats.recipemanager.dto.RecipeRequest;
import org.amoscoats.recipemanager.dto.RecipeResponse;
import org.amoscoats.recipemanager.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecipeController Unit Tests")
class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    private RecipeRequest recipeRequest;
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
    @DisplayName("Should create recipe and return 201 Created")
    void shouldCreateRecipe() {
        // Given
        when(recipeService.createRecipe(recipeRequest)).thenReturn(recipeResponse);

        // When
        ResponseEntity<RecipeResponse> response = recipeController.createRecipe(recipeRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getName()).isEqualTo("Test Recipe");

        verify(recipeService).createRecipe(recipeRequest);
    }

    @Test
    @DisplayName("Should update recipe and return 200 OK")
    void shouldUpdateRecipe() {
        // Given
        Long recipeId = 1L;
        when(recipeService.updateRecipe(recipeId, recipeRequest)).thenReturn(recipeResponse);

        // When
        ResponseEntity<RecipeResponse> response = recipeController.updateRecipe(recipeId, recipeRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);

        verify(recipeService).updateRecipe(recipeId, recipeRequest);
    }

    @Test
    @DisplayName("Should delete recipe and return 204 No Content")
    void shouldDeleteRecipe() {
        // Given
        Long recipeId = 1L;
        doNothing().when(recipeService).deleteRecipe(recipeId);

        // When
        ResponseEntity<Void> response = recipeController.deleteRecipe(recipeId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();

        verify(recipeService).deleteRecipe(recipeId);
    }

    @Test
    @DisplayName("Should get recipe by ID and return 200 OK")
    void shouldGetRecipeById() {
        // Given
        Long recipeId = 1L;
        when(recipeService.getRecipeById(recipeId)).thenReturn(recipeResponse);

        // When
        ResponseEntity<RecipeResponse> response = recipeController.getRecipeById(recipeId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);

        verify(recipeService).getRecipeById(recipeId);
    }

    @Test
    @DisplayName("Should get all recipes when no filters provided")
    void shouldGetAllRecipesWhenNoFiltersProvided() {
        // Given
        RecipeResponse response2 = new RecipeResponse(
                2L, "Recipe 2", false, 2, "Instructions", Set.of("ingredient3")
        );
        List<RecipeResponse> recipes = List.of(recipeResponse, response2);
        when(recipeService.getAllRecipes()).thenReturn(recipes);

        // When
        ResponseEntity<List<RecipeResponse>> response = recipeController.getRecipes(
                null, null, null, null, null
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);

        verify(recipeService).getAllRecipes();
        verify(recipeService, never()).filterRecipes(any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Should get all recipes when empty sets provided")
    void shouldGetAllRecipesWhenEmptySetsProvided() {
        // Given
        List<RecipeResponse> recipes = List.of(recipeResponse);
        when(recipeService.getAllRecipes()).thenReturn(recipes);

        // When
        ResponseEntity<List<RecipeResponse>> response = recipeController.getRecipes(
                null, null, Set.of(), Set.of(), ""
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        verify(recipeService).getAllRecipes();
        verify(recipeService, never()).filterRecipes(any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Should filter recipes by vegetarian")
    void shouldFilterRecipesByVegetarian() {
        // Given
        Boolean vegetarian = true;
        List<RecipeResponse> recipes = List.of(recipeResponse);
        when(recipeService.filterRecipes(vegetarian, null, null, null, null))
                .thenReturn(recipes);

        // When
        ResponseEntity<List<RecipeResponse>> response = recipeController.getRecipes(
                vegetarian, null, null, null, null
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        verify(recipeService).filterRecipes(vegetarian, null, null, null, null);
        verify(recipeService, never()).getAllRecipes();
    }

    @Test
    @DisplayName("Should filter recipes by servings")
    void shouldFilterRecipesByServings() {
        // Given
        Integer servings = 4;
        List<RecipeResponse> recipes = List.of(recipeResponse);
        when(recipeService.filterRecipes(null, servings, null, null, null))
                .thenReturn(recipes);

        // When
        ResponseEntity<List<RecipeResponse>> response = recipeController.getRecipes(
                null, servings, null, null, null
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        verify(recipeService).filterRecipes(null, servings, null, null, null);
    }

    @Test
    @DisplayName("Should filter recipes by including ingredients")
    void shouldFilterRecipesByIncludingIngredients() {
        // Given
        Set<String> includeIngredients = Set.of("potatoes");
        List<RecipeResponse> recipes = List.of(recipeResponse);
        when(recipeService.filterRecipes(null, null, includeIngredients, null, null))
                .thenReturn(recipes);

        // When
        ResponseEntity<List<RecipeResponse>> response = recipeController.getRecipes(
                null, null, includeIngredients, null, null
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        verify(recipeService).filterRecipes(null, null, includeIngredients, null, null);
    }

    @Test
    @DisplayName("Should filter recipes by excluding ingredients")
    void shouldFilterRecipesByExcludingIngredients() {
        // Given
        Set<String> excludeIngredients = Set.of("salmon");
        List<RecipeResponse> recipes = List.of(recipeResponse);
        when(recipeService.filterRecipes(null, null, null, excludeIngredients, null))
                .thenReturn(recipes);

        // When
        ResponseEntity<List<RecipeResponse>> response = recipeController.getRecipes(
                null, null, null, excludeIngredients, null
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        verify(recipeService).filterRecipes(null, null, null, excludeIngredients, null);
    }

    @Test
    @DisplayName("Should filter recipes by search text")
    void shouldFilterRecipesBySearchText() {
        // Given
        String searchText = "oven";
        List<RecipeResponse> recipes = List.of(recipeResponse);
        when(recipeService.filterRecipes(null, null, null, null, searchText))
                .thenReturn(recipes);

        // When
        ResponseEntity<List<RecipeResponse>> response = recipeController.getRecipes(
                null, null, null, null, searchText
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        verify(recipeService).filterRecipes(null, null, null, null, searchText);
    }

    @Test
    @DisplayName("Should filter recipes with multiple criteria")
    void shouldFilterRecipesWithMultipleCriteria() {
        // Given
        Boolean vegetarian = true;
        Integer servings = 4;
        Set<String> includeIngredients = Set.of("potatoes");
        Set<String> excludeIngredients = Set.of("salmon");
        String searchText = "oven";
        List<RecipeResponse> recipes = List.of(recipeResponse);

        when(recipeService.filterRecipes(vegetarian, servings, includeIngredients,
                excludeIngredients, searchText)).thenReturn(recipes);

        // When
        ResponseEntity<List<RecipeResponse>> response = recipeController.getRecipes(
                vegetarian, servings, includeIngredients, excludeIngredients, searchText
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        verify(recipeService).filterRecipes(vegetarian, servings, includeIngredients,
                excludeIngredients, searchText);
    }

    @Test
    @DisplayName("Should return empty list when no recipes match filters")
    void shouldReturnEmptyListWhenNoRecipesMatchFilters() {
        // Given
        when(recipeService.filterRecipes(false, 10, null, null, null))
                .thenReturn(List.of());

        // When
        ResponseEntity<List<RecipeResponse>> response = recipeController.getRecipes(
                false, 10, null, null, null
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();

        verify(recipeService).filterRecipes(false, 10, null, null, null);
    }
}

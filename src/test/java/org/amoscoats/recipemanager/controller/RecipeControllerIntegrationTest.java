package org.amoscoats.recipemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.amoscoats.recipemanager.dto.RecipeRequest;
import org.amoscoats.recipemanager.dto.RecipeResponse;
import org.amoscoats.recipemanager.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@DisplayName("Recipe Controller Integration Tests")
class RecipeControllerIntegrationTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Initialize ObjectMapper
        objectMapper = new ObjectMapper();

        // Clean database before each test
        recipeRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create a new recipe")
    void shouldCreateRecipe() throws Exception {
        RecipeRequest request = new RecipeRequest(
                "Vegetarian Pasta",
                true,
                4,
                "Boil pasta. Add tomato sauce and vegetables. Bake in oven for 20 minutes.",
                Set.of("pasta", "tomato sauce", "bell peppers", "onions", "garlic")
        );

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Vegetarian Pasta"))
                .andExpect(jsonPath("$.vegetarian").value(true))
                .andExpect(jsonPath("$.servings").value(4))
                .andExpect(jsonPath("$.instructions").value(containsString("oven")))
                .andExpect(jsonPath("$.ingredients").isArray())
                .andExpect(jsonPath("$.ingredients", hasSize(5)));
    }

    @Test
    @DisplayName("Should return validation error when creating recipe with missing fields")
    void shouldReturnValidationErrorWhenCreatingInvalidRecipe() throws Exception {
        RecipeRequest request = new RecipeRequest(
                null, // missing name
                null, // missing vegetarian flag
                0,    // invalid servings
                null, // missing instructions
                null  // missing ingredients
        );

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @DisplayName("Should get all recipes")
    void shouldGetAllRecipes() throws Exception {
        // Create test data
        createTestRecipe("Vegetarian Pasta", true, 4, "Boil and bake in oven.",
                Set.of("pasta", "tomato sauce"));
        createTestRecipe("Salmon Dish", false, 2, "Bake salmon in oven.",
                Set.of("salmon", "lemon"));

        mockMvc.perform(get("/api/recipes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Should get recipe by ID")
    void shouldGetRecipeById() throws Exception {
        // Create test recipe
        Long recipeId = createTestRecipe("Test Recipe", true, 4, "Instructions",
                Set.of("ingredient1"));

        mockMvc.perform(get("/api/recipes/{id}", recipeId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipeId))
                .andExpect(jsonPath("$.name").value("Test Recipe"));
    }

    @Test
    @DisplayName("Should return 404 when recipe not found")
    void shouldReturn404WhenRecipeNotFound() throws Exception {
        mockMvc.perform(get("/api/recipes/999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("Recipe not found")));
    }

    @Test
    @DisplayName("Should update an existing recipe")
    void shouldUpdateRecipe() throws Exception {
        // Create test recipe
        Long recipeId = createTestRecipe("Original Recipe", true, 4, "Original instructions",
                Set.of("ingredient1"));

        RecipeRequest updateRequest = new RecipeRequest(
                "Updated Recipe",
                false,
                6,
                "Updated instructions",
                Set.of("new-ingredient1", "new-ingredient2")
        );

        mockMvc.perform(put("/api/recipes/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipeId))
                .andExpect(jsonPath("$.name").value("Updated Recipe"))
                .andExpect(jsonPath("$.vegetarian").value(false))
                .andExpect(jsonPath("$.servings").value(6))
                .andExpect(jsonPath("$.instructions").value("Updated instructions"))
                .andExpect(jsonPath("$.ingredients", hasSize(2)));
    }

    @Test
    @DisplayName("Should delete a recipe")
    void shouldDeleteRecipe() throws Exception {
        // Create test recipe
        Long recipeId = createTestRecipe("Recipe to Delete", true, 4, "Instructions",
                Set.of("ingredient1"));

        // Delete the recipe
        mockMvc.perform(delete("/api/recipes/{id}", recipeId))
                .andDo(print())
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/api/recipes/{id}", recipeId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should filter recipes by vegetarian status")
    void shouldFilterByVegetarian() throws Exception {
        // Create test data
        createTestRecipe("Vegetarian Recipe 1", true, 4, "Instructions", Set.of("pasta"));
        createTestRecipe("Vegetarian Recipe 2", true, 2, "Instructions", Set.of("rice"));
        createTestRecipe("Non-Vegetarian Recipe", false, 4, "Instructions", Set.of("chicken"));

        mockMvc.perform(get("/api/recipes")
                        .param("vegetarian", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].vegetarian", everyItem(is(true))));
    }

    @Test
    @DisplayName("Should filter recipes by number of servings")
    void shouldFilterByServings() throws Exception {
        // Create test data
        createTestRecipe("Recipe for 4", true, 4, "Instructions", Set.of("pasta"));
        createTestRecipe("Another Recipe for 4", false, 4, "Instructions", Set.of("chicken"));
        createTestRecipe("Recipe for 2", true, 2, "Instructions", Set.of("rice"));

        mockMvc.perform(get("/api/recipes")
                        .param("servings", "4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].servings", everyItem(is(4))));
    }

    @Test
    @DisplayName("Should filter recipes by including specific ingredients")
    void shouldFilterByIncludingIngredients() throws Exception {
        // Create test data
        createTestRecipe("Potato Soup", true, 4, "Instructions",
                Set.of("potatoes", "onions", "cream"));
        createTestRecipe("Salmon with Potatoes", false, 2, "Instructions",
                Set.of("salmon", "potatoes", "lemon"));
        createTestRecipe("Chicken Dish", false, 3, "Instructions",
                Set.of("chicken", "rice"));

        mockMvc.perform(get("/api/recipes")
                        .param("includeIngredients", "potatoes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Should filter recipes by excluding specific ingredients")
    void shouldFilterByExcludingIngredients() throws Exception {
        // Create test data
        createTestRecipe("Vegetarian Pasta", true, 4, "Instructions",
                Set.of("pasta", "tomato sauce"));
        createTestRecipe("Salmon Dish", false, 2, "Instructions",
                Set.of("salmon", "lemon"));
        createTestRecipe("Chicken Dish", false, 3, "Instructions",
                Set.of("chicken", "rice"));

        mockMvc.perform(get("/api/recipes")
                        .param("excludeIngredients", "salmon"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Should filter recipes by text search in instructions")
    void shouldFilterBySearchText() throws Exception {
        // Create test data
        createTestRecipe("Baked Pasta", true, 4, "Boil pasta and bake in oven for 30 minutes.",
                Set.of("pasta"));
        createTestRecipe("Oven-Roasted Salmon", false, 2, "Season salmon and bake in oven at 180C.",
                Set.of("salmon"));
        createTestRecipe("Stir-Fried Chicken", false, 3, "Stir-fry chicken in wok with vegetables.",
                Set.of("chicken"));

        mockMvc.perform(get("/api/recipes")
                        .param("searchText", "oven"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Should filter recipes with multiple criteria combined")
    void shouldFilterWithMultipleCriteria() throws Exception {
        // Create test data
        createTestRecipe("Vegetarian Potato Bake", true, 4,
                "Slice potatoes and bake in oven at 200C.",
                Set.of("potatoes", "cheese", "cream"));
        createTestRecipe("Vegetarian Pasta", true, 4,
                "Boil pasta and mix with sauce.",
                Set.of("pasta", "tomato sauce"));
        createTestRecipe("Salmon with Potatoes", false, 2,
                "Bake salmon and potatoes in oven.",
                Set.of("salmon", "potatoes"));
        createTestRecipe("Potato Soup", true, 4,
                "Cook potatoes on stovetop with broth.",
                Set.of("potatoes", "broth"));

        // Filter: vegetarian=true, servings=4, includeIngredients=potatoes, searchText=oven
        mockMvc.perform(get("/api/recipes")
                        .param("vegetarian", "true")
                        .param("servings", "4")
                        .param("includeIngredients", "potatoes")
                        .param("searchText", "oven"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Vegetarian Potato Bake"));
    }

    @Test
    @DisplayName("Should filter recipes excluding ingredients and searching text")
    void shouldFilterExcludingIngredientsAndSearchingText() throws Exception {
        // Create test data
        createTestRecipe("Vegetarian Pasta", true, 4,
                "Boil pasta and bake in oven with vegetables.",
                Set.of("pasta", "tomato sauce", "bell peppers"));
        createTestRecipe("Salmon Bake", false, 2,
                "Season salmon and bake in oven at 180C.",
                Set.of("salmon", "lemon"));
        createTestRecipe("Chicken Stir-Fry", false, 3,
                "Stir-fry chicken in wok.",
                Set.of("chicken", "vegetables"));

        // Filter: excludeIngredients=salmon, searchText=oven
        mockMvc.perform(get("/api/recipes")
                        .param("excludeIngredients", "salmon")
                        .param("searchText", "oven"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Vegetarian Pasta"));
    }

    @Test
    @DisplayName("Should return empty array when no recipes match filters")
    void shouldReturnEmptyArrayWhenNoRecipesMatch() throws Exception {
        // Create test data
        createTestRecipe("Vegetarian Pasta", true, 4, "Instructions",
                Set.of("pasta", "tomato sauce"));

        mockMvc.perform(get("/api/recipes")
                        .param("vegetarian", "false")
                        .param("servings", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should handle multiple ingredients in include filter")
    void shouldHandleMultipleIncludeIngredients() throws Exception {
        // Create test data
        createTestRecipe("Potato and Onion Soup", true, 4, "Instructions",
                Set.of("potatoes", "onions", "cream"));
        createTestRecipe("Simple Potato Dish", true, 2, "Instructions",
                Set.of("potatoes", "butter"));
        createTestRecipe("Onion Rings", true, 2, "Instructions",
                Set.of("onions", "flour"));

        mockMvc.perform(get("/api/recipes")
                        .param("includeIngredients", "potatoes,onions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Potato and Onion Soup"));
    }

    @Test
    @DisplayName("Should handle case-insensitive ingredient search")
    void shouldHandleCaseInsensitiveIngredientSearch() throws Exception {
        // Create test data
        createTestRecipe("Potato Dish", true, 4, "Instructions",
                Set.of("potatoes", "onions"));

        // Search with different case
        mockMvc.perform(get("/api/recipes")
                        .param("includeIngredients", "POTATOES"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("Should handle case-insensitive text search")
    void shouldHandleCaseInsensitiveTextSearch() throws Exception {
        // Create test data
        createTestRecipe("Baked Dish", true, 4, "Bake in OVEN at 180C",
                Set.of("ingredient1"));

        // Search with different case
        mockMvc.perform(get("/api/recipes")
                        .param("searchText", "oven"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // Helper method to create test recipes
    private Long createTestRecipe(String name, boolean vegetarian, int servings,
                                   String instructions, Set<String> ingredients) throws Exception {
        RecipeRequest request = new RecipeRequest(name, vegetarian, servings, instructions, ingredients);

        MvcResult result = mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        RecipeResponse response = objectMapper.readValue(responseBody, RecipeResponse.class);

        assertThat(response.getId()).isNotNull();
        return response.getId();
    }
}

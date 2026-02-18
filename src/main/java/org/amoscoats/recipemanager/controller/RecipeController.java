package org.amoscoats.recipemanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.amoscoats.recipemanager.dto.RecipeRequest;
import org.amoscoats.recipemanager.dto.RecipeResponse;
import org.amoscoats.recipemanager.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for recipe management operations. */
@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@Tag(name = "Recipe Management", description = "APIs for managing favorite recipes")
public class RecipeController {

  private final RecipeService recipeService;

  /**
   * Create a new recipe POST /api/recipes.
   *
   * @param request recipe details to create
   * @return created recipe response
   */
  @Operation(
      summary = "Create a new recipe",
      description =
          "Creates a new recipe with the provided details including name, vegetarian status,"
              + " servings, instructions, and ingredients")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Recipe created successfully",
            content = @Content(schema = @Schema(implementation = RecipeResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content)
      })
  @PostMapping
  public ResponseEntity<RecipeResponse> createRecipe(
      @Parameter(description = "Recipe details to create", required = true) @Valid @RequestBody
          RecipeRequest request) {
    RecipeResponse response = recipeService.createRecipe(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * Update an existing recipe PUT /api/recipes/{id}.
   *
   * @param id recipe ID
   * @param request updated recipe details
   * @return updated recipe response
   */
  @Operation(
      summary = "Update an existing recipe",
      description = "Updates all fields of an existing recipe identified by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Recipe updated successfully",
            content = @Content(schema = @Schema(implementation = RecipeResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "Recipe not found",
            content = @Content)
      })
  @PutMapping("/{id}")
  public ResponseEntity<RecipeResponse> updateRecipe(
      @Parameter(description = "Recipe ID", required = true, example = "1") @PathVariable Long id,
      @Parameter(description = "Updated recipe details", required = true) @Valid @RequestBody
          RecipeRequest request) {
    RecipeResponse response = recipeService.updateRecipe(id, request);
    return ResponseEntity.ok(response);
  }

  /**
   * Delete a recipe DELETE /api/recipes/{id}.
   *
   * @param id recipe ID to delete
   * @return no content response
   */
  @Operation(summary = "Delete a recipe", description = "Deletes a recipe identified by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Recipe deleted successfully"),
        @ApiResponse(
            responseCode = "404",
            description = "Recipe not found",
            content = @Content)
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRecipe(
      @Parameter(description = "Recipe ID to delete", required = true, example = "1")
          @PathVariable Long id) {
    recipeService.deleteRecipe(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Get a single recipe by ID GET /api/recipes/{id}.
   *
   * @param id recipe ID
   * @return recipe response
   */
  @Operation(
      summary = "Get a recipe by ID",
      description = "Retrieves a single recipe by its unique identifier")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Recipe found",
            content = @Content(schema = @Schema(implementation = RecipeResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Recipe not found",
            content = @Content)
      })
  @GetMapping("/{id}")
  public ResponseEntity<RecipeResponse> getRecipeById(
      @Parameter(description = "Recipe ID", required = true, example = "1") @PathVariable Long id) {
    RecipeResponse response = recipeService.getRecipeById(id);
    return ResponseEntity.ok(response);
  }

  /**
   * Get all recipes or filter recipes based on criteria GET /api/recipes.
   *
   * <p>Query parameters: - vegetarian: Boolean (true/false) - filter vegetarian recipes -
   * servings: Integer - filter by number of servings - includeIngredients: Set (comma-separated) -
   * recipes that include these ingredients - excludeIngredients: Set (comma-separated) - recipes
   * that exclude these ingredients - searchText: String - search text within instructions
   *
   * <p>Examples: - GET /api/recipes?vegetarian=true (all vegetarian recipes) - GET
   * /api/recipes?servings=4&amp;includeIngredients=potatoes (recipes for 4 with potatoes) - GET
   * /api/recipes?excludeIngredients=salmon&amp;searchText=oven (no salmon, mentions oven)
   *
   * @param vegetarian filter by vegetarian status
   * @param servings filter by number of servings
   * @param includeIngredients include recipes with these ingredients
   * @param excludeIngredients exclude recipes with these ingredients
   * @param searchText search text within instructions
   * @return list of recipe responses
   */
  @Operation(
      summary = "Get all recipes or filter recipes",
      description =
          "Retrieves all recipes or filters them based on vegetarian status, servings, ingredients"
              + " (include/exclude), and text search in instructions. All filter parameters are"
              + " optional and can be combined.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Recipes retrieved successfully",
            content = @Content(schema = @Schema(implementation = RecipeResponse.class)))
      })
  @GetMapping
  public ResponseEntity<List<RecipeResponse>> getRecipes(
      @Parameter(description = "Filter by vegetarian status", example = "true")
          @RequestParam(required = false)
          Boolean vegetarian,
      @Parameter(description = "Filter by number of servings", example = "4")
          @RequestParam(required = false)
          Integer servings,
      @Parameter(
              description = "Include recipes with these ingredients (comma-separated)",
              example = "potatoes,onions")
          @RequestParam(required = false)
          Set<String> includeIngredients,
      @Parameter(
              description = "Exclude recipes with these ingredients (comma-separated)",
              example = "salmon,chicken")
          @RequestParam(required = false)
          Set<String> excludeIngredients,
      @Parameter(description = "Search text within cooking instructions", example = "oven")
          @RequestParam(required = false)
          String searchText) {
    // If no filters provided, return all recipes
    if (vegetarian == null
        && servings == null
        && (includeIngredients == null || includeIngredients.isEmpty())
        && (excludeIngredients == null || excludeIngredients.isEmpty())
        && (searchText == null || searchText.isEmpty())) {
      List<RecipeResponse> recipes = recipeService.getAllRecipes();
      return ResponseEntity.ok(recipes);
    }

    // Otherwise, apply filters
    List<RecipeResponse> recipes =
        recipeService.filterRecipes(
            vegetarian, servings, includeIngredients, excludeIngredients, searchText);
    return ResponseEntity.ok(recipes);
  }
}

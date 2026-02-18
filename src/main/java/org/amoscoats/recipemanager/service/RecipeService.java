package org.amoscoats.recipemanager.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.amoscoats.recipemanager.dto.RecipeRequest;
import org.amoscoats.recipemanager.dto.RecipeResponse;
import org.amoscoats.recipemanager.entity.Recipe;
import org.amoscoats.recipemanager.mapper.RecipeMapper;
import org.amoscoats.recipemanager.repository.RecipeRepository;
import org.amoscoats.recipemanager.specification.RecipeSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service for recipe management business logic. */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RecipeService {

  private final RecipeRepository recipeRepository;
  private final RecipeMapper recipeMapper;

  /**
   * Creates a new recipe.
   *
   * @param request recipe creation request
   * @return created recipe response
   */
  public RecipeResponse createRecipe(RecipeRequest request) {
    log.info("Creating new recipe with name: {}", request.getName());
    log.debug("Recipe request details: {}", request);
    Recipe recipe = recipeMapper.toEntity(request);
    Recipe savedRecipe = recipeRepository.save(recipe);
    log.info("Successfully created recipe with id: {}", savedRecipe.getId());
    return recipeMapper.toResponse(savedRecipe);
  }

  /**
   * Updates an existing recipe.
   *
   * @param id recipe ID
   * @param request recipe update request
   * @return updated recipe response
   */
  public RecipeResponse updateRecipe(Long id, RecipeRequest request) {
    log.info("Updating recipe with id: {}", id);
    log.debug("Update request details: {}", request);
    Recipe recipe =
        recipeRepository
            .findById(id)
            .orElseThrow(() -> {
              log.error("Recipe not found with id: {}", id);
              return new RuntimeException("Recipe not found with id: " + id);
            });

    // Update all fields
    recipe.setName(request.getName());
    recipe.setVegetarian(request.getVegetarian());
    recipe.setServings(request.getServings());
    recipe.setInstructions(request.getInstructions());

    // Replace ingredients collection with a new mutable one
    recipe.setIngredients(new java.util.HashSet<>(request.getIngredients()));

    Recipe updatedRecipe = recipeRepository.save(recipe);
    log.info("Successfully updated recipe with id: {}", id);
    return recipeMapper.toResponse(updatedRecipe);
  }

  /**
   * Deletes a recipe by ID.
   *
   * @param id recipe ID to delete
   */
  public void deleteRecipe(Long id) {
    log.info("Deleting recipe with id: {}", id);
    if (!recipeRepository.existsById(id)) {
      log.error("Cannot delete recipe - not found with id: {}", id);
      throw new RuntimeException("Recipe not found with id: " + id);
    }
    recipeRepository.deleteById(id);
    log.info("Successfully deleted recipe with id: {}", id);
  }

  /**
   * Retrieves a recipe by ID.
   *
   * @param id recipe ID
   * @return recipe response
   */
  @Transactional(readOnly = true)
  public RecipeResponse getRecipeById(Long id) {
    log.info("Fetching recipe with id: {}", id);
    Recipe recipe =
        recipeRepository
            .findById(id)
            .orElseThrow(() -> {
              log.error("Recipe not found with id: {}", id);
              return new RuntimeException("Recipe not found with id: " + id);
            });
    log.debug("Retrieved recipe: {}", recipe);
    return recipeMapper.toResponse(recipe);
  }

  /**
   * Retrieves all recipes.
   *
   * @return list of all recipe responses
   */
  @Transactional(readOnly = true)
  public List<RecipeResponse> getAllRecipes() {
    log.info("Fetching all recipes");
    List<RecipeResponse> recipes = recipeRepository.findAll().stream()
        .map(recipeMapper::toResponse)
        .collect(Collectors.toList());
    log.info("Retrieved {} recipes", recipes.size());
    return recipes;
  }

  /**
   * Filters recipes based on multiple criteria.
   *
   * @param vegetarian filter by vegetarian status
   * @param servings filter by number of servings
   * @param includeIngredients ingredients that must be present
   * @param excludeIngredients ingredients that must not be present
   * @param searchText text to search in instructions
   * @return list of filtered recipe responses
   */
  @Transactional(readOnly = true)
  public List<RecipeResponse> filterRecipes(
      Boolean vegetarian,
      Integer servings,
      Set<String> includeIngredients,
      Set<String> excludeIngredients,
      String searchText) {
    log.info("Filtering recipes with criteria - vegetarian: {}, servings: {}, includeIngredients: {}, excludeIngredients: {}, searchText: {}",
        vegetarian, servings, includeIngredients, excludeIngredients, searchText);
    Specification<Recipe> spec =
        RecipeSpecification.filterRecipes(
            vegetarian, servings, includeIngredients, excludeIngredients, searchText);

    List<RecipeResponse> recipes = recipeRepository.findAll(spec).stream()
        .map(recipeMapper::toResponse)
        .collect(Collectors.toList());
    log.info("Found {} recipes matching filter criteria", recipes.size());
    return recipes;
  }
}

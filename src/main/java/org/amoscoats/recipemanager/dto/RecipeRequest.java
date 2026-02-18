package org.amoscoats.recipemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Recipe creation/update request DTO. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Recipe creation/update request")
public class RecipeRequest {

  @Schema(
      description = "Name of the recipe",
      example = "Vegetarian Pasta",
      required = true)
  @NotBlank(message = "Recipe name is required")
  private String name;

  @Schema(
      description = "Whether the recipe is vegetarian",
      example = "true",
      required = true)
  @NotNull(message = "Vegetarian flag is required")
  private Boolean vegetarian;

  @Schema(
      description = "Number of servings",
      example = "4",
      required = true,
      minimum = "1")
  @NotNull(message = "Servings is required")
  @Min(value = 1, message = "Servings must be at least 1")
  private Integer servings;

  @Schema(
      description = "Cooking instructions",
      example = "Boil pasta. Add sauce. Bake in oven.",
      required = true)
  @NotBlank(message = "Instructions are required")
  private String instructions;

  @Schema(
      description = "List of ingredients",
      example = "[\"pasta\", \"tomato sauce\", \"garlic\"]",
      required = true)
  @NotNull(message = "Ingredients are required")
  private Set<String> ingredients;
}

package org.amoscoats.recipemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Recipe response DTO. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Recipe response")
public class RecipeResponse {

  @Schema(description = "Unique identifier of the recipe", example = "1")
  private Long id;

  @Schema(description = "Name of the recipe", example = "Vegetarian Pasta")
  private String name;

  @Schema(description = "Whether the recipe is vegetarian", example = "true")
  private Boolean vegetarian;

  @Schema(description = "Number of servings", example = "4")
  private Integer servings;

  @Schema(
      description = "Cooking instructions",
      example = "Boil pasta. Add sauce. Bake in oven.")
  private String instructions;

  @Schema(
      description = "List of ingredients",
      example = "[\"pasta\", \"tomato sauce\", \"garlic\"]")
  private Set<String> ingredients;
}

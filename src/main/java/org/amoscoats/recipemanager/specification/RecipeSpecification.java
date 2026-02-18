package org.amoscoats.recipemanager.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.amoscoats.recipemanager.entity.Recipe;
import org.springframework.data.jpa.domain.Specification;

/** Specification builder for Recipe entity queries. */
public class RecipeSpecification {

  /**
   * Creates a specification for filtering recipes based on multiple criteria.
   *
   * @param vegetarian filter by vegetarian status
   * @param servings filter by number of servings
   * @param includeIngredients ingredients that must be present
   * @param excludeIngredients ingredients that must not be present
   * @param searchText text to search in instructions
   * @return specification for filtering recipes
   */
  public static Specification<Recipe> filterRecipes(
      Boolean vegetarian,
      Integer servings,
      Set<String> includeIngredients,
      Set<String> excludeIngredients,
      String searchText) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      // Filter by vegetarian
      if (vegetarian != null) {
        predicates.add(criteriaBuilder.equal(root.get("vegetarian"), vegetarian));
      }

      // Filter by servings
      if (servings != null) {
        predicates.add(criteriaBuilder.equal(root.get("servings"), servings));
      }

      // Include specific ingredients
      if (includeIngredients != null && !includeIngredients.isEmpty()) {
        for (String ingredient : includeIngredients) {
          Join<Recipe, String> ingredientsJoin = root.join("ingredients");
          predicates.add(
              criteriaBuilder.like(
                  criteriaBuilder.lower(ingredientsJoin.as(String.class)),
                  "%" + ingredient.toLowerCase() + "%"));
        }
      }

      // Exclude specific ingredients
      if (excludeIngredients != null && !excludeIngredients.isEmpty()) {
        for (String ingredient : excludeIngredients) {
          Subquery<Long> subquery = query.subquery(Long.class);
          Root<Recipe> subRoot = subquery.from(Recipe.class);
          Join<Recipe, String> subIngredientJoin = subRoot.join("ingredients");

          subquery
              .select(subRoot.get("id"))
              .where(
                  criteriaBuilder.and(
                      criteriaBuilder.equal(subRoot.get("id"), root.get("id")),
                      criteriaBuilder.like(
                          criteriaBuilder.lower(subIngredientJoin.as(String.class)),
                          "%" + ingredient.toLowerCase() + "%")));

          predicates.add(criteriaBuilder.not(criteriaBuilder.exists(subquery)));
        }
      }

      // Search text within instructions
      if (searchText != null && !searchText.isEmpty()) {
        predicates.add(
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("instructions")),
                "%" + searchText.toLowerCase() + "%"));
      }

      // Make distinct to avoid duplicates from joins
      query.distinct(true);

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}

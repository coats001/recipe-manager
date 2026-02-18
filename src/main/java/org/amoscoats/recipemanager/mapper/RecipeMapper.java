package org.amoscoats.recipemanager.mapper;

import org.amoscoats.recipemanager.dto.RecipeRequest;
import org.amoscoats.recipemanager.dto.RecipeResponse;
import org.amoscoats.recipemanager.entity.Recipe;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/** MapStruct mapper for Recipe entity and DTOs. */
@Mapper(componentModel = "spring")
public interface RecipeMapper {

  /**
   * Converts Recipe entity to RecipeResponse DTO.
   *
   * @param recipe the recipe entity
   * @return the recipe response DTO
   */
  RecipeResponse toResponse(Recipe recipe);

  /**
   * Converts RecipeRequest DTO to Recipe entity.
   *
   * @param request the recipe request DTO
   * @return the recipe entity
   */
  @Mapping(target = "id", ignore = true)
  Recipe toEntity(RecipeRequest request);

  /**
   * Updates an existing Recipe entity from RecipeRequest DTO.
   *
   * @param request the recipe request DTO
   * @param recipe the existing recipe entity to update
   */
  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntity(RecipeRequest request, @MappingTarget Recipe recipe);
}

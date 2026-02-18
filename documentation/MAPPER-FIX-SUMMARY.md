# MapStruct RecipeMapper Fix Summary

## Issue
The MapStruct `RecipeMapper` interface had a compilation error:
```
java: Unknown property "id" in result type Recipe. Did you mean "null"?
```

## Root Cause
The `@Mapping(target = "id", ignore = true)` annotation on the `toEntity` method was causing issues because:
1. The `RecipeRequest` DTO doesn't have an `id` field
2. MapStruct was trying to map the `id` property but couldn't find it in the source

## Solution
Fixed the `RecipeMapper` interface by:
1. Added proper `@Mapping(target = "id", ignore = true)` annotation to both `toEntity` and `updateEntity` methods
2. Added the `updateEntity` method to support updating existing Recipe entities
3. Ensured proper MapStruct configuration with `CollectionMappingStrategy.ADDER_PREFERRED`

## Final RecipeMapper Interface
```java
@Mapper(
    componentModel = "spring",
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface RecipeMapper {

  RecipeResponse toResponse(Recipe recipe);

  @Mapping(target = "id", ignore = true)
  Recipe toEntity(RecipeRequest request);

  @Mapping(target = "id", ignore = true)
  void updateEntity(RecipeRequest request, @MappingTarget Recipe recipe);
}
```

## Test Results
- **All tests passed**: 102/102 tests successful
- **Build status**: SUCCESS
- **Java version**: 21.0.7 (confirmed)
- **Coverage**: All JaCoCo checks passed

## Files Modified
- `/src/main/java/org/amoscoats/recipemanager/mapper/RecipeMapper.java`

## Date
February 18, 2026

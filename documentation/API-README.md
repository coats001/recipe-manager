# Recipe Manager REST API

A Spring Boot application for managing favorite recipes with advanced filtering capabilities.

## Features

- ✅ Create, Read, Update, Delete (CRUD) operations for recipes
- ✅ Filter recipes by vegetarian status
- ✅ Filter recipes by number of servings
- ✅ Include specific ingredients in search
- ✅ Exclude specific ingredients from search
- ✅ Search text within cooking instructions
- ✅ Combine multiple filters for complex queries
- ✅ Input validation
- ✅ PostgreSQL database with Flyway migrations
- ✅ MapStruct for DTO mapping

## Technology Stack

- **Java 21**
- **Spring Boot 4.0.2**
- **Spring Data JPA** with Hibernate
- **PostgreSQL 17.8**
- **Flyway** for database migrations
- **MapStruct** for object mapping
- **Lombok** for boilerplate reduction
- **Maven** for dependency management

## API Endpoints

### Base URL
```
http://localhost:8080/api/recipes
```

### 1. Create a Recipe
**POST** `/api/recipes`

**Request Body:**
```json
{
  "name": "Vegetarian Pasta",
  "vegetarian": true,
  "servings": 4,
  "instructions": "Boil pasta. Add tomato sauce and vegetables. Bake in oven for 20 minutes.",
  "ingredients": ["pasta", "tomato sauce", "bell peppers", "onions", "garlic"]
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "name": "Vegetarian Pasta",
  "vegetarian": true,
  "servings": 4,
  "instructions": "Boil pasta. Add tomato sauce and vegetables. Bake in oven for 20 minutes.",
  "ingredients": ["pasta", "tomato sauce", "bell peppers", "onions", "garlic"]
}
```

### 2. Get All Recipes
**GET** `/api/recipes`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Vegetarian Pasta",
    "vegetarian": true,
    "servings": 4,
    "instructions": "...",
    "ingredients": ["..."]
  }
]
```

### 3. Get Recipe by ID
**GET** `/api/recipes/{id}`

**Response:** `200 OK` or `404 Not Found`

### 4. Update a Recipe
**PUT** `/api/recipes/{id}`

**Request Body:**
```json
{
  "name": "Vegetarian Pasta Updated",
  "vegetarian": true,
  "servings": 6,
  "instructions": "Updated instructions...",
  "ingredients": ["pasta", "tomato sauce", "mozzarella"]
}
```

**Response:** `200 OK`

### 5. Delete a Recipe
**DELETE** `/api/recipes/{id}`

**Response:** `204 No Content` or `404 Not Found`

## Filtering Recipes

All filters can be combined in a single request!

### Filter by Vegetarian Status
**GET** `/api/recipes?vegetarian=true`

Returns only vegetarian recipes.

### Filter by Servings
**GET** `/api/recipes?servings=4`

Returns recipes for exactly 4 servings.

### Filter by Including Ingredients
**GET** `/api/recipes?includeIngredients=potatoes`

Returns recipes that contain potatoes.

**Multiple ingredients:**
**GET** `/api/recipes?includeIngredients=potatoes,onions`

### Filter by Excluding Ingredients
**GET** `/api/recipes?excludeIngredients=salmon`

Returns recipes that do NOT contain salmon.

**Multiple ingredients:**
**GET** `/api/recipes?excludeIngredients=salmon,chicken`

### Search in Instructions
**GET** `/api/recipes?searchText=oven`

Returns recipes that mention "oven" in the cooking instructions.

### Complex Combined Filters

**Example 1:** All vegetarian recipes for 4 people with potatoes
```
GET /api/recipes?vegetarian=true&servings=4&includeIngredients=potatoes
```

**Example 2:** Recipes without salmon but mentioning "oven" in instructions
```
GET /api/recipes?excludeIngredients=salmon&searchText=oven
```

**Example 3:** Non-vegetarian recipes for 2 servings with chicken, excluding dairy
```
GET /api/recipes?vegetarian=false&servings=2&includeIngredients=chicken&excludeIngredients=milk,cheese,cream
```

## Database Schema

### recipes table
| Column       | Type    | Constraints |
|--------------|---------|-------------|
| id           | BIGSERIAL | PRIMARY KEY |
| name         | TEXT    | NOT NULL    |
| vegetarian   | BOOLEAN | NOT NULL    |
| servings     | INTEGER | NOT NULL    |
| instructions | TEXT    | NOT NULL    |

### recipe_ingredients table
| Column       | Type   | Constraints |
|--------------|--------|-------------|
| recipe_id    | BIGINT | FOREIGN KEY → recipes(id), NOT NULL |
| ingredient   | TEXT   | NOT NULL    |
| PRIMARY KEY  | (recipe_id, ingredient) |

## Running the Application

### Prerequisites
- Java 21
- PostgreSQL 17.8 running on localhost:5432
- Database named "recipes" with user "recipes"

### Start the Application
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### Run API Tests
```bash
./test-api.sh
```

This will run a comprehensive test suite demonstrating all API features.

## Project Structure

```
src/main/java/org/amoscoats/recipemanager/
├── controller/
│   └── RecipeController.java          # REST endpoints
├── dto/
│   ├── RecipeRequest.java             # Request DTO with validation
│   └── RecipeResponse.java            # Response DTO
├── entity/
│   └── Recipe.java                    # JPA entity
├── exception/
│   └── GlobalExceptionHandler.java    # Centralized error handling
├── mapper/
│   └── RecipeMapper.java              # MapStruct mapper interface
├── repository/
│   └── RecipeRepository.java          # Spring Data JPA repository
├── service/
│   └── RecipeService.java             # Business logic
└── specification/
    └── RecipeSpecification.java       # JPA Criteria API for filtering
```

## Error Handling

### Validation Errors (400 Bad Request)
```json
{
  "status": 400,
  "errors": {
    "name": "Recipe name is required",
    "servings": "Servings must be at least 1"
  },
  "timestamp": "2026-02-17T19:00:00"
}
```

### Not Found (404)
```json
{
  "status": 404,
  "message": "Recipe not found with id: 99",
  "timestamp": "2026-02-17T19:00:00"
}
```

## Configuration

See `src/main/resources/application.yml` for configuration options:
- Database connection settings
- JPA/Hibernate settings
- Flyway migration settings
- Actuator endpoints

## Health Check

**GET** `/actuator/health`

Returns the application health status.

## Notes

- All ingredient and instruction searches are case-insensitive
- Multiple ingredient filters use partial matching (e.g., "potat" matches "potatoes")
- Recipe IDs are auto-generated sequences
- Deleting a recipe also removes all its ingredients (cascade delete)
- Empty ingredient sets are not allowed (validation)

## Future Enhancements

- Pagination for large result sets
- Sorting options (by name, servings, etc.)
- Recipe ratings and comments
- Recipe categories/tags
- Cooking time tracking
- Nutritional information

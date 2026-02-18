# Recipe Manager API - Quick Reference

## 🚀 Quick Start

```bash
# Start the application
./mvnw spring-boot:run

# Test all features
./test-api.sh
```

## 📍 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/recipes` | Create a new recipe |
| GET    | `/api/recipes` | Get all recipes or filter |
| GET    | `/api/recipes/{id}` | Get recipe by ID |
| PUT    | `/api/recipes/{id}` | Update a recipe |
| DELETE | `/api/recipes/{id}` | Delete a recipe |

## 🔍 Filter Examples (Copy & Paste Ready)

### Basic Filters
```bash
# All vegetarian recipes
curl "http://localhost:8080/api/recipes?vegetarian=true" | jq '.'

# Recipes for 4 servings
curl "http://localhost:8080/api/recipes?servings=4" | jq '.'

# Recipes with potatoes
curl "http://localhost:8080/api/recipes?includeIngredients=potatoes" | jq '.'

# Recipes without salmon
curl "http://localhost:8080/api/recipes?excludeIngredients=salmon" | jq '.'

# Recipes mentioning "oven" in instructions
curl "http://localhost:8080/api/recipes?searchText=oven" | jq '.'
```

### Combined Filters
```bash
# Vegetarian recipes for 4 people with potatoes
curl "http://localhost:8080/api/recipes?vegetarian=true&servings=4&includeIngredients=potatoes" | jq '.'

# Recipes without salmon but mentioning "oven"
curl "http://localhost:8080/api/recipes?excludeIngredients=salmon&searchText=oven" | jq '.'

# Non-vegetarian recipes for 2, with chicken, without dairy
curl "http://localhost:8080/api/recipes?vegetarian=false&servings=2&includeIngredients=chicken&excludeIngredients=milk,cheese,cream" | jq '.'
```

## 📝 CRUD Examples

### Create Recipe
```bash
curl -X POST "http://localhost:8080/api/recipes" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Vegetarian Pasta",
    "vegetarian": true,
    "servings": 4,
    "instructions": "Boil pasta. Add sauce. Bake in oven.",
    "ingredients": ["pasta", "tomato sauce", "garlic"]
  }' | jq '.'
```

### Get All Recipes
```bash
curl "http://localhost:8080/api/recipes" | jq '.'
```

### Get Recipe by ID
```bash
curl "http://localhost:8080/api/recipes/1" | jq '.'
```

### Update Recipe
```bash
curl -X PUT "http://localhost:8080/api/recipes/1" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Pasta",
    "vegetarian": true,
    "servings": 6,
    "instructions": "New instructions...",
    "ingredients": ["pasta", "sauce", "cheese"]
  }' | jq '.'
```

### Delete Recipe
```bash
curl -X DELETE "http://localhost:8080/api/recipes/1" -w "\nHTTP: %{http_code}\n"
```

## ✅ All Requirements Met

- [x] Add recipes
- [x] Remove recipes  
- [x] Fetch recipes
- [x] Filter by vegetarian status
- [x] Filter by number of servings
- [x] Include specific ingredients
- [x] Exclude specific ingredients
- [x] Search text in instructions
- [x] Combine multiple filters

## 🏗️ Project Structure

```
Entity → Repository → Service → Controller
                        ↓
                   Specification (for filtering)
```

## 📚 Documentation

- `API-README.md` - Complete API documentation
- `IMPLEMENTATION-SUMMARY.md` - Technical implementation details
- `test-api.sh` - Comprehensive test script

## 🔗 Useful Links

- Application: http://localhost:8080
- Health Check: http://localhost:8080/actuator/health
- API Base: http://localhost:8080/api/recipes

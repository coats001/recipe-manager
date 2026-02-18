# OpenAPI/Swagger - Quick Reference

## 🚀 Access Points

### Swagger UI (Interactive Documentation)
```
http://localhost:8080/swagger-ui.html
```
**Use this for**: Testing APIs, viewing documentation, trying endpoints

### OpenAPI Spec (JSON)
```
http://localhost:8080/api-docs
```
**Use this for**: Importing to Postman, generating clients

### OpenAPI Spec (YAML)
```
http://localhost:8080/api-docs.yaml
```
**Use this for**: Version control, API gateways, human-readable spec

## 📋 Quick Commands

### Start Application
```bash
./mvnw spring-boot:run
```

### Export OpenAPI Spec
```bash
# Save as JSON
curl http://localhost:8080/api-docs > recipe-api.json

# Save as YAML  
curl http://localhost:8080/api-docs.yaml > recipe-api.yaml
```

### Import to Postman
1. Open Postman
2. Import → Link → Paste: `http://localhost:8080/api-docs`
3. Done!

## 🎯 Available Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/recipes` | Create new recipe |
| GET | `/api/recipes` | Get all recipes (with filters) |
| GET | `/api/recipes/{id}` | Get recipe by ID |
| PUT | `/api/recipes/{id}` | Update recipe |
| DELETE | `/api/recipes/{id}` | Delete recipe |

## 🔍 Filter Parameters

| Parameter | Type | Example | Description |
|-----------|------|---------|-------------|
| `vegetarian` | Boolean | `true` | Filter by vegetarian status |
| `servings` | Integer | `4` | Filter by servings |
| `includeIngredients` | String | `potatoes,onions` | Must include ingredients |
| `excludeIngredients` | String | `salmon,chicken` | Must exclude ingredients |
| `searchText` | String | `oven` | Search in instructions |

## 📝 Example Requests

### Create Recipe
```bash
curl -X POST http://localhost:8080/api/recipes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Vegetarian Pasta",
    "vegetarian": true,
    "servings": 4,
    "instructions": "Boil pasta. Add sauce.",
    "ingredients": ["pasta", "tomato sauce"]
  }'
```

### Get All Recipes
```bash
curl http://localhost:8080/api/recipes
```

### Filter Recipes
```bash
# Vegetarian recipes for 4 people
curl "http://localhost:8080/api/recipes?vegetarian=true&servings=4"

# Recipes with potatoes, no salmon
curl "http://localhost:8080/api/recipes?includeIngredients=potatoes&excludeIngredients=salmon"

# Recipes mentioning "oven"
curl "http://localhost:8080/api/recipes?searchText=oven"
```

## 🎨 Using Swagger UI

1. **Navigate** to http://localhost:8080/swagger-ui.html
2. **Click** on any endpoint to expand
3. **Click** "Try it out" button
4. **Fill in** parameters/request body
5. **Click** "Execute"
6. **View** response

## 📦 Files Created/Modified

### Added
- ✅ `OpenAPIConfig.java` - OpenAPI configuration
- ✅ `OPENAPI-SETUP-GUIDE.md` - Complete guide
- ✅ `OPENAPI-QUICK-REFERENCE.md` - This file

### Modified
- ✅ `pom.xml` - Added springdoc dependency
- ✅ `application.yml` - Added SpringDoc config
- ✅ `RecipeController.java` - Added OpenAPI annotations
- ✅ `RecipeRequest.java` - Added Schema annotations
- ✅ `RecipeResponse.java` - Added Schema annotations

## 🔧 Configuration

### Disable in Production
Add to `application-prod.yml`:
```yaml
springdoc:
  swagger-ui:
    enabled: false
```

### Change Swagger UI Path
```yaml
springdoc:
  swagger-ui:
    path: /docs  # Custom path
```

## 🛠️ Integration Tools

### Postman
Import URL: `http://localhost:8080/api-docs`

### Insomnia
Download: `http://localhost:8080/api-docs.yaml`

### Generate Client SDK
```bash
# Java
openapi-generator-cli generate -i recipe-api.json -g java

# TypeScript
openapi-generator-cli generate -i recipe-api.json -g typescript-axios

# Python
openapi-generator-cli generate -i recipe-api.json -g python
```

## ✅ Checklist

- [ ] Start application: `./mvnw spring-boot:run`
- [ ] Open Swagger UI: http://localhost:8080/swagger-ui.html
- [ ] Try "GET /api/recipes"
- [ ] Test "POST /api/recipes"
- [ ] Export OpenAPI spec
- [ ] Import to Postman/Insomnia

## 📚 Learn More

See `OPENAPI-SETUP-GUIDE.md` for complete documentation.

---

**Your API documentation is live at http://localhost:8080/swagger-ui.html** 🎉

# OpenAPI/Swagger Documentation Setup - Complete Guide

## ✅ What Was Configured

Your Recipe Manager API now has **automatic OpenAPI specification generation** using SpringDoc OpenAPI! This provides interactive API documentation that's automatically generated from your code.

## 🎯 What You Get

### 1. **Interactive Swagger UI**
- **URL**: http://localhost:8080/swagger-ui.html
- Try out API endpoints directly from your browser
- See request/response examples
- Test authentication and headers
- View all available endpoints

### 2. **OpenAPI Specification (JSON)**
- **URL**: http://localhost:8080/api-docs
- Machine-readable API specification
- OpenAPI 3.0 format
- Can be imported into tools like Postman, Insomnia
- Can generate client SDKs

### 3. **OpenAPI Specification (YAML)**
- **URL**: http://localhost:8080/api-docs.yaml
- Human-readable YAML format
- Can be used with API gateways
- Can be version controlled

## 📦 What Was Added

### 1. Maven Dependency
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.7.0</version>
</dependency>
```

### 2. Configuration Class
**File**: `OpenAPIConfig.java`
- API metadata (title, version, description)
- Contact information
- License information
- Server definitions (dev, prod)

### 3. Controller Annotations
**Enhanced**: `RecipeController.java`
- `@Tag` - Groups endpoints
- `@Operation` - Describes operations
- `@ApiResponses` - Documents responses
- `@Parameter` - Describes parameters
- `@Schema` - Defines data models

### 4. DTO Annotations
**Enhanced**: `RecipeRequest.java` and `RecipeResponse.java`
- `@Schema` - Field descriptions and examples
- Automatic validation documentation
- Example values for testing

### 5. Application Configuration
**Updated**: `application.yml`
- SpringDoc paths configured
- Swagger UI customization
- API docs settings

## 🚀 Quick Start

### Step 1: Start the Application
```bash
cd /Users/coats/Dropbox/Bedrijf/2026/ABNAMRO/recipe-manager
./mvnw spring-boot:run
```

### Step 2: Access Swagger UI
Open your browser and navigate to:
```
http://localhost:8080/swagger-ui.html
```

### Step 3: Try It Out!
1. Click on any endpoint (e.g., "GET /api/recipes")
2. Click "Try it out"
3. Fill in parameters (if any)
4. Click "Execute"
5. See the response!

## 📊 Available Endpoints

All documented at: http://localhost:8080/swagger-ui.html

### Recipe Management
- **POST /api/recipes** - Create new recipe
- **GET /api/recipes** - Get all recipes or filter
- **GET /api/recipes/{id}** - Get recipe by ID
- **PUT /api/recipes/{id}** - Update recipe
- **DELETE /api/recipes/{id}** - Delete recipe

### API Documentation
- **GET /api-docs** - OpenAPI spec (JSON)
- **GET /api-docs.yaml** - OpenAPI spec (YAML)
- **GET /swagger-ui.html** - Interactive UI

## 🎨 Swagger UI Features

### 1. **Endpoint Groups**
- Recipes grouped under "Recipe Management"
- Clear organization of API operations

### 2. **Try It Out**
- Execute real API calls
- See request examples
- View response codes and bodies

### 3. **Request Samples**
Auto-generated examples:
```json
{
  "name": "Vegetarian Pasta",
  "vegetarian": true,
  "servings": 4,
  "instructions": "Boil pasta. Add sauce. Bake in oven.",
  "ingredients": ["pasta", "tomato sauce", "garlic"]
}
```

### 4. **Response Samples**
Shows all possible responses:
- 200 OK
- 201 Created
- 204 No Content
- 400 Bad Request
- 404 Not Found

### 5. **Filtering Documentation**
Complete documentation of query parameters:
- `vegetarian` - Boolean filter
- `servings` - Integer filter
- `includeIngredients` - Comma-separated list
- `excludeIngredients` - Comma-separated list
- `searchText` - Text search

## 💡 Using the OpenAPI Spec

### Import into Postman
1. Download spec: http://localhost:8080/api-docs
2. Open Postman
3. File → Import → Paste URL or upload JSON
4. All endpoints imported automatically!

### Import into Insomnia
1. Download spec: http://localhost:8080/api-docs.yaml
2. Open Insomnia
3. Create → Import From → File
4. Select the YAML file

### Generate Client SDK
```bash
# Download the spec
curl http://localhost:8080/api-docs > openapi.json

# Generate Java client
openapi-generator-cli generate -i openapi.json -g java -o ./client-java

# Generate TypeScript client
openapi-generator-cli generate -i openapi.json -g typescript-axios -o ./client-ts
```

## ⚙️ Configuration

### SpringDoc Settings (application.yml)

```yaml
springdoc:
  api-docs:
    path: /api-docs              # OpenAPI spec endpoint
    enabled: true                # Enable API docs
  swagger-ui:
    path: /swagger-ui.html       # Swagger UI path
    enabled: true                # Enable Swagger UI
    operationsSorter: method     # Sort by HTTP method
    tagsSorter: alpha            # Sort tags alphabetically
    tryItOutEnabled: true        # Enable "Try it out"
    filter: true                 # Enable filter box
    displayRequestDuration: true # Show request time
```

### Customize API Info (OpenAPIConfig.java)

```java
Info info = new Info()
    .title("Recipe Manager API")
    .version("1.0.0")
    .description("Your API description")
    .contact(contact)
    .license(license);
```

## 📚 Annotation Reference

### Controller Level
```java
@Tag(name = "Recipe Management", description = "APIs for recipes")
```

### Method Level
```java
@Operation(
    summary = "Create recipe",
    description = "Creates a new recipe with details"
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Created"),
    @ApiResponse(responseCode = "400", description = "Bad Request")
})
```

### Parameter Level
```java
@Parameter(
    description = "Recipe ID",
    required = true,
    example = "1"
)
```

### DTO Level
```java
@Schema(description = "Recipe creation request")
public class RecipeRequest {
    @Schema(description = "Recipe name", example = "Pasta")
    private String name;
}
```

## 🎯 Best Practices

### 1. Document Everything
- Add `@Operation` to all endpoints
- Describe all parameters with `@Parameter`
- Document all response codes with `@ApiResponses`

### 2. Provide Examples
- Use `example` attribute in `@Schema`
- Show realistic data
- Include edge cases

### 3. Group Endpoints
- Use `@Tag` to organize endpoints
- Group related operations
- Use clear, descriptive names

### 4. Keep It Updated
- OpenAPI spec updates automatically
- No manual YAML editing needed
- Documentation stays in sync with code

## 🔧 Customization

### Change UI Theme
```yaml
springdoc:
  swagger-ui:
    syntaxHighlight:
      theme: monokai  # or 'agate', 'tomorrow-night'
```

### Disable in Production
```yaml
spring:
  profiles: prod
springdoc:
  swagger-ui:
    enabled: false  # Disable Swagger UI in production
  api-docs:
    enabled: true   # Keep API docs for tools
```

### Custom Security
```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes("bearer-jwt",
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
}
```

## 📊 OpenAPI Spec Example

Your API generates a complete OpenAPI specification:

```yaml
openapi: 3.0.1
info:
  title: Recipe Manager API
  description: REST API for managing favorite recipes
  version: 1.0.0
  contact:
    name: Recipe Manager Support
    email: support@recipe-manager.example.com
servers:
  - url: http://localhost:8080
    description: Development Server
paths:
  /api/recipes:
    get:
      tags:
        - Recipe Management
      summary: Get all recipes or filter recipes
      operationId: getRecipes
      parameters:
        - name: vegetarian
          in: query
          description: Filter by vegetarian status
          required: false
          schema:
            type: boolean
      # ... more details
```

## 🚀 Advanced Features

### 1. Export OpenAPI Spec
```bash
# Save as JSON
curl http://localhost:8080/api-docs > recipe-api.json

# Save as YAML
curl http://localhost:8080/api-docs.yaml > recipe-api.yaml
```

### 2. Generate Documentation
```bash
# Generate HTML docs with Redoc
npx @redocly/cli build-docs recipe-api.yaml

# Generate Markdown
openapi-generator-cli generate -i recipe-api.json -g markdown
```

### 3. API Contract Testing
```bash
# Validate spec
swagger-cli validate recipe-api.yaml

# Run contract tests
dredd recipe-api.yaml http://localhost:8080
```

### 4. Mock Server
```bash
# Start mock server from spec
prism mock recipe-api.yaml
```

## 📋 Testing with Swagger UI

### Test POST /api/recipes
1. Click on "POST /api/recipes"
2. Click "Try it out"
3. Modify the example JSON:
```json
{
  "name": "My Test Recipe",
  "vegetarian": true,
  "servings": 2,
  "instructions": "Test instructions",
  "ingredients": ["test1", "test2"]
}
```
4. Click "Execute"
5. See response: 201 Created

### Test GET with Filters
1. Click on "GET /api/recipes"
2. Click "Try it out"
3. Set parameters:
   - vegetarian: true
   - servings: 4
   - includeIngredients: potatoes
4. Click "Execute"
5. See filtered results

## 🆘 Troubleshooting

### Swagger UI Not Loading?
**Check**:
- Application is running: http://localhost:8080
- SpringDoc dependency is in pom.xml
- URL is correct: http://localhost:8080/swagger-ui.html

### API Docs Not Showing?
**Verify**:
```yaml
springdoc:
  api-docs:
    enabled: true
```

### Endpoints Missing?
**Ensure**:
- Controller has `@RestController`
- Methods have `@RequestMapping` or variants
- Class is in component scan package

## 🎉 Summary

Your Recipe Manager API now has:

✅ **Interactive Swagger UI** at http://localhost:8080/swagger-ui.html  
✅ **OpenAPI Spec (JSON)** at http://localhost:8080/api-docs  
✅ **OpenAPI Spec (YAML)** at http://localhost:8080/api-docs.yaml  
✅ **Automatic documentation** from code annotations  
✅ **Try-it-out functionality** for testing  
✅ **Request/response examples** for all endpoints  
✅ **Complete API documentation** always in sync  
✅ **Tool integration** (Postman, Insomnia, etc.)  
✅ **Client SDK generation** capability  

## 📚 Additional Resources

- **SpringDoc Docs**: https://springdoc.org/
- **OpenAPI Spec**: https://swagger.io/specification/
- **Swagger UI**: https://swagger.io/tools/swagger-ui/
- **OpenAPI Generator**: https://openapi-generator.tech/

---

**Start the app and visit http://localhost:8080/swagger-ui.html to see your beautiful API documentation!** 🚀

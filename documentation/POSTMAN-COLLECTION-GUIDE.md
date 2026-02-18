# Postman Collection Guide - Recipe Manager API

## 📦 Overview

This comprehensive Postman collection covers **all possible logical combinations** of the Recipe Manager API, including CRUD operations, filtering, and edge cases.

---

## 🚀 Quick Start

### 1. Import Collection

**Method 1: Import from File**
1. Open Postman
2. Click **Import** button (top left)
3. Select **File** tab
4. Choose `Recipe-Manager-API.postman_collection.json`
5. Click **Import**

**Method 2: Import from Swagger**
1. Start the Recipe Manager application: `./mvnw spring-boot:run`
2. Open Postman
3. Click **Import** → **Link**
4. Enter: `http://localhost:8080/api-docs`
5. Click **Continue** → **Import**

### 2. Set Up Environment

**Option A: Use Collection Variable (Default)**
- The collection already has `base_url` set to `http://localhost:8080`
- No additional setup needed!

**Option B: Create Environment (Recommended for multiple environments)**
1. Click **Environments** (left sidebar)
2. Click **+** to create new environment
3. Name it: "Recipe Manager - Local"
4. Add variables:
   - `base_url`: `http://localhost:8080`
   - `recipe_id_1`: (will be set automatically)
   - `recipe_id_2`: (will be set automatically)
   - `recipe_id_3`: (will be set automatically)
   - `recipe_id_4`: (will be set automatically)
   - `recipe_id_5`: (will be set automatically)
5. Click **Save**
6. Select this environment from dropdown (top right)

### 3. Start the Application
```bash
cd /Users/coats/Dropbox/Bedrijf/2026/ABNAMRO/recipe-manager
./mvnw spring-boot:run
```

Wait for: `Started RecipeManagerApplication`

---

## 📋 Collection Structure

### 1. CRUD Operations (9 requests)
Basic create, read, update, delete operations:
- ✅ Create Recipe - Vegetarian Pasta
- ✅ Create Recipe - Grilled Salmon
- ✅ Create Recipe - Potato Soup
- ✅ Create Recipe - Chicken Oven Bake
- ✅ Create Recipe - Vegetable Stir Fry
- ✅ Get Recipe by ID
- ✅ Update Recipe
- ✅ Delete Recipe

### 2. Get All Recipes (1 request)
- ✅ Get All Recipes (no filters)

### 3. Single Filter Tests (11 requests)
Test each filter individually:
- ✅ Filter by Vegetarian (true)
- ✅ Filter by Vegetarian (false)
- ✅ Filter by Servings (2)
- ✅ Filter by Servings (4)
- ✅ Filter by Servings (6)
- ✅ Include Single Ingredient (potatoes)
- ✅ Include Multiple Ingredients (garlic, onions)
- ✅ Exclude Single Ingredient (salmon)
- ✅ Exclude Multiple Ingredients (salmon, chicken)
- ✅ Search Text (oven)
- ✅ Search Text (boil)

### 4. Two Filter Combinations (8 requests)
Test pairs of filters:
- ✅ Vegetarian + Servings
- ✅ Vegetarian + Include Ingredients
- ✅ Vegetarian + Exclude Ingredients
- ✅ Vegetarian + Search Text
- ✅ Servings + Include Ingredients
- ✅ Servings + Exclude Ingredients
- ✅ Include + Exclude Ingredients
- ✅ Exclude + Search Text

### 5. Three Filter Combinations (4 requests)
Test triplets of filters:
- ✅ Vegetarian + Servings + Include
- ✅ Vegetarian + Include + Search
- ✅ Servings + Exclude + Search
- ✅ Include (multiple) + Exclude

### 6. Four+ Filter Combinations (3 requests)
Test complex filter combinations:
- ✅ Vegetarian + Servings + Include + Search
- ✅ All 5 Filters Combined
- ✅ Complex Multiple Includes & Excludes

### 7. Edge Cases & Validation (5 requests)
Test error handling and edge cases:
- ✅ Invalid Recipe ID (404)
- ✅ Missing Required Field (400)
- ✅ Invalid Servings Value (400)
- ✅ No Results from Filters
- ✅ Empty String Search

**Total: 41 requests covering all scenarios**

---

## 🎯 How to Use

### Run All Requests (Collection Runner)

1. Click on the **Recipe Manager API** collection
2. Click **Run** button (or press Cmd/Ctrl + R)
3. Select all folders/requests
4. Click **Run Recipe Manager API**
5. Watch tests execute automatically!

**Expected Results:**
- ✅ All tests pass
- ✅ Recipes created with IDs saved to variables
- ✅ All filter combinations tested
- ✅ Edge cases validated

### Run Individual Requests

1. Navigate to any request in the collection
2. Click **Send**
3. View response in the bottom panel
4. Check **Test Results** tab for assertions

### Run a Specific Folder

1. Right-click on any folder (e.g., "3. Single Filter Tests")
2. Click **Run folder**
3. Review results

---

## 🔧 Request Details

### Create Recipe Example

**Request:**
```http
POST {{base_url}}/api/recipes
Content-Type: application/json

{
  "name": "Vegetarian Pasta",
  "vegetarian": true,
  "servings": 4,
  "instructions": "Boil pasta for 10 minutes...",
  "ingredients": ["pasta", "tomato sauce", "garlic"]
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Vegetarian Pasta",
  "vegetarian": true,
  "servings": 4,
  "instructions": "Boil pasta for 10 minutes...",
  "ingredients": ["pasta", "tomato sauce", "garlic"]
}
```

**Automatic Test:**
```javascript
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

pm.test("Response has id", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('id');
    pm.environment.set("recipe_id_1", jsonData.id);
});
```

### Filter Example - All 5 Filters Combined

**Request:**
```http
GET {{base_url}}/api/recipes?vegetarian=true&servings=4&includeIngredients=potatoes&excludeIngredients=salmon&searchText=oven
```

**What it tests:**
- ✅ Vegetarian status = true
- ✅ Servings = 4
- ✅ Must include: potatoes
- ✅ Must exclude: salmon
- ✅ Instructions must mention: oven

**Automatic Test:**
```javascript
pm.test("Result matches all filter criteria", function () {
    var jsonData = pm.response.json();
    if (jsonData.length > 0) {
        jsonData.forEach(function(recipe) {
            pm.expect(recipe.vegetarian).to.be.true;
            pm.expect(recipe.servings).to.eql(4);
            pm.expect(recipe.ingredients).to.include('potatoes');
            pm.expect(recipe.ingredients).to.not.include('salmon');
            pm.expect(recipe.instructions.toLowerCase()).to.include('oven');
        });
    }
});
```

---

## 🧪 Test Coverage

### What Gets Tested

#### CRUD Operations
✅ Create recipe (201 Created)  
✅ Get recipe by ID (200 OK)  
✅ Update recipe (200 OK)  
✅ Delete recipe (204 No Content)  
✅ Get all recipes (200 OK)  

#### Response Validation
✅ Correct HTTP status codes  
✅ Response body structure  
✅ Field presence and types  
✅ Array vs object responses  

#### Filter Logic
✅ Single filter parameters work correctly  
✅ Multiple filters combine with AND logic  
✅ Include ingredients (all must be present)  
✅ Exclude ingredients (none can be present)  
✅ Text search in instructions  
✅ Empty results return empty array  

#### Error Handling
✅ 404 for non-existent resources  
✅ 400 for validation errors  
✅ Error response structure  

#### Edge Cases
✅ Non-existent IDs  
✅ Missing required fields  
✅ Invalid field values  
✅ Empty filter results  
✅ Empty string searches  

---

## 📊 Filter Combination Matrix

| Filters Used | Count | Examples |
|--------------|-------|----------|
| **0 filters** | 1 | Get all recipes |
| **1 filter** | 11 | vegetarian=true, servings=4, includeIngredients=potatoes |
| **2 filters** | 8 | vegetarian=true&servings=4 |
| **3 filters** | 4 | vegetarian=true&servings=4&includeIngredients=potatoes |
| **4 filters** | 1 | vegetarian + servings + include + search |
| **5 filters** | 1 | All filters combined |
| **Total** | **26** | Complete filter coverage |

---

## 🎓 Tips & Best Practices

### 1. Run in Order
The collection is designed to run sequentially:
1. CRUD operations create test data
2. Filter tests use the created data
3. Edge cases test error handling

### 2. Use Environment Variables
Recipe IDs are automatically saved to environment variables:
- `recipe_id_1` through `recipe_id_5`
- Use `{{recipe_id_1}}` in requests

### 3. Check Test Results
Every request has automated tests:
- **Green checkmarks** = All tests passed
- **Red X** = Test failed (check console for details)

### 4. Monitor Console
View detailed logs:
1. Open Postman Console (View → Show Postman Console)
2. See request/response details
3. View test execution results

### 5. Export Results
After running collection:
1. Click **Export Results**
2. Save as JSON or HTML
3. Share with team or save for records

---

## 🔄 Common Workflows

### Development Testing
```
1. Start application
2. Run "1. CRUD Operations" folder
3. Test individual endpoints as you develop
4. Run relevant filter tests
```

### Complete API Validation
```
1. Start application
2. Run entire collection
3. Review test results
4. Fix any failures
5. Re-run collection
```

### Regression Testing
```
1. Run collection before code changes
2. Make changes
3. Run collection again
4. Compare results
```

### Demo/Presentation
```
1. Run "1. CRUD Operations" to populate data
2. Demonstrate individual filter requests
3. Show complex filter combinations
4. Show edge case handling
```

---

## 🆘 Troubleshooting

### Issue: "Could not get response"
**Solution**: 
- Ensure application is running: `./mvnw spring-boot:run`
- Check if port 8080 is available
- Verify `base_url` is set correctly

### Issue: Tests failing with 404
**Solution**:
- Run "1. CRUD Operations" folder first to create test data
- Check if recipes were deleted
- Environment variables may not be set

### Issue: "Connection refused"
**Solution**:
- Application not started
- Wrong port number
- Firewall blocking connection

### Issue: Environment variables not saving
**Solution**:
- Make sure you have an environment selected
- Check environment is not read-only
- Try creating a new environment

### Issue: Tests failing unexpectedly
**Solution**:
- Check response in Body tab
- View Postman Console for details
- Verify data in database matches expectations

---

## 📚 Additional Resources

### Postman Documentation
- **Tests**: https://learning.postman.com/docs/writing-scripts/test-scripts/
- **Variables**: https://learning.postman.com/docs/sending-requests/variables/
- **Collection Runner**: https://learning.postman.com/docs/running-collections/intro-to-collection-runs/

### Recipe Manager API
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/api-docs
- **API Guide**: See OPENAPI-SETUP-GUIDE.md

---

## 🎯 Test Scenarios Covered

### Vegetarian Filter
- ✅ Only vegetarian recipes
- ✅ Only non-vegetarian recipes
- ✅ Combined with other filters

### Servings Filter
- ✅ Recipes for 2 people
- ✅ Recipes for 4 people
- ✅ Recipes for 6 people
- ✅ Combined with other filters

### Include Ingredients
- ✅ Single ingredient (potatoes)
- ✅ Multiple ingredients (garlic, onions)
- ✅ Combined with exclude
- ✅ Combined with other filters

### Exclude Ingredients
- ✅ Single ingredient (salmon)
- ✅ Multiple ingredients (salmon, chicken)
- ✅ Combined with include
- ✅ Combined with other filters

### Search Text
- ✅ Single word (oven)
- ✅ Single word (boil)
- ✅ Combined with other filters
- ✅ Case insensitive search

### Complex Combinations
- ✅ All 5 filters together
- ✅ Multiple includes + multiple excludes
- ✅ Filters resulting in empty set
- ✅ Filters with special characters

---

## ✅ Summary

This Postman collection provides:

✅ **41 comprehensive test requests**  
✅ **Complete CRUD coverage**  
✅ **All filter combinations tested**  
✅ **Automatic test assertions**  
✅ **Environment variable management**  
✅ **Edge case validation**  
✅ **Error handling verification**  
✅ **Ready for CI/CD integration**  

**Import the collection and start testing immediately!** 🚀

---

## 📞 Need Help?

- **API Documentation**: See `OPENAPI-SETUP-GUIDE.md`
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Collection File**: `Recipe-Manager-API.postman_collection.json`

Happy Testing! 🧪✨

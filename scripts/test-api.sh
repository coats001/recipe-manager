#!/bin/bash

# Recipe Manager API Test Script
echo "======================================"
echo "Recipe Manager API Testing"
echo "======================================"
echo ""

# Base URL
BASE_URL="http://localhost:8080/api/recipes"

echo "1. Create a vegetarian recipe (Vegetarian Pasta)"
echo "--------------------------------------"
RECIPE1=$(curl -s -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Vegetarian Pasta",
    "vegetarian": true,
    "servings": 4,
    "instructions": "Boil pasta. Add tomato sauce and vegetables. Bake in oven for 20 minutes.",
    "ingredients": ["pasta", "tomato sauce", "bell peppers", "onions", "garlic"]
  }')
echo "$RECIPE1" | jq '.'
RECIPE1_ID=$(echo "$RECIPE1" | jq -r '.id')
echo ""

echo "2. Create a non-vegetarian recipe (Salmon with Potatoes)"
echo "--------------------------------------"
RECIPE2=$(curl -s -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Salmon with Potatoes",
    "vegetarian": false,
    "servings": 2,
    "instructions": "Season salmon. Bake potatoes and salmon in oven at 180°C for 30 minutes.",
    "ingredients": ["salmon", "potatoes", "olive oil", "lemon", "dill"]
  }')
echo "$RECIPE2" | jq '.'
RECIPE2_ID=$(echo "$RECIPE2" | jq -r '.id')
echo ""

echo "3. Create another vegetarian recipe (Potato Soup)"
echo "--------------------------------------"
RECIPE3=$(curl -s -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Potato Soup",
    "vegetarian": true,
    "servings": 4,
    "instructions": "Dice potatoes and cook on stovetop with vegetable broth for 25 minutes.",
    "ingredients": ["potatoes", "vegetable broth", "cream", "onions", "herbs"]
  }')
echo "$RECIPE3" | jq '.'
RECIPE3_ID=$(echo "$RECIPE3" | jq -r '.id')
echo ""

echo "4. Create a chicken recipe (Chicken Stir-Fry)"
echo "--------------------------------------"
RECIPE4=$(curl -s -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Chicken Stir-Fry",
    "vegetarian": false,
    "servings": 3,
    "instructions": "Cut chicken into pieces. Stir-fry chicken with vegetables in wok.",
    "ingredients": ["chicken", "broccoli", "carrots", "soy sauce", "ginger"]
  }')
echo "$RECIPE4" | jq '.'
RECIPE4_ID=$(echo "$RECIPE4" | jq -r '.id')
echo ""

echo "5. Get all recipes"
echo "--------------------------------------"
curl -s -X GET "$BASE_URL" | jq '.'
echo ""

echo "6. Filter: Get all vegetarian recipes"
echo "--------------------------------------"
curl -s -X GET "$BASE_URL?vegetarian=true" | jq '.'
echo ""

echo "7. Filter: Get recipes for 4 servings"
echo "--------------------------------------"
curl -s -X GET "$BASE_URL?servings=4" | jq '.'
echo ""

echo "8. Filter: Get recipes with potatoes"
echo "--------------------------------------"
curl -s -X GET "$BASE_URL?includeIngredients=potatoes" | jq '.'
echo ""

echo "9. Filter: Get recipes WITHOUT salmon"
echo "--------------------------------------"
curl -s -X GET "$BASE_URL?excludeIngredients=salmon" | jq '.'
echo ""

echo "10. Filter: Search for recipes mentioning 'oven' in instructions"
echo "--------------------------------------"
curl -s -X GET "$BASE_URL?searchText=oven" | jq '.'
echo ""

echo "11. Complex Filter: Vegetarian recipes for 4 people with potatoes"
echo "--------------------------------------"
curl -s -X GET "$BASE_URL?vegetarian=true&servings=4&includeIngredients=potatoes" | jq '.'
echo ""

echo "12. Complex Filter: Recipes without salmon but mentioning 'oven'"
echo "--------------------------------------"
curl -s -X GET "$BASE_URL?excludeIngredients=salmon&searchText=oven" | jq '.'
echo ""

echo "13. Get a single recipe by ID"
echo "--------------------------------------"
curl -s -X GET "$BASE_URL/$RECIPE1_ID" | jq '.'
echo ""

echo "14. Update a recipe"
echo "--------------------------------------"
curl -s -X PUT "$BASE_URL/$RECIPE1_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Vegetarian Pasta Updated",
    "vegetarian": true,
    "servings": 6,
    "instructions": "Boil pasta. Add tomato sauce and vegetables. Bake in oven for 25 minutes at 200°C.",
    "ingredients": ["pasta", "tomato sauce", "bell peppers", "onions", "garlic", "mozzarella"]
  }' | jq '.'
echo ""

echo "15. Delete a recipe"
echo "--------------------------------------"
curl -s -X DELETE "$BASE_URL/$RECIPE4_ID" -w "\nHTTP Status: %{http_code}\n"
echo ""

echo "16. Verify deletion - Get all recipes again"
echo "--------------------------------------"
curl -s -X GET "$BASE_URL" | jq '.'
echo ""

echo "======================================"
echo "Testing Complete!"
echo "======================================"

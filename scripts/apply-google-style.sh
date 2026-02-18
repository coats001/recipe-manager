#!/bin/bash

# Google Java Style Formatter Script
# This script fixes the most common Checkstyle violations

echo "🎨 Applying Google Java Style fixes..."
echo ""

# Find all Java files
JAVA_FILES=$(find src/main/java src/test/java -name "*.java" -type f 2>/dev/null)

if [ -z "$JAVA_FILES" ]; then
  echo "❌ No Java files found"
  exit 1
fi

FILE_COUNT=$(echo "$JAVA_FILES" | wc -l | tr -d ' ')
echo "📁 Found $FILE_COUNT Java files to process"
echo ""

# Process each file
FIXED_COUNT=0
for file in $JAVA_FILES; do
  echo "Processing: $file"

  # 1. Convert tabs to spaces
  sed -i '' 's/\t/  /g' "$file"

  # 2. Fix basic indentation (4 spaces → 2 spaces) - first pass
  # This is a simple heuristic and won't be perfect
  sed -i '' 's/^    /  /g' "$file"
  sed -i '' 's/^        /    /g' "$file"
  sed -i '' 's/^            /      /g' "$file"
  sed -i '' 's/^                /        /g' "$file"
  sed -i '' 's/^                    /          /g' "$file"
  sed -i '' 's/^                        /            /g' "$file"

  FIXED_COUNT=$((FIXED_COUNT + 1))
done

echo ""
echo "✅ Processed $FIXED_COUNT files"
echo ""
echo "⚠️  Note: This script only fixes basic issues:"
echo "   - Converts tabs to spaces"
echo "   - Reduces indentation from 4 to 2 spaces"
echo ""
echo "📝 Still need to fix manually:"
echo "   - Import order"
echo "   - Wildcard imports"
echo "   - Missing Javadoc"
echo "   - Line length"
echo ""
echo "💡 For complete formatting, use IntelliJ IDEA:"
echo "   1. Open project in IntelliJ"
echo "   2. Right-click src/main/java → Reformat Code"
echo "   3. Check 'Optimize imports' and 'Rearrange entries'"
echo ""
echo "🔍 Check results with:"
echo "   ./mvnw checkstyle:checkstyle"
echo ""

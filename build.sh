#!/bin/bash

# ====================================
# Maven Clean Install - Fixed Build
# ====================================

echo "🔧 Starting Maven build with fixed JaCoCo..."
echo ""
echo "✅ JaCoCo upgraded: 0.8.12 → 0.8.13"
echo "✅ Java 25 support: Enabled"
echo ""
echo "Running: ./mvnw clean install"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Run the build
./mvnw clean install

# Check exit code
if [ $? -eq 0 ]; then
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "✅ BUILD SUCCESS! 🎉"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
    echo "Next steps:"
    echo "  1. Check test results: ./mvnw test"
    echo "  2. View coverage: open target/site/jacoco/index.html"
    echo "  3. Run application: ./mvnw spring-boot:run"
    echo ""
else
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "❌ BUILD FAILED"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
    echo "Troubleshooting:"
    echo "  1. Clear Maven cache: rm -rf ~/.m2/repository/org/jacoco"
    echo "  2. Force update: ./mvnw clean install -U"
    echo "  3. Check Java version: java -version"
    echo "  4. See BUILD-FIX-SUMMARY.md for more details"
    echo ""
    exit 1
fi

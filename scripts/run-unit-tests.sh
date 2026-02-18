#!/bin/bash

echo "======================================"
echo "Recipe Manager - Unit Tests Runner"
echo "======================================"
echo ""

echo "Running all unit tests..."
echo ""

# Run unit tests (excluding integration tests)
./mvnw test -Dtest='!*IntegrationTest'

echo ""
echo "======================================"
echo "Test Execution Complete!"
echo "======================================"
echo ""

# Show test summary
echo "Test Summary:"
echo "-------------"
grep -r "Tests run:" target/surefire-reports/*.txt 2>/dev/null | tail -10

echo ""
echo "For detailed coverage report, run:"
echo "./mvnw test jacoco:report"
echo "Then open: target/site/jacoco/index.html"

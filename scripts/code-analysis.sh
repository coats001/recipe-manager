#!/bin/bash

echo "=========================================="
echo "  Static Code Analysis - Recipe Manager"
echo "=========================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print section header
print_section() {
    echo ""
    echo -e "${BLUE}=========================================="
    echo -e "  $1"
    echo -e "==========================================${NC}"
    echo ""
}

# Function to check command success
check_status() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ $1 completed successfully${NC}"
    else
        echo -e "${RED}✗ $1 failed${NC}"
        FAILURES=$((FAILURES + 1))
    fi
}

FAILURES=0

# Step 1: Compile code
print_section "Step 1: Compiling Code"
./mvnw clean compile
check_status "Compilation"

# Step 2: Run tests with coverage
print_section "Step 2: Running Tests with Coverage"
./mvnw test
check_status "Tests"

# Step 3: Generate JaCoCo coverage report
print_section "Step 3: Generating Coverage Report"
./mvnw jacoco:report
check_status "JaCoCo Coverage"

# Step 4: Run SpotBugs
print_section "Step 4: Running SpotBugs (Bug Detection)"
./mvnw spotbugs:check
check_status "SpotBugs"

# Step 5: Run PMD
print_section "Step 5: Running PMD (Code Quality)"
./mvnw pmd:check
check_status "PMD"

# Step 6: Run Checkstyle
print_section "Step 6: Running Checkstyle (Code Style)"
./mvnw checkstyle:check
check_status "Checkstyle"

# Step 7: Generate site reports
print_section "Step 7: Generating Site Reports"
./mvnw site -DskipTests=true
check_status "Site Generation"

# Summary
print_section "Analysis Summary"

if [ $FAILURES -eq 0 ]; then
    echo -e "${GREEN}✓ All checks passed successfully!${NC}"
else
    echo -e "${YELLOW}⚠ $FAILURES check(s) reported issues${NC}"
fi

echo ""
echo "Reports available at:"
echo -e "  ${BLUE}JaCoCo Coverage:${NC}    target/site/jacoco/index.html"
echo -e "  ${BLUE}SpotBugs:${NC}           target/spotbugs.html"
echo -e "  ${BLUE}PMD:${NC}                target/site/pmd.html"
echo -e "  ${BLUE}Checkstyle:${NC}         target/site/checkstyle.html"
echo -e "  ${BLUE}All Reports:${NC}        target/site/index.html"
echo ""

# Open reports in browser (macOS)
read -p "Open reports in browser? (y/n) " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    open target/site/index.html
fi

echo ""
echo -e "${GREEN}Analysis complete!${NC}"
echo "=========================================="

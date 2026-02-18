#!/bin/bash

echo "=========================================="
echo "  Security Testing - Recipe Manager"
echo "=========================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
MAGENTA='\033[0;35m'
NC='\033[0m' # No Color

# Function to print section header
print_section() {
    echo ""
    echo -e "${MAGENTA}=========================================="
    echo -e "  $1"
    echo -e "==========================================${NC}"
    echo ""
}

# Function to check command success
check_status() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ $1 completed${NC}"
        return 0
    else
        echo -e "${RED}✗ $1 failed${NC}"
        FAILURES=$((FAILURES + 1))
        return 1
    fi
}

FAILURES=0
VULNERABILITIES=0

print_section "Step 1: Dependency Vulnerability Scan"
echo "Running OWASP Dependency-Check..."
echo "This checks all dependencies against the National Vulnerability Database"
echo ""
./mvnw dependency-check:check
if check_status "OWASP Dependency-Check"; then
    echo ""
    echo "Report: target/dependency-check-report.html"
else
    VULNERABILITIES=$((VULNERABILITIES + 1))
fi

print_section "Step 2: Security Bug Detection"
echo "Running SpotBugs with Find Security Bugs plugin..."
echo "This detects security vulnerabilities in your code"
echo ""
./mvnw spotbugs:check
if check_status "SpotBugs Security Scan"; then
    echo ""
    echo "Report: target/spotbugs.html"
else
    VULNERABILITIES=$((VULNERABILITIES + 1))
fi

print_section "Step 3: Code Quality & Security (PMD)"
echo "Running PMD for code quality and security issues..."
echo ""
./mvnw pmd:check
check_status "PMD Analysis"

print_section "Step 4: Static Code Analysis (Checkstyle)"
echo "Running Checkstyle for coding standards..."
echo ""
./mvnw checkstyle:check
check_status "Checkstyle"

print_section "Step 5: Test Coverage"
echo "Running tests with JaCoCo coverage..."
echo ""
./mvnw test jacoco:report
check_status "Tests & Coverage"

print_section "Security Scan Summary"

echo ""
echo "Reports Generated:"
echo -e "  ${BLUE}Dependency Vulnerabilities:${NC}  target/dependency-check-report.html"
echo -e "  ${BLUE}Security Bugs:${NC}               target/spotbugs.html"
echo -e "  ${BLUE}Code Quality:${NC}                target/site/pmd.html"
echo -e "  ${BLUE}Code Style:${NC}                  target/site/checkstyle.html"
echo -e "  ${BLUE}Test Coverage:${NC}               target/site/jacoco/index.html"
echo ""

if [ $VULNERABILITIES -gt 0 ]; then
    echo -e "${YELLOW}⚠ $VULNERABILITIES security scan(s) found issues${NC}"
    echo -e "${YELLOW}Please review the reports above${NC}"
fi

if [ $FAILURES -eq 0 ]; then
    echo -e "${GREEN}✓ Security testing complete!${NC}"
else
    echo -e "${YELLOW}⚠ $FAILURES check(s) reported issues${NC}"
fi

echo ""
echo "Next Steps:"
echo "  1. Review all security reports"
echo "  2. Fix critical vulnerabilities (CVSS >= 7.0)"
echo "  3. Update dependencies with known CVEs"
echo "  4. Address security bugs found by SpotBugs"
echo ""

# Open main security report
read -p "Open OWASP Dependency-Check report? (y/n) " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    if [ -f "target/dependency-check-report.html" ]; then
        open target/dependency-check-report.html
    else
        echo "Report not found. Run './mvnw dependency-check:check' first."
    fi
fi

echo ""
echo -e "${GREEN}Security testing complete!${NC}"
echo "=========================================="

exit $VULNERABILITIES

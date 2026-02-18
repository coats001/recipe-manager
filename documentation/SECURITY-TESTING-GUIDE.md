# Security Testing Guide - Recipe Manager

## 🛡️ Overview

Your Recipe Manager now has **comprehensive security testing** configured with industry-standard tools that scan for vulnerabilities at multiple levels.

---

## 🔐 Security Tools Configured

### 1. **OWASP Dependency-Check** ⭐
**What it does**: Scans all dependencies against the National Vulnerability Database (NVD)  
**Detects**: Known CVEs in third-party libraries  
**Run**: `./mvnw dependency-check:check`  
**Report**: `target/dependency-check-report.html`

**Key Features:**
- Checks Maven dependencies against 100,000+ known vulnerabilities
- CVSS severity scoring
- Dependency tree analysis
- Automatic updates from NVD
- Configurable fail thresholds

### 2. **Find Security Bugs** (SpotBugs Plugin) ⭐
**What it does**: Static analysis for security vulnerabilities in your code  
**Detects**: SQL injection, XSS, weak crypto, path traversal, etc.  
**Run**: `./mvnw spotbugs:check`  
**Report**: `target/spotbugs.html`

**Key Features:**
- 135+ security-focused bug patterns
- Detects:
  - SQL Injection vulnerabilities
  - Cross-Site Scripting (XSS)
  - Command Injection
  - Path Traversal
  - Weak cryptography
  - Insecure random number generation
  - Hard-coded passwords
  - Trust boundary violations

### 3. **PMD Security Rules**
**What it does**: Code analysis with security-focused rules  
**Detects**: Code quality issues that may lead to security problems  
**Run**: `./mvnw pmd:check`  
**Report**: `target/site/pmd.html`

### 4. **Checkstyle**
**What it does**: Enforces secure coding standards  
**Detects**: Coding patterns that may introduce vulnerabilities  
**Run**: `./mvnw checkstyle:check`  
**Report**: `target/site/checkstyle.html`

---

## 🚀 Quick Start

### Run All Security Checks
```bash
./security-check.sh
```
This automated script runs all security tools and generates reports.

### Run Individual Tools
```bash
# Dependency vulnerabilities
./mvnw dependency-check:check

# Security bugs in code
./mvnw spotbugs:check

# Code quality
./mvnw pmd:check

# Coding standards
./mvnw checkstyle:check
```

---

## 📊 Understanding Security Reports

### OWASP Dependency-Check Report

**Location**: `target/dependency-check-report.html`

**Sections:**
1. **Summary** - Total dependencies and vulnerabilities found
2. **Vulnerabilities by Severity** - Critical, High, Medium, Low
3. **Vulnerable Dependencies** - List of affected libraries with CVE details
4. **Dependency Details** - Complete dependency tree

**CVSS Severity Scale:**
- **Critical**: 9.0-10.0 (Fix immediately!)
- **High**: 7.0-8.9 (Fix before release)
- **Medium**: 4.0-6.9 (Fix when possible)
- **Low**: 0.1-3.9 (Consider fixing)

**Example Vulnerability:**
```
CVE-2023-XXXXX (CVSS: 9.8 - CRITICAL)
Affected: org.springframework:spring-core:5.3.20
Description: Remote Code Execution vulnerability
Fix: Upgrade to 5.3.30 or later
```

### Find Security Bugs Report

**Location**: `target/spotbugs.html`

**Bug Categories:**
- **SECURITY** - Security vulnerabilities
- **BAD_PRACTICE** - Code that may lead to security issues
- **CORRECTNESS** - Logic errors that could be exploited

**Common Security Bugs:**

1. **SQL_INJECTION_JDBC**
   - SQL queries built with string concatenation
   - Fix: Use PreparedStatement with parameters

2. **XSS_REQUEST_PARAMETER_TO_SERVLET_WRITER**
   - User input directly written to response
   - Fix: Escape/sanitize output

3. **WEAK_FILENAMEUTILS**
   - Path traversal vulnerability
   - Fix: Validate file paths

4. **PREDICTABLE_RANDOM**
   - Using insecure random number generator
   - Fix: Use SecureRandom

5. **HARD_CODE_PASSWORD**
   - Passwords hard-coded in source
   - Fix: Use environment variables or secrets manager

---

## 🎯 Security Testing Workflow

### Daily Development
1. **SonarLint** in IntelliJ - Real-time security feedback
2. Write secure code following OWASP guidelines
3. Run unit tests with security test cases

### Before Commit
```bash
./mvnw test spotbugs:check
```

### Before Pull Request
```bash
./security-check.sh
```
Review all reports and fix critical/high vulnerabilities

### Weekly/Sprint
```bash
./mvnw dependency-check:check
```
Check for new CVEs in dependencies

### Before Release
```bash
./security-check.sh
# Fix ALL critical and high vulnerabilities
# Document accepted risks for medium/low
```

---

## 🔧 Configuration

### Dependency-Check Settings

**File**: `pom.xml`

```xml
<configuration>
    <failBuildOnCVSS>8</failBuildOnCVSS>  <!-- Fail on High+ -->
    <skipTestScope>true</skipTestScope>    <!-- Ignore test deps -->
    <suppressionFile>dependency-check-suppressions.xml</suppressionFile>
</configuration>
```

**Adjust Thresholds:**
- `failBuildOnCVSS` - Minimum CVSS score to fail build (0-10)
- `skipTestScope` - Whether to check test dependencies
- `skipProvidedScope` - Whether to check provided dependencies

### Suppressing False Positives

**File**: `dependency-check-suppressions.xml`

```xml
<suppress>
    <notes>False positive - not actually vulnerable</notes>
    <gav>org.example:library:1.0.0</gav>
    <cve>CVE-2023-12345</cve>
</suppress>
```

**When to Suppress:**
- False positive (library not actually vulnerable)
- Vulnerability doesn't apply to your usage
- Risk accepted by security team
- Waiting for upstream fix

**Best Practice:**
- Always add detailed notes
- Include ticket/issue reference
- Set expiry date for review
- Get security team approval

---

## 🛠️ Common Vulnerabilities & Fixes

### 1. Dependency Vulnerabilities

**Problem**: Third-party library has known CVE

**Solution:**
```bash
# Find the vulnerable dependency
./mvnw dependency:tree | grep "library-name"

# Update to fixed version in pom.xml
<dependency>
    <groupId>org.example</groupId>
    <artifactId>library-name</artifactId>
    <version>2.0.0</version> <!-- Updated version -->
</dependency>
```

### 2. SQL Injection

**Vulnerable Code:**
```java
String query = "SELECT * FROM recipes WHERE name = '" + userInput + "'";
Statement stmt = connection.createStatement();
ResultSet rs = stmt.executeQuery(query);
```

**Secure Code:**
```java
String query = "SELECT * FROM recipes WHERE name = ?";
PreparedStatement stmt = connection.prepareStatement(query);
stmt.setString(1, userInput);
ResultSet rs = stmt.executeQuery();
```

**Or use Spring Data JPA** (already secure):
```java
recipeRepository.findByName(userInput); // Uses parameterized queries
```

### 3. Cross-Site Scripting (XSS)

**Vulnerable Code:**
```java
@GetMapping("/recipe")
public String getRecipe(@RequestParam String name) {
    return "<h1>Recipe: " + name + "</h1>"; // Dangerous!
}
```

**Secure Code:**
```java
@GetMapping("/recipe")
public String getRecipe(@RequestParam String name) {
    return "<h1>Recipe: " + HtmlUtils.htmlEscape(name) + "</h1>";
}
```

**Or return JSON** (Spring does this automatically):
```java
@GetMapping("/recipe")
public RecipeResponse getRecipe(@RequestParam String name) {
    return recipeService.findByName(name); // Safe - returns JSON
}
```

### 4. Path Traversal

**Vulnerable Code:**
```java
File file = new File("/uploads/" + userFilename);
```

**Secure Code:**
```java
Path basePath = Paths.get("/uploads").toAbsolutePath().normalize();
Path userPath = basePath.resolve(userFilename).normalize();

if (!userPath.startsWith(basePath)) {
    throw new SecurityException("Invalid path");
}
File file = userPath.toFile();
```

### 5. Weak Cryptography

**Vulnerable Code:**
```java
Random random = new Random();
String token = String.valueOf(random.nextInt());
```

**Secure Code:**
```java
SecureRandom secureRandom = new SecureRandom();
byte[] randomBytes = new byte[32];
secureRandom.nextBytes(randomBytes);
String token = Base64.getEncoder().encodeToString(randomBytes);
```

---

## 📋 Security Checklist

### Code Security
- [ ] All user input is validated
- [ ] SQL queries use PreparedStatement or JPA
- [ ] Output is escaped/sanitized
- [ ] File paths are validated
- [ ] Cryptography uses SecureRandom
- [ ] No hard-coded passwords/secrets
- [ ] Error messages don't leak sensitive info
- [ ] Authentication/authorization implemented

### Dependency Security
- [ ] All dependencies up-to-date
- [ ] No critical CVEs in dependencies
- [ ] No high CVEs in dependencies
- [ ] Test dependencies excluded from prod
- [ ] License compliance checked

### Configuration Security
- [ ] Secrets in environment variables
- [ ] HTTPS enabled in production
- [ ] Security headers configured
- [ ] CORS properly configured
- [ ] Rate limiting enabled
- [ ] Logging doesn't expose secrets

---

## 🎯 Integration with CI/CD

### GitHub Actions Example

```yaml
name: Security Scan

on: [push, pull_request]

jobs:
  security:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          
      - name: Run Security Checks
        run: ./security-check.sh
        
      - name: Upload Security Reports
        uses: actions/upload-artifact@v3
        with:
          name: security-reports
          path: target/*.html
```

### GitLab CI Example

```yaml
security_scan:
  stage: test
  script:
    - ./security-check.sh
  artifacts:
    paths:
      - target/dependency-check-report.html
      - target/spotbugs.html
    expire_in: 1 week
  only:
    - merge_requests
    - main
```

---

## 🔍 Additional Security Tools

### Secret Detection (Pre-commit Hook)

**Install Gitleaks:**
```bash
brew install gitleaks

# Or download from: https://github.com/gitleaks/gitleaks/releases
```

**Scan Repository:**
```bash
gitleaks detect --source . --verbose
```

**Pre-commit Hook:**
Create `.git/hooks/pre-commit`:
```bash
#!/bin/bash
gitleaks protect --staged --verbose
```

### Runtime Security (OWASP ZAP)

**For testing running application:**

```bash
# Install ZAP
brew install --cask owasp-zap

# Start your app
./mvnw spring-boot:run

# Run ZAP baseline scan
docker run -t ghcr.io/zaproxy/zaproxy:stable \
  zap-baseline.py -t http://localhost:8080/api
```

---

## 📚 Security Resources

### OWASP Top 10 (2021)
1. Broken Access Control
2. Cryptographic Failures
3. Injection
4. Insecure Design
5. Security Misconfiguration
6. Vulnerable Components (Dependencies)
7. Authentication Failures
8. Software/Data Integrity Failures
9. Security Logging Failures
10. Server-Side Request Forgery

### Spring Security Best Practices
- Use Spring Security for authentication/authorization
- Enable CSRF protection
- Configure secure headers
- Use HTTPS in production
- Implement rate limiting
- Log security events

### References
- **OWASP**: https://owasp.org/
- **Dependency-Check**: https://jeremylong.github.io/DependencyCheck/
- **Find Security Bugs**: https://find-sec-bugs.github.io/
- **NVD Database**: https://nvd.nist.gov/
- **Spring Security**: https://spring.io/projects/spring-security

---

## 🆘 Troubleshooting

### OWASP Dependency-Check is slow?
**Solution**: First run downloads NVD database (~30 minutes). Subsequent runs are faster (~2 minutes).

```bash
# Cache database locally
./mvnw dependency-check:update-only
```

### False positives in reports?
**Solution**: Add suppressions to `dependency-check-suppressions.xml`

### Too many Medium/Low vulnerabilities?
**Solution**: Focus on Critical/High first. Set threshold:
```xml
<failBuildOnCVSS>7</failBuildOnCVSS>
```

### Can't fix vulnerability (no update available)?
**Solution**: 
1. Check if vulnerability applies to your usage
2. Add suppression with detailed justification
3. Consider alternative library
4. Implement compensating controls

---

## 🎉 Summary

Your Recipe Manager now has:

✅ **OWASP Dependency-Check** - CVE scanning for dependencies  
✅ **Find Security Bugs** - Security vulnerability detection  
✅ **PMD Security Rules** - Code quality & security  
✅ **Checkstyle** - Secure coding standards  
✅ **Automated Security Script** - `./security-check.sh`  
✅ **Suppression Configuration** - False positive management  
✅ **Comprehensive Documentation** - This guide  

**Run security checks with:**
```bash
./security-check.sh
```

**Your application is now secured with enterprise-grade security testing!** 🛡️✨

---

For questions or issues, see the [Troubleshooting](#-troubleshooting) section above.

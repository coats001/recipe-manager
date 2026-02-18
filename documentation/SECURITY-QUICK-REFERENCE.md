# Security Testing - Quick Reference

## 🚀 Quick Commands

### Run All Security Checks
```bash
./security-check.sh
```

### Individual Scans
```bash
# Dependency vulnerabilities (CVEs)
./mvnw dependency-check:check

# Security bugs in code
./mvnw spotbugs:check

# Code quality & security
./mvnw pmd:check

# Coding standards
./mvnw checkstyle:check
```

---

## 📊 Reports

| Tool | Report Location | What It Shows |
|------|----------------|---------------|
| **OWASP Dependency-Check** | `target/dependency-check-report.html` | CVEs in dependencies |
| **Find Security Bugs** | `target/spotbugs.html` | Security vulnerabilities in code |
| **PMD** | `target/site/pmd.html` | Code quality issues |
| **Checkstyle** | `target/site/checkstyle.html` | Coding standard violations |
| **JaCoCo** | `target/site/jacoco/index.html` | Test coverage |

---

## 🎯 Security Tools Overview

### 1. OWASP Dependency-Check
- **Checks**: Third-party library vulnerabilities
- **Database**: National Vulnerability Database (NVD)
- **Finds**: Known CVEs with CVSS scores
- **Priority**: ⭐⭐⭐⭐⭐ (Critical)

### 2. Find Security Bugs
- **Checks**: Your code for security issues
- **Detects**: SQL injection, XSS, weak crypto, etc.
- **Patterns**: 135+ security bug patterns
- **Priority**: ⭐⭐⭐⭐⭐ (Critical)

### 3. PMD Security
- **Checks**: Code quality with security focus
- **Detects**: Poor practices leading to vulnerabilities
- **Priority**: ⭐⭐⭐ (Important)

### 4. Checkstyle
- **Checks**: Secure coding standards
- **Enforces**: Best practices
- **Priority**: ⭐⭐ (Good to have)

---

## 🔍 Common Vulnerabilities

### SQL Injection
**Bad:**
```java
"SELECT * FROM recipes WHERE name = '" + input + "'"
```

**Good:**
```java
"SELECT * FROM recipes WHERE name = ?" // PreparedStatement
```

### XSS (Cross-Site Scripting)
**Bad:**
```java
return "<html>" + userInput + "</html>";
```

**Good:**
```java
return HtmlUtils.htmlEscape(userInput);
```

### Weak Random
**Bad:**
```java
new Random().nextInt()
```

**Good:**
```java
new SecureRandom().nextInt()
```

### Hard-coded Secrets
**Bad:**
```java
String password = "mypassword123";
```

**Good:**
```java
String password = System.getenv("DB_PASSWORD");
```

---

## 📋 CVSS Severity Scale

| Score | Severity | Action |
|-------|----------|--------|
| 9.0-10.0 | **CRITICAL** 🔴 | Fix immediately |
| 7.0-8.9 | **HIGH** 🟠 | Fix before release |
| 4.0-6.9 | **MEDIUM** 🟡 | Fix when possible |
| 0.1-3.9 | **LOW** 🟢 | Consider fixing |

---

## 🛠️ Quick Fixes

### Update Vulnerable Dependency
```bash
# Find the dependency
./mvnw dependency:tree | grep "library-name"

# Update version in pom.xml
<dependency>
    <artifactId>library-name</artifactId>
    <version>NEW_VERSION</version>
</dependency>
```

### Suppress False Positive
Edit `dependency-check-suppressions.xml`:
```xml
<suppress>
    <notes>Reason for suppression</notes>
    <gav>org.example:library:1.0.0</gav>
    <cve>CVE-2023-12345</cve>
</suppress>
```

---

## 🎓 Daily Workflow

### Before Commit
```bash
./mvnw test spotbugs:check
```

### Before Pull Request
```bash
./security-check.sh
```

### Weekly
```bash
./mvnw dependency-check:check
```

### Before Release
```bash
./security-check.sh
# Fix all Critical and High vulnerabilities
```

---

## 🔧 Configuration Files

| File | Purpose |
|------|---------|
| `pom.xml` | Maven plugin configuration |
| `dependency-check-suppressions.xml` | CVE suppressions |
| `spotbugs-exclude.xml` | SpotBugs exclusions |
| `security-check.sh` | Automated security script |

---

## 🆘 Common Issues

### "NVD download taking too long"
**Solution**: First run downloads database (~30 min). Cache it:
```bash
./mvnw dependency-check:update-only
```

### "Too many false positives"
**Solution**: Add suppressions to `dependency-check-suppressions.xml`

### "Build fails on vulnerabilities"
**Solution**: Adjust threshold in `pom.xml`:
```xml
<failBuildOnCVSS>8</failBuildOnCVSS>
```

---

## 📚 Documentation

- **Complete Guide**: `SECURITY-TESTING-GUIDE.md` (full documentation)
- **This File**: Quick reference card
- **OWASP**: https://owasp.org/
- **Dependency-Check**: https://jeremylong.github.io/DependencyCheck/

---

## ✅ Security Checklist

- [ ] Run `./security-check.sh`
- [ ] Fix all Critical vulnerabilities
- [ ] Fix all High vulnerabilities
- [ ] Review Medium vulnerabilities
- [ ] Update dependencies
- [ ] No hard-coded secrets
- [ ] All tests passing

---

## 🎉 Quick Start

1. **Run security scan**: `./security-check.sh`
2. **Open report**: `target/dependency-check-report.html`
3. **Fix vulnerabilities**: Update dependencies in `pom.xml`
4. **Re-run**: `./mvnw dependency-check:check`
5. **Verify**: Check report again

---

**Your Recipe Manager is secured with enterprise-grade security testing!** 🛡️

For complete details, see `SECURITY-TESTING-GUIDE.md`

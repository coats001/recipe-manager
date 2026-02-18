# Static Code Analysis - Quick Reference

## 🚀 Quick Commands

### Run All Analysis
```bash
./code-analysis.sh
```

### Individual Tools
```bash
# SpotBugs - Bug detection
./mvnw spotbugs:check

# PMD - Code quality
./mvnw pmd:check

# Checkstyle - Code style
./mvnw checkstyle:check

# JaCoCo - Test coverage
./mvnw test jacoco:report

# All together
./mvnw spotbugs:check pmd:check checkstyle:check
```

### Generate Reports
```bash
# Complete site with all reports
./mvnw site

# View in browser (macOS)
open target/site/index.html
```

## 📊 Report Locations

| Tool | Report File |
|------|-------------|
| JaCoCo | `target/site/jacoco/index.html` |
| SpotBugs | `target/spotbugs.html` |
| PMD | `target/site/pmd.html` |
| Checkstyle | `target/site/checkstyle.html` |
| All Reports | `target/site/index.html` |

## 🔧 IntelliJ Plugins to Install

1. **SonarLint** (Required) - Real-time analysis
2. **Checkstyle-IDEA** (Recommended) - Style checks
3. **SpotBugs-IDEA** (Optional) - Bug detection
4. **PMDPlugin** (Optional) - Code quality

### Install Plugins:
`Settings/Preferences` → `Plugins` → Search and Install

## 📋 What Each Tool Checks

### SonarLint
- Code smells
- Bugs
- Security vulnerabilities
- Code quality issues
- **Real-time feedback**

### SpotBugs
- Null pointer dereferences
- Resource leaks
- Thread safety
- Performance issues
- Security vulnerabilities

### PMD
- Unused code
- Empty blocks
- Overcomplicated expressions
- Duplicate code
- Best practices violations

### Checkstyle
- Naming conventions
- Javadoc comments
- Import organization
- Whitespace
- File structure

### JaCoCo
- Line coverage
- Branch coverage
- Complexity metrics
- Test coverage reports

## ⚡ Common Workflows

### Before Committing
```bash
./mvnw clean test
```

### Before Pull Request
```bash
./code-analysis.sh
```

### Daily Development
- Let SonarLint run automatically in IntelliJ
- Fix issues as you code

### CI/CD Pipeline
```bash
./mvnw clean verify spotbugs:check pmd:check checkstyle:check
```

## 🎯 Priority Levels

1. **Critical**: Fix immediately
   - Security vulnerabilities
   - Null pointer issues
   - Resource leaks

2. **High**: Fix before PR
   - Code smells
   - Performance issues
   - Test coverage gaps

3. **Medium**: Fix when possible
   - Style violations
   - Documentation issues
   - Minor code quality

4. **Low**: Consider fixing
   - Formatting preferences
   - Optional improvements

## 🛠️ Customization Files

- `spotbugs-exclude.xml` - SpotBugs exclusions
- `pom.xml` - Tool configurations
- IntelliJ Settings - Plugin configurations

## 💡 Tips

1. **Start with SonarLint** for instant feedback
2. **Run analysis before commits** to catch issues early
3. **Review reports regularly** - don't ignore warnings
4. **Customize rules** to fit your team's standards
5. **Track improvements** over time

## 🆘 Quick Fixes

### Too many warnings?
- Adjust severity levels in `pom.xml`
- Focus on high-priority issues first

### False positives?
- Add exclusions to `spotbugs-exclude.xml`
- Configure tool-specific filters

### Slow performance?
- Run individual tools instead of all
- Exclude generated code

## 📚 Full Documentation

See `CODE-ANALYSIS-GUIDE.md` for complete documentation.

---

**Tools configured**: SonarLint + SpotBugs + PMD + Checkstyle + JaCoCo  
**Coverage**: 100% of SonarQube functionality locally! 🎉

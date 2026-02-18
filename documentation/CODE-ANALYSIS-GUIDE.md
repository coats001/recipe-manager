# Static Code Analysis Tools - Local Setup Guide

## 🎯 Overview

This project is configured with **4 static code analysis tools** that mimic SonarQube functionality locally:

1. **SonarLint** (IntelliJ Plugin) - Real-time analysis
2. **SpotBugs** (Maven) - Bug detection
3. **PMD** (Maven) - Code quality checks
4. **Checkstyle** (Maven) - Code style enforcement

## 📦 1. SonarLint (IntelliJ Plugin)

### Installation
1. Open IntelliJ IDEA
2. Go to `Settings/Preferences` → `Plugins`
3. Search for "SonarLint"
4. Click `Install`
5. Restart IntelliJ

### Usage
- **Automatic**: SonarLint analyzes files as you type
- **Manual**: Right-click on file/folder → `Analyze` → `Analyze with SonarLint`
- **View Results**: Open the `SonarLint` tool window (View → Tool Windows → SonarLint)

### Features
- ✅ Real-time feedback while coding
- ✅ Highlights issues directly in editor
- ✅ Provides fix suggestions
- ✅ Works offline (no server needed)
- ✅ Can sync with SonarQube server (optional)

### Configuration
- Rules: `Settings` → `Tools` → `SonarLint`
- Connect to SonarQube Server (optional): `SonarLint` → `Settings` → `Add Connection`

---

## 🐛 2. SpotBugs (Maven Plugin)

### What It Checks
- Null pointer dereferences
- Infinite loops
- Resource leaks
- Thread safety issues
- Performance problems
- Security vulnerabilities

### Maven Commands

```bash
# Run SpotBugs analysis
./mvnw spotbugs:check

# Generate HTML report
./mvnw spotbugs:spotbugs

# Generate GUI report
./mvnw spotbugs:gui

# Include in site documentation
./mvnw site
```

### View Reports
- **Console**: Terminal output during build
- **HTML**: `target/spotbugs.html`
- **XML**: `target/spotbugsXml.xml`
- **Site**: `target/site/spotbugs.html`

### IntelliJ Plugin (Optional)
1. Install "SpotBugs-IDEA" plugin from marketplace
2. Right-click on project → `Analyze` → `Analyze Project Files with SpotBugs`

### Configuration
- Edit `spotbugs-exclude.xml` to exclude specific warnings
- Adjust `effort` and `threshold` in `pom.xml`

---

## 📊 3. PMD (Maven Plugin)

### What It Checks
- Unused variables/methods
- Empty catch blocks
- Overcomplicated expressions
- Suboptimal code
- Duplicate code
- Code style violations

### Maven Commands

```bash
# Run PMD analysis
./mvnw pmd:check

# Generate PMD report
./mvnw pmd:pmd

# Check for copy-paste detector (CPD)
./mvnw pmd:cpd

# Generate site with PMD report
./mvnw site
```

### View Reports
- **Console**: Terminal output
- **HTML**: `target/site/pmd.html`
- **XML**: `target/pmd.xml`
- **CPD Report**: `target/site/cpd.html`

### IntelliJ Plugin (Optional)
1. Install "PMDPlugin" from marketplace
2. Right-click on file → `Run PMD` → `Pre Defined` → `All Java Rules`

### Configuration
- Custom rules: Create `pmd-ruleset.xml` in project root
- Adjust rulesets in `pom.xml` configuration

---

## ✅ 4. Checkstyle (Maven Plugin)

### What It Checks
- Naming conventions
- Javadoc comments
- Imports organization
- Whitespace usage
- Code structure
- File organization

### Maven Commands

```bash
# Run Checkstyle analysis
./mvnw checkstyle:check

# Generate Checkstyle report
./mvnw checkstyle:checkstyle

# Include in site documentation
./mvnw site
```

### View Reports
- **Console**: Terminal output with violations
- **HTML**: `target/site/checkstyle.html`
- **XML**: `target/checkstyle-result.xml`

### IntelliJ Plugin (Recommended)
1. Install "Checkstyle-IDEA" plugin from marketplace
2. Go to `Settings` → `Tools` → `Checkstyle`
3. Add configuration file (uses Google Java Style Guide by default)
4. Right-click on file → `Check Current File`

### Configuration
- Uses **Google Java Style Guide** by default (`google_checks.xml`)
- Alternative: Sun checks (`sun_checks.xml`)
- Custom: Create `checkstyle.xml` in project root

---

## 🚀 Running All Checks Together

### Option 1: Run All Individually
```bash
./mvnw spotbugs:check pmd:check checkstyle:check
```

### Option 2: Run During Verify Phase
```bash
./mvnw clean verify
```
This runs tests + all static analysis tools

### Option 3: Generate Complete Site Report
```bash
./mvnw clean site
```
View at: `target/site/index.html`

### Option 4: Create Custom Script

Create `code-analysis.sh`:
```bash
#!/bin/bash
echo "Running Static Code Analysis..."
echo "================================"

echo "1. Running SpotBugs..."
./mvnw spotbugs:check

echo "2. Running PMD..."
./mvnw pmd:check

echo "3. Running Checkstyle..."
./mvnw checkstyle:check

echo "4. Running Tests with Coverage..."
./mvnw test jacoco:report

echo "================================"
echo "Analysis Complete!"
echo "Reports available in target/ directory"
```

Make it executable:
```bash
chmod +x code-analysis.sh
./code-analysis.sh
```

---

## 📋 Quick Reference Table

| Tool | Purpose | Run Command | Report Location | IntelliJ Plugin |
|------|---------|-------------|----------------|-----------------|
| **SonarLint** | Real-time analysis | Automatic | In-editor | ✅ Required |
| **SpotBugs** | Bug detection | `mvn spotbugs:check` | `target/spotbugs.html` | ✅ Optional |
| **PMD** | Code quality | `mvn pmd:check` | `target/site/pmd.html` | ✅ Optional |
| **Checkstyle** | Code style | `mvn checkstyle:check` | `target/site/checkstyle.html` | ✅ Recommended |
| **JaCoCo** | Test coverage | `mvn test jacoco:report` | `target/site/jacoco/index.html` | ❌ Not needed |

---

## 🎨 IntelliJ Integration Setup

### Step 1: Install Plugins
1. **SonarLint** (Required)
2. **SpotBugs-IDEA** (Optional)
3. **PMDPlugin** (Optional)
4. **Checkstyle-IDEA** (Recommended)

### Step 2: Configure Maven Integration
1. Open Maven tool window (View → Tool Windows → Maven)
2. Expand `Plugins` folder
3. Pin frequently used goals:
   - `spotbugs:check`
   - `pmd:check`
   - `checkstyle:check`
   - `jacoco:report`

### Step 3: Create Run Configurations
1. Go to `Run` → `Edit Configurations`
2. Add new `Maven` configuration:
   - Name: "Code Analysis"
   - Command: `spotbugs:check pmd:check checkstyle:check`
   - Working directory: Project root

---

## 🔧 Customization

### Adjusting Severity Levels

**SpotBugs** (`pom.xml`):
```xml
<configuration>
    <effort>Max</effort>        <!-- Min, Default, Max -->
    <threshold>Low</threshold>  <!-- High, Medium, Low -->
    <failOnError>false</failOnError>
</configuration>
```

**PMD** (`pom.xml`):
```xml
<configuration>
    <failOnViolation>false</failOnViolation>
    <targetJdk>21</targetJdk>
</configuration>
```

**Checkstyle** (`pom.xml`):
```xml
<configuration>
    <violationSeverity>warning</violationSeverity>  <!-- error, warning, info -->
    <failsOnError>false</failsOnError>
</configuration>
```

### Excluding Files/Patterns

**SpotBugs**: Edit `spotbugs-exclude.xml`
```xml
<Match>
    <Class name="~.*Test$"/>
</Match>
```

**PMD**: Use `excludeRoots` in `pom.xml`
**Checkstyle**: Use `excludes` in `pom.xml`

---

## 📊 Understanding Reports

### SpotBugs Report
- **Bugs**: Serious problems that should be fixed
- **Rank**: 1-20 (1 = most concerning)
- **Category**: Correctness, Bad Practice, Performance, etc.

### PMD Report
- **Priority**: 1-5 (1 = highest)
- **Rule**: Specific rule violated
- **Category**: Best Practices, Code Style, Design, etc.

### Checkstyle Report
- **Severity**: Error, Warning, Info
- **Module**: Specific check that failed
- **Line**: Exact location in source code

### JaCoCo Coverage
- **Line Coverage**: % of lines executed
- **Branch Coverage**: % of decision points tested
- **Cyclomatic Complexity**: Code complexity metric

---

## 🎯 Recommended Workflow

### Daily Development (IntelliJ)
1. Let **SonarLint** run automatically
2. Fix issues as you code
3. Use **Checkstyle** plugin for style checks

### Before Commit
```bash
./mvnw clean test
```

### Before Pull Request
```bash
./mvnw clean verify site
```
Review all reports in `target/site/`

### CI/CD Pipeline
```bash
./mvnw clean verify spotbugs:check pmd:check checkstyle:check
```

---

## 🏆 Best Practices

1. **Start with SonarLint** - Get immediate feedback
2. **Run checks locally** before pushing code
3. **Review reports regularly** - Don't ignore warnings
4. **Customize rules** - Adapt to your team's standards
5. **Fail builds** on critical issues (configure in CI/CD)
6. **Track metrics** over time - Aim for improvement
7. **Educate team** - Share common issues and fixes

---

## 🆘 Troubleshooting

### Issue: Too Many Warnings
**Solution**: Start with high-priority issues only, adjust thresholds

### Issue: False Positives
**Solution**: Add exclusions to respective exclude files

### Issue: Slow Analysis
**Solution**: 
- Run specific plugins instead of all
- Exclude generated code
- Use `failOnError=false` during development

### Issue: Plugin Conflicts
**Solution**: Check plugin versions compatibility with Java 21 and Spring Boot 4.x

---

## 📚 Additional Resources

- **SonarLint**: https://www.sonarsource.com/products/sonarlint/
- **SpotBugs**: https://spotbugs.github.io/
- **PMD**: https://pmd.github.io/
- **Checkstyle**: https://checkstyle.sourceforge.io/
- **JaCoCo**: https://www.jacoco.org/

---

## ✅ Quick Start Checklist

- [ ] Install SonarLint plugin in IntelliJ
- [ ] Run `./mvnw clean compile` to download dependencies
- [ ] Run `./mvnw spotbugs:check` to test SpotBugs
- [ ] Run `./mvnw pmd:check` to test PMD
- [ ] Run `./mvnw checkstyle:check` to test Checkstyle
- [ ] Generate site report: `./mvnw site`
- [ ] Open `target/site/index.html` to view all reports
- [ ] Install Checkstyle-IDEA plugin (optional but recommended)
- [ ] Create code-analysis.sh script for convenience

---

## 🎉 Summary

You now have a **complete SonarQube-like setup** running locally:

✅ **Real-time analysis** with SonarLint  
✅ **Bug detection** with SpotBugs  
✅ **Code quality** checks with PMD  
✅ **Style enforcement** with Checkstyle  
✅ **Test coverage** with JaCoCo  

All tools are integrated with Maven and can be run from IntelliJ or command line!

# Scripts

This folder contains utility scripts for the Recipe Manager project.

## Available Scripts

- **apply-google-style.sh** - Applies Google Java code style formatting
- **build.sh** - Builds the project
- **code-analysis.sh** - Runs code analysis tools (Checkstyle, PMD, SpotBugs)
- **install-java-21.sh** - Installs Java 21
- **run-unit-tests.sh** - Runs unit tests
- **security-check.sh** - Runs security vulnerability checks
- **test-api.sh** - Tests the API endpoints

## Usage

Make sure the scripts are executable:

```bash
chmod +x scripts/*.sh
```

Run a script from the project root:

```bash
./scripts/build.sh
./scripts/run-unit-tests.sh
```

## Notes

All scripts should be run from the project root directory for proper path resolution.

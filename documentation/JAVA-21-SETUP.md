# Java 21 Setup Guide

## Current Status
You are currently running Java 25.0.2. This project requires Java 21.

## Quick Solution (Recommended)

### Option 1: Using Homebrew (Easiest)

1. **Install Java 21 with Homebrew:**
   ```bash
   brew install openjdk@21
   ```

2. **Set Java 21 as default for this terminal session:**
   ```bash
   export JAVA_HOME="$(brew --prefix openjdk@21)/libexec/openjdk.jdk/Contents/Home"
   export PATH="$JAVA_HOME/bin:$PATH"
   ```

3. **Verify:**
   ```bash
   java --version
   ```

4. **Make it permanent (add to ~/.zshrc):**
   ```bash
   echo 'export JAVA_HOME="$(brew --prefix openjdk@21)/libexec/openjdk.jdk/Contents/Home"' >> ~/.zshrc
   echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.zshrc
   source ~/.zshrc
   ```

### Option 2: Using SDKMAN (Recommended for Java version management)

1. **Install SDKMAN if not already installed:**
   ```bash
   curl -s "https://get.sdkman.io" | bash
   source "$HOME/.sdkman/bin/sdkman-init.sh"
   ```

2. **Install Java 21:**
   ```bash
   sdk install java 21.0.5-tem
   ```

3. **Set Java 21 as default:**
   ```bash
   sdk default java 21.0.5-tem
   ```

4. **Verify:**
   ```bash
   java --version
   ```

### Option 3: Manual Download

1. Download Java 21 from [Adoptium](https://adoptium.net/temurin/releases/?version=21)
2. Install the .pkg file
3. Set JAVA_HOME in ~/.zshrc:
   ```bash
   export JAVA_HOME="/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home"
   export PATH="$JAVA_HOME/bin:$PATH"
   ```

## Quick Commands

Run these commands to install and switch to Java 21:

```bash
# Install Java 21
brew install openjdk@21

# Switch to Java 21 for current session
export JAVA_HOME="$(brew --prefix openjdk@21)/libexec/openjdk.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"

# Verify
java --version

# Build the project
./mvnw clean install
```

## After Installing Java 21

Once Java 21 is set as your default, rebuild the project:

```bash
cd /Users/coats/Dropbox/Bedrijf/2026/ABNAMRO/recipe-manager
./mvnw clean install
```

## Troubleshooting

### If Java 25 still shows up after setting JAVA_HOME

1. Check your PATH:
   ```bash
   echo $PATH
   ```

2. Make sure JAVA_HOME is set correctly:
   ```bash
   echo $JAVA_HOME
   ```

3. Restart your terminal or source your profile:
   ```bash
   source ~/.zshrc
   ```

### Multiple Java versions management

Use jenv or SDKMAN to manage multiple Java versions:

**With jenv:**
```bash
brew install jenv
jenv add $(/usr/libexec/java_home -v 21)
jenv global 21
```

**With SDKMAN:**
```bash
sdk use java 21.0.5-tem
```

## For This Project Only

If you want to use Java 21 only for this project without changing your system default:

1. **Create a .envrc file in the project root:**
   ```bash
   echo 'export JAVA_HOME="$(brew --prefix openjdk@21)/libexec/openjdk.jdk/Contents/Home"' > .envrc
   echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> .envrc
   ```

2. **Install direnv:**
   ```bash
   brew install direnv
   echo 'eval "$(direnv hook zsh)"' >> ~/.zshrc
   source ~/.zshrc
   ```

3. **Allow direnv in this directory:**
   ```bash
   direnv allow .
   ```

Now Java 21 will be used automatically when you're in this project directory!

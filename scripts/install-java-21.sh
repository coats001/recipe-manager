#!/bin/bash

# Quick Java 21 Setup Script
# Run this script to install and switch to Java 21

set -e  # Exit on any error

echo "========================================="
echo "Java 21 Quick Setup"
echo "========================================="
echo ""

# Step 1: Install Java 21 using Homebrew
echo "Step 1: Installing OpenJDK 21 via Homebrew..."
brew install openjdk@21

echo ""
echo "Step 2: Getting Java 21 path..."
JAVA_21_HOME="$(brew --prefix openjdk@21)/libexec/openjdk.jdk/Contents/Home"
echo "Java 21 Home: $JAVA_21_HOME"

echo ""
echo "Step 3: Setting up environment variables..."

# Check if already in .zshrc
if grep -q "JAVA_HOME.*openjdk@21" ~/.zshrc 2>/dev/null; then
    echo "Java 21 configuration already exists in ~/.zshrc"
else
    echo ""
    echo "# Java 21 Configuration" >> ~/.zshrc
    echo "export JAVA_HOME=\"\$(brew --prefix openjdk@21)/libexec/openjdk.jdk/Contents/Home\"" >> ~/.zshrc
    echo "export PATH=\"\$JAVA_HOME/bin:\$PATH\"" >> ~/.zshrc
    echo "Added Java 21 configuration to ~/.zshrc"
fi

echo ""
echo "Step 4: Applying changes to current session..."
export JAVA_HOME="$JAVA_21_HOME"
export PATH="$JAVA_HOME/bin:$PATH"

echo ""
echo "========================================="
echo "✓ Setup Complete!"
echo "========================================="
echo ""
echo "Current Java version:"
java --version

echo ""
echo "JAVA_HOME is set to:"
echo "$JAVA_HOME"

echo ""
echo "========================================="
echo "Next Steps:"
echo "========================================="
echo "1. Close and reopen your terminal, OR"
echo "2. Run: source ~/.zshrc"
echo ""
echo "Then verify with: java --version"
echo ""
echo "To build your project:"
echo "cd /Users/coats/Dropbox/Bedrijf/2026/ABNAMRO/recipe-manager"
echo "./mvnw clean install"
echo "========================================="

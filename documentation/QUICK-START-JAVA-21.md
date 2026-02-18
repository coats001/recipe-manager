# 🚀 Quick Start: Switch to Java 21

## The Fastest Way (Copy and Paste These Commands)

Open your terminal and run these commands:

```bash
# 1. Install Java 21
brew install openjdk@21

# 2. Set Java 21 for current terminal session
export JAVA_HOME="$(brew --prefix openjdk@21)/libexec/openjdk.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"

# 3. Verify it worked
java --version

# 4. Make it permanent (add to your shell config)
echo '' >> ~/.zshrc
echo '# Java 21 Configuration' >> ~/.zshrc
echo 'export JAVA_HOME="$(brew --prefix openjdk@21)/libexec/openjdk.jdk/Contents/Home"' >> ~/.zshrc
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.zshrc

# 5. Reload your shell configuration
source ~/.zshrc

# 6. Verify again
java --version
```

## Or Use the Script

We've created a script that does everything for you:

```bash
cd /Users/coats/Dropbox/Bedrijf/2026/ABNAMRO/recipe-manager
./install-java-21.sh
```

After running either method, you should see:

```
openjdk 21.0.x 2024-xx-xx
OpenJDK Runtime Environment (build 21.0.x+xx)
OpenJDK 64-Bit Server VM (build 21.0.x+xx, mixed mode, sharing)
```

## Then Build Your Project

```bash
cd /Users/coats/Dropbox/Bedrijf/2026/ABNAMRO/recipe-manager
./mvnw clean install
```

## Troubleshooting

**If you still see Java 25 after running the commands:**

1. Close your terminal completely and open a new one
2. Run: `source ~/.zshrc`
3. Check: `java --version`
4. Check: `echo $JAVA_HOME`

**If Homebrew is not installed:**

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

## Files Created

- `JAVA-21-SETUP.md` - Detailed setup guide with multiple options
- `install-java-21.sh` - Automated installation script
- `QUICK-START-JAVA-21.md` - This file

## Need Help?

See `JAVA-21-SETUP.md` for more detailed instructions and alternative methods.

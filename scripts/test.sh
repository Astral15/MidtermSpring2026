#!/usr/bin/env sh
set -eu

# Compile main sources
scripts/compile.sh

# Run built-in self-test
java -cp out Main --self-test

# Compile and run JUnit characterization tests
if [ -d "test" ]; then
  mkdir -p out/test
  javac -cp "out:lib/junit-platform-console-standalone-1.10.2.jar" -d out/test test/*.java
  java -cp "out:out/test:lib/junit-platform-console-standalone-1.10.2.jar" \
    org.junit.platform.console.ConsoleLauncher \
    --scan-class-path=out/test \
    --fail-if-no-tests
fi


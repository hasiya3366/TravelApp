#!/usr/bin/env bash
set -e

# Railway build helper - ensures the Maven wrapper and production profile are used
./mvnw -Pproduction -DskipTests clean package

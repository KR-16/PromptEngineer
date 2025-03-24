#!/bin/bash

# Setup script for Unix-like systems (Linux/MacOS)

echo "Setting up Scenario Analyzer development environment..."

# Check for Java 17
if ! command -v java &> /dev/null; then
    echo "Java not found. Please install Java 17 or later."
    echo "Visit: https://adoptium.net/"
    exit 1
fi

java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F. '{print $1}')
if [ "$java_version" -lt 17 ]; then
    echo "Java 17 or later is required. Current version: $java_version"
    echo "Please upgrade Java from: https://adoptium.net/"
    exit 1
fi

# Check for Node.js
if ! command -v node &> /dev/null; then
    echo "Node.js not found. Please install Node.js 14 or later."
    echo "Visit: https://nodejs.org/"
    exit 1
fi

node_version=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
if [ "$node_version" -lt 14 ]; then
    echo "Node.js 14 or later is required. Current version: $node_version"
    echo "Please upgrade Node.js from: https://nodejs.org/"
    exit 1
fi

# Check for Maven
if ! command -v mvn &> /dev/null; then
    echo "Maven not found. Using included Maven wrapper."
fi

# Check for environment variables
if [ -z "$OPENAI_API_KEY" ]; then
    echo "WARNING: OPENAI_API_KEY environment variable is not set"
    echo "Please set it using: export OPENAI_API_KEY=your_api_key_here"
fi

# Install backend dependencies
echo "Installing backend dependencies..."
if command -v mvn &> /dev/null; then
    mvn clean install
else
    ./mvnw clean install
fi

# Install frontend dependencies
echo "Installing frontend dependencies..."
cd frontend
npm install

echo "Setup complete! You can now start the application:"
echo "Backend: ./mvnw spring-boot:run"
echo "Frontend: cd frontend && npm start" 
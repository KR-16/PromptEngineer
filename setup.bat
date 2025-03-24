@echo off
REM Setup script for Windows

echo Setting up Scenario Analyzer development environment...

REM Check for Java 17
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Java not found. Please install Java 17 or later.
    echo Visit: https://adoptium.net/
    exit /b 1
)

REM Check for Node.js
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo Node.js not found. Please install Node.js 14 or later.
    echo Visit: https://nodejs.org/
    exit /b 1
)

REM Check for Maven
mvn -v >nul 2>&1
if %errorlevel% neq 0 (
    echo Maven not found. Using included Maven wrapper.
)

REM Check for environment variables
if "%OPENAI_API_KEY%"=="" (
    echo WARNING: OPENAI_API_KEY environment variable is not set
    echo Please set it using: set OPENAI_API_KEY=your_api_key_here
)

REM Install backend dependencies
echo Installing backend dependencies...
if %errorlevel% equ 0 (
    mvn clean install
) else (
    mvnw.cmd clean install
)

REM Install frontend dependencies
echo Installing frontend dependencies...
cd frontend
npm install

echo Setup complete! You can now start the application:
echo Backend: mvnw.cmd spring-boot:run
echo Frontend: cd frontend ^& npm start 
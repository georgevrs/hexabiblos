@echo off
REM Batch script to build and run the chatbot application
REM This script sets up Java environment and runs Maven build + Cargo deployment

REM Set Java Home (adjust path if your JDK is in a different location)
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"

REM Verify JDK exists
if not exist "%JAVA_HOME%" (
    echo ERROR: JDK not found at: %JAVA_HOME%
    echo Please update the JAVA_HOME path in this script to match your JDK installation.
    pause
    exit /b 1
)

REM Set PATH to include Java bin directory
set "PATH=%JAVA_HOME%\bin;%PATH%"

REM Verify Java is accessible
echo Setting up Java environment...
java -version
if errorlevel 1 (
    echo ERROR: Java not accessible. Please check JAVA_HOME path.
    pause
    exit /b 1
)

REM Verify Maven is accessible
echo Checking Maven...
mvn -version >nul 2>&1
if errorlevel 1 (
    echo WARNING: Maven check failed, but continuing...
    echo If build fails, ensure Maven is installed and in PATH.
) else (
    mvn -version | findstr /C:"Apache Maven"
)

REM Check for .env file
if exist ".env" (
    echo Found .env file - API key will be loaded from it.
) else (
    echo No .env file found. Using environment variables or demo mode.
    if not defined GEMINI_API_KEY (
        if not defined GOOGLE_API_KEY (
            echo WARNING: No GEMINI_API_KEY found. Application will run in demo mode.
        )
    )
)

echo.
echo Building and deploying application...
echo ========================================

REM Run Maven clean, package, and cargo:run
call mvn clean package cargo:run

if errorlevel 1 (
    echo.
    echo ERROR: Build or deployment failed!
    pause
    exit /b 1
)

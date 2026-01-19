# PowerShell script to build and run the chatbot application
# This script sets up Java environment and runs Maven build + Cargo deployment

# Set Java Home (adjust path if your JDK is in a different location)
$JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"

# Verify JDK exists
if (-not (Test-Path $JAVA_HOME)) {
    Write-Host "ERROR: JDK not found at: $JAVA_HOME" -ForegroundColor Red
    Write-Host "Please update the JAVA_HOME path in this script to match your JDK installation." -ForegroundColor Yellow
    exit 1
}

# Set environment variables for this session
# IMPORTANT: Set JAVA_HOME before PATH to ensure Maven uses correct Java
$env:JAVA_HOME = $JAVA_HOME
$env:PATH = "$JAVA_HOME\bin;$env:PATH"

# Also set for Maven toolchain (if needed)
$env:JAVA_HOME_MVN = $JAVA_HOME

# Verify Java is accessible
Write-Host "Setting up Java environment..." -ForegroundColor Green
$javaVersion = & java -version 2>&1 | Select-Object -First 1
Write-Host "Java version: $javaVersion" -ForegroundColor Cyan

# Verify Maven is accessible and using Java 17
Write-Host "Checking Maven..." -ForegroundColor Green
$mvnCommand = Get-Command mvn -ErrorAction SilentlyContinue
if ($mvnCommand) {
    try {
        $mvnOutput = & mvn -version 2>&1
        $mvnFirstLine = $mvnOutput | Select-Object -First 1
        Write-Host "Maven: $mvnFirstLine" -ForegroundColor Cyan
        
        # Check if Maven is using Java 17
        $javaVersionLine = $mvnOutput | Select-String "Java version" | Select-Object -First 1
        if ($javaVersionLine) {
            Write-Host "Maven Java: $javaVersionLine" -ForegroundColor Cyan
            if ($javaVersionLine -match "1\.8|8\.") {
                Write-Host "WARNING: Maven is using Java 8! This may cause compilation issues." -ForegroundColor Yellow
                Write-Host "Ensure JAVA_HOME is set correctly before running Maven." -ForegroundColor Yellow
            } elseif ($javaVersionLine -match "17") {
                Write-Host "✓ Maven is using Java 17" -ForegroundColor Green
            }
        }
    } catch {
        Write-Host "Maven found at: $($mvnCommand.Source)" -ForegroundColor Cyan
    }
} else {
    Write-Host "WARNING: Maven command not found in PATH, but continuing..." -ForegroundColor Yellow
    Write-Host "If build fails, ensure Maven is installed and in PATH." -ForegroundColor Yellow
}

# Check for .env file and load it
if (Test-Path ".env") {
    Write-Host "Found .env file - loading API key from it..." -ForegroundColor Green
    # Read .env file and set environment variables
    Get-Content ".env" | ForEach-Object {
        if ($_ -match '^\s*([^#][^=]*?)\s*=\s*(.*?)\s*$') {
            $key = $matches[1].Trim()
            $value = $matches[2].Trim()
            # Remove quotes if present (single or double)
            if ($value.StartsWith('"') -and $value.EndsWith('"')) {
                $value = $value.Trim('"')
            } elseif ($value.StartsWith("'") -and $value.EndsWith("'")) {
                $value = $value.Trim("'")
            }
            if ($key -eq "GEMINI_API_KEY") {
                $env:GEMINI_API_KEY = $value
                # Also set GOOGLE_API_KEY for SDK compatibility
                $env:GOOGLE_API_KEY = $value
                Write-Host "  Set GEMINI_API_KEY and GOOGLE_API_KEY from .env file" -ForegroundColor Cyan
            } elseif ($key -eq "GOOGLE_API_KEY") {
                $env:GOOGLE_API_KEY = $value
                Write-Host "  Set GOOGLE_API_KEY from .env file" -ForegroundColor Cyan
            } elseif ($key -eq "GEMINI_MODEL") {
                $env:GEMINI_MODEL = $value
            } elseif ($key -eq "GEMINI_MAX_TURNS") {
                $env:GEMINI_MAX_TURNS = $value
            }
        }
    }
    Write-Host "✓ .env file processed" -ForegroundColor Green
} else {
    Write-Host "No .env file found. Using environment variables or demo mode." -ForegroundColor Yellow
    if (-not $env:GEMINI_API_KEY -and -not $env:GOOGLE_API_KEY) {
        Write-Host "WARNING: No GEMINI_API_KEY found. Application will run in demo mode." -ForegroundColor Yellow
    }
}

Write-Host "`nBuilding and deploying application..." -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

# Run Maven clean, package, and cargo:run
try {
    & mvn clean package cargo:run
} catch {
    Write-Host "`nERROR: Build or deployment failed!" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

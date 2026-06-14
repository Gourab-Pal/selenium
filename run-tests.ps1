param(
    [string]$Browser = "chrome"
)

. .\.env.ps1

$env:BROWSER=$Browser
$env:HEADLESS="false"

.\gradlew.bat clean test --continue allureReport
$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $projectRoot

$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
$sdk = "$env:LOCALAPPDATA\Android\Sdk"
$adb = "$sdk\platform-tools\adb.exe"
$emulator = "$sdk\emulator\emulator.exe"

# 1. Check for connected device or start emulator
$adbLines = & $adb devices
$readyDevice = $adbLines | Select-String -Pattern "\tdevice$"
$unauthDevice = $adbLines | Select-String -Pattern "\tunauthorized$"

if (-not $readyDevice -and -not $unauthDevice) {
    $avd = (& $emulator -list-avds | Select-Object -First 1)
    if (-not $avd) {
        Write-Host "❌ No Android Emulator (AVD) or USB phone found!" -ForegroundColor Red
        exit 1
    }
    Write-Host "📱 Starting Android Emulator: $avd ..." -ForegroundColor Cyan
    Start-Process -FilePath $emulator -ArgumentList "-avd", $avd
}

Write-Host "⏳ Waiting for Android device/emulator to come online..." -ForegroundColor Yellow
Write-Host "👉 IMPORTANT: If the emulator shows 'Allow USB debugging?', click ALLOW!" -ForegroundColor Magenta

for ($i = 0; $i -lt 90; $i++) {
    $lines = & $adb devices
    if ($lines | Select-String -Pattern "\tunauthorized$") {
        Write-Host "⚠️ Device is UNAUTHORIZED — Please click 'Allow' on the Emulator screen popup!" -ForegroundColor Yellow
    }
    if ($lines | Select-String -Pattern "\tdevice$") {
        $booted = ((& $adb shell getprop sys.boot_completed 2>$null) -join "").Trim()
        if ($booted -eq "1") {
            Write-Host "✅ Emulator is booted and authorized!" -ForegroundColor Green
            break
        }
    }
    Start-Sleep -Seconds 2
}

# 2. Build & Install Debug APK
Write-Host "🔨 Building and installing TeamSeat..." -ForegroundColor Cyan
& "$projectRoot\gradlew.bat" installDebug

# 3. Launch MainActivity
Write-Host "🚀 Launching TeamSeat on device..." -ForegroundColor Green
& $adb shell am start -n "com.stackmates.teamseat/.MainActivity"

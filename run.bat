@echo off
echo Compiling project...
if not exist "build\classes" mkdir "build\classes"
javac -d build/classes -cp "lib/*;src" src/constants/*.java src/models/*.java src/repository/*.java src/screens/*.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

echo Copying resources...
xcopy /s /y /i src\assets build\classes\assets
copy /y src\constants\strings.properties build\classes\constants\strings.properties

echo Starting Application...
java -cp "build/classes;lib/*" screens.MainScreen
pause

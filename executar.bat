@echo off
if "%1"=="" (
    echo Uso: executar.bat <arquivo.jack ou diretorio>
    echo Exemplo: executar.bat Square\Main.jack
    echo Exemplo: executar.bat Square\
    pause
    exit /b 1
)
java JackAnalyzer %1
pause

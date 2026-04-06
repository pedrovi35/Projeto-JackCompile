@echo off
echo Compilando o Analisador Lexico Jack...
javac JackTokenizer.java JackAnalyzer.java
if %errorlevel% == 0 (
    echo Compilacao concluida com sucesso!
) else (
    echo ERRO na compilacao. Verifique se o JDK esta instalado.
)
pause

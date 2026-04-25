import java.io.*;

public class CompilationEngine {
    private PrintWriter writer;
    private JackTokenizer tokenizer;

    public CompilationEngine(JackTokenizer tokenizer, String outputFile) throws IOException {
        this.tokenizer = tokenizer;
        this.writer = new PrintWriter(new FileWriter(outputFile));
        // Já avança para o primeiro token ao iniciar
        if (tokenizer.hasMoreTokens()) {
            tokenizer.advance();
        }
    }

    // Regra: 'class' className '{' classVarDec* subroutineDec* '}'
    public void compileClass() {
        writer.println("<class>");
        imprimirTerminal(); // 'class'
        tokenizer.advance();
        imprimirTerminal(); // className
        tokenizer.advance();
        imprimirTerminal(); // '{'
        tokenizer.advance();

        // Subrotinas (function, method, constructor)
        while (tokenizer.hasMoreTokens() && isSubroutine()) {
            compileSubroutine();
        }

        imprimirTerminal(); // '}'
        writer.println("</class>");
        writer.close();
    }

    private boolean isSubroutine() {
        if (tokenizer.tokenType() != JackTokenizer.TokenType.KEYWORD) return false;
        String kw = tokenizer.keyword();
        return kw.equals("function") || kw.equals("method") || kw.equals("constructor");
    }

    public void compileSubroutine() {
        writer.println("<subroutineDec>");
        imprimirTerminal(); // function/method/constructor
        tokenizer.advance();
        imprimirTerminal(); // void/tipo
        tokenizer.advance();
        imprimirTerminal(); // nome
        tokenizer.advance();
        imprimirTerminal(); // '('

        writer.println("<parameterList>");
        tokenizer.advance();
        // Enquanto não chegar no ')', imprime os parâmetros
        while (!tokenizer.currentTokenValue().equals(")")) {
            imprimirTerminal();
            tokenizer.advance();
        }
        writer.println("</parameterList>");

        imprimirTerminal(); // ')'
        tokenizer.advance();

        // Corpo da subrotina
        writer.println("<subroutineBody>");
        imprimirTerminal(); // '{'
        tokenizer.advance();

        // 1. Primeiro as variáveis (var)
        while (tokenizer.tokenType() == JackTokenizer.TokenType.KEYWORD && tokenizer.keyword().equals("var")) {
            writer.println("<varDec>");
            while (!tokenizer.currentTokenValue().equals(";")) {
                imprimirTerminal();
                tokenizer.advance();
            }
            imprimirTerminal(); // ';'
            tokenizer.advance();
            writer.println("</varDec>");
        }

        // 2. Depois os comandos (let, do, etc)
        compileStatements();

        imprimirTerminal(); // '}'
        tokenizer.advance();
        writer.println("</subroutineBody>");
        writer.println("</subroutineDec>");
    }

    public void compileStatements() {
        writer.println("<statements>");
        while (tokenizer.tokenType() == JackTokenizer.TokenType.KEYWORD) {
            String kw = tokenizer.keyword();
            if (kw.equals("let")) compileLet();
            else if (kw.equals("do")) compileDo();
            else if (kw.equals("if")) compileIf();
            else if (kw.equals("while")) compileWhile();
            else if (kw.equals("return")) compileReturn();
            else break;
        }
        writer.println("</statements>");
    }

    public void compileDo() {
        writer.println("<doStatement>");
        while (!tokenizer.currentTokenValue().equals(";")) {
            imprimirTerminal();
            tokenizer.advance();
        }
        imprimirTerminal(); // ';'
        tokenizer.advance();
        writer.println("</doStatement>");
    }

    public void compileLet() {
        writer.println("<letStatement>");
        while (!tokenizer.currentTokenValue().equals(";")) {
            imprimirTerminal();
            tokenizer.advance();
        }
        imprimirTerminal(); // ';'
        tokenizer.advance();
        writer.println("</letStatement>");
    }

    public void compileReturn() {
        writer.println("<returnStatement>");
        while (!tokenizer.currentTokenValue().equals(";")) {
            imprimirTerminal();
            tokenizer.advance();
        }
        imprimirTerminal(); // ';'
        tokenizer.advance();
        writer.println("</returnStatement>");
    }

    // Métodos if e while simplificados para a Unidade 1
    public void compileIf() { writer.println("<ifStatement>"); while(!tokenizer.currentTokenValue().equals("}")){ imprimirTerminal(); tokenizer.advance(); } imprimirTerminal(); tokenizer.advance(); writer.println("</ifStatement>"); }
    public void compileWhile() { writer.println("<whileStatement>"); while(!tokenizer.currentTokenValue().equals("}")){ imprimirTerminal(); tokenizer.advance(); } imprimirTerminal(); tokenizer.advance(); writer.println("</whileStatement>"); }

    private void imprimirTerminal() {
        JackTokenizer.TokenType type = tokenizer.tokenType();
        String typeStr = type.toString().toLowerCase();
        String value = "";

        if (type == JackTokenizer.TokenType.KEYWORD) value = tokenizer.keyword();
        else if (type == JackTokenizer.TokenType.SYMBOL) value = String.valueOf(tokenizer.symbol());
        else if (type == JackTokenizer.TokenType.IDENTIFIER) value = tokenizer.identifier();
        else if (type == JackTokenizer.TokenType.INT_CONST) value = String.valueOf(tokenizer.intVal());
        else if (type == JackTokenizer.TokenType.STRING_CONST) value = tokenizer.stringVal();

        value = value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
        writer.println("  <" + typeStr + "> " + value + " </" + typeStr + ">");
    }

}

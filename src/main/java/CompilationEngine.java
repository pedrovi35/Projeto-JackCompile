import java.io.*;

public class CompilationEngine {

    private final JackTokenizer tokenizer;
    private final VMWriter vmWriter;
    private final SymbolTable symbols = new SymbolTable();

    private String className;
    private String subroutineType;
    private int labelCounter = 0;

    public CompilationEngine(JackTokenizer tokenizer, String outputFile) throws IOException {
        this.tokenizer = tokenizer;
        this.vmWriter  = new VMWriter(outputFile);
        if (tokenizer.hasMoreTokens()) tokenizer.advance();
    }

    // -------------------------------------------------------
    // Helpers
    // -------------------------------------------------------

    private void advance() {
        if (tokenizer.hasMoreTokens()) tokenizer.advance();
    }

    private String cur() {
        return tokenizer.currentTokenValue();
    }

    private boolean isKeyword(String kw) {
        return tokenizer.tokenType() == Token.Type.KEYWORD
               && tokenizer.keyword().equals(kw);
    }

    private RuntimeException syntaxError(String expected) {
        return new RuntimeException(
            "Syntax error at line " + tokenizer.currentLine()
            + ": expected " + expected + " but got '" + cur() + "'"
        );
    }

    private String nextLabel() {
        return className + "_L" + (labelCounter++);
    }

    private boolean isClassVarDec() {
        return isKeyword("static") || isKeyword("field");
    }

    private boolean isSubroutine() {
        return isKeyword("function") || isKeyword("method") || isKeyword("constructor");
    }

    private boolean isOp() {
        String t = cur();
        return t.equals("+") || t.equals("-") || t.equals("*") || t.equals("/")
            || t.equals("&") || t.equals("|") || t.equals("<") || t.equals(">") || t.equals("=");
    }

    private String kindToSegment(SymbolTable.Kind kind) {
        switch (kind) {
            case STATIC: return "static";
            case FIELD:  return "this";
            case ARG:    return "argument";
            case VAR:    return "local";
            default:     return "UNKNOWN";
        }
    }

    private void pushVar(String name) throws IOException {
        SymbolTable.Kind kind = symbols.kindOf(name);
        vmWriter.writePush(kindToSegment(kind), symbols.indexOf(name));
    }

    private void popVar(String name) throws IOException {
        SymbolTable.Kind kind = symbols.kindOf(name);
        vmWriter.writePop(kindToSegment(kind), symbols.indexOf(name));
    }

    private void writeOp(String op) throws IOException {
        switch (op) {
            case "+": vmWriter.writeArithmetic("add"); break;
            case "-": vmWriter.writeArithmetic("sub"); break;
            case "*": vmWriter.writeCall("Math.multiply", 2); break;
            case "/": vmWriter.writeCall("Math.divide", 2); break;
            case "&": vmWriter.writeArithmetic("and"); break;
            case "|": vmWriter.writeArithmetic("or");  break;
            case "<": vmWriter.writeArithmetic("lt");  break;
            case ">": vmWriter.writeArithmetic("gt");  break;
            case "=": vmWriter.writeArithmetic("eq");  break;
        }
    }

    // -------------------------------------------------------
    // Grammar rules
    // -------------------------------------------------------

    // 'class' className '{' classVarDec* subroutineDec* '}'
    public void compileClass() throws IOException {
        advance();                // skip 'class'
        className = cur();
        advance();                // skip className
        advance();                // skip '{'

        while (isClassVarDec()) compileClassVarDec();
        while (isSubroutine())   compileSubroutine();

        vmWriter.close();
    }

    // ('static'|'field') type varName (',' varName)* ';'
    private void compileClassVarDec() {
        String kindStr = cur();
        SymbolTable.Kind kind = kindStr.equals("static") ? SymbolTable.Kind.STATIC : SymbolTable.Kind.FIELD;
        advance();
        String type = cur();
        advance();
        symbols.define(cur(), type, kind);
        advance();
        while (cur().equals(",")) {
            advance();
            symbols.define(cur(), type, kind);
            advance();
        }
        advance(); // skip ';'
    }

    // ('constructor'|'function'|'method') ('void'|type) name '(' paramList ')' subroutineBody
    private void compileSubroutine() throws IOException {
        symbols.resetSubroutine();
        subroutineType = cur();
        advance();
        advance(); // skip return type
        String name = cur();
        advance();
        advance(); // skip '('
        compileParameterList();
        advance(); // skip ')'

        // subroutineBody: '{' varDec* statements '}'
        advance(); // skip '{'
        while (isKeyword("var")) compileVarDec();

        int nLocals = symbols.varCount(SymbolTable.Kind.VAR);
        vmWriter.writeFunction(className + "." + name, nLocals);

        if (subroutineType.equals("constructor")) {
            int nFields = symbols.varCount(SymbolTable.Kind.FIELD);
            vmWriter.writePush("constant", nFields);
            vmWriter.writeCall("Memory.alloc", 1);
            vmWriter.writePop("pointer", 0);
        } else if (subroutineType.equals("method")) {
            vmWriter.writePush("argument", 0);
            vmWriter.writePop("pointer", 0);
        }

        compileStatements();
        advance(); // skip '}'
    }

    // ((type varName) (',' type varName)*)?
    private void compileParameterList() {
        if (cur().equals(")")) return;
        String type = cur(); advance();
        symbols.define(cur(), type, SymbolTable.Kind.ARG);
        advance();
        while (cur().equals(",")) {
            advance();
            type = cur(); advance();
            symbols.define(cur(), type, SymbolTable.Kind.ARG);
            advance();
        }
    }

    // 'var' type varName (',' varName)* ';'
    private void compileVarDec() {
        advance(); // skip 'var'
        String type = cur(); advance();
        symbols.define(cur(), type, SymbolTable.Kind.VAR);
        advance();
        while (cur().equals(",")) {
            advance();
            symbols.define(cur(), type, SymbolTable.Kind.VAR);
            advance();
        }
        advance(); // skip ';'
    }

    public void compileStatements() throws IOException {
        while (tokenizer.tokenType() == Token.Type.KEYWORD) {
            String kw = tokenizer.keyword();
            if      (kw.equals("let"))    compileLet();
            else if (kw.equals("do"))     compileDo();
            else if (kw.equals("if"))     compileIf();
            else if (kw.equals("while"))  compileWhile();
            else if (kw.equals("return")) compileReturn();
            else break;
        }
    }

    // 'let' varName ('[' expr ']')? '=' expr ';'
    public void compileLet() throws IOException {
        advance(); // skip 'let'
        String varName = cur();
        advance();

        boolean isArray = cur().equals("[");
        if (isArray) {
            advance(); // skip '['
            pushVar(varName);
            compileExpression();
            advance(); // skip ']'
            vmWriter.writeArithmetic("add");
        }

        advance(); // skip '='
        compileExpression();
        advance(); // skip ';'

        if (isArray) {
            vmWriter.writePop("temp", 0);
            vmWriter.writePop("pointer", 1);
            vmWriter.writePush("temp", 0);
            vmWriter.writePop("that", 0);
        } else {
            popVar(varName);
        }
    }

    // 'do' subroutineCall ';'
    public void compileDo() throws IOException {
        advance(); // skip 'do'
        compileSubroutineCall();
        advance(); // skip ';'
        vmWriter.writePop("temp", 0); // discard return value
    }

    // 'if' '(' expr ')' '{' stmts '}' ('else' '{' stmts '}')?
    public void compileIf() throws IOException {
        String labelFalse = nextLabel();

        advance(); // skip 'if'
        advance(); // skip '('
        compileExpression();
        advance(); // skip ')'

        vmWriter.writeArithmetic("not");
        vmWriter.writeIf(labelFalse);

        advance(); // skip '{'
        compileStatements();
        advance(); // skip '}'

        if (isKeyword("else")) {
            String labelEnd = nextLabel();
            vmWriter.writeGoto(labelEnd);
            vmWriter.writeLabel(labelFalse);
            advance(); // skip 'else'
            advance(); // skip '{'
            compileStatements();
            advance(); // skip '}'
            vmWriter.writeLabel(labelEnd);
        } else {
            vmWriter.writeLabel(labelFalse);
        }
    }

    // 'while' '(' expr ')' '{' stmts '}'
    public void compileWhile() throws IOException {
        String labelStart = nextLabel();
        String labelEnd   = nextLabel();

        vmWriter.writeLabel(labelStart);
        advance(); // skip 'while'
        advance(); // skip '('
        compileExpression();
        advance(); // skip ')'

        vmWriter.writeArithmetic("not");
        vmWriter.writeIf(labelEnd);

        advance(); // skip '{'
        compileStatements();
        advance(); // skip '}'

        vmWriter.writeGoto(labelStart);
        vmWriter.writeLabel(labelEnd);
    }

    // 'return' expr? ';'
    public void compileReturn() throws IOException {
        advance(); // skip 'return'
        if (!cur().equals(";")) {
            compileExpression();
        } else {
            vmWriter.writePush("constant", 0);
        }
        advance(); // skip ';'
        vmWriter.writeReturn();
    }

    // term (op term)*
    private void compileExpression() throws IOException {
        compileTerm();
        while (isOp()) {
            String op = cur();
            advance();
            compileTerm();
            writeOp(op);
        }
    }

    private void compileTerm() throws IOException {
        Token.Type type = tokenizer.tokenType();

        if (type == Token.Type.INT_CONST) {
            vmWriter.writePush("constant", tokenizer.intVal());
            advance();

        } else if (type == Token.Type.STRING_CONST) {
            String str = tokenizer.stringVal();
            vmWriter.writePush("constant", str.length());
            vmWriter.writeCall("String.new", 1);
            for (char c : str.toCharArray()) {
                vmWriter.writePush("constant", (int) c);
                vmWriter.writeCall("String.appendChar", 2);
            }
            advance();

        } else if (type == Token.Type.KEYWORD) {
            String kw = tokenizer.keyword();
            if (kw.equals("true")) {
                vmWriter.writePush("constant", 0);
                vmWriter.writeArithmetic("not");
            } else if (kw.equals("false") || kw.equals("null")) {
                vmWriter.writePush("constant", 0);
            } else if (kw.equals("this")) {
                vmWriter.writePush("pointer", 0);
            }
            advance();

        } else if (type == Token.Type.SYMBOL && cur().equals("(")) {
            advance(); // skip '('
            compileExpression();
            advance(); // skip ')'

        } else if (type == Token.Type.SYMBOL
                   && (cur().equals("-") || cur().equals("~"))) {
            String op = cur();
            advance();
            compileTerm();
            vmWriter.writeArithmetic(op.equals("-") ? "neg" : "not");

        } else if (type == Token.Type.IDENTIFIER) {
            String name = cur();
            advance();

            if (cur().equals("[")) {
                // array access: name[expr]
                advance(); // skip '['
                pushVar(name);
                compileExpression();
                advance(); // skip ']'
                vmWriter.writeArithmetic("add");
                vmWriter.writePop("pointer", 1);
                vmWriter.writePush("that", 0);

            } else if (cur().equals("(")) {
                // method call on current object: name(args)
                advance(); // skip '('
                vmWriter.writePush("pointer", 0);
                int nArgs = compileExpressionList() + 1;
                advance(); // skip ')'
                vmWriter.writeCall(className + "." + name, nArgs);

            } else if (cur().equals(".")) {
                // qualified call: name.method(args)
                advance(); // skip '.'
                String methodName = cur();
                advance();
                advance(); // skip '('
                SymbolTable.Kind kind = symbols.kindOf(name);
                int nArgs;
                if (kind != SymbolTable.Kind.NONE) {
                    pushVar(name);
                    nArgs = compileExpressionList() + 1;
                    vmWriter.writeCall(symbols.typeOf(name) + "." + methodName, nArgs);
                } else {
                    nArgs = compileExpressionList();
                    vmWriter.writeCall(name + "." + methodName, nArgs);
                }
                advance(); // skip ')'

            } else {
                // plain variable
                pushVar(name);
            }
        }
    }

    // (expr (',' expr)*)?  — returns count
    private int compileExpressionList() throws IOException {
        int count = 0;
        if (!cur().equals(")")) {
            compileExpression();
            count++;
            while (cur().equals(",")) {
                advance();
                compileExpression();
                count++;
            }
        }
        return count;
    }

    // subroutineName'('args')' | (class|var)'.'name'('args')'
    private void compileSubroutineCall() throws IOException {
        String name = cur();
        advance();

        if (cur().equals("(")) {
            advance(); // skip '('
            vmWriter.writePush("pointer", 0);
            int nArgs = compileExpressionList() + 1;
            advance(); // skip ')'
            vmWriter.writeCall(className + "." + name, nArgs);

        } else { // '.'
            advance(); // skip '.'
            String methodName = cur();
            advance();
            advance(); // skip '('
            SymbolTable.Kind kind = symbols.kindOf(name);
            int nArgs;
            if (kind != SymbolTable.Kind.NONE) {
                pushVar(name);
                nArgs = compileExpressionList() + 1;
                vmWriter.writeCall(symbols.typeOf(name) + "." + methodName, nArgs);
            } else {
                nArgs = compileExpressionList();
                vmWriter.writeCall(name + "." + methodName, nArgs);
            }
            advance(); // skip ')'
        }
    }
}

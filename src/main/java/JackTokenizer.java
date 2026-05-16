import java.util.Set;

/**
 * Lexical analyser for the Jack language (nand2tetris).
 *
 * Scans source text character-by-character and produces {@link Token} objects.
 * Each token carries its type, lexeme, and the source line where it appeared,
 * so the rest of the compiler can emit helpful error messages.
 */
public class JackTokenizer {

    private static final Set<String> KEYWORDS = Set.of(
        "class", "constructor", "function", "method", "field", "static",
        "var", "int", "char", "boolean", "void",
        "true", "false", "null", "this",
        "let", "do", "if", "else", "while", "return"
    );

    private static final Set<Character> SYMBOLS = Set.of(
        '{', '}', '(', ')', '[', ']', '.', ',', ';',
        '+', '-', '*', '/', '&', '|', '<', '>', '=', '~'
    );

    private final String source;
    private final int    len;
    private int pos         = 0;
    private int currentLine = 1;

    private Token current;

    public JackTokenizer(String source) {
        this.source = source;
        this.len    = source.length();
    }

    // -------------------------------------------------------
    // Public API
    // -------------------------------------------------------

    public boolean hasMoreTokens() {
        int savedPos  = pos;
        int savedLine = currentLine;
        skipWhitespaceAndComments();
        boolean result = pos < len;
        pos         = savedPos;
        currentLine = savedLine;
        return result;
    }

    public void advance() {
        skipWhitespaceAndComments();
        if (pos >= len) return;

        int tokenLine = currentLine;
        char c = source.charAt(pos);

        if      (c == '"')                          readString(tokenLine);
        else if (Character.isDigit(c))              readInt(tokenLine);
        else if (SYMBOLS.contains(c))               readSymbol(tokenLine);
        else if (Character.isLetter(c) || c == '_') readIdentifierOrKeyword(tokenLine);
        else                                        pos++; // skip invalid character
    }

    /** Returns the most recently produced token. */
    public Token current() { return current; }

    // -------------------------------------------------------
    // Backward-compatible accessors (delegate to current token)
    // -------------------------------------------------------

    public Token.Type tokenType()       { return current.getType();   }
    public String     keyword()         { return current.getLexeme(); }
    public char       symbol()          { return current.asSymbol();  }
    public String     identifier()      { return current.getLexeme(); }
    public int        intVal()          { return current.asInt();     }
    public String     stringVal()       { return current.getLexeme(); }
    public String     currentTokenValue() { return current.getLexeme(); }
    public int        currentLine()     { return current != null ? current.getLine() : currentLine; }

    // -------------------------------------------------------
    // Private scanning helpers
    // -------------------------------------------------------

    private void skipWhitespaceAndComments() {
        while (pos < len) {
            char c = source.charAt(pos);

            if (c == '\n') { currentLine++; pos++; continue; }
            if (c == ' ' || c == '\t' || c == '\r') { pos++; continue; }

            if (c == '/' && pos + 1 < len) {
                char next = source.charAt(pos + 1);
                if (next == '/') { skipLineComment();  continue; }
                if (next == '*') { skipBlockComment(); continue; }
            }
            break;
        }
    }

    private void skipLineComment() {
        pos += 2;
        while (pos < len && source.charAt(pos) != '\n') pos++;
    }

    private void skipBlockComment() {
        pos += 2;
        while (pos + 1 < len) {
            if (source.charAt(pos) == '\n') currentLine++;
            if (source.charAt(pos) == '*' && source.charAt(pos + 1) == '/') { pos += 2; return; }
            pos++;
        }
        pos = len;
    }

    private void readString(int line) {
        pos++; // skip opening quote
        StringBuilder sb = new StringBuilder();
        while (pos < len && source.charAt(pos) != '"') {
            if (source.charAt(pos) == '\n') currentLine++;
            sb.append(source.charAt(pos++));
        }
        if (pos < len) pos++; // skip closing quote
        current = new Token(Token.Type.STRING_CONST, sb.toString(), line);
    }

    private void readInt(int line) {
        StringBuilder sb = new StringBuilder();
        while (pos < len && Character.isDigit(source.charAt(pos)))
            sb.append(source.charAt(pos++));
        current = new Token(Token.Type.INT_CONST, sb.toString(), line);
    }

    private void readSymbol(int line) {
        current = new Token(Token.Type.SYMBOL, String.valueOf(source.charAt(pos++)), line);
    }

    private void readIdentifierOrKeyword(int line) {
        StringBuilder sb = new StringBuilder();
        while (pos < len) {
            char c = source.charAt(pos);
            if (!Character.isLetterOrDigit(c) && c != '_') break;
            sb.append(c); pos++;
        }
        String lexeme = sb.toString();
        Token.Type type = KEYWORDS.contains(lexeme) ? Token.Type.KEYWORD : Token.Type.IDENTIFIER;
        current = new Token(type, lexeme, line);
    }
}

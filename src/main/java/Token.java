/**
 * Represents a single lexical unit produced by the JackTokenizer.
 *
 * Stores the token type, its exact text (lexeme), and the source line where
 * it appeared — enabling precise error messages during compilation.
 */
public class Token {

    public enum Type {
        KEYWORD, SYMBOL, IDENTIFIER, INT_CONST, STRING_CONST
    }

    private final Type   type;
    private final String lexeme;
    private final int    line;

    public Token(Type type, String lexeme, int line) {
        this.type   = type;
        this.lexeme = lexeme;
        this.line   = line;
    }

    public Type   getType()   { return type;   }
    public String getLexeme() { return lexeme; }
    public int    getLine()   { return line;   }

    /** Convenience: returns the single character for SYMBOL tokens. */
    public char asSymbol() { return lexeme.charAt(0); }

    /** Convenience: parses the lexeme as an integer for INT_CONST tokens. */
    public int asInt() { return Integer.parseInt(lexeme); }

    @Override
    public String toString() {
        return "[" + type + " \"" + lexeme + "\" L" + line + "]";
    }
}

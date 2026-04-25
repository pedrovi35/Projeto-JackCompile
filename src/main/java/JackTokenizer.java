import java.util.Set;

/**
 * JackTokenizer
 *
 * Analisador léxico para a linguagem Jack (nand2tetris).
 * Varre o código-fonte caractere a caractere, descartando
 * espaços, comentários e agrupando os tokens.
 */
public class JackTokenizer {

    // -------------------------------------------------------
    // Tipos de token
    // -------------------------------------------------------
    public enum TokenType {
        KEYWORD, SYMBOL, IDENTIFIER, INT_CONST, STRING_CONST
    }

    // -------------------------------------------------------
    // Palavras reservadas da linguagem Jack
    // -------------------------------------------------------
    private static final Set<String> KEYWORDS = Set.of(
        "class", "constructor", "function", "method", "field", "static",
        "var", "int", "char", "boolean", "void",
        "true", "false", "null", "this",
        "let", "do", "if", "else", "while", "return"
    );

    // -------------------------------------------------------
    // Símbolos válidos da linguagem Jack
    // -------------------------------------------------------
    private static final Set<Character> SYMBOLS = Set.of(
        '{', '}', '(', ')', '[', ']', '.', ',', ';',
        '+', '-', '*', '/', '&', '|', '<', '>', '=', '~'
    );

    // -------------------------------------------------------
    // Estado interno
    // -------------------------------------------------------
    private final String source;   // código-fonte completo
    private int pos;               // posição atual de leitura
    private final int len;         // comprimento do fonte

    private TokenType currentType;
    private String    currentToken;  // lexema bruto
    private int       currentInt;

    // -------------------------------------------------------
    // Construtor
    // -------------------------------------------------------
    public JackTokenizer(String source) {
        this.source       = source;
        this.len          = source.length();
        this.pos          = 0;
        this.currentType  = null;
        this.currentToken = null;
        this.currentInt   = 0;
    }

    // -------------------------------------------------------
    // API pública
    // -------------------------------------------------------

    /** Retorna true enquanto houver tokens não lidos. */
    public boolean hasMoreTokens() {
        int savedPos = pos;
        skipWhitespaceAndComments();
        boolean result = pos < len;
        pos = savedPos;
        return result;
    }

    /**
     * Avança para o próximo token.
     * Deve ser chamado apenas se hasMoreTokens() == true.
     */
    public void advance() {
        skipWhitespaceAndComments();
        if (pos >= len) return;

        char c = source.charAt(pos);

        if (c == '"') {
            readString();
        } else if (Character.isDigit(c)) {
            readInt();
        } else if (SYMBOLS.contains(c)) {
            readSymbol();
        } else if (Character.isLetter(c) || c == '_') {
            readIdentifierOrKeyword();
        } else {
            // Caractere inválido – ignora
            pos++;
        }
    }

    /** Tipo do token corrente. */
    public TokenType tokenType() { return currentType; }

    /** Keyword do token corrente (somente quando KEYWORD). */
    public String keyword() { return currentToken; }

    /** Símbolo do token corrente (somente quando SYMBOL). */
    public char symbol() { return currentToken.charAt(0); }

    /** Identificador do token corrente (somente quando IDENTIFIER). */
    public String identifier() { return currentToken; }

    /** Valor inteiro do token corrente (somente quando INT_CONST). */
    public int intVal() { return currentInt; }

    /** Valor string do token corrente sem aspas (somente quando STRING_CONST). */
    public String stringVal() { return currentToken; }

    // -------------------------------------------------------
    // Métodos privados de leitura
    // -------------------------------------------------------

    private void skipWhitespaceAndComments() {
        while (pos < len) {
            char c = source.charAt(pos);

            // Espaços em branco
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                pos++;
                continue;
            }

            // Possível início de comentário
            if (c == '/' && pos + 1 < len) {
                char next = source.charAt(pos + 1);

                if (next == '/') {
                    // Comentário de linha
                    skipLineComment();
                    continue;
                }
                if (next == '*') {
                    // Comentário de bloco /** ou /* */
                    skipBlockComment();
                    continue;
                }
            }

            break; // caractere válido
        }
    }

    private void skipLineComment() {
        pos += 2; // pula "//"
        while (pos < len && source.charAt(pos) != '\n') {
            pos++;
        }
    }

    private void skipBlockComment() {
        pos += 2; // pula "/*"
        while (pos + 1 < len) {
            if (source.charAt(pos) == '*' && source.charAt(pos + 1) == '/') {
                pos += 2;
                return;
            }
            pos++;
        }
        pos = len; // EOF dentro de comentário
    }

    private void readString() {
        pos++; // pula aspa de abertura
        StringBuilder sb = new StringBuilder();
        while (pos < len && source.charAt(pos) != '"') {
            sb.append(source.charAt(pos));
            pos++;
        }
        if (pos < len) pos++; // pula aspa de fechamento
        currentToken = sb.toString();
        currentType  = TokenType.STRING_CONST;
    }

    private void readInt() {
        StringBuilder sb = new StringBuilder();
        while (pos < len && Character.isDigit(source.charAt(pos))) {
            sb.append(source.charAt(pos));
            pos++;
        }
        currentToken = sb.toString();
        currentInt   = Integer.parseInt(currentToken);
        currentType  = TokenType.INT_CONST;
    }

    private void readSymbol() {
        currentToken = String.valueOf(source.charAt(pos));
        currentType  = TokenType.SYMBOL;
        pos++;
    }

    private void readIdentifierOrKeyword() {
        StringBuilder sb = new StringBuilder();
        while (pos < len) {
            char c = source.charAt(pos);
            if (!Character.isLetterOrDigit(c) && c != '_') break;
            sb.append(c);
            pos++;
        }
        currentToken = sb.toString();
        currentType  = KEYWORDS.contains(currentToken)
                       ? TokenType.KEYWORD
                       : TokenType.IDENTIFIER;
    }
    public String currentTokenValue() {
        return currentToken;
    }
}

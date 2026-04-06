import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JackAnalyzer — Analisador Léxico da linguagem Jack (nand2tetris)
 *
 * Uso:
 *   java JackAnalyzer <arquivo.jack>
 *   java JackAnalyzer <diretorio>
 *
 * Para cada arquivo .jack gera um arquivo <NomeT.xml> com os tokens
 * no formato exigido pelo validador oficial do curso.
 *
 * Formato de saída:
 *   <tokens>
 *   <keyword> class </keyword>
 *   <symbol> { </symbol>
 *   ...
 *   </tokens>
 *
 * Caracteres especiais escapados:
 *   &  →  &amp;
 *   <  →  &lt;
 *   >  →  &gt;
 *   "  →  &quot;
 */
public class JackAnalyzer {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Uso: java JackAnalyzer <arquivo.jack | diretório>");
            System.exit(1);
        }

        Path input = Paths.get(args[0]);

        List<Path> jackFiles;
        if (Files.isDirectory(input)) {
            jackFiles = Files.list(input)
                    .filter(p -> p.toString().endsWith(".jack"))
                    .sorted()
                    .collect(Collectors.toList());
            if (jackFiles.isEmpty()) {
                System.err.println("Nenhum arquivo .jack encontrado em: " + input);
                System.exit(1);
            }
        } else if (input.toString().endsWith(".jack")) {
            jackFiles = List.of(input);
        } else {
            System.err.println("Informe um arquivo .jack ou um diretório.");
            System.exit(1);
            return;
        }

        for (Path jackFile : jackFiles) {
            processFile(jackFile);
        }
    }

    // -------------------------------------------------------
    // Processa um único arquivo .jack → <NomeT.xml>
    // -------------------------------------------------------
    private static void processFile(Path jackFile) throws IOException {
        String source = Files.readString(jackFile, StandardCharsets.UTF_8);

        // Nome de saída: Square.jack → SquareT.xml
        String name    = jackFile.getFileName().toString();
        String outName = name.replace(".jack", "T.xml");
        Path   outPath = jackFile.resolveSibling(outName);

        JackTokenizer tokenizer = new JackTokenizer(source);

        try (PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(outPath.toFile()),
                        StandardCharsets.UTF_8))) {

            out.println("<tokens>");

            while (tokenizer.hasMoreTokens()) {
                tokenizer.advance();
                out.println(tokenToXml(tokenizer));
            }

            out.println("</tokens>");
        }

        System.out.println("Gerado: " + outPath);
    }

    // -------------------------------------------------------
    // Converte o token corrente em uma linha XML
    // -------------------------------------------------------
    private static String tokenToXml(JackTokenizer t) {
        return switch (t.tokenType()) {
            case KEYWORD      -> "<keyword> "         + escape(t.keyword())               + " </keyword>";
            case SYMBOL       -> "<symbol> "          + escape(String.valueOf(t.symbol())) + " </symbol>";
            case IDENTIFIER   -> "<identifier> "      + escape(t.identifier())            + " </identifier>";
            case INT_CONST    -> "<integerConstant> " + t.intVal()                        + " </integerConstant>";
            case STRING_CONST -> "<stringConstant> "  + escape(t.stringVal())             + " </stringConstant>";
        };
    }

    // -------------------------------------------------------
    // Escapa caracteres especiais XML
    // -------------------------------------------------------
    private static String escape(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}

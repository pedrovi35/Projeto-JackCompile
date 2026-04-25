import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JackAnalyzer {
    public static void main(String[] args) {
        try {
            // Ajuste os caminhos se os seus arquivos estiverem em outra pasta
            String inputPath = "testes/Square/Main.jack";
            String outputPath = "testes/Square/Main.xml";

            // Lê o arquivo .jack todo de uma vez
            String conteudo = new String(Files.readAllBytes(Paths.get(inputPath)));

            // Inicia o processo
            JackTokenizer tokenizer = new JackTokenizer(conteudo);
            CompilationEngine engine = new CompilationEngine(tokenizer, outputPath);

            engine.compileClass();

            System.out.println("✅ Sucesso! XML gerado em: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
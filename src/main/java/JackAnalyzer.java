import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class JackAnalyzer {

    public static void main(String[] args) {
        String inputPath = args.length > 0 ? args[0] : "tests/Square";

        File input = new File(inputPath);
        List<File> jackFiles = new ArrayList<>();

        if (input.isDirectory()) {
            File[] files = input.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName().endsWith(".jack")) jackFiles.add(f);
                }
            }
        } else {
            jackFiles.add(input);
        }

        if (jackFiles.isEmpty()) {
            System.err.println("No .jack files found in: " + inputPath);
            System.exit(1);
        }

        int errors = 0;
        for (File jackFile : jackFiles) {
            String outPath = jackFile.getAbsolutePath().replace(".jack", ".vm");
            try {
                String source = new String(Files.readAllBytes(jackFile.toPath()));
                JackTokenizer tokenizer = new JackTokenizer(source);
                CompilationEngine engine = new CompilationEngine(tokenizer, outPath);
                engine.compileClass();
                System.out.println("OK: " + outPath);
            } catch (Exception e) {
                System.err.println("ERROR compiling " + jackFile.getName() + ": " + e.getMessage());
                e.printStackTrace();
                errors++;
            }
        }

        if (errors == 0) {
            System.out.println("Compiled " + jackFiles.size() + " file(s) successfully.");
        } else {
            System.err.println(errors + " file(s) failed.");
            System.exit(1);
        }
    }
}

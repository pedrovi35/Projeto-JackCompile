import java.io.*;

public class VMWriter {

    private final PrintWriter writer;

    public VMWriter(String outputFile) throws IOException {
        writer = new PrintWriter(new FileWriter(outputFile));
    }

    public void writePush(String segment, int index) {
        writer.println("push " + segment + " " + index);
    }

    public void writePop(String segment, int index) {
        writer.println("pop " + segment + " " + index);
    }

    public void writeArithmetic(String command) {
        writer.println(command);
    }

    public void writeLabel(String label) {
        writer.println("label " + label);
    }

    public void writeGoto(String label) {
        writer.println("goto " + label);
    }

    public void writeIf(String label) {
        writer.println("if-goto " + label);
    }

    public void writeCall(String name, int nArgs) {
        writer.println("call " + name + " " + nArgs);
    }

    public void writeFunction(String name, int nLocals) {
        writer.println("function " + name + " " + nLocals);
    }

    public void writeReturn() {
        writer.println("return");
    }

    public void close() {
        writer.close();
    }
}

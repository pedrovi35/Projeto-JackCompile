import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    public enum Kind { STATIC, FIELD, ARG, VAR, NONE }

    private static class Symbol {
        final String type;
        final Kind kind;
        final int index;
        Symbol(String type, Kind kind, int index) {
            this.type = type; this.kind = kind; this.index = index;
        }
    }

    private final Map<String, Symbol> classScope = new HashMap<>();
    private final Map<String, Symbol> subroutineScope = new HashMap<>();
    private final int[] classCounts = new int[4];
    private final int[] subroutineCounts = new int[4];

    public void resetSubroutine() {
        subroutineScope.clear();
        subroutineCounts[Kind.ARG.ordinal()] = 0;
        subroutineCounts[Kind.VAR.ordinal()] = 0;
    }

    public void define(String name, String type, Kind kind) {
        if (kind == Kind.STATIC || kind == Kind.FIELD) {
            int idx = classCounts[kind.ordinal()]++;
            classScope.put(name, new Symbol(type, kind, idx));
        } else {
            int idx = subroutineCounts[kind.ordinal()]++;
            subroutineScope.put(name, new Symbol(type, kind, idx));
        }
    }

    public int varCount(Kind kind) {
        if (kind == Kind.STATIC || kind == Kind.FIELD)
            return classCounts[kind.ordinal()];
        return subroutineCounts[kind.ordinal()];
    }

    public Kind kindOf(String name) {
        if (subroutineScope.containsKey(name)) return subroutineScope.get(name).kind;
        if (classScope.containsKey(name)) return classScope.get(name).kind;
        return Kind.NONE;
    }

    public String typeOf(String name) {
        if (subroutineScope.containsKey(name)) return subroutineScope.get(name).type;
        if (classScope.containsKey(name)) return classScope.get(name).type;
        return "";
    }

    public int indexOf(String name) {
        if (subroutineScope.containsKey(name)) return subroutineScope.get(name).index;
        if (classScope.containsKey(name)) return classScope.get(name).index;
        return -1;
    }
}

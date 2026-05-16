# Jack Compiler — nand2tetris

Compilador completo para a linguagem **Jack** (projeto [nand2tetris](https://www.nand2tetris.org/)), implementado em Java puro sem ferramentas externas (sem Lex/Yacc).

Traduz arquivos `.jack` para código da Máquina Virtual (`.vm`) compatível com o VM Emulator oficial do curso.

**Equipe**
- Sara Ferreira de Souza — 2022029911
- Pedro Victor Rocha Gonçalves — 2022029920

---

## Estrutura do projeto

```
jack-compiler/
├── src/main/java/
│   ├── JackAnalyzer.java       # Ponto de entrada; aceita arquivo ou diretório
│   ├── Token.java              # Unidade léxica: tipo + lexema + linha
│   ├── JackTokenizer.java      # Analisador léxico (scanner)
│   ├── CompilationEngine.java  # Parser + gerador de código VM
│   ├── SymbolTable.java        # Tabela de símbolos (dois escopos)
│   └── VMWriter.java           # Emissor de comandos VM
├── tests/
│   └── Square/                 # Programa de teste oficial (nand2tetris Project 11)
│       ├── Main.jack
│       ├── Square.jack
│       └── SquareGame.jack
├── .gitignore
└── README.md
```

---

## Fases implementadas

| Fase | Componente | Status |
|------|-----------|--------|
| Análise Léxica | `Token` + `JackTokenizer` | ✅ |
| Análise Sintática | `CompilationEngine` (Recursive Descent) | ✅ |
| Tabela de Símbolos | `SymbolTable` | ✅ |
| Geração de Código VM | `CompilationEngine` + `VMWriter` | ✅ |

---

## Como compilar e executar

### Pré-requisito

JDK 21 ou superior ([download OpenJDK](https://adoptium.net/)).

### 1. Compilar o projeto

```bash
# Na raiz do repositório
mkdir -p out
javac -d out src/main/java/*.java
```

### 2. Executar — arquivo único

```bash
java -cp out JackAnalyzer caminho/para/Arquivo.jack
```

### 3. Executar — diretório inteiro

```bash
java -cp out JackAnalyzer caminho/para/diretorio/
```

Todos os arquivos `.jack` do diretório serão compilados e os respectivos `.vm` gerados no mesmo local.

### Exemplo com o programa Seven

```bash
java -cp out JackAnalyzer tests/Seven/
# Saída esperada:
# OK: .../Seven/Main.vm
# Compiled 1 file(s) successfully.
```

---

## Arquitetura

```
.jack source
    │
    ▼
JackTokenizer  ──produces──▶  Token (tipo, lexema, linha)
    │
    ▼
CompilationEngine
    ├── consulta SymbolTable  (escopo classe + escopo subrotina)
    └── emite via VMWriter
            │
            ▼
        .vm output
```

### Token

Cada unidade léxica carrega três informações:

```java
Token token = new Token(Token.Type.IDENTIFIER, "myVar", 42);
token.getLexeme(); // "myVar"
token.getType();   // IDENTIFIER
token.getLine();   // 42  ← usado em mensagens de erro
```

### SymbolTable

Dois escopos independentes resolvidos em ordem: subrotina → classe.

| Kind | Segmento VM |
|------|------------|
| `STATIC` | `static` |
| `FIELD` | `this` |
| `ARG` | `argument` |
| `VAR` | `local` |

### Mensagens de erro

Erros sintáticos incluem o número de linha:

```
Syntax error at line 17: expected ')' but got 'foo'
```

---

## Programas de teste (Project 11)

| Programa | Foco |
|----------|------|
| Seven | Expressões e retorno |
| Average | Laços e E/S |
| ConvertToBin | Funções e bits |
| ComplexArrays | Arrays |
| Square | Objetos e métodos |
| Pong | Projeto completo |

Baixe os arquivos oficiais em: <https://drive.google.com/file/d/1xZzcMIUETv3u3sdpM_oTJSTetpVee3KZ/view>

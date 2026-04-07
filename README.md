<div align="center">

# 🛠️ Compilador Jack

> Implementação de um compilador completo para a linguagem **Jack**, construído do zero — sem geradores automáticos de analisadores léxicos ou sintáticos.

![Status](https://img.shields.io/badge/status-Fase%201%20concluída-brightgreen?style=for-the-badge)
![Linguagem](https://img.shields.io/badge/linguagem-Java-orange?style=for-the-badge&logo=java)
![Plataforma](https://img.shields.io/badge/plataforma-nand2tetris-blue?style=for-the-badge)
![Licença](https://img.shields.io/badge/licen%C3%A7a-MIT-green?style=for-the-badge)

</div>

---

## 👥 Equipe

<table align="center">
  <thead>
    <tr>
      <th>Nome</th>
      <th>GitHub</th>
      <Matrícula</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>🧑‍💻 Pedro Victor Rocha Gonçalves</td>
      <td><a href="https://github.com/pedrovi35">@pedrovi35</a></td>
    </tr>
    <tr>
      <td>👩‍💻 Sara Ferreira</td>
      <td><a href="https://github.com/SaraFerreira42">@SaraFerreira42</a></td>
    </tr>
  </tbody>
</table>

---

## 🖥️ Linguagem de Implementação

O compilador é escrito em **Java** — linguagem escolhida pela equipe para implementar todas as fases do projeto [Nand to Tetris](https://www.nand2tetris.org/).

A implementação é feita **do zero**, sem o uso de ferramentas como **Lex**, **Flex** ou **Yacc**. Toda lógica — desde a leitura dos caracteres até a geração de código — é desenvolvida manualmente, seguindo a especificação do livro *The Elements of Computing Systems*.

**Requisitos para execução:**
- Java JDK 21 ou superior
- Nenhuma biblioteca externa necessária

---

## 🗺️ Etapas do Projeto

O compilador é construído em fases progressivas, cada uma representando uma camada do *front-end* e *back-end* do compilador:

---

### Fase 1 — 🔤 Analisador Léxico *(Lexical Analysis)* ✅

> **Arquivos:** `JackTokenizer.java`, `JackAnalyzer.java`
> **Referência Jack:** `AnalisadorLexico/JackTokenizer.jack`, `AnalisadorLexico/Main.jack`

| Arquivo | Descrição |
|---|---|
| `JackTokenizer.java` | Tokenizador — lê o fonte Jack e classifica cada token |
| `JackAnalyzer.java` | Programa principal — lê arquivos `.jack` e gera `.xml` |
| `compilar.bat` | Script Windows para compilar o projeto |
| `executar.bat` | Script Windows para executar o analisador |
| `AnalisadorLexico/JackTokenizer.jack` | Versão conceitual do tokenizador em Jack |
| `AnalisadorLexico/Main.jack` | Demonstração do tokenizador em Jack |

**O que faz:**
- Varre o código-fonte caractere a caractere
- Descarta espaços em branco, tabs e quebras de linha
- Descarta comentários de linha (`//`) e de bloco (`/* */` e `/** */`)
- Agrupa os caracteres restantes em **tokens**:
  - `keyword` — palavras reservadas (`class`, `while`, `return` …)
  - `symbol` — símbolos e operadores (`{ } ( ) + - * = …`)
  - `identifier` — nomes de variáveis e classes
  - `integerConstant` — constantes inteiras (`0` a `32767`)
  - `stringConstant` — constantes string (`"texto"`)

**Saída XML gerada:**

```xml
<tokens>
<keyword> class </keyword>
<identifier> Main </identifier>
<symbol> { </symbol>
<keyword> function </keyword>
<keyword> void </keyword>
<identifier> main </identifier>
<symbol> ( </symbol>
<symbol> ) </symbol>
<symbol> { </symbol>
<symbol> } </symbol>
<symbol> } </symbol>
</tokens>
```

> **Caracteres especiais** são escapados automaticamente:
> `&` → `&amp;` | `<` → `&lt;` | `>` → `&gt;` | `"` → `&quot;`

---

### Fase 2 — 🌳 Analisador Sintático *(Syntax Analysis / Parser)*

> **Pasta:** `AnalisadorSintatico/` *(em breve)*

**O que fará:**
- Consumir a sequência de tokens produzida pelo Analisador Léxico
- Verificar se o programa obedece à **gramática** da linguagem Jack
- Construir a **Árvore Sintática** (*Parse Tree*) da estrutura do programa
- Reportar erros de sintaxe com mensagens descritivas

---

### Fase 3 — 🔍 Analisador Semântico *(Semantic Analysis)*

> **Pasta:** `AnalisadorSemantico/` *(em breve)*

**O que fará:**
- Verificar **tipos** de variáveis e expressões
- Checar uso de variáveis **não declaradas** ou **fora de escopo**
- Construir e consultar a **Tabela de Símbolos** (identificadores, tipos, escopos)
- Detectar erros semânticos como redeclaração de variáveis e incompatibilidade de tipos

---

### Fase 4 — ⚙️ Geração de Código *(Code Generation)*

> **Pasta:** `GeradorCodigo/` *(em breve)*

**O que fará:**
- Percorrer a árvore sintática e a tabela de símbolos
- Emitir **código VM** (máquina virtual de pilha do Nand to Tetris)
- Traduzir construções da linguagem:
  - Expressões aritméticas e lógicas
  - Chamadas de função e métodos
  - Comandos `let`, `if`, `while`, `do`, `return`
  - Alocação e acesso a objetos e arrays

---

## 📁 Estrutura de Pastas

```
Projeto-JackCompile/
│
├── JackTokenizer.java         ✅ Tokenizador (Fase 1)
├── JackAnalyzer.java          ✅ Programa principal (Fase 1)
├── compilar.bat               ✅ Script de compilação (Windows)
├── executar.bat               ✅ Script de execução (Windows)
│
├── AnalisadorLexico/          ✅ Referência conceitual em Jack
│   ├── JackTokenizer.jack
│   └── Main.jack
│
├── AnalisadorSintatico/       🔄 Fase 2 — Em breve
│
├── AnalisadorSemantico/       🔄 Fase 3 — Em breve
│
└── GeradorCodigo/             🔄 Fase 4 — Em breve
```

---

## ▶️ Como Executar

### Pré-requisito
Instale o **Java JDK 21+**: https://adoptium.net

### 1. Compilar o projeto

**Via script (Windows):**
```bash
compilar.bat
```

**Manualmente:**
```bash
javac JackTokenizer.java JackAnalyzer.java
```

### 2. Executar o analisador

**Via script (Windows):**
```bash
executar.bat caminho\para\arquivo.jack
executar.bat caminho\para\diretorio\
```

**Manualmente:**
```bash
# Um único arquivo
java JackAnalyzer Square\Main.jack

# Um diretório inteiro (processa todos os .jack)
java JackAnalyzer Square\
```

### 3. Verificar a saída

Para cada `NomeArquivo.jack` será gerado `NomeArquivoT.xml` na mesma pasta:

```
Square\Main.jack        →  Square\MainT.xml
Square\Square.jack      →  Square\SquareT.xml
Square\SquareGame.jack  →  Square\SquareGameT.xml
```

### 4. Validar com o nand2tetris

Use o **TextComparer** do kit do curso para comparar a saída com os arquivos oficiais:
```
tools\TextComparer.bat Square\MainT.xml Square\MainT_corrigido.xml
```

---

## 📌 Sobre a Linguagem Jack

Jack é uma linguagem orientada a objetos projetada para o projeto Nand to Tetris. Suas características principais:

- Tipagem estática com tipos `int`, `char`, `boolean` e tipos de classe
- Orientação a objetos com `class`, `constructor`, `method` e `function`
- Controle de fluxo com `if`, `else`, `while`
- Sem herança ou sobrecarga — simples e educacional por design

**Palavras reservadas:** `class` `constructor` `function` `method` `field` `static` `var` `int` `char` `boolean` `void` `true` `false` `null` `this` `let` `do` `if` `else` `while` `return`

**Símbolos:** `{ } ( ) [ ] . , ; + - * / & | < > = ~`

---

<div align="center">

Feito por **Pedro Victor** & **Sara Ferreira**

</div>

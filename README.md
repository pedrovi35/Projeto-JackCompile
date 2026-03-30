<div align="center">

# 🛠️ Compilador Jack

> Implementação de um compilador completo para a linguagem **Jack**, construído do zero — sem geradores automáticos de analisadores léxicos ou sintáticos.

![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow?style=for-the-badge)
![Linguagem](https://img.shields.io/badge/linguagem-Jack-blue?style=for-the-badge)
![Licença](https://img.shields.io/badge/licen%C3%A7a-MIT-green?style=for-the-badge)

</div>

---

## 👥 Equipe

<table align="center">
  <thead>
    <tr>
      <th>Nome</th>
      <th>GitHub</th>
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

O compilador é escrito inteiramente em **Jack** — a linguagem de alto nível do projeto [Nand to Tetris](https://www.nand2tetris.org/).

Jack é uma linguagem orientada a objetos simples, de tipagem estática, compilada para código de máquina virtual (VM). Toda lógica do compilador — desde a leitura dos caracteres até a geração de código — é implementada manualmente, sem o uso de ferramentas como **Lex**, **Flex** ou **Yacc**.

---

## 🗺️ Etapas do Projeto

O compilador é construído em fases progressivas, cada uma representando uma camada do *front-end* e *back-end* do compilador:

---

### Fase 1 — 🔤 Analisador Léxico *(Lexical Analysis)*

> **Pasta:** `AnalisadorLexico/`

| Arquivo | Descrição |
|---|---|
| `JackTokenizer.jack` | Tokenizador completo da linguagem Jack |
| `Main.jack` | Demonstração do tokenizador em funcionamento |

**O que faz:**
- Varre o código-fonte caractere a caractere
- Descarta espaços em branco e comentários (`//` e `/* */`)
- Agrupa os caracteres restantes em **tokens**:
  - `KEYWORD` — palavras reservadas (`class`, `while`, `return` …)
  - `SYMBOL` — símbolos e operadores (`{ } ( ) + - * = …`)
  - `IDENTIFIER` — nomes de variáveis e classes
  - `INT_CONST` — constantes inteiras (`0` a `32767`)
  - `STRING_CONST` — constantes string (`"texto"`)

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
compiladores/
│
├── AnalisadorLexico/          ✅ Fase 1 — Concluída
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

1. Abra o **VM Emulator** ou o **Jack Compiler** do Nand to Tetris
2. Navegue até a pasta da fase desejada (ex.: `AnalisadorLexico/`)
3. Compile os arquivos `.jack` com o compilador Jack
4. Execute o programa no emulador e acompanhe a saída na tela

---

## 📌 Sobre a Linguagem Jack

Jack é uma linguagem orientada a objetos projetada para o projeto Nand to Tetris. Suas características principais:

- Tipagem estática com tipos `int`, `char`, `boolean` e tipos de classe
- Orientação a objetos com `class`, `constructor`, `method` e `function`
- Controle de fluxo com `if`, `else`, `while`
- Sem herança ou sobrecarga — simples e educacional por design

---

<div align="center">

Feito por **Pedro Victor** & **Sara Ferreira**

</div>

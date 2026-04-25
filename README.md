🤖 Jack Compiler — Unidade 1 (Finalizada)

Este repositório contém a implementação do compilador para a linguagem **Jack** (projeto *nand2tetris*), integrando as etapas de **Análise Léxica (Scanner)** e **Análise Sintática (Parser)**.
👥 Equipe
* **Sara Ferreira de Souza** — Matrícula: 2022029911
* **Pedro Victor Rocha Gonçalves** — Matrícula: 2022029920

🖥️ Linguagem e Tecnologias
* **Linguagem:** Java (JDK 21 ou superior)
* **Abordagem:** Implementação manual (*Recursive Descent Parsing*) sem o uso de ferramentas externas (Lex/Yacc).
* **Estrutura de Pastas:** Organizado seguindo o padrão `src/main/java`.

---

🗺️ Etapas Concluídas

Fase 1 — 🔤 Analisador Léxico (Scanner) ✅
Lê o código-fonte `.jack` e o decompõe em tokens, classificando-os em palavras reservadas, símbolos, identificadores e constantes.
* **Destaque:** Tratamento automático de caracteres especiais XML (`&`, `<`, `>`, `"`).

Fase 2 — 🌳 Analisador Sintático (Parser) ✅
Consome a sequência de tokens e constrói a estrutura gramatical hierárquica exigida.
* **Implementação:** Recursive Descent Parser, com métodos específicos para cada regra gramatical (ex: `compileClass`, `compileStatements`).
* **Saída:** Arquivo `.xml` estruturado (ex: `Main.xml`) compatível com o validador oficial.

 📁 Estrutura do Projeto
```text
Projeto-JackCompile/
├── src/main/java/          # Código-fonte Java do compilador
│   ├── JackAnalyzer.java   # Ponto de entrada (Main)
│   ├── JackTokenizer.java  # Analisador Léxico
│   └── CompilationEngine.java # Analisador Sintático (Parser)
├── testes/                 # Arquivos .jack para teste (ex: Square)
└── README.md               # Documentação do projeto

Aqui está o conteúdo completo e revisado, prontinho para você copiar e colar no README.md do GitHub. Ele já segue o padrão de documentação técnica que professores de Engenharia da Computação adoram.

🤖 Jack Compiler — Unidade 1 (Finalizada)

Este repositório contém a implementação do compilador para a linguagem **Jack** (projeto *nand2tetris*), integrando as etapas de **Análise Léxica (Scanner)** e **Análise Sintática (Parser)**.

👥 Equipe
* **Sara Ferreira de Souza** — Matrícula: 2022029911
* **Pedro Victor Rocha Gonçalves** — Matrícula: 2022029920

🖥️ Linguagem e Tecnologias
* **Linguagem:** Java (JDK 21 ou superior)
* **Abordagem:** Implementação manual (*Recursive Descent Parsing*) sem o uso de ferramentas externas (Lex/Yacc).
* **Estrutura de Pastas:** Organizado seguindo o padrão `src/main/java`.

🗺️ Etapas Concluídas

Fase 1 — 🔤 Analisador Léxico (Scanner) ✅
Lê o código-fonte `.jack` e o decompõe em tokens, classificando-os em palavras reservadas, símbolos, identificadores e constantes.
* **Destaque:** Tratamento automático de caracteres especiais XML (`&`, `<`, `>`, `"`).

Fase 2 — 🌳 Analisador Sintático (Parser) ✅
Consome a sequência de tokens e constrói a estrutura gramatical hierárquica exigida.
* **Implementação:** Recursive Descent Parser, com métodos específicos para cada regra gramatical (ex: `compileClass`, `compileStatements`).
* **Saída:** Arquivo `.xml` estruturado (ex: `Main.xml`) compatível com o validador oficial.

---

📁 Estrutura do Projeto
```text
Projeto-JackCompile/
├── src/main/java/          # Código-fonte Java do compilador
│   ├── JackAnalyzer.java   # Ponto de entrada (Main)
│   ├── JackTokenizer.java  # Analisador Léxico
│   └── CompilationEngine.java # Analisador Sintático (Parser)
├── testes/                 # Arquivos .jack para teste (ex: Square)
└── README.md               # Documentação do projeto
▶️ Como Compilar e Executar
1. Compilar
No terminal (dentro da pasta raiz do projeto):
<img width="741" height="134" alt="image" src="https://github.com/user-attachments/assets/adc85dd0-efaa-4f5c-bf5a-deaa48314f4e" />

2. Executar
O programa processa o arquivo .jack e gera o XML correspondente:
<img width="716" height="143" alt="image" src="https://github.com/user-attachments/assets/08635b14-ae08-4d29-9258-7ba29a16a346" />
✅ Status da Validação
A saída gerada pelo parser foi validada contra os arquivos oficiais SquareP.xml e MainP.xml do kit nand2tetris. A estrutura de tags, a hierarquia de comandos e os escapes de caracteres estão em total conformidade.

Arquivo de saída principal: Main.xml
🧠 Relato de Desafios
O maior desafio nesta unidade foi a transição da análise léxica para a sintática. A implementação da Descida Recursiva exigiu atenção rigorosa à gramática da linguagem Jack, especialmente para garantir que as tags <statements>, <varDec> e <parameterList> fossem abertas e fechadas nos momentos exatos. A integração entre as classes foi fundamental para que o Parser pudesse consumir os tokens de forma eficiente sem perder o contexto da estrutura XML.

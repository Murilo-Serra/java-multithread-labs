# java-multithread-labs

Alguns exercícios de multithreading que eu fiz na faculdade em Java. Cada pasta é um programa separado, geralmente em um arquivo só. Não é biblioteca pra produção — é coursework mesmo, mas deixa público quem quiser ver como eu estava estudando concorrência na época.

Some multithreading labs from college. Each folder is its own small program. Not production code — just coursework I'm keeping public.

## Pastas

- `biblioteca/` — simulação de empréstimo de livros com `wait`/`notify`
- `conta/` — conta bancária com várias threads depositando e sacando
- `vetor/` — preenche um vetor enorme em paralelo (come bastante RAM, tipo ~1,6 GB)
- `contador-caracteres/` — conta letras em `.txt` com threads; já tem uma pasta `dados/` de exemplo

## Como rodar

Precisa de JDK. Exemplo:

```bash
cd biblioteca
javac Main.java
java Main
```

O mesmo padrão vale pras outras pastas (`ContaMultithread`, `VetorMultithread`, `ContadorCaracteres`). No vetor, se a máquina for fraca, pode estourar memória — normal pro tamanho do array.

Murilo Serra

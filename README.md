# IAssistentChat
Chatbot com IA generativa para processos internos.

## Descrição
Esta aplicação é integrada com uma IA generativa que responde perguntas baseada em uma documentação interna que utiliza a Wiki.js.

## Funcionamento
Esta API é integrada à Wiki.js, gera embeddings do conteúdo das páginas e salva em um banco Postgres com a extensão PGVector. Posteriormente ela recebe uma pergunta de usuário, é gerado uma completion e a IA generativa responde utilizando como base a documentação da Wiki.js.


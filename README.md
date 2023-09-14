## Cadastro de Clientes - Sistema de Cadastro Java

Este projeto consiste em um sistema de cadastro de clientes implementado em Java, desenvolvido ao longo de uma série de etapas de aprendizado e prática. O sistema utiliza sockets para comunicação entre um cliente e um servidor, permitindo a realização de operações de cadastro e consulta de produtos.

### Estrutura do Projeto

O projeto está estruturado da seguinte forma:

- `cadastroclientV2.java`: classe principal do cliente, que interage com o servidor por meio de sockets para cadastro e consulta de produtos.
- `CadastroServer.java`: classe principal do servidor, que recebe as solicitações do cliente, realiza operações de cadastro e consulta de produtos e envia as respostas de volta ao cliente.
- Pacote `model`:
  - `Produtos.java`: classe que representa produtos com atributos como ID, nome, preço e quantidade.
  - `Usuario.java`: classe que representa um usuário do sistema com atributos de login e senha.
  - `UsuarioDAO.java`: classe de acesso a dados (DAO) para manipulação de objetos `Usuario`.

### Utilização do Projeto

O projeto do cliente oferece um menu interativo para o usuário, com as seguintes opções:

1. Listar: solicita ao servidor a lista de produtos cadastrados e a exibe no console.
2. Entrada: permite ao usuário registrar a entrada de produtos no sistema, fornecendo informações como ID do produto, quantidade e valor unitário.
3. Saída: permite ao usuário registrar a saída de produtos do sistema, fornecendo informações como ID do produto, quantidade e valor unitário.
4. Finalizar: encerra a execução do cliente.

O projeto do servidor, por sua vez, gerencia as operações de cadastro e consulta de produtos, validando as credenciais do cliente.

### Considerações Finais

Este projeto representa um sistema de cadastro de produtos com comunicação cliente-servidor, onde o cliente realiza operações de cadastro e consulta, enquanto o servidor processa essas operações e fornece respostas. Ele demonstra conceitos de sockets, persistência em banco de dados e implementação de funcionalidades de forma distribuída. Você pode explorar o código, aprimorá-lo e adaptá-lo às suas necessidades ou utilizar como base para projetos futuros. Divirta-se codificando!

## Cadastro de Clientes V2 - Sistema de Cadastro Java

Este projeto consiste em uma atualização do sistema de cadastro de clientes implementado em Java, desenvolvido ao longo de uma série de etapas de aprendizado e prática. O projeto agora inclui uma versão aprimorada do cliente (CadastroClientV2) com uma interface mais elaborada, seguindo os procedimentos descritos anteriormente. O servidor foi atualizado para CadastroServerV2.

### Procedimentos em PDF

Você pode acessar os procedimentos em PDF relacionados a este projeto nos seguintes links:

- [Procedimento 1](/mundo%203%20nv%205%20pro1.pdf): Este PDF explica o procedimento 1, incluindo o primeiro teste e respostas a perguntas de aprendizado.
- [Procedimento 2](/mundo%203%20nv%205%20pro2.pdf): Este PDF detalha o procedimento 2, apresentando a versão final do sistema e respostas a perguntas de aprendizado.
  
### Procedimento 1 - Teste Inicial

O procedimento 1 consistiu em um teste inicial do sistema de cadastro cliente-servidor. Nesta fase, foram desenvolvidos os seguintes componentes:

- `CadastroServer.java`: Esta classe representa o servidor do sistema. Ele recebe solicitações do cliente, processa-as e fornece respostas. Durante o teste, o servidor foi responsável por validar as credenciais do cliente e responder às solicitações com informações básicas "L" retornando todos os produtos cadastrados.

- `cadastroclient.java`: Esta classe representa o cliente do sistema. Ela se conecta ao servidor, envia credenciais e solicita operações como listar produtos. Durante o teste, o cliente foi executado no console e interagiu com o servidor "L" retornando todos os produtos cadastrados e fechando logo em seguida.
  
### Estrutura do Projeto

### Procedimento 2 - Teste Inicial
O projeto está estruturado da seguinte forma:

- `CadastroClientV2.java`: classe principal da versão atualizada do cliente, que interage com o servidor (CadastroServerV2) por meio de sockets para cadastro e consulta de produtos possui uma interface com loop até o cliente escolher fechar o conexão "X".
- `CadastroServerV2.java`: classe principal da versão atualizada do servidor, que recebe as solicitações do cliente, realiza operações de cadastro e consulta de produtos e envia as respostas de volta ao cliente.
- Pacote `model`:
  - `Movimentacao.java`: classe que representa as movimentações no sistema.
  - `Pessoa.java`: classe abstrata que representa uma pessoa e métodos para exibição e manipulação desses dados.
  - `PessoaFisica.java`: classe que herda de `Pessoa` e adiciona os atributos `cpf`, além de métodos específicos.
  - `PessoaJuridica.java`: classe que herda de `Pessoa` e adiciona o atributo `cnpj`, além de métodos específicos.
  - `Produtos.java`: classe que representa produtos com atributos como `id`, `nome`, `precoDeVenda` e `quantidade`.
  - `Usuario.java`: classe que representa um usuário do sistema.
  
- Pacote `controller`:
  - `MovimentacaoJpaController.java`: classe de gerenciamento para entidades de movimentação no sistema.
  - `PessoaJpaController.java`: classe de gerenciamento para entidades de pessoa.
  - `PessoaFisicaJpaController.java`: classe de gerenciamento para entidades de pessoa física.
  - `PessoaJuridicaJpaController.java`: classe de gerenciamento para entidades de pessoa jurídica.
  - `ProdutosJpaController.java`: classe de gerenciamento para entidades de produtos.
  - `UsuarioJpaController.java`: classe de gerenciamento para entidades de usuários.


### Utilização do Projeto

A versão atualizada do projeto do cliente oferece um menu interativo para o usuário, com as seguintes opções:

1. Listar: solicita ao servidor a lista de produtos cadastrados e a exibe na interface gráfica.
2. Entrada: permite ao usuário registrar a entrada de produtos no sistema, fornecendo informações como ID do produto, ID pessoa, quantidade e valor unitário.
3. Saída: permite ao usuário registrar a saída de produtos do sistema, fornecendo informações como ID do produto, ID pessoa, quantidade e valor unitário.
4. Finalizar: encerra a execução do cliente.

O servidor atualizado (CadastroServerV2) gerencia as operações de cadastro e consulta de produtos, validando as credenciais do cliente.


### Considerações Finais

Esta versão atualizada do projeto representa um sistema de cadastro de produtos com uma interface gráfica para o cliente e comunicação cliente-servidor. O servidor processa as operações solicitadas pelo cliente e fornece respostas em tempo real. O projeto demonstra conceitos de sockets, persistência em banco de dados e implementação de funcionalidades distribuídas. Sinta-se à vontade para explorar o código, aprimorá-lo e adaptá-lo às suas necessidades ou utilizá-lo como base para projetos futuros. Divirta-se codificando! ";)"

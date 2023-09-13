/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastroserver;


import controller.MovimentacaoJpaController;
import controller.PessoaJpaController;
import controller.ProdutosJpaController;
import controller.UsuarioJpaController;
import controller.exceptions.NonexistentEntityException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Movimentacao;
import model.Pessoa;
import model.Produtos;

import model.Usuario;

public class CadastroThreadV2 extends Thread {
    private ProdutosJpaController ctrlProd;
    private UsuarioJpaController ctrlUsu;
    private MovimentacaoJpaController ctrlMov;
    private PessoaJpaController ctrlPessoa;
    private Socket s1;
    
    public CadastroThreadV2(ProdutosJpaController ctrlProd, UsuarioJpaController ctrlUsu, 
                            MovimentacaoJpaController ctrlMov, PessoaJpaController ctrlPessoa, Socket s1) {
        this.ctrlProd = ctrlProd;
        this.ctrlUsu = ctrlUsu;
        this.ctrlMov = ctrlMov;
        this.ctrlPessoa = ctrlPessoa;
        this.s1 = s1;
    }

    // ...

    @Override
    public void run() {
	try (
	    ObjectOutputStream saida = new ObjectOutputStream(s1.getOutputStream());
	    ObjectInputStream entrada = new ObjectInputStream(s1.getInputStream())
	) {
	    // Obter o login e a senha a partir da entrada
	    String login = (String) entrada.readObject();
	    String senha = (String) entrada.readObject();

	    // Utilizar ctrlUsu para verificar o login
	    Usuario usuario = ctrlUsu.findUsuariosenha(login, senha);

	    if (usuario == null) {
		// Se o usuário for nulo, encerrar a conexão
		System.out.println("Usuário inválido. Conexão encerrada.");
		return;
	    }

	    // Loop de resposta
	    while (true) {
		// Obter o comando a partir da entrada
		String comando = (String) entrada.readObject();

		if ("L".equals(comando)) {
		    // Utilizar ctrlProd para retornar o conjunto de produtos através da saída
		    List<Produtos> produtos = ctrlProd.findProdutosEntities();
		    saida.writeObject(produtos);
		    
		} else if ("E".equalsIgnoreCase(comando)) {
		    if (realizarEntrada(entrada, usuario)) {
			saida.writeObject("Entrada realizada com sucesso.");
		    } else {
			saida.writeObject("Erro ao realizar entrada.");
		    }
		} else if ("S".equalsIgnoreCase(comando)) {
		    if (realizarSaida(entrada, usuario)) {
			saida.writeObject("Saída realizada com sucesso.");
		    } else {
			saida.writeObject("Erro ao realizar saída.");
		    }
		}else if ("X".equals(comando)) {
		    saida.writeObject("SAINDO");
		}
	    }
	    } catch (IOException | ClassNotFoundException e) {
		e.printStackTrace();
	    } catch (Exception ex) {
		Logger.getLogger(CadastroThreadV2.class.getName()).log(Level.SEVERE, null, ex);
	    } finally {
		try {
		    s1.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
    }

    private boolean realizarEntrada(ObjectInputStream entrada, Usuario usuario) throws IOException, ClassNotFoundException {
	// Receber os dados para entrada de produtos
	Integer idPessoaObj = (Integer) entrada.readObject();
	Integer idProdutoObj = (Integer) entrada.readObject();
	Integer quantidadeObj = (Integer) entrada.readObject();
	Double valorUnitarioObj = (Double) entrada.readObject();
	
	int idPessoa = idPessoaObj.intValue();
	int idProduto = idProdutoObj.intValue();
	int quantidade = quantidadeObj.intValue();
	double valorUnitario = valorUnitarioObj.doubleValue();

	// Obtenha as entidades Pessoa e Produtos usando os controladores correspondentes
	Pessoa pessoa = ctrlPessoa.findPessoa(idPessoa);
	Produtos produto = ctrlProd.findProdutos(idProduto);

	// Verifique se as entidades foram encontradas
	if (pessoa == null || produto == null) {
	    System.out.println("Pessoa ou Produto não encontrado. Movimento não registrado.");
	    return false;
	}

	// Verifique se a quantidade é válida (maior que zero)
	if (quantidade <= 0) {
	    System.out.println("Quantidade inválida. Movimento não registrado.");
	    return false;
	}

	// Crie um objeto Movimentacao para entrada de produtos
	Movimentacao movimento = new Movimentacao();
	movimento.setUsuario(usuario);
	movimento.setTipo("E"); // Tipo de movimento de entrada
	movimento.setPessoa(pessoa);
	movimento.setProdutos(produto);
	movimento.setQuantidadeES(quantidade);
	movimento.setPrecoUnitario(BigDecimal.valueOf(valorUnitario));
	int novaQuantidade = produto.getQuantidade() + quantidade;
	try {

		// Atualize a quantidade do produto
		produto.setQuantidade(novaQuantidade);
		ctrlProd.edit(produto);

		
	    } catch (Exception ex) {
		System.out.println("Erro ao realizar a persistencia em produto.");
		ex.printStackTrace();
		return false;
	    }
	try{
		// Persista o movimento
		ctrlMov.create(movimento);
		return true;
		}catch (Exception ex) {
		System.out.println("Erro ao realizar a persistencia em movimento.");
		ex.printStackTrace();
		return false;
	    }
    }

    private boolean realizarSaida(ObjectInputStream entrada, Usuario usuario) throws IOException, ClassNotFoundException {
	// Receber os dados para saída de produtos
	Integer idPessoaObj = (Integer) entrada.readObject();
	Integer idProdutoObj = (Integer) entrada.readObject();
	Integer quantidadeObj = (Integer) entrada.readObject();
	Double valorUnitarioObj = (Double) entrada.readObject();
	
	int idPessoa = idPessoaObj.intValue();
	int idProduto = idProdutoObj.intValue();
	int quantidade = quantidadeObj.intValue();
	double valorUnitario = valorUnitarioObj.doubleValue();

	// Obtenha as entidades Pessoa e Produtos usando os controladores correspondentes
	Pessoa pessoa = ctrlPessoa.findPessoa(idPessoa);
	Produtos produto = ctrlProd.findProdutos(idProduto);

	// Verifique se as entidades foram encontradas
	if (pessoa == null || produto == null) {
	    System.out.println("Pessoa ou Produto não encontrado. Movimento não registrado.");
	    return false;
	}

	// Verifique se a quantidade é válida (maior que zero)
	if (quantidade <= 0) {
	    System.out.println("Quantidade inválida. Movimento não registrado.");
	    return false;
	}

	int novaQuantidade = produto.getQuantidade() - quantidade;

	if (novaQuantidade >= 0) {
	    // Crie um objeto Movimentacao para saída de produtos
	    Movimentacao movimento = new Movimentacao();
	    movimento.setUsuario(usuario);
	    movimento.setTipo("S"); // Tipo de movimento de saída
	    movimento.setPessoa(pessoa);
	    movimento.setProdutos(produto);
	    movimento.setQuantidadeES(quantidade);
	    movimento.setPrecoUnitario(BigDecimal.valueOf(valorUnitario));
	    
	    try {
		// Atualize a quantidade do produto
		produto.setQuantidade(novaQuantidade);
		ctrlProd.edit(produto);

		
	    } catch (Exception ex) {
		System.out.println("Erro ao realizar a persistencia em produto.");
		ex.printStackTrace();
		return false;
	    }
	    try{
		// Persista o movimento
		ctrlMov.create(movimento);
		return true;
		}catch (Exception ex) {
		System.out.println("Erro ao realizar a persistencia em movimento.");
		ex.printStackTrace();
		return false;
	    }
	} else {
	    System.out.println("Estoque insuficiente para a saída.");
	    return false;
	}
    }

}

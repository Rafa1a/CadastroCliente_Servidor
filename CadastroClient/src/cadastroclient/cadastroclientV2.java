/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastroclient;

import java.io.*;
import java.net.*;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import model.Produtos;


/**
 *
 * @author Windows 10
 */
public class cadastroclientV2 {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4321);

            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());

            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

            // Enviar login e senha para o servidor 
            saida.writeObject("op1"); // login
            saida.writeObject("op1"); // senha
	    
	    // Instancie a janela para mensagens CODIGO NAO FUNCIONAL...corrigir erros
	   /* 
	    JFrame frame = new JFrame();
	    SaidaFrame saidaFrame = new SaidaFrame(frame);
	    saidaFrame.setVisible(true);

	    // Crie um JTextArea na janela para exibir as mensagens
	    JTextArea textArea = new JTextArea();
	    saidaFrame.add(new JScrollPane(textArea));

	    // Instancie a Thread para preenchimento assíncrono (Passo 5)
	    //  canal de entrada do Socket e o JTextArea para a Thread
	    ThreadClient threadClient = new ThreadClient(entrada, textArea);
	    threadClient.start();
		*/
           
	    
            while (true) {
                System.out.println("Menu:");
                System.out.println("L - Listar");
                System.out.println("X - Finalizar");
                System.out.println("E - Entrada");
                System.out.println("S - Saída");

                System.out.print("Escolha uma opção: ");
                String comando = teclado.readLine();

                if (comando.equalsIgnoreCase("L")) {
                    // Envie o comando "L" para o servidor
		    saida.writeObject("L");
		    // Receba e exiba a resposta do servidor (lista de produtos)
		    Object resposta = entrada.readObject();
		    if (resposta instanceof List) {
			List<Produtos> produtos = (List<Produtos>) resposta;
			System.out.println("Lista de produtos: ");

			for (Produtos produto : produtos) {
			    System.out.println("ID: " + produto.getIdProduto());
			    System.out.println("Nome: " + produto.getNome());
			    System.out.println("Preço: " + produto.getPrecoDeVenda());
			    System.out.println("Quantidade: " + produto.getQuantidade());
			    System.out.println("---------------------------");
			}
		    } else {
			System.out.println("Resposta do servidor não é uma lista de produtos.");
		    }

                } else if (comando.equalsIgnoreCase("X")) {
		    saida.writeObject("X");
		    Object resposta = entrada.readObject();
		    System.out.println(resposta);
                    break;
                } else if (comando.equalsIgnoreCase("E") || comando.equalsIgnoreCase("S")) {
                    // Envie o comando (E ou S) para o servidor
                    saida.writeObject(comando);

                    // Obtenha os dados da pessoa, produto, quantidade e valor unitário via teclado
                    System.out.print("ID da pessoa: ");
                    int idPessoa = Integer.parseInt(teclado.readLine());

                    System.out.print("ID do produto: ");
                    int idProduto = Integer.parseInt(teclado.readLine());

                    System.out.print("Quantidade: ");
                    int quantidade = Integer.parseInt(teclado.readLine());

                    System.out.print("Valor unitário: ");
                    double valorUnitario = Double.parseDouble(teclado.readLine());
		    
                    // Envie os dados para o servidor
                    saida.writeObject(idPessoa);
                    saida.writeObject(idProduto);
                    saida.writeObject(quantidade);
                    saida.writeObject(valorUnitario);
		    

                    // Receba a resposta do servidor e exiba-a 
                    Object resposta = entrada.readObject();
		    
                    System.out.println(resposta);
                }
            }

            // Feche os recursos
            saida.close();
            entrada.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}


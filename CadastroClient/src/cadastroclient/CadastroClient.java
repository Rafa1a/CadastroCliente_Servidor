/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastroclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import model.Produtos;

public class CadastroClient {
    public static void main(String[] args) {
        String servidorIP = "localhost";
        int servidorPorta = 4321;

        try (Socket clienteSocket = new Socket(servidorIP, servidorPorta);
             ObjectOutputStream saida = new ObjectOutputStream(clienteSocket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(clienteSocket.getInputStream())) {

            // Escrever o login e a senha na saída 
            saida.writeObject("op1"); // Login
            saida.writeObject("op1"); // Senha

            // Enviar o comando "L" no canal de saída
            saida.writeObject("L");

            // Receber a coleção de entidades no canal de entrada
            List<Produtos> produtos = (List<Produtos>) entrada.readObject();

            // Apresentar o nome de cada entidade recebida
            System.out.println("Produtos:");
            for (Produtos produto : produtos) {
                System.out.println(produto.getNome());
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}


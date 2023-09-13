/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastroserver;

import controller.ProdutosJpaController;
import controller.UsuarioJpaController;
import java.io.*;
import java.net.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
/**
 *
 * @author Windows 10
 */
public class CadastroServer {

     public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");
        ProdutosJpaController ctrl = new ProdutosJpaController(emf);
        UsuarioJpaController ctrlUsu = new UsuarioJpaController(emf);
        ServerSocket servidorSocket = null;

        try {
            servidorSocket = new ServerSocket(4321);
            System.out.println("Servidor aguardando conexões na porta 4321...");

            while (true) {
                Socket clienteSocket = servidorSocket.accept();
                CadastroThread thread = new CadastroThread(ctrl, ctrlUsu, clienteSocket);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (servidorSocket != null && !servidorSocket.isClosed()) {
                try {
                    servidorSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

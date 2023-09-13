package cadastroserver;

import controller.MovimentacaoJpaController;
import controller.PessoaJpaController;
import controller.ProdutosJpaController;
import controller.UsuarioJpaController;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class cadastroServerV2 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");
        ProdutosJpaController ctrlProd = new ProdutosJpaController(emf);
        UsuarioJpaController ctrlUsu = new UsuarioJpaController(emf);
        MovimentacaoJpaController ctrlMov = new MovimentacaoJpaController(emf);
        PessoaJpaController ctrlPessoa = new PessoaJpaController(emf);
	
        ServerSocket servidorSocket = null;

        try {
            servidorSocket = new ServerSocket(4321);
            System.out.println("Servidor aguardando conexões na porta 4321 v2...");

            while (true) {
                Socket clienteSocket = servidorSocket.accept();
                CadastroThreadV2 thread = new CadastroThreadV2(ctrlProd, ctrlUsu, ctrlMov, ctrlPessoa, clienteSocket);
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

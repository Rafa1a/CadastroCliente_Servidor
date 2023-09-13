package cadastroserver;
import controller.ProdutosJpaController;
import controller.UsuarioJpaController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import model.Produtos;
import model.Usuario;

public class CadastroThread extends Thread {
    private ProdutosJpaController ctrl;
    private UsuarioJpaController ctrlUsu;
    private Socket s1;
    
    public CadastroThread(ProdutosJpaController ctrl, UsuarioJpaController ctrlUsu, Socket s1) {
        this.ctrl = ctrl;
        this.ctrlUsu = ctrlUsu;
        this.s1 = s1;
    }

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
                    // Utilizar ctrl para retornar o conjunto de produtos através da saída
                    List<Produtos> produtos = ctrl.findProdutosEntities();
                    saida.writeObject(produtos);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                s1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

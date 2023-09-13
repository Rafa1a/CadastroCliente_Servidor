/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastroclient;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class ThreadClient extends Thread {
    private ObjectInputStream entrada;
    private JTextArea textArea;

    public ThreadClient(ObjectInputStream entrada, JTextArea textArea) {
        this.entrada = entrada;
        this.textArea = textArea;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Receba os dados enviados pelo servidor via método readObject
                Object objetoRecebido = entrada.readObject();
		textArea.append("rafinha creu");
		break;
               /* // Utilize invokeLater para acessar componentes Swing
                SwingUtilities.invokeLater(() -> {
                    if (objetoRecebido instanceof String) {
                        // Para objetos do tipo String, apenas adicione ao JTextArea
                        String mensagem = (String) objetoRecebido;
                        textArea.append(mensagem + "\n");
                    } else if (objetoRecebido instanceof List) {
                        // Para objetos do tipo List, acrescente o nome e a quantidade de cada produto ao JTextArea
                        List<String> listaProdutos = (List<String>) objetoRecebido;
                        for (String produto : listaProdutos) {
                            textArea.append(produto + "\n");
                        }
                    }
                });*/
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

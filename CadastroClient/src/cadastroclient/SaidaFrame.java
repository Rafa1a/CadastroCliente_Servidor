/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastroclient;

import javax.swing.*;

public class SaidaFrame extends JDialog {
    public JTextArea texto;

    public SaidaFrame(JFrame frame) {
        super(frame, "Mensagens do Servidor", false);
        
        texto = new JTextArea(20, 40); // 20 linhas, 40 colunas
        texto.setEditable(false); // Não permitir edição
        
        JScrollPane scrollPane = new JScrollPane(texto);
        getContentPane().add(scrollPane);
        
        // Defina as dimensões da janela
        setBounds(100, 100, 400, 300); // (x, y, largura, altura)
    }
}

package control;

import javax.swing.*;

import view.Selecao;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Selecao selecao = new Selecao();
            selecao.setVisible(true);
        });
    }
}

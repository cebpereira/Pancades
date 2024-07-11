package view;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

@SuppressWarnings("serial")
public class Selecao extends JFrame implements ActionListener {
    private JButton[] champButtons = new JButton[6];
    private JButton[] scenarioButtons = new JButton[3];
    private JButton fightButton;
    private String selectedChamp1, selectedChamp2, selectedScenario;
    private int click = 1;

    public Selecao() {
        setTitle("Seleção de Campeões");
        setSize(800, 600);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initChampButtons();
        initScenarioButtons();
        initFightButton();
    }

    private void initChampButtons() {
        String[] champs = {"akuma", "chunli", "guile", "iori", "ryu", "zangief"};
        for (int i = 0; i < champButtons.length; i++) {
            ImageIcon icon = loadImage("/" + champs[i] + "/Player1/stopped.gif");
            champButtons[i] = new JButton();
            if (icon != null) {
                champButtons[i].setIcon(icon);
            } else {
                champButtons[i].setText(champs[i]);
            }
            champButtons[i].setBounds(40 + (i * 120), 238, 100, 244);
            champButtons[i].addActionListener(this);
            champButtons[i].setActionCommand(champs[i]);
            add(champButtons[i]);
        }
    }

    private void initScenarioButtons() {
        String[] scenarios = {"cenario1", "cenario2", "cenario3"};
        for (int i = 0; i < scenarioButtons.length; i++) {
            scenarioButtons[i] = new JButton(scenarios[i]);
            scenarioButtons[i].setBounds(40 + (i * 240), 100, 200, 50);
            scenarioButtons[i].addActionListener(this);
            scenarioButtons[i].setActionCommand(scenarios[i]);
            add(scenarioButtons[i]);
        }
    }

    private void initFightButton() {
        fightButton = new JButton("FIGHT!");
        fightButton.setBounds(350, 500, 100, 50);
        fightButton.addActionListener(this);
        add(fightButton);
    }

    private ImageIcon loadImage(String path) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                BufferedImage img = ImageIO.read(imgURL);
                return new ImageIcon(img.getScaledInstance(88, 127, Image.SCALE_DEFAULT));
            } else {
                System.err.println("Couldn't find file: " + path);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (click == 1) {
            selectedChamp1 = command;
            click = 2;
        } else if (click == 2) {
            selectedChamp2 = command;
            click = 3;
        } else if (click == 3) {
            selectedScenario = command;
            click = 4;
        } else if (click == 4 && e.getSource() == fightButton) {
            this.setVisible(false);
            new GamePanel(selectedChamp1, selectedChamp2, selectedScenario).setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Selecao().setVisible(true));
    }
}

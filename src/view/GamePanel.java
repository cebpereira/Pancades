package view;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;
import control.PlayerThread;
import model.Player;

@SuppressWarnings("serial")
public class GamePanel extends JFrame {
    private Player player1, player2;
    private BackgroundPanel backgroundPanel;
    private JProgressBar healthBarP1, healthBarP2;
    private PlayerThread player1Thread, player2Thread;
    private Thread gameLoopThread;

    private boolean p1Right, p1Left, p1Punch, p1Kick, p1Defend, p1Crouch, p1Jump;
    private boolean p2Right, p2Left, p2Punch, p2Kick, p2Defend, p2Crouch, p2Jump;

    public GamePanel(String champ1, String champ2, String scenario) {
        initPlayers(champ1, champ2);
        initUI(scenario);
        startThreads();
    }

    private void initPlayers(String champ1, String champ2) {
        player1 = new Player(100, 430, champ1, "Player1");
        player2 = new Player(600, 430, champ2, "Player2");
    }

    private void initUI(String scenario) {
        setTitle("Jogo de Luta");
        setSize(800, 600);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        backgroundPanel = new BackgroundPanel("/scenarios/" + scenario + ".gif");
        backgroundPanel.setBounds(0, 0, 800, 600);
        setContentPane(backgroundPanel);
        backgroundPanel.setLayout(null);

        healthBarP1 = new JProgressBar(0, 100);
        healthBarP1.setValue(100);
        healthBarP1.setBounds(50, 30, 300, 30);
        healthBarP1.setForeground(Color.GREEN);
        backgroundPanel.add(healthBarP1);

        healthBarP2 = new JProgressBar(0, 100);
        healthBarP2.setValue(100);
        healthBarP2.setBounds(450, 30, 300, 30);
        healthBarP2.setForeground(Color.GREEN);
        backgroundPanel.add(healthBarP2);

        backgroundPanel.add(player1);
        backgroundPanel.add(player2);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyRelease(e);
            }
        });
    }

    private void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            // Controles do Player 1
            case KeyEvent.VK_A: p1Left = true; break;
            case KeyEvent.VK_D: p1Right = true; break;
            case KeyEvent.VK_J: p1Punch = true; break;
            case KeyEvent.VK_K: p1Kick = true; break;
            case KeyEvent.VK_L: p1Defend = true; break;
            case KeyEvent.VK_S: p1Crouch = true; break;
            case KeyEvent.VK_W: p1Jump = true; break;
            // Controles do Player 2
            case KeyEvent.VK_LEFT: p2Left = true; break;
            case KeyEvent.VK_RIGHT: p2Right = true; break;
            case KeyEvent.VK_NUMPAD1: p2Punch = true; break;
            case KeyEvent.VK_NUMPAD2: p2Kick = true; break;
            case KeyEvent.VK_NUMPAD3: p2Defend = true; break;
            case KeyEvent.VK_DOWN: p2Crouch = true; break;
            case KeyEvent.VK_UP: p2Jump = true; break;
        }
    }

    private void handleKeyRelease(KeyEvent e) {
        switch (e.getKeyCode()) {
            // Controles do Player 1
            case KeyEvent.VK_A: p1Left = false; break;
            case KeyEvent.VK_D: p1Right = false; break;
            case KeyEvent.VK_J: p1Punch = false; break;
            case KeyEvent.VK_K: p1Kick = false; break;
            case KeyEvent.VK_L: p1Defend = false; break;
            case KeyEvent.VK_S: p1Crouch = false; break;
            case KeyEvent.VK_W: p1Jump = false; break;
            // Controles do Player 2
            case KeyEvent.VK_LEFT: p2Left = false; break;
            case KeyEvent.VK_RIGHT: p2Right = false; break;
            case KeyEvent.VK_NUMPAD1: p2Punch = false; break;
            case KeyEvent.VK_NUMPAD2: p2Kick = false; break;
            case KeyEvent.VK_NUMPAD3: p2Defend = false; break;
            case KeyEvent.VK_DOWN: p2Crouch = false; break;
            case KeyEvent.VK_UP: p2Jump = false; break;
        }
    }

    private void startThreads() {
        player1Thread = new PlayerThread(player1);
        player2Thread = new PlayerThread(player2);
        gameLoopThread = new Thread(this::gameLoop);

        new Thread(player1Thread).start();
        new Thread(player2Thread).start();
        gameLoopThread.start();
    }

    private void gameLoop() {
        while (true) {
            updateGame();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGame() {
        if (p1Right) player1.moveRight();
        if (p1Left) player1.moveLeft();
        if (p1Punch) player1.punch();
        if (p1Kick) player1.kick();
        if (p1Defend) player1.defend();
        if (p1Crouch) player1.crouch();
        if (p1Jump) player1.jump();
        if (!p1Right && !p1Left && !p1Punch && !p1Kick && !p1Defend && !p1Crouch && !p1Jump) player1.stop();

        if (p2Right) player2.moveRight();
        if (p2Left) player2.moveLeft();
        if (p2Punch) player2.punch();
        if (p2Kick) player2.kick();
        if (p2Defend) player2.defend();
        if (p2Crouch) player2.crouch();
        if (p2Jump) player2.jump();
        if (!p2Right && !p2Left && !p2Punch && !p2Kick && !p2Defend && !p2Crouch && !p2Jump) player2.stop();

        checkCollisions();
        updateHealthBars();
        repaint();
    }

    private void checkCollisions() {
        Rectangle r1 = player1.getBounds();
        Rectangle r2 = player2.getBounds();

        if (r1.intersects(r2)) {
            if (p1Punch) {
                if (player2.isDefending()) {
                    player2.takeDamage(1); // Reduced damage for defended punch
                } else {
                    player2.takeDamage(3); // Normal punch damage
                }
            }
            if (p1Kick) {
                if (player2.isDefending()) {
                    player2.takeDamage(2); // Reduced damage for defended kick
                } else {
                    player2.takeDamage(4); // Normal kick damage
                }
            }

            if (p2Punch) {
                if (player1.isDefending()) {
                    player1.takeDamage(1); // Reduced damage for defended punch
                } else {
                    player1.takeDamage(3); // Normal punch damage
                }
            }
            if (p2Kick) {
                if (player1.isDefending()) {
                    player1.takeDamage(2); // Reduced damage for defended kick
                } else {
                    player1.takeDamage(4); // Normal kick damage
                }
            }
        }

        // Check for winner
        if (player1.getHealth() <= 0) {
            showWinner("Player 2");
        } else if (player2.getHealth() <= 0) {
            showWinner("Player 1");
        }
    }

    private void showWinner(String winner) {
        JOptionPane.showMessageDialog(this, winner + " wins!");
        System.exit(0);
    }

    private void updateHealthBars() {
        healthBarP1.setValue(player1.getHealth());
        healthBarP2.setValue(player2.getHealth());

        healthBarP1.setForeground(player1.getHealth() > 30 ? Color.GREEN : Color.RED);
        healthBarP2.setForeground(player2.getHealth() > 30 ? Color.GREEN : Color.RED);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GamePanel("akuma", "ryu", "cenario1").setVisible(true));
    }
}

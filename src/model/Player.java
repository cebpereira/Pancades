package model;

import java.awt.Rectangle;

import javax.swing.*;

@SuppressWarnings("serial")
public class Player extends JLabel {
    private int x, y;
    private String champ, playerType;
    private ImageIcon walkLeft, walkRight, punch, kick, defense, stopped, crouch, jump;
    private int health = 100;
    private boolean defending = false;
    private boolean jumping = false;
    @SuppressWarnings("unused")
	private boolean crouching = false;
    private int jumpHeight = 50;
    private int originalY;

    public Player(int x, int y, String champ, String playerType) {
        this.x = x;
        this.y = y;
        this.originalY = y;
        this.champ = champ;
        this.playerType = playerType;
        loadSprites();
        setBounds(x, y, 150, 150);  // 150 x 150 - acima disso fica estranho e abaixo distorcido
    }

    private void loadSprites() {
        String basePath = "/" + champ + "/" + playerType + "/";
        walkRight = new ImageIcon(getClass().getResource(basePath + "walk_right.gif"));
        walkLeft = new ImageIcon(getClass().getResource(basePath + "walk_left.gif"));
        punch = new ImageIcon(getClass().getResource(basePath + "punch.gif"));
        kick = new ImageIcon(getClass().getResource(basePath + "kick.gif"));
        defense = new ImageIcon(getClass().getResource(basePath + "defense.gif"));
        stopped = new ImageIcon(getClass().getResource(basePath + "stopped.gif"));
        crouch = new ImageIcon(getClass().getResource(basePath + "crouch.gif"));
        jump = new ImageIcon(getClass().getResource(basePath + "jump.gif"));
        setIcon(stopped);
    }

    public void update() {
        if (jumping) {
            y -= 5;
            if (y <= originalY - jumpHeight) {
                jumping = false;
            }
        } else if (y < originalY) {
            y += 5;
            if (y > originalY) {
                y = originalY;
            }
        }
        setBounds(x, y, 150, 150);
    }

    public void moveRight() {
        x += 5;
        setIcon(walkRight);
        setBounds(x, y, 150, 150);
    }

    public void moveLeft() {
        x -= 5;
        setIcon(walkLeft);
        setBounds(x, y, 150, 150);
    }

    public void punch() {
        setIcon(punch);
        setBounds(x, y, 150, 150);
    }

    public void kick() {
        setIcon(kick);
        setBounds(x, y, 150, 150);
    }

    public void defend() {
        defending = true;
        setIcon(defense);
        setBounds(x, y, 150, 150);
    }

    public void crouch() {
        crouching = true;
        setIcon(crouch);
        setBounds(x, y, 150, 150);
    }

    public void jump() {
        if (!jumping && y == originalY) {
            jumping = true;
            setIcon(jump);
            setBounds(x, y, 150, 150);
        }
    }

    public void stop() {
        setIcon(stopped);
        defending = false;
        crouching = false;
        jumping = false;
        setBounds(x, y, 150, 150);
    }

    public boolean isDefending() {
        return defending;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 150, 150);
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) health = 0;
    }

    public int getHealth() {
        return health;
    }
}

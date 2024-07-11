package control;

import model.Player;

public class PlayerThread implements Runnable {
    private Player player;
    private boolean running = true;

    public PlayerThread(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        while (running) {
            player.update();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
    }
}

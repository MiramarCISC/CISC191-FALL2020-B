package threadingExample;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ThreadDemo extends Thread {
    private static final int NUM_ACTIVITIES = 200;
    private Random rand = new Random();
    private Player player;

    public ThreadDemo(Player player) {
        this.player = player;
    }

    public void run() {
        int numPotions;
        int attackStrength;

        attackStrength = rand.nextInt(5);
        for (int i = 0; i < NUM_ACTIVITIES; i++) {
            if (i % 2 == 0) {
                numPotions = player.getNumPotions();
                if (numPotions > 0) {
                    player.increaseHP(10);
                    player.setNumPotions(--numPotions);
                }
            } else {
                    if (player.getHP() <= 0) {
                        break;
                    }
                    System.out.println(super.getName() + " is attacking");
                    player.decreaseHP(20 * attackStrength);
                    System.out.println("Your HP is: " + player.getHP());

            }
        }
    }
}

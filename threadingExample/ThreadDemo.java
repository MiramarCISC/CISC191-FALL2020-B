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

    /** Run method*/
    public void run() {
        int numPotions;
        int attackStrength;

        // Randomly assign monster's strength
        attackStrength = rand.nextInt(5);
        for (int i = 0; i < NUM_ACTIVITIES; i++) {
            // If it is an even method, then add potions
            if (i % 2 == 0) {
                numPotions = player.getNumPotions();
                if (numPotions > 0) {
                    player.increaseHP(10);
                    player.setNumPotions(--numPotions);
                }

            // If it is an odd method, then attack
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

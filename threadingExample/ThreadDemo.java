/**************************************************************
 * Class: ThreadDemo
 * Author: Anita Cheung
 * Date: October 20, 2020
 * Purpose: demonstrates a thread by synchronizing udpates to
 * Player's HP
 **************************************************************
 * */

package threadingExample;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ThreadDemo extends Thread {
    // Constants
    private static int POTION_VALUE = 10;
    private static int MONSTER_ATTACK = 20;

    // Variables
    private static final int NUM_ACTIVITIES = 200;
    private Random rand = new Random();
    private Player player;

    /**
     * ThreadDemo Constructor
     */
    public ThreadDemo(Player player) {
        this.player = player;
    }

    /**
     * run
     * @params: none
     * @return: none
     * Purpose: runnable method to update HP
     */
    public void run() {
        // Variables
        int numPotions;
        int attackStrength;

        // Randomly assign monster's strength
        attackStrength = rand.nextInt(5);

        // Update HP
        for (int i = 0; i < NUM_ACTIVITIES; i++) {
            // If potions used, increase healthpoints
            if (player.getHealthPointChange() > 0) {
                numPotions = player.getNumPotions();
                System.out.println("You used " + numPotions + " potions.");
                player.increaseHP(POTION_VALUE * numPotions);
                player.setNumPotions(0);
                player.setHealthPointChange(0);

            // If not a potion, then the monster attacks
            } else {
                    if (player.getHealthPoints() <= 0) {
                        break;
                    }
                    System.out.println(super.getName() + " attacked! You lost " +
                                      (attackStrength * MONSTER_ATTACK) + " HP!");
                    player.decreaseHP(MONSTER_ATTACK * attackStrength);
                    System.out.println("Your HP is: " + player.getHealthPoints());
            }
        }
    }
}

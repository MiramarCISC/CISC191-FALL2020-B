/**************************************************************
 * Class: ThreadingLab
 * Author: Anita Cheung
 * Date: October 20, 2020
 * Purpose: demonstrates use of the ThreadDemo thread to update
 * player's HP
 **************************************************************
 * */

package threadingExample;

public class ThreadingLab {
    // Variables
    public static final int NUM_MONSTERS = 2; // thread = monster

    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        // Constants
        int POTION_VALUE = 10;

        // Variables
        Player player = new Player();
        int numPotions;

        // Update and validate the number of potions based on available potions.
        System.out.println("How many potions do you want to use?");
        numPotions = TextIO.getInt();
        int availablePotions = player.getNumPotions();
        while (numPotions > availablePotions) {
            System.out.println("Not enough potions. Please choose a number less" +
                    " than or equal to " + availablePotions + ".");
            numPotions = TextIO.getInt();
        }
        // Set potion based on validated value
        player.setNumPotions(availablePotions - numPotions);
        player.setHealthPointChange(POTION_VALUE * numPotions);

        // Start array of threads
        ThreadDemo[] tasks = new ThreadDemo[NUM_MONSTERS];
        for (int i = 0; i < NUM_MONSTERS; i++) {
            tasks[i] = new ThreadDemo(player);
            tasks[i].setName("Monster " + (i+1));
            tasks[i].start();
        }

        // Report HP
        for (int i = 0; i < NUM_MONSTERS; i++) {
            System.out.println("Game over");
            while (tasks[i].isAlive()) {
                try {
                    tasks[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

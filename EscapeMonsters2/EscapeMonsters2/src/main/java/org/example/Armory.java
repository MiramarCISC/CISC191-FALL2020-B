package org.example;

/**
 * Create weapon class for Escape the Monsters game
 *
 * @Author: Anita Cheung
 * @Date: 12/13/2020
 */

public class Armory extends Treasures {
    // Variables
    int attackStrength;

    /**
     * Constructor for Armory class
     * @param imgFile
     */
    public Armory(String imgFile) {
        this.imgFile = imgFile;
        attackStrength = rand.nextInt(5);
    }

    /**
     * Returns weapon strength of Armory object
     * @return
     */
    public int getAttackStrength() {
        return this.attackStrength;
    }
}

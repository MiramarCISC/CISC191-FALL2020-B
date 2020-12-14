package org.example;

/**
 * Create player class for Escape the Monsters game
 *
 * @Author: Anita Cheung
 * @Date: 12/13/2020
 */

public class Player extends Character {

    /**
     * Default constructor for Player class
     */
    public Player() {
        this.healthPoints = 100;
    }

    /**
     * Constructor for Player class
     * @param imgFile
     */
    public Player(String imgFile)
    {
        this.healthPoints = 100;
        this.imgFile = imgFile;
        this.attackStrength = 10;
    }

    /**
     * Increase player health points
     */
    public void encounterPotion() {
        this.healthPoints += 10;
    }

    /**
     * Increase player attack strength
     * @param increasedDamage
     */
    public void increaseAttackStrength(int increasedDamage) {
        this.attackStrength += increasedDamage;
    }

    /**
     * Player is attacked and decreases player health points
     * @param damage
     * @param timeStart
     * @param timeEnd
     */
    public void attacked(int damage, long timeStart, long timeEnd) {
        /**
         * Get attacked every 1/10 second
         */
        if (Math.round((timeStart - timeEnd) / 10) % 10 == 0) {
            this.healthPoints -= damage;
        }
    }
}

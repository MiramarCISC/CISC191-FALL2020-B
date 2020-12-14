package org.example;

/**
 * Create monster class for Escape the Monsters game
 *
 * @Author: Anita Cheung
 * @Date: 12/13/2020
 */

public class Monster extends Character {
    // Class Attribute
    int direction;

    /**
     * Constructor for monster
     * @param imgFile
     */
    public Monster(String imgFile) {
        this.xPos = rand.nextInt(1000);
        this.yPos = rand.nextInt(500);
        this.healthPoints = rand.nextInt(50);
        this.imgFile = imgFile;
        this.attackStrength = Math.round(this.healthPoints/10) + 5;
        this.direction = rand.nextInt(4);
    }

    /**
     * Updates direction of the monster
     * @param direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Gets the direction of the monster
     * @return
     */
    public int getDirection() {
        return this.direction;
    }

}

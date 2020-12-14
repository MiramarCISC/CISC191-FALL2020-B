package org.example;

import java.util.Random;

/**
 * Create Character abstract class for Escape the Monsters game
 *
 * @Author: Anita Cheung
 * @Date: 12/13/2020
 */

public abstract class Character extends Thread {
    // Variables
    Random rand = new Random();
    double xPos,
           yPos,
           imgHeight,
           imgWidth;
    String imgFile;
    int healthPoints,
        attackStrength;

    /**
     * Constructor for Character class
     */
    public Character() {
        xPos = 0;
        yPos = 0;
        imgHeight = 30;
        imgWidth = 30;
    }

    /**
     * Set x coordinate for character object
     * @param xPos
     */
    public void setXPos(double xPos) {
        this.xPos = xPos;
    }

    /**
     * Set y coordinate for character object
     * @param yPos
     */
    public void setYPos(double yPos) {
        this.yPos = yPos;
    }

    /**
     * Get x coordinate for character object
     * @return
     */
    public double getXPos() {
        return this.xPos;
    }

    /**
     * Get y coordinate for character object
     * @return
     */
    public double getYPos() {
        return this.yPos;
    }

    /**
     * Get file name for character object
     * @return
     */
    public String getImgFileName() {
        return this.imgFile;
    }

    /**
     * Get height of character object image
     * @return
     */
    public double getImgHeight() {
        return imgHeight;
    }

    /**
     * Get width of character object image
     * @return
     */
    public double getImgWidth() {
        return imgWidth;
    }

    /**
     * Set health points of character object
     * @param healthPoints
     */
    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    /**
     * Get health points of character object
     * @return
     */
    public int getHealthPoints() {
        return this.healthPoints;
    }

    /**
     * Reduce health points of character object due to being attacked
     * @param damage
     */
    public void attacked(int damage) {
        this.healthPoints -= damage;
    }

    /**
     * Get attack strength of character object
     * @return
     */
    public int getAttackStrength() {
        return this.attackStrength;
    }

    /**
     * Check whether character object health points is equal to or less than zero (game over)
     * @return
     */
    public boolean checkStatus() {
        if (this.healthPoints <= 0) {
            return true;
        }
        else
        {
            return false;
        }
    }

}

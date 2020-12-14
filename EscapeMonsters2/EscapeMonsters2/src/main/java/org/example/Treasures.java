package org.example;

import java.util.Random;

/**
 * Create treasure abstract class for Escape the Monsters game
 *
 * @Author: Anita Cheung
 * @Date: 12/13/2020
 */

public abstract class Treasures {
    // Variables
    Random rand = new Random();
    String imgFile;
    double xPos,
           yPos,
           imgHeight,
           imgWidth;

    /**
     * Constructor for Treasures class
     */
    public Treasures() {
        this.imgHeight = 30;
        this.imgWidth = 30;
        xPos = rand.nextInt(1000);
        yPos = rand.nextInt(500);
    }

    /**
     * Get x coordinate of treasure object
     * @return
     */
    public double getXPos() {
        return this.xPos;
    }

    /**
     * Get y coordinate of treasure object
     * @return
     */
    public double getYPos() {
        return this.yPos;
    }

    /**
     * Get height of treasure object image
     * @return
     */
    public double getImgHeight() {
        return this.imgHeight;
    }

    /**
     * Get width of treasure object image
     * @return
     */
    public double getImgWidth() {
        return this.imgWidth;
    }
}

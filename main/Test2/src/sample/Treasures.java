package sample;

import java.util.Random;

public abstract class Treasures {
    Random rand = new Random();
    String imgFile;
    double xPos,
           yPos,
           imgHeight,
           imgWidth;

    public Treasures() {
        this.imgHeight = 30;
        this.imgWidth = 30;
        xPos = rand.nextInt(1000);
        yPos = rand.nextInt(500);
    }

    public double getXPos() {
        return this.xPos;
    }

    public double getYPos() {
        return this.yPos;
    }

    public double getImgHeight() {
        return this.imgHeight;
    }

    public double getImgWidth() {
        return this.imgWidth;
    }
}

package gameplay;

import java.util.Random;

public abstract class Character extends Thread {
    Random rand = new Random();
    double xPos,
           yPos,
           imgHeight,
           imgWidth;
    String imgFile;
    int healthPoints,
        attackStrength;

    public Character() {
        xPos = 0;
        yPos = 0;
        imgHeight = 30;
        imgWidth = 30;
    }

    public void setXPos(double xPos) {
        this.xPos = xPos;
    }

    public void setYPos(double yPos) {
        this.yPos = yPos;
    }

    public double getXPos() {
        return this.xPos;
    }

    public double getYPos() {
        return this.yPos;
    }

    public String getImgFileName() {
        return this.imgFile;
    }

    public double getImgHeight() {
        return imgHeight;
    }

    public double getImgWidth() {
        return imgWidth;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getHealthPoints() {
        return this.healthPoints;
    }

    public void attacked(int damage) {
        this.healthPoints -= damage;
    }

    public int getAttackStrength() {
        return this.attackStrength;
    }

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

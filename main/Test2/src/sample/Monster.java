package sample;

import java.util.Random;

public class Monster extends Character {
    // Class Attributes
    Random rand = new Random();

    public Monster(String imgFile) {
        this.xPos = rand.nextInt(1000);
        this.yPos = rand.nextInt(500);
        this.healthPoints = rand.nextInt(50);
        this.imgFile = imgFile;
        this.attackStrength = this.healthPoints/10;
    }

}

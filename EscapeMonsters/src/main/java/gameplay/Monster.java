package gameplay;

public class Monster extends Character {
    // Class Attribute
    int direction;

    public Monster(String imgFile) {
        this.xPos = rand.nextInt(1000);
        this.yPos = rand.nextInt(500);
        this.healthPoints = rand.nextInt(50);
        this.imgFile = imgFile;
        this.attackStrength = Math.round(this.healthPoints/10) + 5;
        this.direction = rand.nextInt(4);
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return this.direction;
    }

}

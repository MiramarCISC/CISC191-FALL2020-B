package sample;

public class Player extends Character {
    // Class Attributes
    int numPotions;

    public Player(String imgFile)
    {
        this.numPotions = 0;
        this.healthPoints = 100;
        this.imgFile = imgFile;
        this.attackStrength = 10;
    }

    public void usePotion() {
        if (this.numPotions > 0) {
            this.numPotions--;
            this.healthPoints += 10;
        }
    }

    public void increaseAttackStrength(int increasedDamage) {
        this.attackStrength += increasedDamage;
    }
}

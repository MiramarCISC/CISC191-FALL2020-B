package sample;


public class Player extends Character {

    public Player(String imgFile)
    {
        this.healthPoints = 100;
        this.imgFile = imgFile;
        this.attackStrength = 10;
    }

    public void encounterPotion() {
        this.healthPoints += 10;
    }

    public void increaseAttackStrength(int increasedDamage) {
        this.attackStrength += increasedDamage;
    }

    public void attacked(int damage) {
        try {
            Thread.sleep(1 * 1000);
            this.healthPoints -= damage;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}

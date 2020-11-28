package sample;


public class Player extends Character {

    public Player() {
        this.healthPoints = 100;
    }

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

    // Get attacked every 1/10 second
    public void attacked(int damage, long timeStart, long timeEnd) {
        if (Math.round((timeStart - timeEnd) / 10) % 10 == 0) {
            this.healthPoints -= damage;
            System.out.println("You've been hit! Player HP: " + this.getHealthPoints());
        }
    }
}

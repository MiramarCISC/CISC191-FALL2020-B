package gameplay;

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

    public void attacked(int damage, long timeStart, long timeEnd) {
        /**
         * Get attacked every 1/10 second
         */
        if (Math.round((timeStart - timeEnd) / 10) % 10 == 0) {
            this.healthPoints -= damage;
        }
    }
}

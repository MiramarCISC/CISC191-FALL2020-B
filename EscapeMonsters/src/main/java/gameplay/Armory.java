package gameplay;

public class Armory extends Treasures {
    int attackStrength;

    public Armory(String imgFile) {
        this.imgFile = imgFile;
        attackStrength = rand.nextInt(5);
    }

    public int getAttackStrength() {
        return this.attackStrength;
    }
}

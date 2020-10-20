package threadingExample;

public class Player {
    // Constants
    private final int MAXPOINTS = 1000;

    // Variables
    private int healthPoints;
    private int numPotions;

    Player() {
        this.healthPoints = MAXPOINTS;
        this.numPotions = 0;
    }

    synchronized int getHealthPoints() {
        return healthPoints;
    }

    synchronized void increaseHP(int numPoints) {
        if (healthPoints + numPoints > MAXPOINTS) {
            healthPoints = MAXPOINTS;
        } else {
            healthPoints += numPoints;
        }
        numPotions -= healthPoints / 10;
    }

    synchronized void decreaseHP(int numPoints) {
        if (healthPoints - numPoints <= 0) {
            healthPoints = 0;
        } else {
            healthPoints -= numPoints;
        }
    }

    public int getHP() {
        return healthPoints;
    }
    public int getNumPotions() { return numPotions;}
    public void setNumPotions(int numPotions) { this.numPotions = numPotions;}

}

package threadingExample;

public class Player {
    // Constants
    private final int MAXPOINTS = 1000;

    // Variables
    private int healthPoints;
    private int numPotions;

    /** Constructor */
    Player() {
        this.healthPoints = MAXPOINTS;
        this.numPotions = 0;
    }

    /** Synchronized health points method to avoid race conditions*/
    synchronized int getHealthPoints() {
        return healthPoints;
    }

    /** Increase HP to be no more than maximum points*/
    synchronized void increaseHP(int numPoints) {
        if (healthPoints + numPoints > MAXPOINTS) {
            healthPoints = MAXPOINTS;
        } else {
            healthPoints += numPoints;
        }
        numPotions -= healthPoints / 10;
    }

    /** Decrease HP if greater than 0*/
    synchronized void decreaseHP(int numPoints) {
        if (healthPoints - numPoints <= 0) {
            healthPoints = 0;
        } else {
            healthPoints -= numPoints;
        }
    }

    // Accessor methods
    public int getHP() {
        return healthPoints;
    }
    public int getNumPotions() { return numPotions;}

    // Mutator methods
    public void setNumPotions(int numPotions) { this.numPotions = numPotions;}

}

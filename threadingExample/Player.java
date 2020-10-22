/**************************************************************
 * Class: Player
 * Author: Anita Cheung
 * Date: October 20, 2020
 * Purpose: creates an instance of Player for game with ability
 * to heal (using potions) and for health to decrease, by
 * monster attacks.
 **************************************************************
 * */

package threadingExample;

public class Player {
    // Constants
    private final int MAXPOINTS = 1000;

    // Variables
    private int healthPoints;
    private int numPotions;
    private int healthPointChange;

    /**
     * Class Constructor
     * */
    Player() {
        this.healthPoints = MAXPOINTS;
        this.numPotions = 10;
        this.healthPointChange = 0;
    }

    /**
     * getHealthPoints
     * @params: none
     * @return: healthPoints
     * Purpose: health points method to avoid race conditions
     * */
    synchronized int getHealthPoints() {
        return healthPoints;
    }

    /**
     * increaseHP
     * @param: numPoints
     * @return: none
     * Purpose: increase HP to no more than max points
     */
    synchronized void increaseHP(int numPoints) {
        if (healthPoints + numPoints > MAXPOINTS) {
            healthPoints = MAXPOINTS;
        } else {
            healthPoints += numPoints;
        }
        numPotions -= healthPoints / 10;
    }

    /**
     * decreaseHP
     * @param: numPoints
     * @return: none
     * Purpose: decreases HP to at most 0
     */
    synchronized void decreaseHP(int numPoints) {
        if (healthPoints - numPoints <= 0) {
            healthPoints = 0;
        } else {
            healthPoints -= numPoints;
        }
    }

    /**
     * getHealthPointChange
     * @params: none
     * @return: healthPointChange
     * Purpose: return the change in healthPoints
     */
    synchronized public int getHealthPointChange() {
        return healthPointChange;
    }

    /**
     * setHealthPointChange
     * @params: newChange
     * @return none
     * Purpose: update the change in healthPoints
     */
    synchronized public void setHealthPointChange(int newChange) {
        healthPointChange = newChange;
    }

    /**
     * getNumPotions
     * @params: none
     * @return: numPotions
     * Purpose: returns the number of potions available
     */
    public int getNumPotions() { return numPotions;}

    /** setNumPotions
     * @params: numPotions
     * @return: none
     * Purpose: sets the number of potions available for use
     */
    // Mutator methods
    public void setNumPotions(int numPotions) { this.numPotions = numPotions;}

}

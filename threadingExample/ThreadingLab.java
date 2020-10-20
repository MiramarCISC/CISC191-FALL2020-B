package threadingExample;

public class ThreadingLab {
    /**Thread is number of monsters*/
    public static final int NUM_MONSTERS = 2;

    /**Main function*/
    public static void main(String[] args) {
        Player player = new Player();

        // Use potions first
        System.out.println("How many potions do you want to use?");
        int numPotions = TextIO.getInt();
        player.setNumPotions(numPotions);

        // Start array of threads -- should this be an arraylist instead?
        ThreadDemo[] tasks = new ThreadDemo[NUM_MONSTERS];
        for (int i = 0; i < NUM_MONSTERS; i++) {
            tasks[i] = new ThreadDemo(player);
            tasks[i].setName("Monster " + (i+1));
            tasks[i].start();
        }

        // Report HP
        for (int i = 0; i < NUM_MONSTERS; i++) {
            System.out.println("Your HP is: " + player.getHP());
            while (tasks[i].isAlive()) {
                try {
                    tasks[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

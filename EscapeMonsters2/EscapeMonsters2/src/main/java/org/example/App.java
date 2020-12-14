package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;
import java.util.Vector;

/**
 * This is a simple RPG game where the player eliminates as many monsters as possible. The player can collect
 * potions and weapons along the way to help prolong the mission.
 *
 * @Author: Anita Cheung
 * @Date: 12/13/2020
 *
 * References:
 * https://www.youtube.com/watch?v=yG8YCLYccVo
 * https://stackoverflow.com/questions/29057870/in-javafx-how-do-i-move-a-sprite-across-the-screen
 * https://www.youtube.com/watch?v=kkZ-YNv7B0E
 * https://www.youtube.com/watch?v=yG8YCLYccVo
 */
public class App extends Application {

    // Constants
    int WINDOW_HEIGHT = 500;
    int WINDOW_WIDTH = 1000;
    int OBJECT_SIZE = 30;

    // Variables
    boolean goUp, goDown, goLeft, goRight, attacked;
    double minX, minY, maxX, maxY;
    int numMonsters = 0;
    long timeNow = System.currentTimeMillis();
    long monsterTime = System.currentTimeMillis();
    long potionTime = System.currentTimeMillis();
    long weaponTime = System.currentTimeMillis();
    Vector<Node> monsters = new Vector<>();
    Vector<Node> potions = new Vector<>();
    Vector<Node> weapons = new Vector<>();
    Vector<Monster> monsterList = new Vector<>();
    Vector<Armory> weaponList = new Vector<>();
    Node icon;
    Random rand = new Random();
    Text healthPoints;
    Rectangle healthBarPanel;

    /**
     * Initiates gameplay
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        // Game title
        primaryStage.setTitle("Escape the Monsters");

        // Instantiate game
        try {

            // Setup Background
            Group root = new Group();

            // Setup Scene
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            minX = 0;
            minY = 0;
            maxX = WINDOW_WIDTH - OBJECT_SIZE;
            maxY = WINDOW_HEIGHT - OBJECT_SIZE;

            // Setup Start Game
            displayIntroduction(primaryStage, root, scene);

            // Display GUI
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates movement on gameboard
     * @param primaryStage
     * @param root
     * @param scene
     * @param hpPane
     * @param icon
     * @param myPlayer
     */
    public void movement(Stage primaryStage, Group root, Scene scene, StackPane hpPane, Node icon, Player myPlayer) {

        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case D:
                    attacked = true;
                    break;
                case UP:
                    goUp = true;
                    break;
                case DOWN:
                    goDown = true;
                    break;
                case LEFT:
                    goLeft = true;
                    break;
                case RIGHT:
                    goRight = true;
                    break;
            }
        });

        scene.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case D:
                    attacked = false;
                    break;
                case UP:
                    goUp = false;
                    break;
                case DOWN:
                    goDown = false;
                    break;
                case LEFT:
                    goLeft = false;
                    break;
                case RIGHT:
                    goRight = false;
                    break;
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            final double delta = 5;
            double currX, currY, monsterX, monsterY;
            long currTime;

            @Override
            public void handle(long arg0) {
                // Move player
                currX = icon.getLayoutX();
                currY = icon.getLayoutY();
                currTime = System.currentTimeMillis();

                // Check that player has not died
                if (myPlayer.checkStatus()) {
                    root.getChildren().remove(icon);
                    this.stop();
                    displayClose(primaryStage, root, scene);
                }

                // Check that player is close to potions
                checkPotions(root, hpPane, myPlayer);

                // Check that player is close to weapons
                checkWeapons(root, hpPane, myPlayer);

                // Check that player is close to monsters
                for (int i = 0 ; i < monsters.size(); i++) {
                    monsterX = monsters.get(i).getLayoutX();
                    monsterY = monsters.get(i).getLayoutY();

                    // Attacked if monster is encountered
                    if (checkDistance(monsterX, monsterY, currX, currY)) {
                        // Only attack if the monster is still alive
                        try {
                            // Player attacked
                            myPlayer.attacked(monsterList.get(i).getAttackStrength(), currTime, timeNow);

                            // Attack specific monster
                            if (attacked) {
                                try {
                                    monsterList.get(i).attacked(myPlayer.getAttackStrength());
                                } catch (Exception e) {
                                    continue;
                                }
                            }

                            // Remove monster once dead
                            if (monsterList.get(i).checkStatus()) {
                                root.getChildren().remove(monsters.get(i));
                                monsters.remove(monsters.get(i));
                                monsterList.remove(monsterList.get(i));
                                numMonsters++;
                                characterStatus(hpPane, myPlayer);
                            }

                        } catch (Exception e) {
                            System.out.println("Monster is already removed.");
                        }
                    }

                }

                // Move monsters
                moveMonsters(currTime);

                // Relocate player depending on arrow press and not at scene boundary
                if (goUp && (currY - delta> minY)) currY -= delta;
                if (goDown && (currY + delta < maxY)) currY += delta;
                if (goLeft && (currX - delta > minX)) currX -= delta;
                if (goRight && (currX + delta < maxX)) currX += delta;

                icon.relocate(currX, currY);
                myPlayer.setXPos(currX);
                myPlayer.setYPos(currY);

                // Add a monster at set time interval
                addMonsters(root);

                // Add a potion at set time interval
                addPotions(root);

                // Add a weapon at set time interval
                addWeapons(root);

                // Update character status panel
                characterStatus(hpPane, myPlayer);
            }
        };
        timer.start();
    }

    /**
     * Displays game starting screen
     * @param primaryStage
     * @param root
     * @param scene
     */
    public void displayIntroduction(Stage primaryStage, Group root, Scene scene) {
        // Variables
        StackPane introPane = new StackPane();
        Rectangle background = new Rectangle(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Set the Title of the Game
        Text title = new Text();
        title.setText("Escape the Monsters");
        title.setFill(Paint.valueOf("white"));
        introPane.setAlignment(title, Pos.TOP_CENTER);
        try {
            String fontFileString = "fonts/FireBrathers-PersonalUse.ttf";
            File file = new File(fontFileString);
            FileInputStream fileStream = new FileInputStream(file);
            title.setFont(Font.loadFont(fileStream, 70));
        } catch (Exception e) {
            title.setFont(Font.font("Verdana", FontWeight.BOLD,70));
        }

        // Add a button to start the game
        Button startGame = new Button("Start Game");
        startGame.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        startGame.setMinWidth(100);
        startGame.setMaxWidth(WINDOW_WIDTH/4);
        startGame.setMinHeight(25);
        startGame.setMaxHeight(WINDOW_HEIGHT/10);
        introPane.setAlignment(startGame, Pos.BOTTOM_CENTER);
        startGame.setOnAction(actionEvent -> {
            clearPlay(root);
            prepareBackground(primaryStage, root, scene);
        });

        // Add background, title, and start button to frame
        introPane.setMargin(title, new Insets(WINDOW_HEIGHT/4, 0, 0, 0));
        introPane.setMargin(startGame, new Insets(0, 0, WINDOW_HEIGHT/5, 0));
        introPane.getChildren().addAll(background, title, startGame);
        root.getChildren().add(introPane);
    }

    /**
     * Displays ending credits for end of gameplay
     * @param primaryStage
     * @param root
     * @param scene
     */
    public void displayClose(Stage primaryStage, Group root, Scene scene) {
        // Variables
        root.getChildren().clear();
        StackPane endPane = new StackPane();

        // Add new background
        Rectangle background = new Rectangle(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Add new text
        Text title = new Text();
        title.setText("GAME OVER");
        title.setFill(Paint.valueOf("white"));
        title.setFont(Font.font("Verdana", FontWeight.BOLD,70));
        endPane.setAlignment(title, Pos.TOP_CENTER);

        // Add new button to replay
        Button restartGame = new Button("Play Again");
        restartGame.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        restartGame.setMinWidth(100);
        restartGame.setMaxWidth(WINDOW_WIDTH/4);
        restartGame.setMinHeight(25);
        restartGame.setMaxHeight(WINDOW_HEIGHT/10);
        endPane.setAlignment(restartGame, Pos.BOTTOM_CENTER);
        restartGame.setOnAction(actionEvent -> {
            clearPlay(root);
            prepareBackground(primaryStage, root, scene);
        });

        // Set Margins
        endPane.setMargin(title, new Insets (WINDOW_HEIGHT/4, 0, 0, 0));
        endPane.setMargin(restartGame, new Insets(0, 0, WINDOW_HEIGHT/5, 0));
        endPane.getChildren().addAll(background, title, restartGame);
        root.getChildren().add(endPane);
    }

    /**
     * Removes all objects from the screen without exiting
     * @param root
     */
    public void clearPlay(Group root) {
        // Remove all items from the screen
        root.getChildren().clear();
    }

    /**
     * Sets up the gameboard with background and health points panel
     * @param primaryStage
     * @param root
     * @param scene
     */
    public void prepareBackground(Stage primaryStage, Group root, Scene scene) {
        try {
            // Gameplay
            StackPane backgroundPane = new StackPane();
            StackPane gamePane = new StackPane();
            StackPane hpPane = new StackPane();
            root.getChildren().addAll(backgroundPane, gamePane, hpPane);

            // Set background
            String backgroundFileName = "images/background2.png";
            Image backgroundImage = new Image (backgroundFileName);
            ImageView backgroundiv = new ImageView();
            backgroundiv.setImage(backgroundImage);
            backgroundiv.setPreserveRatio(true);
            backgroundiv.setFitWidth(WINDOW_WIDTH);
            backgroundPane.getChildren().add(backgroundiv);

            // Initiate player sprite
            String spriteFileName = "images/sprite.png";
            Player newPlayer = new Player(spriteFileName);
            Image spriteImage = new Image(spriteFileName);
            ImageView spriteIcon = new ImageView(spriteImage);
            spriteIcon.setFitHeight(newPlayer.getImgHeight());
            spriteIcon.setFitWidth(newPlayer.getImgWidth());
            icon = spriteIcon;
            icon.relocate(newPlayer.getXPos(), newPlayer.getYPos());
            root.getChildren().add(icon);

            // Initiate potions
            createPotions(root);

            // Initiate monster
            createMonsters(root);

            // Setup label display
            characterStatus(hpPane, newPlayer);

            // Capture player movement
            movement(primaryStage, root, scene, hpPane, icon, newPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the health points panel with updated character stats
     * @param hpPane
     * @param newPlayer
     */
    public void characterStatus(StackPane hpPane, Player newPlayer) {
        // Remove previous health points display
        hpPane.getChildren().clear();

        // Setup and add new health points display
        healthBarPanel = new Rectangle(WINDOW_WIDTH/4, WINDOW_HEIGHT/4);
        healthBarPanel.opacityProperty().setValue(0.4);
        healthPoints = new Text();
        displayLabel(newPlayer);
        healthPoints.setFill(Paint.valueOf("white"));
        hpPane.getChildren().addAll(healthBarPanel, healthPoints);
    }

    /**
     * Updates the content of the health points panel
     * @param myPlayer
     */
    public void displayLabel(Player myPlayer) {
        // Display character information
        healthPoints.setText("Player HP: " +  myPlayer.getHealthPoints() +
                "\nPlayer Attack Strength: " + myPlayer.getAttackStrength() +
                "\nMonsters Killed: " + numMonsters);
    }

    /**
     * Checks the distance between two points
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public boolean checkDistance(double x1, double y1, double x2, double y2) {
        // Check the distance between two points
        double distance = Math.sqrt(Math.pow(x1 - x2, 2) + (Math.pow(y1 - y2, 2)));

        // Return true is the distance is less than 50
        return (distance <= 50);
    }

    /**
     * Creates a new potion at a certain period of time
     * @param root
     */
    public void addPotions(Group root) {
        // Create a new potion at set time interval
        if (Math.round((System.currentTimeMillis() - potionTime) / 10) % 200 == 0) {
            potionTime = System.currentTimeMillis();
            createPotions(root);
        }
    }

    /**
     * Checks distance between potion and players
     * @param root
     * @param hpPane
     * @param myPlayer
     */
    public void checkPotions(Group root, StackPane hpPane, Player myPlayer) {
        // Variables
        double potionX, potionY, currX, currY;

        // Get player position
        currX = myPlayer.getXPos();
        currY = myPlayer.getYPos();

        // Check that every potion is close to player
        for (int i = 0 ; i < potions.size(); i++) {
            potionX = potions.get(i).getLayoutX();
            potionY = potions.get(i).getLayoutY();

            // Apply potion and remove from imagery if potion is encountered
            if (checkDistance(potionX, potionY, currX, currY)) {
                myPlayer.encounterPotion();
                root.getChildren().remove(potions.get(i));
                potions.remove(i);
                characterStatus(hpPane, myPlayer);
            }
        }
    }

    /**
     * Creates a new potion and adds to gameboard
     * @param root
     */
    public void createPotions(Group root) {
        try {
            // Setup a potion node
            Node newPotionNode;
            String potionImg = "images/potion.png";
            Potion newPotion = new Potion(potionImg);
            Image potionImage = new Image(potionImg);
            ImageView potionIcon = new ImageView(potionImage);
            potionIcon.setFitHeight(newPotion.getImgHeight());
            potionIcon.setFitWidth(newPotion.getImgWidth());
            newPotionNode = potionIcon;
            newPotionNode.relocate(newPotion.getXPos(), newPotion.getYPos());

            // Add a potion to the screen
            root.getChildren().add(newPotionNode);

            // Add a potion to the node list
            potions.add(newPotionNode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a weapon at a set time interval
     * @param root
     */
    public void addWeapons(Group root) {
        // Add a new weapon at set time interval
        if (Math.round((System.currentTimeMillis() - weaponTime)/10) % 5000 == 0) {
            weaponTime = System.currentTimeMillis();
            createWeapons(root);
        }
    }

    /**
     * Checks distance between a weapon and player
     * @param root
     * @param hpPane
     * @param myPlayer
     */
    public void checkWeapons(Group root, StackPane hpPane, Player myPlayer) {
        // Variables
        double weaponX, weaponY, currX, currY;

        // Get player position
        currX = myPlayer.getXPos();
        currY = myPlayer.getYPos();

        // Check that player is close to every weapon
        for (int i = 0 ; i < weapons.size(); i++) {
            weaponX = weapons.get(i).getLayoutX();
            weaponY = weapons.get(i).getLayoutY();

            // Apply weapon and remove from imagery if weapon is encountered
            if (checkDistance(weaponX, weaponY, currX, currY)) {
                myPlayer.increaseAttackStrength(weaponList.get(i).getAttackStrength());
                root.getChildren().remove(weapons.get(i));
                weapons.remove(i);
                weaponList.remove(i);
                characterStatus(hpPane, myPlayer);
            }
        }
    }

    /**
     * Creates a weapon and adds to the game board
     * @param root
     */
    public void createWeapons(Group root) {
        try {
            // Setup a potion node
            Node newWeaponNode;
            String weaponImg = "images/weapon.png";
            Armory newWeapon = new Armory(weaponImg);
            Image weaponImage = new Image(weaponImg);
            ImageView weaponIcon = new ImageView(weaponImage);
            weaponIcon.setFitHeight(newWeapon.getImgHeight());
            weaponIcon.setFitWidth(newWeapon.getImgWidth());
            newWeaponNode = weaponIcon;
            newWeaponNode.relocate(newWeapon.getXPos(), newWeapon.getYPos());

            // Add a potion to the screen
            root.getChildren().add(newWeaponNode);

            // Add a potion to the node list
            weapons.add(newWeaponNode);

            // Add weapon to weapon list
            weaponList.add(newWeapon);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a monster at set time interval; more monsters are added over time
     * @param root
     */
    public void addMonsters(Group root) {
        // Variables
        int numTimes = (int)((System.currentTimeMillis() - timeNow) / 10000);

        // Create new monsters at certain time interval
        if (Math.round((System.currentTimeMillis() - monsterTime)/10) % 1000 == 0) {
            monsterTime = System.currentTimeMillis();
            for (int i = 0; i < numTimes; i++) {
                createMonsters(root);
            }
        }
    }

    /**
     * Updates monster movement
     * @param currTime
     */
    public void moveMonsters(long currTime) {
        // Variables
        int direction;
        double monsterX, monsterY;
        final double monsterDelta = 1.0;

        // Move each monster in list
        for (int i = 0; i < monsters.size(); i++) {
            monsterX = monsters.get(i).getLayoutX();
            monsterY = monsters.get(i).getLayoutY();
            try {
                // Change direction every 5 seconds or when the edge of the screen is reached
                if (Math.round((currTime - timeNow) / 10) % 50 == 0 ||
                        ((monsterX - monsterDelta) < 0) || ((monsterX + monsterDelta) > maxX) ||
                        ((monsterY - monsterDelta) < 0) || ((monsterY + monsterDelta) > maxY)) {
                    monsterList.get(i).setDirection(rand.nextInt(3));
                }

                // Get the direction of each monster
                direction = monsterList.get(i).getDirection();

                // Move monster in certain direction until edge of screen is reached
                if (direction == 0 && (monsterY - monsterDelta > minY)) monsterY -= monsterDelta;
                if (direction == 1 && (monsterY + monsterDelta < maxY)) monsterY += monsterDelta;
                if (direction == 2 && (monsterX - monsterDelta > minX)) monsterX -= monsterDelta;
                if (direction == 3 && (monsterX + monsterDelta < maxX)) monsterX += monsterDelta;
                monsters.get(i).relocate(monsterX, monsterY);
                monsterList.get(i).setXPos(monsterX);
                monsterList.get(i).setYPos(monsterY);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds a new monster to the game board
     * @param root
     */
    public void createMonsters(Group root) {
        try {
            // Setup a monster node
            Node newMonsterNode;
            String monsterImg = "images/monster.png";
            Monster newMonster = new Monster(monsterImg);
            Image monsterImage = new Image(monsterImg);
            ImageView monsterIcon = new ImageView(monsterImage);
            monsterIcon.setFitHeight(newMonster.getImgHeight());
            monsterIcon.setFitWidth(newMonster.getImgWidth());
            newMonsterNode = monsterIcon;
            newMonsterNode.relocate(newMonster.getXPos(), newMonster.getYPos());

            // Add monster to screen
            root.getChildren().add(newMonsterNode);

            // Add monster node to list
            monsters.add(newMonsterNode);

            // Add monster to monster list
            monsterList.add(newMonster);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}
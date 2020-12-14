package gameplay;

// Refer to https://stackoverflow.com/questions/29057870/in-javafx-how-do-i-move-a-sprite-across-the-screen
// Refer to https://www.youtube.com/watch?v=kkZ-YNv7B0E
// Refer to https://www.youtube.com/watch?v=yG8YCLYccVo

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;
import java.util.Vector;

public class Main extends Application implements Runnable {
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
    Vector<Node> monsters = new Vector<Node>();
    Vector<Node> potions = new Vector<Node>();
    Vector<Node> weapons = new Vector<Node>();
    Vector<Monster> monsterList = new Vector<Monster>();
    Vector<Armory> weaponList = new Vector<Armory>();
    Node icon;
    Random rand = new Random();
    Text healthPoints;
    Rectangle healthBarPanel;

    @Override
    public void start(Stage primaryStage) throws Exception{
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

    public void movement(Stage primaryStage, Group root, Scene scene, StackPane hpPane, Node icon, Player myPlayer) {

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
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
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
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
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            double delta = 5;
            double currX, currY, monsterX, monsterY;
            long currTime;

            @Override
            public void handle(long arg0) {
                // MOVE ICON
                currX = icon.getLayoutX();
                currY = icon.getLayoutY();
                currTime = System.currentTimeMillis();

                // Check that player has not died
                if (myPlayer.checkStatus() == true) {
                    root.getChildren().remove(icon);
                    this.stop();
                    displayClose(primaryStage, root, scene);
                }

                // CHECK THAT ICON IS CLOSE TO POTIONS
                checkPotions(root, hpPane, myPlayer);

                // CHECK THAT ICON IS CLOSE TO WEAPONS
                checkWeapons(root, hpPane, myPlayer);

                // CHECK THAT ICON IS CLOSE TO MONSTER
                // For every monster in screen, track location and interaction
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
                                } catch (Exception e) {}
                            }

                            // Remove monster once dead
                            if (monsterList.get(i).checkStatus() == true) {
                                root.getChildren().remove(monsters.get(i));
                                monsters.remove(monsters.get(i));
                                monsterList.remove(monsterList.get(i));
                                numMonsters++;
                                characterStatus(hpPane, myPlayer);
                            }

                        } catch (Exception e) {}
                    }

                }

                // Move monsters
                moveMonsters(currTime);

                // Relocate player
                // Limit movement to correct arrow press and not at scene boundary
                if (goUp && (currY - delta> minY)) currY -= delta;
                if (goDown && (currY + delta < maxY)) currY += delta;
                if (goLeft && (currX - delta > minX)) currX -= delta;
                if (goRight && (currX + delta < maxX)) currX += delta;

                icon.relocate(currX, currY);
                myPlayer.setXPos(currX);
                myPlayer.setYPos(currY);

                // Add a monster every 5 seconds
                addMonsters(root);

                // Add a potion every 5 seconds
                addPotions(root);

                // Add a weapon every 50 seconds
                addWeapons(root);

                characterStatus(hpPane, myPlayer);
            }
        };
        timer.start();
    }

    public void displayIntroduction(Stage primaryStage, Group root, Scene scene) {
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

        // Add a Button
        Button startGame = new Button("Start Game");
        startGame.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        startGame.setMinWidth(100);
        startGame.setMaxWidth(WINDOW_WIDTH/4);
        startGame.setMinHeight(25);
        startGame.setMaxHeight(WINDOW_HEIGHT/10);
        introPane.setAlignment(startGame, Pos.BOTTOM_CENTER);
        startGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                clearPlay(root);
                prepareBackground(primaryStage, root, scene);
            }
        });

        // Add Rectangle and Title to the Root
        introPane.setMargin(title, new Insets (WINDOW_HEIGHT/4, 0, 0, 0));
        introPane.setMargin(startGame, new Insets(0, 0, WINDOW_HEIGHT/5, 0));
        introPane.getChildren().addAll(background, title, startGame);
        root.getChildren().add(introPane);
    }

    public void displayClose(Stage primaryStage, Group root, Scene scene) {
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

        // Add new button
        Button restartGame = new Button("Play Again");
        restartGame.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        restartGame.setMinWidth(100);
        restartGame.setMaxWidth(WINDOW_WIDTH/4);
        restartGame.setMinHeight(25);
        restartGame.setMaxHeight(WINDOW_HEIGHT/10);
        endPane.setAlignment(restartGame, Pos.BOTTOM_CENTER);
        restartGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                clearPlay(root);
                prepareBackground(primaryStage, root, scene);
            }
        });

        // Set Margins
        endPane.setMargin(title, new Insets (WINDOW_HEIGHT/4, 0, 0, 0));
        endPane.setMargin(restartGame, new Insets(0, 0, WINDOW_HEIGHT/5, 0));
        endPane.getChildren().addAll(background, title, restartGame);
        root.getChildren().add(endPane);
    }

    public void clearPlay(Group root) {
        root.getChildren().clear();
    }

    public void prepareBackground(Stage primaryStage, Group root, Scene scene) {
        try {
            // Gameplay
            StackPane backgroundPane = new StackPane();
            StackPane gamePane = new StackPane();
            StackPane hpPane = new StackPane();
            root.getChildren().addAll(backgroundPane, gamePane, hpPane);

            // Set Background
            // Eventually simply prompt map to give a scene and give boolean for which locations can be moved; give map dimensions
            String backgroundFileName = "images/background2.png";
            Image backgroundImage = new Image (backgroundFileName);
            ImageView backgroundiv = new ImageView();
            backgroundiv.setImage(backgroundImage);
            backgroundiv.setPreserveRatio(true);
            backgroundiv.setFitWidth(WINDOW_WIDTH);
            backgroundPane.getChildren().add(backgroundiv);

            // Initiate Sprite
            String spriteFileName = "images/sprite.png";
            Player newPlayer = new Player(spriteFileName);
            Image spriteImage = new Image(spriteFileName);
            ImageView spriteIcon = new ImageView(spriteImage);
            spriteIcon.setFitHeight(newPlayer.getImgHeight());
            spriteIcon.setFitWidth(newPlayer.getImgWidth());
            icon = spriteIcon;
            icon.relocate(newPlayer.getXPos(), newPlayer.getYPos());
            root.getChildren().add(icon);

            // Initiate Potions
            createPotions(root);

            // Initiate Monster
            createMonsters(root);

            // Setup Label Display
            characterStatus(hpPane, newPlayer);

            movement(primaryStage, root, scene, hpPane, icon, newPlayer);
        } catch (Exception e) {

        }
    }

    public void characterStatus(StackPane hpPane, Player newPlayer) {
        hpPane.getChildren().clear();
        healthBarPanel = new Rectangle(WINDOW_WIDTH/4, WINDOW_HEIGHT/4);
        healthBarPanel.opacityProperty().setValue(0.4);
        healthPoints = new Text();
        displayLabel(newPlayer);
        healthPoints.setFill(Paint.valueOf("white"));
        hpPane.getChildren().addAll(healthBarPanel, healthPoints);
    }

    public void displayLabel(Player myPlayer) {
        healthPoints.setText("Player HP: " +  myPlayer.getHealthPoints() +
                "\nPlayer Attack Strength: " + myPlayer.getAttackStrength() +
                "\nMonsters Killed: " + numMonsters);
    }

    public boolean checkDistance(double x1, double y1, double x2, double y2) {
        double distance = Math.sqrt(Math.pow(x1 - x2, 2) + (Math.pow(y1 - y2, 2)));
        return (distance <= 50);
    }

    public void addPotions(Group root) {
        if (Math.round((System.currentTimeMillis() - potionTime) / 10) % 200 == 0) {
            potionTime = System.currentTimeMillis();
            createPotions(root);
        }
    }

    public void checkPotions(Group root, StackPane hpPane, Player myPlayer) {
        double potionX, potionY, currX, currY;

        currX = myPlayer.getXPos();
        currY = myPlayer.getYPos();
        // CHECK THAT ICON IS CLOSE TO POTIONS
        // For every potion in the screen, perform task
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

    public void addWeapons(Group root) {
        if (Math.round((System.currentTimeMillis() - weaponTime)/10) % 5000 == 0) {
            weaponTime = System.currentTimeMillis();
            createWeapons(root);
        }
    }

    public void checkWeapons(Group root, StackPane hpPane, Player myPlayer) {
        double weaponX, weaponY, currX, currY;

        currX = myPlayer.getXPos();
        currY = myPlayer.getYPos();
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

    public void addMonsters(Group root) {
        if (Math.round((System.currentTimeMillis() - monsterTime)/10) % 1000 == 0) {
            monsterTime = System.currentTimeMillis();
            createMonsters(root);
        }
    }

    public void checkMonsters(Group root, StackPane hpPane, Player myPlayer) {

    }

    public void moveMonsters(long currTime) {
        int direction;
        double monsterX, monsterY, monsterDelta;
        monsterDelta = 1;

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
            }
        }
    }

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

    public void run() {}

    public static void main(String[] args) {
        launch(args);
    }
}
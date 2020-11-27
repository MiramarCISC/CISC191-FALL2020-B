package sample;

// Refer to https://stackoverflow.com/questions/29057870/in-javafx-how-do-i-move-a-sprite-across-the-screen
// Refer to https://www.youtube.com/watch?v=kkZ-YNv7B0E

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.Group;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.util.Random;
import java.util.Vector;

public class Main extends Application implements Runnable {
    // Variables
    boolean goUp, goDown, goLeft, goRight, attacked;
    double minX, minY, maxX, maxY;
    long timeNow = System.currentTimeMillis();
    long monsterTime = System.currentTimeMillis();
    long potionTime = System.currentTimeMillis();
    Vector<Node> monsters = new Vector<Node>();
    Vector<Node> potions = new Vector<Node>();
    Vector<Monster> monsterList = new Vector<Monster>();
    Node icon;
    String path = System.getProperty("user.dir");

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Game title
        primaryStage.setTitle("Escape the Monsters");

        // Instantiate game
        try {

            // Setup Background
            Group root = new Group();
            StackPane pane = new StackPane();
            root.getChildren().add(pane);

            // Set Background
            // Eventually simply prompt map to give a scene and give boolean for which locations can be moved; give map dimensions
            String backgroundFileName = path + "\\src\\sample\\images\\background2.png";
            FileInputStream backgroundStream = new FileInputStream(backgroundFileName);
            Image backgroundImage = new Image(backgroundStream);
            ImageView backgroundiv = new ImageView();
            backgroundiv.setImage(backgroundImage);
            backgroundiv.setPreserveRatio(true);
            backgroundiv.setFitWidth(1000);
            pane.getChildren().add(backgroundiv);

            // Initiate Sprite
            String spriteFileName = path + "\\src\\sample\\images\\sprite.png";
            Player newPlayer = new Player(spriteFileName);
            FileInputStream spriteStream = new FileInputStream(spriteFileName);
            Image spriteImage = new Image(spriteStream);
            ImageView spriteIcon = new ImageView(spriteImage);
            spriteIcon.setFitHeight(newPlayer.getImgHeight());
            spriteIcon.setFitWidth(newPlayer.getImgWidth());
            icon = spriteIcon;
            icon.relocate(newPlayer.getXPos(), newPlayer.getYPos());
            root.getChildren().add(icon);

            // Setup Scene
            Scene scene = new Scene(root, 1000, 500);
            minX = 0;
            minY = 0;
            maxX = 1000 - 30;
            maxY = 500 - 30;

            // Initiate Potions
            createPotions(scene, root);

            // Initiate Monster
            createMonsters(scene, root);

            // Display GUI
            primaryStage.setScene(scene);
            primaryStage.show();

            // Sprite and monster movements
            movement(primaryStage, root, scene, icon, newPlayer);

            // Output Player's HP
            System.out.println("Player HP: " + newPlayer.getHealthPoints());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void movement(Stage primaryStage, Group root, Scene scene, Node icon, Player myPlayer) {
        // eventually move character position to character class and use get and set

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
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
                    case D:
                        attacked = true;
                        break;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
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
                    case D:
                        attacked = false;
                        break;
                }
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            double delta = 5;
            double monsterDelta = 1;
            int direction;
            Random rand = new Random();

            @Override
            public void handle(long arg0) {
                // Icon
                double currX = icon.getLayoutX();
                double currY = icon.getLayoutY();
                long time2 = System.currentTimeMillis();

                if (goUp && (currY - delta> minY)) currY -= delta;
                if (goDown && (currY + delta < maxY)) currY += delta;
                if (goLeft && (currX - delta > minX)) currX -= delta;
                if (goRight && (currX + delta < maxX)) currX += delta;

                // CHECK THAT CLOSE TO POTIONS
                double potionX;
                double potionY;

                for (int i = 0 ; i < potions.size(); i++) {
                    potionX = potions.get(i).getLayoutX();
                    potionY = potions.get(i).getLayoutY();
                    if (checkDistance(potionX, potionY, currX, currY)) {
                        myPlayer.encounterPotion();
                        root.getChildren().remove(potions.get(i));
                        potions.remove(i);
                        System.out.println("Player HP: " + myPlayer.getHealthPoints());
                    }
                }

                // CHECK THAT CLOSE TO MONSTER
                double monsterX;
                double monsterY;
                for (int i = 0 ; i < monsters.size(); i++) {
                    monsterX = monsters.get(i).getLayoutX();
                    monsterY = monsters.get(i).getLayoutY();
                    if (myPlayer.checkStatus() == true) {
                        System.out.println("Game over.");
                        root.getChildren().remove(icon);
                        primaryStage.close();
                    }
                    if (checkDistance(monsterX, monsterY, currX, currY)) {
                        try {
                            myPlayer.attacked(monsterList.get(i).getAttackStrength(), time2, timeNow);
                            if (attacked) {
                                try {
                                    System.out.println("Attacking!");
                                    monsterList.get(i).attacked(myPlayer.getAttackStrength());
                                } catch (Exception e) {
                                }
                                if (monsterList.get(i).checkStatus() == true) {
                                    System.out.println("You defeated a monster!");
                                    root.getChildren().remove(monsters.get(i));
                                    monsterList.remove(monsterList.get(i));
                                } else {
                                    System.out.println("Player HP: " + myPlayer.getHealthPoints() +
                                            " Monster HP: " + monsterList.get(i).getHealthPoints());
                                }
                            }
                        } catch (Exception e) {
                        }
                    }

                    try {
                        if (Math.round(time2 - timeNow) % 5000 == 0 ||
                                ((monsterX - monsterDelta) < 0)|| ((monsterX + monsterDelta) > maxX) ||
                                ((monsterY - monsterDelta) < 0) || ((monsterY + monsterDelta) > maxY)) {

                            monsterList.get(i).setDirection(rand.nextInt(4));
                        }
                        direction = monsterList.get(i).getDirection();
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

                // Add a monster and potion
                if (Math.round((System.currentTimeMillis() - monsterTime)/10) % 500 == 0) {
                    monsterTime = System.currentTimeMillis();
                    createMonsters(scene, root);
                }

                if (Math.round((System.currentTimeMillis() - potionTime)/10) % 500 == 0) {
                    potionTime = System.currentTimeMillis();
                    createPotions(scene, root);
                }

                // Relocate player
                icon.relocate(currX, currY);
                myPlayer.setXPos(currX);
                myPlayer.setYPos(currY);
            }
        };
        timer.start();
    }

    public void monsterMovement(Scene scene, Node monsterNode, Monster thisMonster) {
        // Eventually move all monster parameters to class and update position/ get position
        // add an attack method where if the monster is close enough, change attack at a timer

        AnimationTimer timer = new AnimationTimer() {
            double monsterDelta = 1;
            int direction;
            Random rand = new Random();

            @Override
            public void handle(long arg0) {
                double monsterCurrX = monsterNode.getLayoutX();
                double monsterCurrY = monsterNode.getLayoutY();
                long time2 = System.currentTimeMillis();

                if ((time2 - timeNow) % 3000 == 0 ||
                     ((monsterCurrX - monsterDelta) < 0)|| ((monsterCurrX + monsterDelta) > maxX) ||
                        ((monsterCurrY - monsterDelta) < 0) || ((monsterCurrY + monsterDelta) > maxY)) {

                    direction = rand.nextInt(4);
                }
                if (direction == 0 && (monsterCurrY - monsterDelta > minY)) monsterCurrY -= monsterDelta;
                if (direction == 1 && (monsterCurrY + monsterDelta < maxY)) monsterCurrY += monsterDelta;
                if (direction == 2 && (monsterCurrX - monsterDelta > minX)) monsterCurrX -= monsterDelta;
                if (direction == 3 && (monsterCurrX + monsterDelta < maxX)) monsterCurrX += monsterDelta;
                monsterNode.relocate(monsterCurrX, monsterCurrY);
                thisMonster.setXPos(monsterCurrX);
                thisMonster.setYPos(monsterCurrY);
            }
        };
        timer.start();
    }

    public boolean checkDistance(double x1, double y1, double x2, double y2) {
        double distance = Math.sqrt(Math.pow(x1 - x2, 2) + (Math.pow(y1 - y2, 2)));
        return (distance <= 50);
    }
    public void createPotions(Scene scene, Group root) {
        try {
            Node newPotionNode;
            String potionImg = path + "\\src\\sample\\images\\potion.png";
            Potion newPotion = new Potion(potionImg);
            FileInputStream potionStream = new FileInputStream(potionImg);
            Image potionImage = new Image(potionStream);
            ImageView potionIcon = new ImageView(potionImage);
            potionIcon.setFitHeight(newPotion.getImgHeight());
            potionIcon.setFitWidth(newPotion.getImgWidth());
            newPotionNode = potionIcon;
            potions.add(newPotionNode);
            newPotionNode.relocate(newPotion.getXPos(), newPotion.getYPos());
            root.getChildren().add(newPotionNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createMonsters(Scene scene, Group root) {
        try {
            Node newMonsterNode;
            String monsterImg = path + "\\src\\sample\\images\\monster.png";
            Monster newMonster = new Monster(monsterImg);
            FileInputStream monsterStream = new FileInputStream(monsterImg);
            Image monsterImage = new Image(monsterStream);
            ImageView monsterIcon = new ImageView(monsterImage);
            monsterIcon.setFitHeight(newMonster.getImgHeight());
            monsterIcon.setFitWidth(newMonster.getImgWidth());
            newMonsterNode = monsterIcon;
            monsters.add(newMonsterNode);
            monsterList.add(newMonster);
            newMonsterNode.relocate(newMonster.getXPos(), newMonster.getYPos());
            root.getChildren().add(newMonsterNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
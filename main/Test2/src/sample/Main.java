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
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.Group;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.util.Random;
import java.util.Vector;

public class Main extends Application {
    boolean goUp, goDown, goLeft, goRight, attacked;
    double minX, minY, maxX, maxY;
    Node icon;
    Node monster;
    Vector<Node> potions;
    Node potion;
    long timeNow = System.currentTimeMillis();

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Test Game");

        // Monsters
        Random rand = new Random();
        timeNow = System.currentTimeMillis();
        String path = System.getProperty("user.dir");

        try {
            Group root = new Group();

            StackPane pane = new StackPane();
            Pane monsterPane = new Pane();
            root.getChildren().add(pane);
            root.getChildren().add(monsterPane);

            // Set Background
            // Eventually simply prompt map to give a scene and give boolean for which locations can be moved; give map dimensions
            FileInputStream backgroundStream = new FileInputStream("C:\\Users\\cheun\\Desktop\\CISC191-FALL2020-B\\main\\Test2\\src\\sample\\images\\background2.png");
            Image backgroundImage = new Image(backgroundStream);
            ImageView backgroundiv = new ImageView();
            backgroundiv.setImage(backgroundImage);
            backgroundiv.setPreserveRatio(true);
            backgroundiv.setFitWidth(1000);
            pane.getChildren().add(backgroundiv);

            // Sprite
            // Eventually move sprite details to class: get file name, get icon size
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

            // Monster
            // Eventually move monster to class: get file name; get icon size (needs attack strength, speed)
            // Randomly generate monster (create a new method to handle this and add to vector here)
            String monsterImg = path + "\\src\\sample\\images\\monster.png";
            Monster newMonster = new Monster(monsterImg);
            FileInputStream monsterStream = new FileInputStream(monsterImg);
            Image monsterImage = new Image(monsterStream);
            ImageView monsterIcon = new ImageView(monsterImage);
            monsterIcon.setFitHeight(newMonster.getImgHeight());
            monsterIcon.setFitWidth(newMonster.getImgWidth());
            monster = monsterIcon;
            monster.relocate(newMonster.getXPos(), newMonster.getYPos());
            root.getChildren().add(monster);

            // Potion
            String potionImg = path + "\\src\\sample\\images\\potion.png";
            Potion newPotion = new Potion(potionImg);
            FileInputStream potionStream = new FileInputStream(potionImg);
            Image potionImage = new Image(potionStream);
            ImageView potionIcon = new ImageView(potionImage);
            potionIcon.setFitHeight(newPotion.getImgHeight());
            potionIcon.setFitWidth(newPotion.getImgWidth());
            potion = potionIcon;
            potion.relocate(newPotion.getXPos(), newPotion.getYPos());
            root.getChildren().add(potion);

            // Scene
            Scene scene = new Scene(root, 1000, 500);
            minX = 0;
            minY = 0;
            maxX = 1000 - 30;
            maxY = 500 - 30;
            primaryStage.setScene(scene);
            primaryStage.show();

            // Sprite and monster movements
            // add threading here? perform movement for character and loop through monster vector
            movement(root, scene, icon, newPlayer, monster, newMonster, potion);
            //monsterMovement(scene, monsterIcon, newMonster);

            System.out.println("Player HP: " + newPlayer.getHealthPoints()
                    + ", Monster HP: " + newMonster.getHealthPoints());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void movement(Group root, Scene scene, Node icon, Player myPlayer, Node monsterIcon, Monster myMonster, Node myPotion) {
        // eventually move character position to character class and use get and set
        // eventually add a keypress event to update character attacks/potions, etc.
        // find out how to end the game (close window or close panes) when the character has died

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
                double monsterX = myMonster.getXPos();
                double monsterY = myMonster.getYPos();
                double potionX = potion.getLayoutX();
                double potionY = potion.getLayoutY();

                double distance = Math.sqrt(Math.pow((currX-monsterX),2)+Math.pow((currY-monsterY), 2));
                double distance2 = Math.sqrt(Math.pow((currX-potionX),2)+Math.pow((currY-potionY), 2));

                if (goUp && (currY - delta> minY)) currY -= delta;
                if (goDown && (currY + delta < maxY)) currY += delta;
                if (goLeft && (currX - delta > minX)) currX -= delta;
                if (goRight && (currX + delta < maxX)) currX += delta;
                if (distance2 <= 100) {
                    myPlayer.encounterPotion();
                    root.getChildren().remove(potion);
                    System.out.println("Player HP: " + myPlayer.getHealthPoints());
                }
                if (attacked && distance <= 100) {
                    myMonster.attacked(myPlayer.attackStrength);
                    myPlayer.attacked(myMonster.attackStrength);

                    if (myPlayer.checkStatus() == true) {
                        System.out.println("Game over.");
                        root.getChildren().remove(icon);
                    } else if (myMonster.checkStatus() == true) {
                        System.out.println("You defeated the monster!");
                        root.getChildren().remove(monster);
                    } else {
                        System.out.println("Player HP: " + myPlayer.getHealthPoints()
                                + ", Monster HP: " + myMonster.getHealthPoints());
                    }
                }
                icon.relocate(currX, currY);
                myPlayer.setXPos(currX);
                myPlayer.setYPos(currY);

                // Monster Information
                double monsterCurrX = monster.getLayoutX();
                double monsterCurrY = monster.getLayoutY();
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
                monster.relocate(monsterCurrX, monsterCurrY);
                myMonster.setXPos(monsterCurrX);
                myMonster.setYPos(monsterCurrY);


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

    public static void main(String[] args) {
        launch(args);
    }
}
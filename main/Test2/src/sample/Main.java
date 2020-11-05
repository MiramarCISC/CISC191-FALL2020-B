package sample;

// Refer to https://stackoverflow.com/questions/29057870/in-javafx-how-do-i-move-a-sprite-across-the-screen
// Refer to https://www.youtube.com/watch?v=kkZ-YNv7B0E

import javafx.animation.Animation;
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

public class Main extends Application {
    boolean goUp, goDown, goLeft, goRight;
    double minX, minY, maxX, maxY;
    Node icon;
    long timeNow = System.currentTimeMillis();

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Test Game");

        // Monsters
        Node monster;
        Random rand = new Random();
        timeNow = System.currentTimeMillis();

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
            FileInputStream spriteStream = new FileInputStream("C:\\Users\\cheun\\Desktop\\CISC191-FALL2020-B\\main\\Test2\\src\\sample\\images\\sprite.png");
            Image spriteImage = new Image(spriteStream);
            ImageView spriteIcon = new ImageView(spriteImage);
            spriteIcon.setFitHeight(30);
            spriteIcon.setFitWidth(30);
            icon = spriteIcon;
            icon.relocate(300, 200);
            root.getChildren().add(icon);

            // Monster
            // Eventually move monster to class: get file name; get icon size (needs attack strength, speed)
            // Randomly generate monster (create a new method to handle this and add to vector here)
            FileInputStream monsterStream = new FileInputStream("C:\\Users\\cheun\\Desktop\\CISC191-FALL2020-B\\main\\Test2\\src\\sample\\images\\monster.png");
            Image monsterImage = new Image(monsterStream);
            ImageView monsterIcon = new ImageView(monsterImage);
            monsterIcon.setFitHeight(30);
            monsterIcon.setFitWidth(30);
            monster = monsterIcon;
            monster.relocate(rand.nextInt(1000), rand.nextInt(500));
            root.getChildren().add(monster);

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
            movement(scene, icon);
            monsterMovement(scene, monsterIcon);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void movement(Scene scene, Node icon) {
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
                }
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            double delta = 5;

            @Override
            public void handle(long arg0) {
                double currX = icon.getLayoutX();
                double currY = icon.getLayoutY();

                if (goUp && (currY - delta> minY)) currY -= delta;
                if (goDown && (currY + delta < maxY)) currY += delta;
                if (goLeft && (currX - delta > minX)) currX -= delta;
                if (goRight && (currX + delta < maxX)) currX += delta;
                icon.relocate(currX, currY);
            }
        };
        timer.start();
    }

    public void monsterMovement(Scene scene, Node monster) {
        // Eventually move all monster parameters to class and update position/ get position
        // add an attack method where if the monster is close enough, change attack at a timer
        double time;
        double currX = monster.getLayoutX();
        double currY = monster.getLayoutY();

        AnimationTimer timer = new AnimationTimer() {
            double delta = 5;
            int direction;
            Random rand = new Random();

            @Override
            public void handle(long arg0) {
                double currX = monster.getLayoutX();
                double currY = monster.getLayoutY();
                long time2 = System.currentTimeMillis();

                if ((time2 - timeNow) % 100 == 0 ||
                     ((currX - delta) < 0)|| ((currX + delta) > maxX) ||
                        ((currY - delta) < 0) || ((currY + delta) > maxY)) {

                    direction = rand.nextInt(4);
                }
                if (direction == 0 && (currY - delta> minY)) currY -= delta;
                if (direction == 1 && (currY + delta< maxY)) currY += delta;
                if (direction == 2 && (currX - delta > minX)) currX -= delta;
                if (direction == 3 && (currX + delta < maxX)) currX += delta;
                monster.relocate(currX, currY);
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package sample;

// Refer to https://stackoverflow.com/questions/29057870/in-javafx-how-do-i-move-a-sprite-across-the-screen
// Refer to https://www.youtube.com/watch?v=kkZ-YNv7B0E

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;

/*
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.*;
import javafx.scene.web.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import java.io.*;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.paint.*;
*/

public class Main extends Application {
    boolean goUp, goDown, goLeft, goRight;
    Node icon;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Test Game");
        try {
            Group root = new Group();

            StackPane pane = new StackPane();
            root.getChildren().add(pane);

            // Set Background
            FileInputStream backgroundStream = new FileInputStream("C:\\Users\\cheun\\Desktop\\CISC191-FALL2020-B\\main\\Test2\\src\\sample\\images\\background2.png");
            Image background = new Image(backgroundStream);
            ImageView backgroundiv = new ImageView();
            backgroundiv.setImage(background);
            backgroundiv.setPreserveRatio(true);
            backgroundiv.setFitWidth(1000);
            pane.getChildren().addAll(backgroundiv);

            // Sprite
            FileInputStream input = new FileInputStream("C:\\Users\\cheun\\Desktop\\CISC191-FALL2020-B\\main\\Test2\\src\\sample\\images\\sprite.png");
            Image myIconImage = new Image(input);
            ImageView myIcon = new ImageView(myIconImage);
            myIcon.setFitHeight(30);
            myIcon.setFitWidth(30);
            icon = myIcon;
            icon.relocate(300, 200);
            root.getChildren().add(icon);

            Scene scene = new Scene(root, 1000, 500);
            primaryStage.setScene(scene);
            primaryStage.show();

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

                    if (goUp) currY -= delta;
                    if (goDown) currY += delta;
                    if (goLeft) currX -= delta;
                    if (goRight) currX += delta;
                    icon.relocate(currX, currY);
                }
            };
            timer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*try {
            // Set Title
            primaryStage.setTitle("Test Game");

            // Set Scene
            StackPane pane = new StackPane();
            Pane characterPane = new Pane();
            Scene scene = new Scene(pane, 500, 500);

            // Set Background Image
            FileInputStream backgroundStream = new FileInputStream("C:\\Users\\cheun\\Desktop\\CISC191-FALL2020-B\\main\\Test2\\src\\sample\\images\\background2.png");
            Image background = new Image( backgroundStream );
            ImageView backgroundiv = new ImageView();
            backgroundiv.setImage(background);
            pane.getChildren().addAll(backgroundiv, characterPane);

            // Set Sprite Image
            FileInputStream spriteStream = new FileInputStream("C:\\Users\\cheun\\Desktop\\CISC191-FALL2020-B\\main\\Test2\\src\\sample\\images\\sprite.png");
            Image sprite = new Image( spriteStream );
            ImageView spriteiv = new ImageView();
            spriteiv.setImage(sprite);
            spriteiv.setFitHeight(40);
            spriteiv.setFitWidth(40);
            icon = spriteiv;
            icon.relocate(200,200);
            characterPane.getChildren().add(icon);

            // Scene Display
            primaryStage.setScene(scene);
            primaryStage.show();

            // Key Press
            icon.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    switch (keyEvent.getCode()) {
                        case UP: goUp=true; break;
                        case DOWN: goDown=true; break;
                        case LEFT: goLeft=true; break;
                        case RIGHT: goRight=true; break;
                    }
                }
            });

            icon.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    switch (keyEvent.getCode()) {
                        case UP: goUp=false; break;
                        case DOWN: goDown=false; break;
                        case LEFT: goLeft=false; break;
                        case RIGHT: goRight=false; break;
                    }
                }
            });

            AnimationTimer timer = new AnimationTimer() {
                double delta = 5;

                @Override
                public void handle(long arg0) {
                    double currX = icon.getLayoutX();
                    double currY = icon.getLayoutY();

                    if (goUp) currY -= delta;
                    if (goDown) currY+= delta;
                    if (goLeft) currX -= delta;
                    if (goRight) currX += delta;
                    icon.relocate(currX,currY);
                }
            };
            timer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }*/


        /*
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Test Game");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
         */

        /*
        primaryStage.setTitle( "Canvas Example" );

        Group root = new Group();
        Scene theScene = new Scene( root );
        primaryStage.setScene( theScene );

        Canvas canvas = new Canvas( 800, 400 );
        Pane pane = new Pane(root);
        root.getChildren().add( canvas );

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill( Color.RED );
        gc.setStroke( Color.BLACK );
        gc.setLineWidth(2);
        Font theFont = Font.font( "Times New Roman", FontWeight.BOLD, 48 );
        gc.setFont( theFont );
        gc.fillText( "Hello, World!", 60, 50 );
        gc.strokeText( "Hello, World!", 60, 50 );

        FileInputStream inputStream = new FileInputStream("C:\\Users\\cheun\\Desktop\\CISC191-FALL2020-B\\main\\Test2\\src\\sample\\images\\background.jpg");

        //ImageView background = new ImageView(new Image(inputStream,400,400,false,true));
        Image image = new Image( inputStream );
        BackgroundImage backgroundImage= new BackgroundImage(image,
                                                            null,
                                                            null,
                                                            null,
                                                            null);
        Background background = new Background(backgroundImage);
        pane.setBackground(background);

        //Image background = new Image( inputStream );
        //gc.drawImage( background, 80, 80 );

        primaryStage.show();*/

    }


    public static void main(String[] args) {
        launch(args);
    }
}
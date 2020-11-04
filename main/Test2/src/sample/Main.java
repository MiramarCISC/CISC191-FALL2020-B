package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {

            FileInputStream inputStream = new FileInputStream("C:\\Users\\cheun\\Desktop\\CISC191-FALL2020-B\\main\\Test2\\src\\sample\\images\\background.jpg");
            FileInputStream inputStream2 = new FileInputStream("C:\\Users\\cheun\\Desktop\\CISC191-FALL2020-B\\main\\Test2\\src\\sample\\images\\background2.png");
            Image image = new Image( inputStream2 );

            ImageView iv = new ImageView();
            iv.setImage(image);

            StackPane pane = new StackPane();
            Scene scene = new Scene(pane, 500, 500);

            pane.getChildren().add(iv);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


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

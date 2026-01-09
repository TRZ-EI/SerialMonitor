package org.talamona.terminal.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

/** 
 * Drawing Text
 * DrawingText.java
 * @author carldea
 */
public class DrawingTextOriginal extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chapter 2 Drawing Text");
        Group root = new Group();
        Scene scene = new Scene(root, 300, 250, Color.WHITE);
        Random rand = new Random(System.currentTimeMillis());

        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt((int) scene.getWidth());
            int y = rand.nextInt((int) scene.getHeight());
            int red = rand.nextInt(255);
            int green = rand.nextInt(255);
            int blue = rand.nextInt(255);

            Text text = new Text(x, y, "JavaFX 8");

            int rot = rand.nextInt(360);
            text.setFill(Color.rgb(red, green, blue, .99));
            text.setRotate(rot);
            root.getChildren().add(text);
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void addMouseEventToStart(final Scene scene){
        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                if (me.getButton() == MouseButton.PRIMARY) {
                    try {





                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                else if (me.getButton() == MouseButton.SECONDARY) {
                    Platform.runLater(() ->{
                        // TODO: start process on mouse action
                    });
                }
                me.consume();

            }
        });
    }

}
package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = new ColorPane();
        //Parent root = new CanvasPainter(300);

        BorderPane root = new BorderPane();
        Fractal fc = new Fractal(-0.7453982727809063, 0.12298451741244903);
        //Fractal fc = new Fractal(0.435396403, 0.451687191, 0.367981352, 0.380210061, 0.44, 0.37);
        root.setCenter(fc);

        HBox sp = new HBox();
        root.setTop(sp);

        Button play = new Button("Play");
        play.setOnAction(actionEvent -> {
            fc.play();
        });
        sp.getChildren().add(play);

        Button pause = new Button("Pause");
        pause.setOnAction(actionEvent -> {
            fc.pause();
        });
        sp.getChildren().add(pause);

        Button stop = new Button("Stop");
        stop.setOnAction(actionEvent -> {
            fc.stop();
        });
        sp.getChildren().add(stop);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

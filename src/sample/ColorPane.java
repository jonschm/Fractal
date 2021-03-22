package sample;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ColorPane extends Pane {
    public ColorPane() {
        //setBackground(new Background(new BackgroundFill(Color.rgb(200,100,0), CornerRadii.EMPTY, Insets.EMPTY)));

        Rectangle[][] squares = new Rectangle[4][4];

        ColorGenerator cg = new ColorGenerator(Color.rgb(200, 200, 0));

        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                squares[i][j] = new Rectangle();
                squares[i][j].widthProperty().bind(widthProperty().divide(4));
                squares[i][j].heightProperty().bind(heightProperty().divide(4));
                squares[i][j].xProperty().bind(widthProperty().divide(4).multiply(i));
                squares[i][j].yProperty().bind(heightProperty().divide(4).multiply(j));

                squares[i][j].setFill(cg.highDiffrence());
                getChildren().add(squares[i][j]);
            }
        }

    }
}

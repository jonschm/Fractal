package sample;

import javafx.scene.layout.Pane;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class CanvasPainter extends Pane {

    private double pixels;

    public CanvasPainter(double pixels) {
        this.pixels = pixels;

        Canvas c = new Canvas(pixels, pixels);
        getChildren().add(c);
        ColorGenerator cg = new ColorGenerator(Color.YELLOWGREEN);

        for (int i = 0; i < pixels; i++) {
            for (int j = 0; j < pixels; j++) {
                c.getGraphicsContext2D().setFill(cg.highDiffrence());
                c.getGraphicsContext2D().fillRect(i,j,1,1);
            }
        }
    }
}

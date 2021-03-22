package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;



public class Fractal extends Pane {

    private double lowerXBorder, higherXBorder, lowerYBorder, higherYBorder;
    private double lowerXFrame, higherXFrame, lowerYFrame, higherYFrame;
    private double stepWidth;

    private double zoomPointX, zoomPointY;  //IMPORTANT zoomPoint must be within the Borders
    private int sizeX, sizeY;

    private double zoomFactor;
    private double fps;
    private int amountFrames;
    private int currentFrame;

    private int maxIterations;
    private byte amountColors;

    private int currentDisplayedFrame;
    private Timeline animation;

    private FractalThread[] threads;

    private Color[] colors;
    private Frame[] frames;

    private ImageView iv;
    private WritableImage wi;
    private PixelWriter pw;

    private boolean julia;
    private double juliaA, juliaB;

    public Fractal() {
        this(0.0, 0.0);
    }

    public Fractal(double zoomX, double zoomY) { this(-3.0, 3.0, -3.0, 3.0, zoomX, zoomY); }

    public Fractal(double lowerX, double higherX, double lowerY, double higherY, double zoomX, double zoomY) {
        this(lowerX, higherX, lowerY, higherY, zoomX, zoomY, 1.025);
    }

    public Fractal(double lowerX, double higherX, double lowerY, double higherY, double zoomX, double zoomY, double zoom) {
        this(lowerX, higherX, lowerY, higherY, zoomX, zoomY, zoom, 600, 600);
    }

    public Fractal(double lowerXBorder, double higherXBorder, double lowerYBorder, double higherYBorder, double zoomPointX, double zoomPointY, double zoomFactor, int sizeX, int sizeY) {
        this.lowerXBorder = lowerXBorder;
        this.higherXBorder = higherXBorder;
        this.lowerYBorder = lowerYBorder;
        this.higherYBorder = higherYBorder;
        this.zoomPointX = zoomPointX;
        this.zoomPointY = zoomPointY;
        this.zoomFactor = zoomFactor;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        currentFrame = 0;
        currentDisplayedFrame = -1;
        amountFrames = 1300;
        fps = 20;

        julia = false;
        juliaA = -0.75;
        juliaB = 0.83;

        maxIterations = 300;
        byte colors = 8;

        threads = new FractalThread[amountFrames];

        if (this.zoomPointX < lowerXBorder || this.zoomPointX > higherXBorder)
            throw new IllegalArgumentException("Zoom Point must be within the borders");
        if (this.zoomPointY < lowerYBorder || this.zoomPointY > higherYBorder)
            throw new IllegalArgumentException("Zoom Point must be within the borders");

        recreateColors(new ColorGenerator(Color.LIGHTCYAN), colors);
        createAnimation();

        computeInitialFrameBorders();

        frames = new Frame[amountFrames];
        for (int i = 0; i < frames.length; i++) {
            if (julia) {
                frames[i] = new Frame(juliaA, juliaB, sizeX, sizeY, maxIterations, amountColors);
            } else {
                frames[i] = new Frame(sizeX, sizeY, maxIterations, amountColors);
            }

        }
        computeBorders();
        computeFrames();

        iv = new ImageView();
        wi = new WritableImage(sizeX, sizeY);
        pw = wi.getPixelWriter();
        addListener();

        paintFrame(frames[0].getColors());
        currentDisplayedFrame = 0;
        iv.setImage(wi);
        getChildren().add(iv);



    }

    private void addListener() {
        iv.setOnMouseClicked(e-> {
            Frame f = frames[currentDisplayedFrame];
            double x = f.getLowerXBorder() + e.getX() * f.getStepWidth();
            double y = f.getLowerYBorder() + (sizeY - e.getY()) * f.getStepWidth();
            System.out.println(x + "   " + y);
        });

    }

    private void computeInitialFrameBorders() {
        double stepX = difference(lowerXBorder, higherXBorder) / sizeX;
        double stepY = difference(lowerYBorder, higherYBorder) / sizeY;

        if (stepX > stepY) {
            stepWidth = stepX;
            double extra = (stepWidth * sizeY) - difference(lowerYBorder, higherYBorder);
            lowerYFrame = lowerYBorder - (extra/2);
            higherYFrame = higherYBorder + (extra/2);
            lowerXFrame = lowerXBorder;
            higherXFrame = higherXBorder;
        } else {
            stepWidth = stepY;
            double extra = (stepWidth * sizeX) - difference(lowerXBorder, higherXBorder);
            lowerXFrame = lowerXBorder - (extra/2);
            higherXFrame = higherXBorder + (extra/2);
            lowerYFrame = lowerYBorder;
            higherYFrame = higherYBorder;
        }

    }

    private void paintFrame(byte[][] frame) {
        for(int x = 0; x < frame.length; x++) {
            for (int y = 0; y  < frame[x].length; y++) {
                pw.setColor(x, frame[x].length -1 -y, colors[frame[x][y]]);
            }
        }
    }

    public void play() {
        animation.play();
    }

    public void pause() {
        animation.pause();
    }

    //pauses and resets the animation
    public void stop() {
        animation.stop();
        currentDisplayedFrame = 0;
        paintFrame(frames[0].getColors());
    }

    private void computeFrames() {
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new FractalThread(frames[i]);
        }
        try {
            for (int i = 0; i < threads.length; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void createAnimation() {
        EventHandler<ActionEvent> nextFrameEvent = e -> {
            currentDisplayedFrame++;

            if (currentDisplayedFrame >= currentFrame) {
                currentDisplayedFrame--;
                animation.pause();
            } else {
                paintFrame(frames[currentDisplayedFrame].getColors());
            }
        };

        animation = new Timeline(new KeyFrame(Duration.millis(1000/fps), nextFrameEvent));
        animation.setCycleCount(amountFrames);
    }

    private void computeBorders() {
        for (int i = 0; i < frames.length; i++) {
            if (currentFrame != 0) {
                stepWidth = stepWidth / zoomFactor;

                //XBorders
                double diffToLower = difference(lowerXFrame, zoomPointX);
                double diffToHigher = difference(higherXFrame, zoomPointX);
                diffToLower = (diffToLower == 0) ? 0 : (diffToLower / zoomFactor);
                diffToHigher = (diffToHigher == 0) ? 0 : (diffToHigher / zoomFactor);
                lowerXFrame = zoomPointX - diffToLower;
                higherXFrame = zoomPointX + diffToHigher;

                //YBorders
                diffToLower = difference(lowerYFrame, zoomPointY);
                diffToHigher = difference(higherYFrame, zoomPointY);
                diffToLower = (diffToLower == 0) ? 0 : (diffToLower / zoomFactor);
                diffToHigher = (diffToHigher == 0) ? 0 : (diffToHigher / zoomFactor);
                lowerYFrame = zoomPointY - diffToLower;
                higherYFrame = zoomPointY + diffToHigher;
            }

            frames[currentFrame].setBorders(lowerXFrame, lowerYFrame);
            frames[currentFrame].setStepWidth(stepWidth);
            currentFrame++;
        }
    }

    public Color[] recreateColors(ColorGenerator cg, byte amountColors) {
        this.amountColors = amountColors;
        colors = new Color[amountColors];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = cg.generate();
        }
        return colors;
    }

    //absolute difference between 2 numbers
    private double difference(double a, double b) {
        if (a < 0){
            if (b < 0){
                return Math.abs(a-b);
            } else {
                return (-a) + b;
            }
        } else if (b < 0) {
            return a + (-b);
        } else {
            return Math.abs(a-b);
        }
    }



    //GETTERS and SETTERS

    public double getLowerXBorder() {
        return lowerXBorder;
    }

    public void setLowerXBorder(double lowerXBorder) {
        if (lowerXBorder < zoomPointX)
            this.lowerXBorder = lowerXBorder;
        else
            throw new IllegalArgumentException("Zoom Point must be within the borders");
        computeInitialFrameBorders();
    }

    public double getHigherXBorder() {
        return higherXBorder;
    }

    public void setHigherXBorder(double higherXBorder) {
        if (higherXBorder > zoomPointX)
            this.higherXBorder = higherXBorder;
        else
            throw new IllegalArgumentException("Zoom Point must be within the borders");
        computeInitialFrameBorders();
    }

    public double getLowerYBorder() {
        return lowerYBorder;
    }

    public void setLowerYBorder(double lowerYBorder) {
        if (lowerYBorder < zoomPointY)
            this.lowerYBorder = lowerYBorder;
        else
            throw new IllegalArgumentException("Zoom Point must be within the borders");
        computeInitialFrameBorders();
    }

    public double getHigherYBorder() {
        return higherYBorder;
    }

    public void setHigherYBorder(double higherYBorder) {
        if (higherYBorder > zoomPointY)
            this.higherYBorder = higherYBorder;
        else
            throw new IllegalArgumentException("Zoom Point must be within the borders");
        computeInitialFrameBorders();
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public double getZoomPointX() {
        return zoomPointX;
    }

    public void setZoomPointX(double zoomPointX) {
        if (lowerXFrame < zoomPointX && higherXFrame > zoomPointX)
            this.zoomPointX = zoomPointX;
        else
            throw new IllegalArgumentException("Zoom Point must be within the borders");
    }

    public double getZoomPointY() {
        return zoomPointY;
    }

    public void setZoomPointY(double zoomPointY) {
        if (lowerYFrame < zoomPointY && higherYFrame > zoomPointY)
            this.zoomPointY = zoomPointY;
        else
            throw new IllegalArgumentException("Zoom Point must be within the borders");
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getAmountFrames() {
        return amountFrames;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public byte getAmountColors() {
        return amountColors;
    }

    public Color[] getColors() {
        return colors;
    }

    public void setColors(Color[] colors) {
        this.colors = colors;
    }
}

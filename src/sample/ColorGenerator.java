package sample;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Random;

public class ColorGenerator {

    private Color start;
    private Color last;

    public double diffOne = 1;
    public double diffLast = 1;
    public double diffStart = 1;

    public double diffOneHigh = 1;
    public double minHighDiff = 3;

    public ColorGenerator(Color p) {
        start = p;
        last = p;
    }

    public Color generate() {
        Color current;
        do {
            current = getColor(diffOne);
        } while (! (diff(current, last) < diffLast && diff(current, start) < diffStart));

        last = current;
        return current;
    }

    public Color highDiffrence() {
        Color current;
        do {
            current = getColor(diffOneHigh);
        } while (diff(current, last) > minHighDiff);
        last = current;
        return current;
    }

    private Color getColor(double diffOne) {
        Random random = new Random();

        double red = last.getRed(), green = last.getGreen(), blue = last.getBlue();

        double diff = diffOne * random.nextDouble();
        if (random.nextBoolean()) {
            diff *= -1;
        }
        red = cap(red + diff);

        diff = diffOne * random.nextDouble();
        if (random.nextBoolean()) {
            diff *= -1;
        }
        green = cap(green + diff);

        diff = diffOne * random.nextDouble();
        if (random.nextBoolean()) {
            diff *= -1;
        }
        blue = cap(blue + diff);

        return Color.color(red, green, blue);
    }

    private double diff(Color c1, Color c2) {
        double diff = 0;
        if (c1.getRed() > c2.getRed())
            diff += (c1.getRed()-c2.getRed());
        else
            diff += (c2.getRed()-c1.getRed());
        if (c1.getGreen() > c2.getGreen())
            diff += (c1.getGreen()-c2.getGreen());
        else
            diff += (c2.getGreen()-c1.getGreen());
        if (c1.getBlue() > c2.getBlue())
            diff += (c1.getBlue()-c2.getBlue());
        else
            diff += (c2.getBlue()-c1.getBlue());
        return diff;
    }

    private double cap(double input) {
        if(input < 0)
            return 0;
        if(input > 1)
            return 1;
        return input;
    }
}

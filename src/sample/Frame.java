package sample;

public class Frame {

    private double lowerXBorder;
    private double lowerYBorder;

    private double stepWidth;

    private byte[][] colors;
    private int maxIterations;
    private byte amountColors;

    private boolean julia;
    private double a, b;

    public Frame(int sizeX, int sizeY, int maxIterations, byte amountColors) {
        julia = false;
        colors = new byte[sizeX][sizeY];
        this.maxIterations = maxIterations;
        this.amountColors = amountColors;
    }

    public Frame(double a, double b, int sizeX, int sizeY, int maxIterations, byte amountColors) {
        this(sizeX, sizeY, maxIterations, amountColors);
        julia = true;
        this.a = a;
        this.b = b;
    }

    void computeMandelbrot() {
        for(int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[x].length; y++) {
                colors[x][y] = mandelbrot(lowerXBorder + x*stepWidth, lowerYBorder + y*stepWidth);
            }
        }
    }

    private byte mandelbrot(double x, double y) {
        double xn = x;
        double yn = y;

        return point(x, y, xn, yn);
    }

    private byte point(double x, double y, double xn, double yn) {
        for (int i = 0; i < maxIterations; i++) {
            if (Math.sqrt(xn*xn + yn*yn) > 2) {
                return (byte) (i % amountColors);
            }

            double tempX = xn*xn - yn*yn + x;
            yn = 2*xn*yn + y;
            xn = tempX;
        }
        return (byte) (maxIterations % amountColors);
    }

    void computeJulia() {
        for(int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[x].length; y++) {
                colors[x][y] = julia(lowerXBorder + x*stepWidth, lowerYBorder + y*stepWidth);
            }
        }
    }

    private byte julia(double x, double y) {
        return point(a, b, x, y);
    }

    public boolean isJulia() {
        return julia;
    }

    public void setBorders(double lowerXBorder, double lowerYBorder) {
        this.lowerXBorder = lowerXBorder;
        this.lowerYBorder = lowerYBorder;
    }

    public void setStepWidth(double stepWidth) {
        this.stepWidth = stepWidth;
    }

    public byte[][] getColors() {
        return colors;
    }

    public double getLowerXBorder() {
        return lowerXBorder;
    }

    public double getLowerYBorder() {
        return lowerYBorder;
    }

    public double getStepWidth() {
        return stepWidth;
    }
}

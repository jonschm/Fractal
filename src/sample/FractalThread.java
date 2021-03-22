package sample;

public class FractalThread extends Thread {

    Frame frame;

    public FractalThread(Frame frame) {
        this.frame = frame;
        this.start();
    }

    @Override
    public void run() {
        if (frame.isJulia()) {
            frame.computeJulia();
        } else {
            frame.computeMandelbrot();
        }
    }
}

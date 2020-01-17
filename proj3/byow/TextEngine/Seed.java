package byow.TextEngine;

import edu.princeton.cs.introcs.StdDraw;

public class Seed implements TextScreen {
    private int xScale;
    private int yScale;
    public Seed(int width, int height) {
        xScale = width;
        yScale = height;
    }
    public double[] getCoords() {
        return new double[] {xScale / 2.0, yScale / 2.0 - 2};
    }
    public void screen() {
        StdDraw.text(xScale / 2.0, yScale / 2.0, "Please enter a seed: ( ; to continue )");
        //return new double[] {xScale / 2.0, yScale / 2.0 - 2};
    }
}

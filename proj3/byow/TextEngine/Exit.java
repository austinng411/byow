package byow.TextEngine;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class Exit implements TextScreen {
    private int xScale;
    private int yScale;
    public Exit(int width, int height) {
        xScale = width;
        yScale = height;
    }
    public double[] getCoords() { return new double[] {}; }
    public void screen() {
        StdDraw.clear(Color.black);
        StdDraw.text(xScale / 2.0, yScale / 2.0, "wao");
    }
}

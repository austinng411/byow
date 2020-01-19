package byow.TextEngine;

import edu.princeton.cs.introcs.StdDraw;

public class Exit implements TextScreen {
    private int xScale;
    private int yScale;
    public Exit(int width, int height) {
        xScale = width;
        yScale = height;
    }
    public double[] getCoords() { return null; }
    public void screen() {
        StdDraw.text(xScale / 2.0, yScale / 2.0, "wao");
    }
}

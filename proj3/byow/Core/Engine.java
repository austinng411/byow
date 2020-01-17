package byow.Core;

import byow.TileEngine.*;
import byow.TextEngine.*;
import edu.princeton.cs.algs4.*;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @source Josh Hug
 * UC Berkeley CS61B Data Structures
 * sp19.datastructur.es
 *
 * This is a continuation of project 3 Build Your Own World from CS61B sp19, done on my own time a full semester after the completion of 61B, mostly for fun.
 * Credits given to Prof Hug and CS61B for the inspiration and all the initial framework and resources.
 * Engine class is the only place currently with original code, as well as various TETiles in Tileset.
 * A lot of work has already been done from before the project's first submission.
 * Goals include to rewrite much of the code and to develop the project further as there were several ideas I did not get to implement at the time.
 *
 * @source Rohit Deshpande
 * my project partner during 61b.
 * though this is now a solo project.
 *
 */

public class Engine {
    TERenderer ter = new TERenderer();
    Random seed;
    private Position avatar;
    private List<Position> spawns = new ArrayList<>();
    // private MinPQ<Room> rooms = new MinPQ<>(new RoomCompare());
    /* Feel free to change the width and height. */
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    private static final int SECTION_SIZE = 10;
    private static final int MAX_ROOM_WIDTH = 4;
    private static final int MAX_ROOM_HEIGHT = 8;
    private static final int DEAD_END = 5;

    private static final Font monaco30 = new Font("Monaco", Font.BOLD, 30);
    private static final Font monaco15 = new Font("Monaco", Font.BOLD, 30);

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */

    public void interactWithKeyboard() {
        // goal - streamline how frames are drawn
        // should only need to draw a new frame once all the possible moves
        // in a single frame have already occurred

        setWindow();
        String game_seed = seedPrompt();
        TETile[][] finalWorldFrame = interactWithInputString(game_seed);
        avatar = placeAvatar(finalWorldFrame);

        ter.initialize(WIDTH, HEIGHT+5);
        ter.renderFrame(finalWorldFrame);
        move(finalWorldFrame);
    }

    private Position placeAvatar(TETile[][] t) {
        Position spawn = spawns.get(seed.nextInt(spawns.size()));
        t[spawn.x][spawn.y] = Tileset.AVATAR;
        return spawn;
    }

    // INTERFACE //

    private void setWindow() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    private void darkWindow() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
    }

    private String displayTextInput(TextScreen text) {
        // for now this supports displaying a single prompt and showing a single input
        // can probably make something like tileset which is just a bunch of class methods that has
        // premade text screens i.e. a seed screen and character name screen.

        darkWindow();
        StdDraw.setFont(monaco15);
        String input = "";
        text.screen(); // how about a space to continue transition screen ?
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char in = StdDraw.nextKeyTyped();
                if (in == ';') { return input; }
                input  = input + in;
                StdDraw.clear(Color.BLACK);
                text.screen();
                double[] c = text.getCoords();
                StdDraw.text(c[0], c[1], input);
                StdDraw.show();
            }
        }
    }

    private String seedPrompt() {
        return displayTextInput(new Seed(WIDTH, HEIGHT));
    }

    private void seedFrame() {
        //StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        //StdDraw.setXscale(0, WIDTH);
        //StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        //StdDraw.enableDoubleBuffering();
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0, "Please enter a seed: ");
        Font font2 = new Font("Monaco", Font.BOLD, 15);
        StdDraw.show();
        StdDraw.pause(1000);
    }

    // MOVEMENT //
    private void move(TETile[][] t) {
        char c = ' ';
        while (true) {
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(40, 34, avatar.x + ", " + avatar.y);
            StdDraw.show();
            if (StdDraw.hasNextKeyTyped()) {
                c = StdDraw.nextKeyTyped();
                boolean w = (c == 'w' || c == 'W');
                boolean a = (c == 'a' || c == 'A');
                boolean s = (c == 's' || c == 'S');
                boolean d = (c == 'd' || c == 'D');
                boolean p = (c == 't' || c == 'T');
                boolean un = (c == 'u' || c == 'U');
                if (w) {
                    Position next = up(t, avatar);
                    if (!next.equals(avatar)) {
                        avatar = next;
                    }
                } else if (a) {
                    Position next = left(t, avatar);
                    if (!next.equals(avatar)) {
                        avatar = next;
                    }
                } else if (s) {
                    Position next = down(t, avatar);
                    if (!next.equals(avatar)) {
                        avatar = next;
                    }
                } else if (d) {
                    Position next = right(t, avatar);
                    if (!next.equals(avatar)) {
                        avatar = next;
                    }
                }
            }
            //StdDraw.pause(0);
            ter.renderFrame(t);
        }
    }

    private Position up(TETile[][] t, Position p) {
        if (p.y + 1 < HEIGHT) {
            if (t[p.x][p.y + 1].equals(Tileset.FLOOR)) {
                t[p.x][p.y + 1] = Tileset.AVATAR;
                t[p.x][p.y] = Tileset.FLOOR;
                return new Position(p.x, p.y + 1);
            }
        }
        return p;
    }
    private Position down(TETile[][] t, Position p) {
        if (p.y - 1 >= 0) {
            if (t[p.x][p.y - 1].equals(Tileset.FLOOR)) {
                t[p.x][p.y - 1] = Tileset.AVATAR;
                t[p.x][p.y] = Tileset.FLOOR;
                return new Position(p.x, p.y - 1);
            }
        }
        return p;
    }
    private Position left(TETile[][] t, Position p) {
        if (p.x - 1 >= 0) {
            if (t[p.x - 1][p.y].equals(Tileset.FLOOR)) {
                t[p.x - 1][p.y] = Tileset.AVATAR;
                t[p.x][p.y] = Tileset.FLOOR;
                return new Position(p.x - 1, p.y);
            }
        }
        return p;
    }
    private Position right(TETile[][]t, Position p) {
        if (p.x + 1 < WIDTH) {
            if (t[p.x + 1][p.y].equals(Tileset.FLOOR)) {
                t[p.x + 1][p.y] = Tileset.AVATAR;
                t[p.x][p.y] = Tileset.FLOOR;
                return new Position(p.x + 1, p.y);
            }
        }
        return p;
    }


    // MAP GENERATION //

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        seed = initializeRNG(input);
        //createMap(finalWorldFrame);
        //ter.initialize(WIDTH, HEIGHT+5);
        //ter.renderFrame(finalWorldFrame);
        //return finalWorldFrame;
        return createMap(finalWorldFrame);
    }

    private Random initializeRNG(String input) {
        // long s = Long.parseLong(input.substring(1, input.indexOf('s')));
        long s = Long.parseLong(input);
        return new Random(s);
        // for now input is just integers
    }

    private TETile[][] createMap(TETile[][] frame) {
        initializeTiles(frame);
        floorPlan(frame);
        addWalls(frame);
        return frame;
    }

    //////////////////////////////////////////////////////////

    private void initializeTiles(TETile[][] t) {
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[0].length; j++) {
                t[i][j] = Tileset.NOTHING;
            }
        }
    }

    //////////////////////////////////////////////////////////

    private void floorPlan(TETile[][] t) {
        MinPQ<Position> rooms = new MinPQ<>(new PointCompare());
        fillRooms(t, rooms);
        fillHallways(t, rooms);
    }

    //////////////////////////////////////////////////////////

    private void fillRooms(TETile[][] t, MinPQ<Position> r) {
        MacroGrid sections = new MacroGrid(t, SECTION_SIZE);
        for (int i = 0; i < sections.xSections; i++) {
            for (int j = 0; j < sections.ySections; j++) {
                if (!sections.isFilled(i, j)) {
                    addRoom(t, sections, i, j, r);
                }
            }
        }
    }

    private void addRoom(TETile[][] t, MacroGrid sections, int sectionXCoord, int sectionYCoord, MinPQ<Position> rooms) {
        int x1 = sections.lowerX(sectionXCoord) + seed.nextInt(sections.sectionSize);
        if (x1 >= WIDTH) {
            return;
        }
        int y1 = sections.lowerY(sectionYCoord) + seed.nextInt(sections.sectionSize);
        if (y1 >= HEIGHT) {
            return;
        }
        int x2 = x1 + 2 + seed.nextInt(MAX_ROOM_WIDTH);
        if (x2 >= WIDTH) {
            return;
        }
        int y2 = y1 + 2 + seed.nextInt(MAX_ROOM_HEIGHT);
        if (y2 >= HEIGHT) {
            return;
        }
        Position ll = new Position(x1, y1);
        Position ur = new Position(x2, y2);
        buildRoom(t, ll, ur);
        Position r = markRoom(ll, ur);
        rooms.insert(r);
        spawns.add(r);
        sections.updateFill(sectionXCoord, sectionYCoord);
    }

    private void buildRoom(TETile[][] t, Position lowLeft, Position upRight) {
        for (int i = lowLeft.x; i < upRight.x; i++) {
            for (int j = lowLeft.y; j < upRight.y; j++) {
                t[i][j] = Tileset.FLOOR;
            }
        }
    }

    private Position markRoom(Position lowLeftCorner, Position upRightCorner) {
        int xPtr = lowLeftCorner.x + seed.nextInt(upRightCorner.x - lowLeftCorner.x);
        int yPtr = lowLeftCorner.y + seed.nextInt(upRightCorner.y - lowLeftCorner.y);
        return new Position(xPtr, yPtr);
    }

    //////////////////////////////////////////////////////////

    private void fillHallways(TETile[][] t, MinPQ<Position> rooms) {
        // connect rooms with halls in order described by priority queue, generally in diagonal order
        Position cur = rooms.delMin();
        while (rooms.size() > 0) {
            Position next = rooms.delMin();
            connect(cur, next, t);
            cur = next;
        }
    }

    private void connect(Position p, Position q, TETile[][] t) {
        // random chance whether the hall is a northwest or southeast corner
        if (seed.nextBoolean()) {
            horizontalHall(t, p.x, q.x, p.y);
            verticalHall(t, p.y, q.y, q.x);
        } else {
            horizontalHall(t, p.x, q.x, q.y);
            verticalHall(t, p.y, q.y, p.x);
        }
    }

    private void horizontalHall(TETile[][] t, int px, int qx, int cy) {
        int x1 = Math.min(px, qx);
        int x2 = Math.max(px, qx);
        for (int i = x1; i < x2+1; i++) {
            t[i][cy] = Tileset.FLOOR;
            //if (deadend()) { break; }
            //broken
        }
    }

    private void verticalHall(TETile[][] t, int py, int qy, int cx) {
        int y1 = Math.min(py, qy);
        int y2 = Math.max(py, qy);
        for (int i = y1; i < y2; i++) {
            t[cx][i] = Tileset.FLOOR;
            //if (deadend()) { break; }
        }
    }

    private boolean deadend() { return seed.nextInt(100) < DEAD_END; }

    //////////////////////////////////////////////////////////

    private void addWalls(TETile[][] t) {
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[0].length; j++) {
                if (wall(t, i, j)) {
                    t[i][j] = wall();
                }
            }
        }
    }

    private TETile wall() {
        int w = seed.nextInt(100);
        if (w < 5) { return Tileset.WALL3; }
        if (w < 20) { return Tileset.WALL2; }
        return Tileset.WALL1;
    }

    private boolean wall(TETile[][] t, int x, int y) {
        if (t[x][y].equals(Tileset.FLOOR)) { return false; }
        boolean north = y+1 < HEIGHT && t[x][y+1].equals(Tileset.FLOOR);
        boolean northeast = x+1 < WIDTH && y+1 < HEIGHT && t[x+1][y+1].equals(Tileset.FLOOR);
        boolean east = x+1 < WIDTH && t[x+1][y].equals(Tileset.FLOOR);
        boolean southeast = x+1 < WIDTH && y-1 > 0 && t[x+1][y-1].equals(Tileset.FLOOR);
        boolean south = y-1 > 0 && t[x][y-1].equals(Tileset.FLOOR);
        boolean southwest = x-1 > 0 && y-1 > 0 && t[x-1][y-1].equals(Tileset.FLOOR);
        boolean west = x-1 > 0 && t[x-1][y].equals(Tileset.FLOOR);
        boolean northwest = x-1 > 0 && y+1 < HEIGHT && t[x-1][y+1].equals(Tileset.FLOOR);
        return north || northeast || east || southeast || south || southwest || west || northwest;
    }

    //////////////////////////////////////////////////////////

    private class MacroGrid {
        private int xSections;
        private int ySections;
        private int sectionSize;
        private boolean[][] filledSections;

        /**
         * MacroGrid is a utility class used to help with the process of adding rooms in a way
         * that is well spaced and random, given certain constraining values.
         * MG corresponds to large sections of tiles in the map described by t.
         * i.e. if sectionSize is 10 and t is 80x30 then the MG for this map will by 8x3
         *      then there are 24 sections of 10x10 tiles that are considered when building rooms
         *      in the 2400 tiles
         * update method is used so that when a room is added that exists in a section of the MG
         * this section is now filled and another room cannot be initialized using that section.
         * this occurs since the parameters of a room are not constrained to stay inside its
         * starter section. a room that occupies two sections will close off the second section.
         * in this way the max number of rooms is x*y
         */

        private MacroGrid(TETile[][] t, int size) {
            xSections = t.length / size;
            ySections = t[0].length / size;
            sectionSize = size;
            filledSections = new boolean[xSections][ySections];
        }

        private int numberOfSections() { return xSections * ySections; }
        private int getX(int sectionNumber) { return sectionNumber % xSections; }
        // get the x coordinate of the given number section
        private int getY(int sectionNumber) { return sectionNumber / xSections; }
        // get the y coordinate of the given number section
        private int getSectionNumber(Position p) {
            int x = p.x / xSections;
            int y = p.y / ySections;
            return y * xSections + x;
        }
        // get the section number given coordinates from the original map

        private int lowerX(int xCoord) {
            return xCoord * sectionSize + 1;
        }
        // get the map lower x bound of the given grid x coordinate
        private int lowerY(int yCoord) {
            return yCoord * sectionSize + 1;
        }
        // get the map lower y bound of the given grid y coordinate
        private boolean isFilled(int xCoord, int yCoord) { return filledSections[xCoord][yCoord]; }
        // return if the given number section is filled
        private void updateFill(int xCoord, int yCoord) { filledSections[xCoord][yCoord] = true; }
        // update the boolean grid so the given number section is filled
    }

    private class Position {
        private int x;
        private int y;
        private Position(int xx, int yy) {
            x = xx;
            y = yy;
        }
        private boolean equals(Position other) {
            return x == other.x && y == other.y;
        }
    }

    private class Room {
        /**
         * Room object which is described by a random point within its bounds
         * this point is used for spawning, etc
         */
        private int xPtr;
        private int yPtr;
        private Room(Position lowLeftCorner, Position upRightCorner) {
            xPtr = lowLeftCorner.x + seed.nextInt(upRightCorner.x - lowLeftCorner.x);
            yPtr = lowLeftCorner.y + seed.nextInt(upRightCorner.y - lowLeftCorner.y);
        }
    }

    private static class PointCompare implements Comparator<Position> {
        /**
         * Comparator implementation used for storing Rooms in the priority queue
         *
         * orders the rooms by their positions, by only considering delta y if delta x is small
         * in this way we achieve an ordering that generally moves diagonally across the map, which
         * results in well spaced hallway connections which are generated using the priority queue.
         */
        public int compare(Position r1, Position r2) {
            if (Math.abs(r1.x - r2.x) < 10) {
                return Integer.compare(r1.y, r2.y);
            }
            return Integer.compare(r1.x, r2.x);
        }
    }
}

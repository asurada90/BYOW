package byow.Core;

import java.util.Random;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Room {
    /** Width of the entire Room. */
    private int width;
    /** Width of the entire Room. */
    private int height;
    private Random seedHolder;
    private Position bottomLeftCorner;
    private Position bottomRightCorner;
    private Position topLeftCorner;
    private Position topRightCorner;
    private Position randomPointWithin;
    public TETile[][] map;

    /**
     * A constructor for the Room class
     * @param seed - the given seed that determines randomness
     * @param map - the map for our world
     */
    public Room(long seed, TETile[][] map) {
        seedHolder = new Random(seed);
        width = Math.floorMod(seedHolder.nextInt(), 6) + 2;
        height = Math.floorMod(seedHolder.nextInt(), 6) + 2;
        bottomLeftCorner = new Position(Math.floorMod(seedHolder.nextInt(), (Engine.WIDTH - 8)) + 1,
                Math.floorMod(seedHolder.nextInt(), (Engine.HEIGHT - 8)) + 1);
        topRightCorner = new Position(bottomLeftCorner.getxPos() + width - 1,
                bottomLeftCorner.getyPos() + height - 1);
        bottomRightCorner = new Position(topRightCorner.getxPos(), bottomLeftCorner.getyPos());
        topLeftCorner = new Position(bottomLeftCorner.getxPos(), topRightCorner.getyPos());
        this.map = map;
        randomPointWithin = new Position(Math.floorMod(seedHolder.nextInt(), width) + bottomLeftCorner.getxPos(),
                Math.floorMod(seedHolder.nextInt(), height) + bottomLeftCorner.getyPos());
    }

    /**
     * Makes the tiles of a Room based on the area
     * the Room encompasses.
     */
    public void makeRoomTiles() {
        for (int i = bottomLeftCorner.getxPos(); i <= topRightCorner.getxPos(); i++) {
            for (int j = bottomLeftCorner.getyPos(); j <= topRightCorner.getyPos(); j++) {
                map[i][j] = Tileset.FLOOR;
            }
        }
    }

    /**
     * Return the randomPointWithin field's values.
     * @return
     */
    public Position getRandomPointWithin() {
        return randomPointWithin;
    }

    public boolean isOverlappingRoom(Room[] rooms) {
        for (Room r : rooms) {
            if (this == r || r == null) {
                continue;
            }

            if (checkOverlap(r)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkOverlap(Room r) {
        boolean xOverlap = false;
        boolean yOverlap = false;

        int left = r.bottomLeftCorner.getxPos();
        int bottom = r.bottomLeftCorner.getyPos();
        int right = r.topRightCorner.getxPos();
        int top = r.topRightCorner.getyPos();

        int thisLeft = this.bottomLeftCorner.getxPos();
        int thisBottom = this.bottomLeftCorner.getyPos();
        int thisRight = this.topRightCorner.getxPos();
        int thisTop = this.topRightCorner.getyPos();

        if ((bottom - 1 <= thisBottom && top + 1 >= thisBottom) ||
                (bottom - 1 <= thisTop && top + 1 >= thisTop)) {
            yOverlap = true;
        }

        if ((left - 1 <= thisLeft && right + 1 >= thisLeft) ||
                (left - 1 <= thisRight && right + 1 >= thisRight)) {
            xOverlap = true;
        }

        return xOverlap && yOverlap;
    }
}

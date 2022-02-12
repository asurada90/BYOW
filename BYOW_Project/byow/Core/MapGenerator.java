package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import javax.print.attribute.standard.PrinterMakeAndModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class MapGenerator {
    /** Desired seed for world generation. */
    public String seed;
    /** Map which world generation is based off. */
    public TETile[][] map;
    /** Width of the world map. */
    private int mapWidth;
    /** Height of the world map. */
    private int mapHeight;
    /** Array to store all initialized Room instances */
    private Room[] rooms;
    /** Disjoint set to know which Room instances are connected. */
    private UnionFind connections;
    /** The playable Avatar instance. */
    private Avatar avatar;

    /** Base minimum number of rooms for generation. */
    private static final int MIN_NUM_ROOMS = 20;
    /** Range of number of rooms that could go beyond the minimum required number. */
    private static final int NUM_RANGE_OF_ROOMS = 15;
    /** Upper bound of how long a hallway can be. */
    private static final int LENGTH_OF_HALLWAY_BOUND = 15;

    public MapGenerator(TETile[][] savedMap) {
        this.map = savedMap;
        this.mapWidth = this.map.length;
        this.mapHeight = this.map[0].length;
        for (int i = 0; i < this.mapWidth; i++) {
            for (int j = 0; j < this.mapHeight; j++) {
                if (this.map[i][j] == Tileset.AVATAR) {
                    this.avatar = new Avatar(new Position(i, j), getMap());
                    break;
                }
            }
            if (this.avatar != null) {
                break;
            }
        }
    }

    /**
     * Constructor for MapGenerator class for an initial board
     * @param w - desired width of board
     * @param h - desired height of board
     * @param s - desired seed (taken in as a String)
     */
    public MapGenerator(int w, int h, String s) {
        mapWidth = w;
        mapHeight = h;
        seed = s;
        map = new TETile[Engine.WIDTH][Engine.HEIGHT];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = Tileset.NOTHING;
            }
        }
        int rand = (new Random(Long.parseLong(seed))).nextInt();
        int rangeOfRooms = NUM_RANGE_OF_ROOMS;
        int minNumRooms = MIN_NUM_ROOMS;
        rooms = new Room[Math.floorMod(rand, rangeOfRooms) + minNumRooms];
        connections = new UnionFind(rooms.length);
    }

    /**
     * A function for the purpose of generating rooms.
     * Initializes N rooms, where N = rooms.length, and
     * stores them into the rooms[] array. Initialization
     * of each room is based on the seed + the index i in
     * the for loop. The map is also passed in for use with
     * room functions.
     */
    public void generateRooms() {
        for (int i = 0; i < rooms.length; i++) {
            rooms[i] = new Room(Long.parseLong(seed) + i, map);
            int randomizer = rooms.length;
            while (rooms[i].isOverlappingRoom(rooms)) {
                int newRandomSeed = (int) Long.parseLong(seed) + i + randomizer;
                rooms[i] = new Room(newRandomSeed, map);
                randomizer = randomizer + 100;
            }
            rooms[i].makeRoomTiles();
        }
    }

    /**
     * Gets the current state of the TETile[][] map field.
     * @return TETile[][] map
     */
    public TETile[][] getMap() {
        return map;
    }

    /**
     * Goes through entire map and generates
     * walls around the existing dots. Dots
     * will represent hallways and rooms. Calls
     * on isAdjacentToFloor function to decide
     * where to place walls.
     */
    public void buildWalls() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == Tileset.NOTHING && isAdjacentToFloor(new Position(i, j))) {
                    map[i][j] = Tileset.WALL;
                }
            }
        }
    }

    /**
     * Checks a Tile that is Tileset.NOTHING and sees
     * if any of the Tiles around it are a Tileset.FLOOR
     * tile. If so, return true, otherwise false. This
     * check involves looking in the eight directions
     * that surround a given Position on the current map.
     * @param pos - Position that ill either stay as a
     *              Tileset.NOTHING or be changed to a
     *              Tileset.WALL as a result of checking,
     * @return boolean true or false
     */
    private boolean isAdjacentToFloor(Position pos) {
        int xCheck = pos.getxPos();
        int yCheck = pos.getyPos();
        Position north = new Position(xCheck, yCheck + 1);
        Position northeast = new Position(xCheck + 1, yCheck + 1);
        Position east = new Position(xCheck + 1, yCheck);
        Position southeast = new Position(xCheck + 1, yCheck - 1);
        Position south = new Position(xCheck, yCheck - 1);
        Position southwest = new Position(xCheck - 1, yCheck - 1);
        Position west = new Position(xCheck - 1, yCheck);
        Position northwest = new Position(xCheck - 1, yCheck + 1);

        if (isFloor(north)) {
            return true;
        } else if (isFloor(northeast)) {
            return true;
        } else if (isFloor(east)) {
            return true;
        } else if (isFloor(southeast)) {
            return true;
        } else if (isFloor(south)) {
            return true;
        } else if (isFloor(southwest)) {
            return true;
        } else if (isFloor(west)) {
            return true;
        } else if (isFloor(northwest)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * A function to check if the given Position is a
     * Tileset.FLOOR or not. If the Position is of this
     * type, return true, else return false.
     * @param pos - Position argument to be check is of
     *              type Tileset.FLOOR or not
     * @return true or false
     */
    private boolean isFloor(Position pos) {
        int x = pos.getxPos();
        int y = pos.getyPos();

        if (x < 0 || x >= mapWidth || y < 0 || y >= mapHeight) {
            return false;
        }

        if (map[x][y] == Tileset.FLOOR) {
            return true;
        }
        return false;
    }

    /**
     * Generates an Avatar instance onto the map. Does
     * this by checking if the randomly chosen floor
     * (based on the seed) is a floor or not. If the
     * chosen tile is of Tileset.FLOOR, an instance
     * of the Avatar class will be initialized and
     * stored into this.avatar. As per calling the
     * constructor of the Avatar class, the chosen tile
     * will change to Tileset.AVATAR.
     */
    public void generateAvatar() {
        boolean validSpace = true;
        Random rand = (new Random(Long.parseLong(seed)));
        while (validSpace) {
            int randX = Math.floorMod(rand.nextInt(), mapWidth);
            int randY = Math.floorMod(rand.nextInt(), mapHeight);
            Position newPos = new Position(randX, randY);
            if (isFloor(newPos)) {
                avatar = new Avatar(newPos, getMap());
                validSpace = false;
            }
        }
    }

    /**
     * Moves avatar LEFT based on movement function
     * in the Avatar class.
     */
    public boolean moveAvatarLeft() {
        return avatar.moveLeft();
    }

    /**
     * Moves avatar Right based on movement function
     * in the Avatar class.
     */
    public boolean moveAvatarRight() {
        return avatar.moveRight();
    }

    /**
     * Moves avatar UP based on movement function
     * in the Avatar class.
     */
    public boolean moveAvatarUp() {
        return avatar.moveUp();
    }

    /**
     * Moves avatar DOWN based on movement function
     * in the Avatar class.
     */
    public boolean moveAvatarDown() {
        return avatar.moveDown();
    }

    /**
     * Generates a Tileset.LOCKED_DOOR on the map. The
     * 'door' placement will be based on the given seed.
     * Given the seed, a pseudorandomly selected point on
     * the map will be checked if it can be a door or not.
     * This validity check will be done by the isValidDoor
     * function. The check will occur until a valid door is
     * found on the map.
     */
    public void generateDoor() {
        boolean validSpace = true;
        Random rand = (new Random(Long.parseLong(seed)));
        while (validSpace) {
            int randX = Math.floorMod(rand.nextInt(), mapWidth);
            int randY = Math.floorMod(rand.nextInt(), mapHeight);
            if (isValidDoor(new Position(randX, randY))) {
                map[randX][randY] = Tileset.LOCKED_DOOR;
                validSpace = false;
            }
        }
    }

    public void generateKey() {
        boolean validSpace = true;
        Random rand = (new Random(Long.parseLong(seed)));
        while (validSpace) {
            int randX = Math.floorMod(rand.nextInt(), mapWidth);
            int randY = Math.floorMod(rand.nextInt(), mapHeight);
            Position newPos = new Position(randX, randY);
            if (isFloor(newPos)) {
                map[randX][randY] = Tileset.FLOWER;
                validSpace = false;
            }
        }
    }

    /**
     * A function used to validate if a given Position
     * is capable of being a door on the map. The conditions
     * for being a valid door is that the given Position
     * itself is of type Tileset.WALL and that there is at
     * least one floor directly adjacent to the Position.
     * @param pos - Position to be checked
     * @return true or false
     */
    private boolean isValidDoor(Position pos) {
        int xCheck = pos.getxPos();
        int yCheck = pos.getyPos();
        if (map[xCheck][yCheck] != Tileset.WALL) {
            return false;
        }

        Position north = new Position(xCheck, yCheck + 1);
        Position east = new Position(xCheck + 1, yCheck);
        Position south = new Position(xCheck, yCheck - 1);
        Position west = new Position(xCheck - 1, yCheck);

        if (isFloor(north)) {
            return true;
        } else if (isFloor(east)) {
            return true;
        } else if (isFloor(south)) {
            return true;
        } else if (isFloor(west)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * A function used for the generation of hallways.
     * Each loop serves a purpose in creating these
     * hallways based on the layout of our rooms:
     *  LOOP 1: Goes through each Room and sees if it
     *          is close enough to the next Room in
     *          rooms. Only connects rooms that
     *          are within the given bound (close
     *          enough).
     *  LOOP 2: Checks among rooms that are not connected
     *          if they are close enough to be connected.
     *          Differs from LOOP 1 as this checks the
     *          links between almost ALL Rooms. Only
     *          connects rooms that are within the given
     *          bound (close enough).
     *  LOOP 3: A final check to make sure every room is
     *          accessible from any starting location.
     *          Loops through unconnected rooms and connects
     *          them regardless of their distance.
     */
    public void generateHallways() {
        // LOOP 1
        for (int i = 0; i < rooms.length - 1; i++) {
            connectRooms(rooms[i], rooms[i + 1], true, i, i + 1);
        }

        // LOOP 2
        for (int k = 0; k < rooms.length - 1; k++) {
            for (int i = 0; i < rooms.length - 1; i++) {
                if (!connections.connected(i, Math.floorMod(i + k, rooms.length))) {
                    connectRooms(rooms[i], rooms[Math.floorMod(i + k, rooms.length)], true,
                            i, Math.floorMod(i + k, rooms.length));
                }
            }
        }

        // LOOP 3
        for (int i = 0; i < rooms.length - 1; i++) {
            if (!connections.connected(i, Math.floorMod(i + 2, rooms.length))) {
                connectRooms(rooms[i], rooms[Math.floorMod(i + 2, rooms.length)], false,
                        i, Math.floorMod(i + 2, rooms.length));
            }
        }
    }

    /**
     * Connects two rooms by using connectRoomsByPosition function.
     * @param r1 - first room to connect
     * @param r2 - second room to connect
     * @param useBound - decider to use the LENGTH_OF_HALLWAY_BOUND
     * @param i1 - index of first room in rooms[] array
     * @param i2 - index of second room in rooms[] array
     */
    public void connectRooms(Room r1, Room r2, boolean useBound, int i1, int i2) {
        connectRoomsByPosition(r1.getRandomPointWithin(), r2.getRandomPointWithin(), useBound, i1, i2);
    }

    /**
     * Checks the 'distance' between two rooms.
     * How we calculate 'distance' in our rooms:
     *      1) Get the randomPointWithin field
     *         from our Room. These will be the
     *         two arguments that got passed in,
     *      2) Calculate the differences of both
     *         Positions' specific x and y values.
     *      3) Take the absolute values of both
     *         differences and add them together.
     * @param pos1 - randomPointWithin of the first Room
     * @param pos2 - randomPointWithin of the second Room
     * @return int (represents the 'distance')
     */
    private int absoluteDistance(Position pos1, Position pos2) {
        int xDifference = pos1.getxPos() - pos2.getxPos();
        int yDifference = pos1.getyPos() - pos2.getyPos();

        int absSum = Math.abs(xDifference) + Math.abs(yDifference);
        return absSum;
    }

    /**
     * Connects two rooms based on the passed in Position values,
     * First calculates the distance between the two positions
     * and determines if they should be connected based off the
     * comparison against LENGTH_OF_HALLWAY_BOUND and a value
     * pseudorandomly calculated based on the seed. If useBound
     * is true and the distance is within this bound, the two
     * rooms will be connected based off the following function
     * calls. If useBound is true and the distance goes beyond
     * the bound, nothing happens. If useBound is false, a
     * hallway will be made regardless of distance.
     *
     * To make L-shaped (or curved) hallways, we find two points
     * that intersect the two positions on their own respective
     * x-axis and y-axis. We then pseudorandomly choose from
     * these two points and connect both Rooms to that point,
     * thus connecting both Rooms.
     *
     * @param rp1 - Position of first Room
     * @param rp2 - Position of second Room
     * @param useBound - decider on if to use bound or not
     * @param i1 - index of first Room in rooms[] array
     * @param i2 - index of second Room in rooms[] array
     */
    public void connectRoomsByPosition(Position rp1, Position rp2, boolean useBound, int i1, int i2) {
        int dist = absoluteDistance(rp1, rp2);

        if (useBound && dist > (LENGTH_OF_HALLWAY_BOUND)) {
            return;
        } else {
            connections.union(i1, i2);
        }

        Position option1 = new Position(rp1.getxPos(), rp2.getyPos());
        Position option2 = new Position(rp2.getxPos(), rp1.getyPos());

        Position chosen;
        int rand = (new Random(Long.parseLong(seed))).nextInt();
        int chosenOption = Math.floorMod(rand, 2) + 1;
        if (chosenOption == 1) {
            chosen = option1;
        } else {
            chosen = option2;
        }

        if (rp1.shareYAxis(rp2)) {
            connectHorizontal(rp1, rp2);
        } else if (rp1.shareXAxis(rp2)) {
            connectVertical(rp1, rp2);
        } else {
            connectHallway(rp1, chosen);
            connectHallway(rp2, chosen);
        }
    }

    /**
     * Connects a hallway vertically based on the passed in
     * Position arguments.
     * @param rp1 - first Position of a Room to be connected
     * @param rp2 - second Position of a Room to be connected
     */
    private void connectVertical(Position rp1, Position rp2) {
        int x = rp1.getxPos();
        int y1 = rp1.getyPos();
        int y2 = rp2.getyPos();
        int smallerY;
        int biggerY;
        if (y1 > y2) {
            biggerY = y1;
            smallerY = y2;
        } else {
            biggerY = y2;
            smallerY = y1;
        }

        for (int i = smallerY; i <= biggerY; i++) {
            map[x][i] = Tileset.FLOOR;
        }
    }

    /**
     * Connects a hallway horizontally based on the passed in
     * Position arguments.
     * @param rp1 - first Position of a Room to be connected
     * @param rp2 - second Position of a Room to be connected
     */
    private void connectHorizontal(Position rp1, Position rp2) {
        int y = rp1.getyPos();
        int x1 = rp1.getxPos();
        int x2 = rp2.getxPos();
        int smallerX;
        int biggerX;
        if (x1 > x2) {
            biggerX = x1;
            smallerX = x2;
        } else {
            biggerX = x2;
            smallerX = x1;
        }

        for (int i = smallerX; i <= biggerX; i++) {
            map[i][y] = Tileset.FLOOR;
        }
    }

    /**
     * Crates a hallway between two Positions.
     * @param p1 - first Position to be connected
     * @param p2 - second Position to be connected
     */
    public void connectHallway(Position p1, Position p2) {
        if (p1.shareYAxis(p2)) {
            connectHorizontal(p1, p2);
        } else if (p1.shareXAxis(p2)) {
            connectVertical(p1, p2);
        }
    }

    /**
     * @source - https://www.javatpoint.com/java-get-current-date
     */
    public void displayHUD() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateAndTime = dateTimeFormatter.format(now);

        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();

        if (x < 0 || x >= mapWidth || y < 0 || y >= mapHeight) {
            return;
        }

        String tileType = "";
        if (map[x][y] == Tileset.FLOOR) {
            tileType = "floor";
        } else if (map[x][y] == Tileset.LOCKED_DOOR) {
            tileType = "locked door";
        } else if (map[x][y] == Tileset.UNLOCKED_DOOR) {
            tileType = "unlocked door";
        }  else if (map[x][y] == Tileset.WALL) {
            tileType = "wall";
        }  else if (map[x][y] == Tileset.AVATAR) {
            tileType = "avatar";
        } else if (map[x][y] == Tileset.FLOWER) {
            tileType = "key";
        } else {

        }

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(mapWidth - 8,  mapHeight - 1, tileType);
        StdDraw.text(8,  mapHeight - 1, dateAndTime);
        StdDraw.show();
    }

    public TETile[][] createLineOfSightMap() {
        TETile[][] los = new TETile[mapWidth][mapHeight];
        Position avaPos = avatar.getPos();
        int x = avaPos.getxPos();
        int y = avaPos.getyPos();
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (i < x - 5 || i > x + 5 || j < y - 5 || j > y + 5) {
                    los[i][j] = Tileset.NOTHING;
                } else {
                    los[i][j] = this.map[i][j];
                }
            }
        }
        return los;
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);

        MapGenerator mapGen = new MapGenerator(Engine.WIDTH, Engine.HEIGHT, "8004217737854698935");
        mapGen.generateRooms();
        mapGen.generateHallways();
        mapGen.buildWalls();
        mapGen.generateDoor();

        ter.renderFrame(mapGen.getMap());
    }

}

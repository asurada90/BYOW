package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Avatar {
    /** Position of Avatar instance on map. */
    Position pos;
    /** World the Avatar traverses and explores. */
    TETile[][] map;

    public Avatar(Position pos, TETile[][] map) {
        this.pos = pos;
        this.map = map;
        this.map[pos.getxPos()][pos.getyPos()] = Tileset.AVATAR;
    }

    public Position getPos() {
        return pos;
    }

    /**
     * Moves the avatar LEFT on the TETile[][] map.
     * Doesn't move if moving LEFT would hit a wall.
     */
    public boolean moveLeft() {
        int x = pos.getxPos();
        int y = pos.getyPos();
        if (map[x - 1][y] == Tileset.WALL) {
            return false;
        } else if (map[x - 1][y] == Tileset.FLOOR) {
            map[x][y] = Tileset.FLOOR;
            map[x - 1][y] = Tileset.AVATAR;
            this.pos = new Position(x - 1, y);
            return false;
        } else if (map[x - 1][y] == Tileset.UNLOCKED_DOOR) {
            return true;
        } else if (map[x - 1][y] == Tileset.FLOWER) {
            map[x][y] = Tileset.FLOOR;
            map[x - 1][y] = Tileset.AVATAR;
            this.pos = new Position(x - 1, y);
            Position unlocker = findLockedDoor();
            map[unlocker.getxPos()][unlocker.getyPos()] = Tileset.UNLOCKED_DOOR;
            return false;
        }

        return false;
    }

    /**
     * Moves the avatar RIGHT on the TETile[][] map.
     * Doesn't move if moving RIGHT would hit a wall.
     */
    public boolean moveRight() {
        int x = pos.getxPos();
        int y = pos.getyPos();
        if (map[x + 1][y] == Tileset.WALL) {
            return false;
        } else if (map[x + 1][y] == Tileset.FLOOR) {
            map[x][y] = Tileset.FLOOR;
            map[x + 1][y] = Tileset.AVATAR;
            this.pos = new Position(x + 1, y);
            return false;
        } else if (map[x + 1][y] == Tileset.UNLOCKED_DOOR) {
            return true;
        } else if (map[x + 1][y] == Tileset.FLOWER) {
            map[x][y] = Tileset.FLOOR;
            map[x + 1][y] = Tileset.AVATAR;
            this.pos = new Position(x + 1, y);
            Position unlocker = findLockedDoor();
            map[unlocker.getxPos()][unlocker.getyPos()] = Tileset.UNLOCKED_DOOR;
            return false;
        }

        return false;
    }

    /**
     * Moves the avatar UP on the TETile[][] map.
     * Doesn't move if moving UP would hit a wall.
     */
    public boolean moveUp() {
        int x = pos.getxPos();
        int y = pos.getyPos();
        if (map[x][y + 1] == Tileset.WALL) {
            return false;
        } else if (map[x][y + 1] == Tileset.FLOOR) {
            map[x][y] = Tileset.FLOOR;
            map[x][y + 1] = Tileset.AVATAR;
            this.pos = new Position(x, y + 1);
            return false;
        } else if (map[x][y + 1] == Tileset.UNLOCKED_DOOR) {
            return true;
        } else if (map[x][y + 1] == Tileset.FLOWER) {
            map[x][y] = Tileset.FLOOR;
            map[x][y + 1] = Tileset.AVATAR;
            this.pos = new Position(x, y + 1);
            Position unlocker = findLockedDoor();
            map[unlocker.getxPos()][unlocker.getyPos()] = Tileset.UNLOCKED_DOOR;
            return false;
        }

        return false;
    }

    /**
     * Moves the avatar DOWN on the TETile[][] map.
     * Doesn't move if moving DOWN would hit a wall.
     */
    public boolean moveDown() {
        int x = pos.getxPos();
        int y = pos.getyPos();
        if (map[x][y - 1] == Tileset.WALL) {
            return false;
        } else if (map[x][y - 1] == Tileset.FLOOR) {
            map[x][y] = Tileset.FLOOR;
            map[x][y - 1] = Tileset.AVATAR;
            this.pos = new Position(x, y - 1);
            return false;
        } else if (map[x][y - 1] == Tileset.UNLOCKED_DOOR) {
            return true;
        } else if (map[x][y - 1] == Tileset.FLOWER) {
            map[x][y] = Tileset.FLOOR;
            map[x][y - 1] = Tileset.AVATAR;
            this.pos = new Position(x, y - 1);
            Position unlocker = findLockedDoor();
            map[unlocker.getxPos()][unlocker.getyPos()] = Tileset.UNLOCKED_DOOR;
            return false;
        }

        return false;
    }

    private Position findLockedDoor() {
        Position door = null;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (this.map[i][j] == Tileset.LOCKED_DOOR) {
                    door = new Position(i, j);
                    break;
                }
            }
            if (door != null) {
                break;
            }
        }
        return door;
    }
}

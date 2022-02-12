MapGenerator Class
* Fields
   * TETILE[][] map
      * Description: A map tile based on our width and height
   * String seed
      * Description: A randomly generated seed
   * int mapWidth
      * Description: A width of our map
   * int mapHeight
      * Description: A height of our map
   * Room[] rooms
      * Description: Rooms in our map
   * UnionFind connections
      * Description: Creates a UnionFInd data structure holding N items. Initially, all items are in disjoint sets.
   * Avatar avatar
      * Description:
   * static final int MIN_NUM_ROOMS
   * static final int NUM_RANGE_OF_ROOMS
   * static final int LENGTH_OF_HALLWAY_BOUND
* Functions
   * MapGenerator(int w, int h, String s)
   * void generateRooms()
   * public TETile[][] getMap()
   * void buildWalls()
   * boolean isAdjacentToFloor(Position pos)
   * boolean isFloor(Position pos)
   * void generateAvatar()
   * void moveAvatarLeft()
   * void moveAvatarRight()
   * void moveAvatarUp()
   * void moveAvatarDown()
   * void generateDoor()
   * boolean isValidDoor(Position pos)
   * void generateHallways()
   * void connectRooms(Room r1, Room r2, boolean useBound, int i1, int i2)
   * int absoluteDistance(Position pos1, Position pos2)
   * void connectRoomsByPosition(Position rp1, Position rp2, boolean useBound, int i1, int i2)
   * void connectVertical(Position rp1, Position rp2)
   * void connectHorizontal(Position rp1, positions rp2)
   * void connectHallway(Positions p1, Position p2)
   * psvm // for testing purposes


Position Class
* Fields
   * int xPos
      * Description: x coordinate of position
   * int yPos
      * Description: y coordinate of position
* Functions
   * Position(int x, int y)
   * int getxPos()
   * int getyPos()
   * int setxPos()
   * int setyPos()
   * boolean shareYAxis(Position other)
   * boolean shareXAxis(Position other)
   * boolean equals(Object o)


Room Class
* Fields
   * int width
      * Description: A width value of our room
   * int height
      * Description: A height value of our room
   * Random seedHolder
      * Description: Creates and hold our random seed
   * Position bottomLeftCorner
      * Description: A bottom left corner of our room
   * Position topRightCorner
      * Description: A top right corner of our room
   * Position randomPointWithin
      * Description: A random point(position) within our room
   * TETile[][] map
      * Description: A room tile with a random width & height based on our seed
* Functions
   * Room(long seed, TETILE[][] map)
   * void makeRoomTiles()
   * Position getRandomPointWithin()


Avatar Class
* Fields
   * Position pos
      * Description: A position of our avatar
   * TETile[][] map
      * Description: 
* Functions
   * Avatar(Position pos, TETile[] map)
   * void moveLeft()
   * void moveRight()
   * void moveUp()
   * void moveDown()

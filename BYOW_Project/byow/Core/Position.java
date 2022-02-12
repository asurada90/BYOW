package byow.Core;

public class Position {
    private int xPos;
    private int yPos;

    public Position(int x, int y) {
        xPos = x;
        yPos = y;
    }

    /**
     * Return the value in xPos field
     * @return int xPos
     */
    public int getxPos() {
        return xPos;
    }

    /**
     * Return the value in yPos field
     * @return int yPos
     */
    public int getyPos() {
        return yPos;
    }

    /**
     * Set the value of the xPos field
     */
    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    /**
     * Set the value of the yPos field
     */
    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    /**
     * Sees if the current Position (this) and the passed
     * in Position share the same yPos value.
     * @param other - Position to be checked against
     * @return true or false
     */
    public boolean shareYAxis(Position other) {
        return this.yPos == other.getyPos();
    }

    /**
     * Sees if the current Position (this) and the passed
     * in Position share the same xPos value.
     * @param other - Position to be checked against
     * @return true or false
     */
    public boolean shareXAxis(Position other) {
        return this.xPos == other.getxPos();
    }

    @Override
    /**
     * Specified equals function for Position class.
     * Returns true if both Positions share the same
     * x and y values in xPos and yPos.
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (o instanceof Position) {
            Position other = (Position) o;
            return this.shareXAxis(other) && this.shareYAxis(other);
        } else {
            throw new IllegalArgumentException("Must pass in a position object.");
        }
    }
}

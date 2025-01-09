public interface Grid {
    /**
     * The width of this grid
     * @return the width of this grid
     */
    public int  width();
    /**
     * The height of this grid
     * @return the height of this grid
     */
    public int  height();
    /**
     * Sets the value at a specific point on the grid
     * @param p the x,y coordinate to set
     * @param v the value to set at that coordinate
     * @return 0 if the coordinate is not on the grid, otherwise v
     */
    public char set(Point p, char v);
    /**
     * Gets the value at a specific point on the grid
     * @param p the x,y coordinate to get
     * @return 0 if the coordinate is not on the grid, otherwise the value
     */
    public char get(Point p);
    /**
     * Copies data from this grid onto the grid g
     * @param p the coordinate on this grid that corrasponds to 0,0 in grid g
     * @param g a grid to fill in
     */
    public void sub(Point p, Grid g);
}

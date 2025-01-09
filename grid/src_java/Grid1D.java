public class Grid1D implements Grid {
    private char[] data;
    private int width;
    private int height;

    private Grid1D() { }

    public static Grid grid_init(int width, int height, char v) {
        if(width<=0 || height<=0) { return null; }
        Grid1D self = new Grid1D();
        self.width = width;
        self.height = height;
        self.data = new char[width*height];
        for(int i=0; i<width*height; i++) {
            self.data[i] = v;
        }
        return self;
    }
    
    /**
     * The width of this grid
     * @return the width of this grid
     */
    public int  width() {
        return this.width;
    }

    /**
     * The height of this grid
     * @return the height of this grid
     */
    public int  height() {
        return this.height;
    }

    /**
     * Sets the value at a specific point on the grid
     * @param p the x,y coordinate to set
     * @param v the value to set at that coordinate
     * @return 0 if the coordinate is not on the grid, otherwise v
     */
    public char set(Point p, char v) {
        if(p.x<0 || p.x>=this.width || p.y<0 || p.y>=this.height) {
            return 0;
        }
        this.data[p.y*this.width + p.x] = v;
        return v;
    }

    /**
     * Gets the value at a specific point on the grid
     * @param p the x,y coordinate to get
     * @return 0 if the coordinate is not on the grid, otherwise the value
     */
    public char get(Point p) {
        if(p.x<0 || p.x>=this.width || p.y<0 || p.y>=this.height) {
            return 0;
        }
        return this.data[p.y*this.width+p.x];
    }

    /**
     * Copies data from this grid onto the grid g
     * @param p the coordinate on this grid that corrasponds to 0,0 in grid g
     * @param sg a grid to fill in
     */
    public void sub(Point p, Grid sg) {
        if(sg==null) {
            return;
        }
        for(int dy=0; dy<sg.height(); dy++) {
            for(int dx=0; dx<sg.width(); dx++) {
                Point c = Point.init(dx,dy);
                c = Point.add(p,c);
                sg.set(p, this.get(c));
            }
        }
    }
}

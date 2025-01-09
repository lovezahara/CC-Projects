/**
 * Representation of a 2D point
 */
#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#ifndef POINT_H
#define POINT_H

struct point {
    int x; // the x coordinate
    int y; // the y coordinate
};

// typedef associates a type with an alias. I'm using it here so that I can
// write "point_t" instead of "struct point"
typedef struct point point_t;


    /**
     * Generate a new point
     * @param x the x coordinate
     * @param y the y coordinate
     * @return a point with correctly set x and y
     */
    struct point point_init(int x, int y) {
        // Porting Note: JAVA forces non-native types into the heap. In your
        //                 port, the point data should be in the stack.
	struct point this = {x,y};
        //this.x = x;
        //this.y = y;
	return this;
    }

    /**
     * Adds two point together
     * @param p1 point a
     * @param p2 point b
     * @return a new point computed by adding the xs and ys
     */
     struct point point_add(struct point p1, struct point p2) {
        // Porting Note: JAVA forces non-native types into the heap. In your
        //                 port, the point data should be in the stack.
        struct point p = point_init(0,0);
        p.x = p1.x + p2.x;
        p.y = p1.y + p2.y;
        return p;
    }

#endif

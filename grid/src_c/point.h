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
  Generate a point
    In: x - the x coordinate
        y - the y coordinate
    Return: a correctly set point
*/
point_t point_init(int x, int y);

/**
  Add the components of two points
    In: p1 - the first point
        p2 - the second point
    Return: a point obtained by adding the coordinates
*/
point_t point_add(point_t p1, point_t p2);

#endif

#ifndef GRID_H
#define GRID_H
#include <stdlib.h>
#include "point.h"
#include <stdio.h>
#include "grid.h"
// Only struct grid* are going to be used in the interface. Only the grid
// implementation code needs to be aware of the memory size/layout of a 
// struct grid. This typedef will allow struct grid to be defined in the 
// implementation and let the header refer to pointers to that specific type.

typedef struct grid {
        int width;
        int height;
        char* data;
}grid_t; 


/**
  Initializes a new grid with given dimensions where all locations have been
  set to v.
    In: width - the width of the grid
        height - the height of the grid
        v      - the character to place in all grid locations
    Return: If width and height are both greater than 0, a pointer to the
            struct grid created
         Otherwise, 0
*/
grid_t* grid_init(int width, int height, char v){
	if(width<=0 || height<=0){ return 0;}
	grid_t* this = malloc(sizeof(grid_t));
	this->width = width;
	this->height = height;
	this->data = malloc(sizeof(char*)*(width*height));
	for(int i=0; i<(width*height);i++){
		this->data[i] = v;
	}
	return this;


}
/**
  Frees all the memory associated with a grid
    In: grid - a pointer to the struct grid to be freed
*/
void    grid_free(grid_t* grid){
	if(grid == NULL){return;}
		free(grid->data);
		free(grid);
	}

/**
  The width of this grid
    In:  this - a pointer to the struct grid
    Return: 0 if this is null
            Otherwise, the width of this grid
*/
int     grid_width(grid_t* this){
	if(this != NULL){
	return this->width;}
	return 0;
	}

/**
  The height of this grid
    In:  this - a pointer to the struct grid
    Return: 0 if this is null
            Otherwise, the width of this grid
*/
int     grid_height(grid_t* this){
	 if(this != NULL){
        return this->height;}
        return 0;
        }
/**
  Sets the value at a point on the grid
    In: this - a pointer to the grid
        p    - the x,y pair to set
        v    - the value to set at that coordinate
    Return: 0 if this is null
            If p is on the grid, v
            Otherwise, 0
*/
char    grid_set(grid_t* this, point_t p, char v){
		if(this == NULL ||p.x<0 || p.x>=this->width || p.y<0 || p.y>=this->height) {
           		 return 0;
        	}
		this->data[(p.y*this->width) + p.x] = v;
		return v;
	}

/**
  Gets the value at a point on the grid
    In: this - a pointer to the grid
        p    - the x,y pair to get
    Return: 0 if this is null
            If p is on the grid, the value at that coordinate
            Otherwise, 0
*/
char    grid_get(grid_t* this, point_t p){
		 if(this == NULL||p.x<0 || p.x>=this->width || p.y<0 || p.y>=this->height) {
         		   return 0;
       		 }
		return this->data[(p.y*this->width) + p.x];
	}

/**
  Populates sg grid with a region of this grid
  For coordinates is sg that are outside of this, the value in sg should be 0
    In: this - a pointer to the grid
        p    - the x,y coordinate on this that coorasponds to 0,0 in sg
               Note: p might not be a point on this grid
        sg   - a pointer to the grid to populate
    Out: Do nothing if this or sg are null
*/
void    grid_sub(grid_t* this, point_t p, grid_t* sg){
		if (this == NULL||sg == NULL){
			return;
		}
		for(int dy=0; dy<grid_height(sg); dy++) {
            		for(int dx=0; dx<grid_width(sg); dx++) {
                		point_t c = point_init(dx,dy);
               	        	point_t n = point_init(dx,dy);
			         n = point_add(p,c);

                		grid_set(sg,c,grid_get(this,n));
            		}
        	}

	}

#endif

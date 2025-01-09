#ifndef FILE_ACCESS_H
#define FILE_ACCESS_H

#include "grid.h"

grid_t* read_map(char* filename);

int     write_map(char* filename, grid_t* map);

#endif

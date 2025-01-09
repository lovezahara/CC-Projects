#include <stdio.h>

#include "file_access.h"
#include "grid.h"

int main(int argc, char** argv) {
    if(argc != 4) {
        printf("Usage: %s <infile> <outfile>\n",argv[0]);
        return 1;
    }

    grid_t* map = read_map(argv[1]);
    if(map==0) {
        printf("Unable to read %s\n",argv[1]);
        return 1;
    }
    int err = write_map(argv[2], map);
    grid_t* test = grid_init(30,30,0);
    grid_sub(map,point_init(10,5),test);
    int t = write_map(argv[3],test);
    if(err||t ) {
        printf("Unabel to write %s\n",argv[2]);
        return 1;
    }
    grid_free(test);
    grid_free(map);
    return 0;
}

#ifndef FILE_ACCESS_H
#define FILE_ACCESS_H

#include "grid.h"
#include <stdlib.h>

grid_t* read_map(char* filename){
	  if(filename==NULL) {printf("%s\n","filename error");
       	  return NULL; }
            // NOTE: File opening can be done with fopen()
            // NOTE: getline() reads a line at a time from a file
            FILE* f = fopen(filename,"r");
	    if(f == NULL){
	        printf("%s\n","file opening error");    
	    	return NULL;
	    }
	    char* lineptr = 0;
	    size_t linesz = 0;
	    ssize_t nchars = getline(&lineptr,&linesz,f);
	    int width;
            int height;
            int scanres = sscanf(lineptr,"%d %d",&width,&height);
    	    if (scanres == EOF){
			printf("%s\n","Scan error");
			return NULL;
		}
	    grid_t* ret = grid_init(width, height, '#');
	    int size = 0;
	    while(nchars != -1 && size != height*width){
        	    for(int y=0; y<height; y++) {
			 nchars = getline(&lineptr,&linesz,f);
			 for(int x=0; x<width; x++) {
			 size+=1;	
                   	 grid_set(ret,point_init(x,y), lineptr[x]);
			}
            	}
	}
            free(lineptr);
            fclose(f);
	  
            return ret;}
int     write_map(char* filename, grid_t* map){
	 // NOTE: C does not have exceptions... You'll need to detect
        //       that the file did not open or getline() failed
	    if(filename==NULL) {
		   printf("%s\n","filename error");
		   return 1; }
            if(map==NULL) {
		   printf("%s\n","map error");
		    return 1; }
	    FILE* f = fopen(filename,"w");
            if(f == NULL){
                printf("%s\n","fopen error");
		return 1;
            }
	   
            // NOTE: fprintf() will let you write to a file
            int printres = fprintf(f,"%d %d",grid_width(map),grid_height(map));
	    if(printres < 0){printf("%s\n","fprint error");}
	    for(int y=0; y<grid_height(map); y++) {
                fprintf(f,"%s","\n");
		for(int x=0; x<grid_width(map); x++) {
		    fprintf(f,"%c",grid_get(map,point_init(x,y)));
		}
	    }
  	    fclose(f);	    
            return 0;
        }
#endif

#include "hexdump.h"
#include <stdio.h>
#include <stdint.h>

// dumping raw data to the screen can be helpful sometimes... this
// is some simple code to do that

// Dumps a byte buffer to stdout
void hexdump(void* buf, size_t numbytes) {
    fhexdump(stdout, buf, numbytes);
}

// Dumps a byte buffer to a file
void fhexdump(FILE* f, void* buf, size_t numbytes) {
    size_t i=0;
    uint8_t* data = (uint8_t*)buf;
    while(i<numbytes) {
        for(int j=0;j<16;j++) {
            if(j+i<numbytes) {
                fprintf(f,"%.2hhx",data[j+i]);
            } else {
                fprintf(f,"  ");
            }
            if((j+1)%4==0) {
                fprintf(f," ");
            }
        }
        fprintf(f,"   ");
        for(int j=0;j<16 && j+i<numbytes;j++) {
            if(data[j+i]<32 || data[j+i]>126) {
                fprintf(f,".");
            } else {
                fprintf(f,"%c",data[j+i]);
            }
            if((j+1)%4==0) {
                fprintf(f," ");
            }
        }
        fprintf(f,"\n");
        i+=16;
    }
}

#ifndef HEXDUMP_H
#define HEXDUMP_H
#include <stdio.h>
#include <stddef.h>

// dumping raw data to the screen can be helpful sometimes... this
// is some simple code to do that

// Dumps a byte buffer to stdout
void hexdump(void* buf, size_t numbytes);

// Dumps a byte buffer to a file
void fhexdump(FILE* f, void* buf, size_t numbytes);

#endif

#include "hexdump.h"
#include "msg_buffer.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char** argv) {
    MAX_USERNAME_LEN=31;
    MAX_MESSAGE_LEN=31;
    char username[MAX_USERNAME_LEN];
    char message[MAX_MESSAGE_LEN];
    // Sender buffer
    struct msg_buffer mb;
    // Reciever buffer
    struct msg_buffer* rb = malloc(sizeof(struct msg_buffer));
    init_msg_buffer(&mb);
    init_msg_buffer(rb);
    printf("-------- init ---------\n");
    printf("mtype: %hhu\nmsglen: %u\ndatasz: %u\ndata: %p\n",mb.mtype,mb.msglen,mb.datasz,mb.data);
    printf("mtype: %hhu\nmsglen: %u\ndatasz: %u\ndata: %p\n",rb->mtype,rb->msglen,rb->datasz,rb->data);

    printf("--------- HELO --------\n");
    pack_helo(&mb, "lisa");
    printf("mtype: %hhu\nmsglen: %u\ndatasz: %u\ndata: %p\n",mb.mtype,mb.msglen,mb.datasz,mb.data);
    hexdump(mb.data, mb.msglen);
    
    rb->data   = malloc(4096);
    rb->datasz = 4096;
    bcopy(mb.data, rb->data, mb.datasz);
    unpack_type(rb);
    printf("mtype: %hhu\nmsglen: %u\ndatasz: %u\ndata: %p\n",rb->mtype,rb->msglen,rb->datasz,rb->data);
    hexdump(rb->data, rb->msglen);
    unpack_username(rb,username);
    printf("Got username %s\n",username);
    
    printf("--------- WLCM --------\n");
    pack_wlcm(&mb);
    printf("mtype: %hhu\nmsglen: %u\ndatasz: %u\ndata: %p\n",mb.mtype,mb.msglen,mb.datasz,mb.data);
    hexdump(mb.data, mb.msglen);
    
    rb->mtype=0xFF; rb->msglen=0;
    bcopy(mb.data, rb->data, mb.datasz);
    unpack_type(rb);
    printf("mtype: %hhu\nmsglen: %u\ndatasz: %u\ndata: %p\n",rb->mtype,rb->msglen,rb->datasz,rb->data);
    hexdump(rb->data, rb->msglen);

    printf("--------- BBYE --------\n");
    pack_bbye(&mb, REASON_NAME_TOO_LONG);
    printf("mtype: %hhu\nmsglen: %u\ndatasz: %u\ndata: %p\n",mb.mtype,mb.msglen,mb.datasz,mb.data);
    hexdump(mb.data, mb.msglen);

    rb->mtype=0xFF; rb->msglen=0;
    bcopy(mb.data, rb->data, mb.datasz);
    unpack_type(rb);
    printf("mtype: %hhu\nmsglen: %u\ndatasz: %u\ndata: %p\n",rb->mtype,rb->msglen,rb->datasz,rb->data);
    hexdump(rb->data, rb->msglen);

    printf("--------- CHAT --------\n");
    pack_chat(&mb, "steve", "Yay basic testing!");
    printf("mtype: %hhu\nmsglen: %u\ndatasz: %u\ndata: %p\n",mb.mtype,mb.msglen,mb.datasz,mb.data);
    hexdump(mb.data, mb.msglen);

    rb->mtype=0xFF;; rb->msglen=0;
   
    bcopy(mb.data, rb->data, mb.datasz);
    unpack_type(rb);
    printf("mtype: %hhu\nmsglen: %u\ndatasz: %u\ndata: %p\n",rb->mtype,rb->msglen,rb->datasz,rb->data);
    hexdump(rb->data, rb->msglen);
    unpack_username(rb,username);
    printf("Got username %s\n",username);
    unpack_message(rb,message);
    printf("Got message %s\n",message);

    free_msg_buffer(&mb);
    free_msg_buffer(rb);
    return 0;
}


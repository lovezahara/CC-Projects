#include <stdint.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <string.h>
#include "msg_buffer.h"

#include <stdio.h>



const uint8_t MSG_BBYE=0;
const uint8_t MSG_HELO=2;
const uint8_t MSG_WLCM=3;
const uint8_t MSG_CHAT=4;

const uint8_t REASON_UNKNOWN_ERROR=0;
const uint8_t REASON_NAME_TOO_LONG=16;
const uint8_t REASON_CHAT_TOO_LONG=17;
// Max username length -- This is a global to help with buffer allocation
//                         The client should set this when the first WLCM
//                             is received.
//                         The server should set this once at the beginning
//                             of execution.
//                        0xFF indicates unset; valid settings must be less
//     								 than 255. 
uint8_t  MAX_USERNAME_LEN=0xFF;
// Max message length  -- This is a global to help with buffer allocation
//                         The client should set this when the first WLCM
//                             is received.
//                         The server should set this once at the beginning
//                             of execution.
//                        0xFFFF indicates unset; valid settings must be less
//                            than 65535
uint16_t MAX_MESSAGE_LEN =0xFFFF;


// Initialize msg_buffer memory
// Do not check current values in the msg_buffer and
//    set mtype=0xFF
//    set msglen=0
//    set datasz=0
//    set data=0
void init_msg_buffer(struct msg_buffer* mbuf){
	mbuf->mtype = 0xFF;
	mbuf->msglen = 0;
	mbuf->datasz = 0;
	mbuf->data = 0;

}

// Free memory associated with a message buffer
// This function should be safe to call on stack and heap allocated
//     msg_buffers. You should be able to use our understanding of 
//     how the stack and heap are placed in memory to determine if
//     mbuf point to space in the heap or stack;
// Always free the memory data points at
// Sometimes free the memory mbuf points at
void free_msg_buffer(struct msg_buffer* mbuf){
	
	if(mbuf->data !=0){
		free(mbuf->data);
	}
	if(mbuf >(struct msg_buffer*) &mbuf){
	    //in stack, do nothing
	}
	else{
		free(mbuf);
	}
}
void check_msg_buf(struct msg_buffer* mbuf) {
    // checks if this is the first time we've used this
    //    ...in which case we'll need to allocate some memory
    if(mbuf->data== 0) {
        mbuf->data = calloc(1024, sizeof(uint8_t));
        mbuf->datasz = 1024;
    }
    // checks if this is too small
    if(mbuf->datasz < mbuf->msglen) {
        free(mbuf->data);
        mbuf->datasz = mbuf->msglen*2;
        mbuf->data = calloc(mbuf->datasz, sizeof(uint8_t));
    }
}


// Return 0 if the packing is successful, otherwise 1
// username is a cstring containing the username
// Since the WLCM will not have been received at the time the
//     HELO is packed, reject on names longer than 254 characters
int pack_helo(struct msg_buffer* mbuf, uint8_t* username){
	int len = strlen(username);
	if(len>254){
		return 1;
	}
	mbuf->mtype = MSG_HELO;
        mbuf->msglen = 2+len;

	check_msg_buf(mbuf);
	mbuf->data[0] = MSG_HELO;
	mbuf->data[1] = len;
		
	for(int i = 0; i<len; i++){
		mbuf->data[2+i] = username[i];
	}

	return 0;
}

// Return 0 if the packing is successful, otherwise 1
// Should fail if the maximum lengths have been unset
int pack_wlcm(struct msg_buffer* mbuf){
        

	if(MAX_MESSAGE_LEN == 0xFFFF||MAX_USERNAME_LEN==0xFF){
		return 1;
	}
	mbuf->mtype = MSG_WLCM;
        mbuf->msglen = 4;

	check_msg_buf(mbuf);
	mbuf->data[0] = MSG_WLCM;
	mbuf->data[1] = MAX_USERNAME_LEN;
	
	//short* mlen = (short*)mbuf->data+2;
	//*mlen = htons(MAX_MESSAGE_LEN);

	uint16_t max = htons(MAX_MESSAGE_LEN);
        memcpy(&mbuf->data[2],&max,2);
	

	return 0;
}

// Return 0 if the packing is successful, otherwise 1
// This should never fail...
int pack_bbye(struct msg_buffer* mbuf, uint8_t reason_code){

	mbuf->mtype = MSG_BBYE;
        mbuf->msglen = 2;

	check_msg_buf(mbuf);
	mbuf->data[0] = MSG_BBYE;
	mbuf->data[1] = reason_code;
	return 0;
}
// Return 0 if the packing is successful, otherwise non-zero
// username is a cstring containing the username
// message is a cstring contaniing the message
// Should fail if the maximum lengths have been unset
//     returning 1
// Should fail if the username is longer than the max
//     returning REASON_NAME_TOO_LONG
// Should fail if the message is longer than the max
//     returning REASON_CHAT_TOO_LONG
int pack_chat(struct msg_buffer* mbuf, uint8_t* username, uint8_t* message){

	if(MAX_MESSAGE_LEN == 0xFFFF||MAX_USERNAME_LEN==0xFF){
                return 1;
        }

        int ulen = strlen(username);
        int mlen = strlen(message);

        mbuf->mtype = MSG_CHAT;
        mbuf->msglen = 4+ulen+mlen;

	check_msg_buf(mbuf);

	if(ulen > MAX_USERNAME_LEN){return REASON_NAME_TOO_LONG;}
	if(mlen > MAX_MESSAGE_LEN) {return REASON_CHAT_TOO_LONG;}
	mbuf->data[0] = MSG_CHAT;
	mbuf->data[1] = ulen;
	
        uint16_t mlenb = htons(mlen);
	memcpy(&mbuf->data[2],&mlenb,2);	

	for(int i = 0; i<ulen; i++){
                mbuf->data[4+i] = username[i];
	}
	
        for(int i = 0; i<mlen; i++){
                mbuf->data[4+ulen+i] = message[i];
        }
 	 return 0;
}

// Return the type of message in the buffer
// If mbuf->data and mbuf->datasz are non-zero and 
//    mbuf->mtype is 0xFF, this function must correctly set mbuf->mtype 
//    and mbuf->msglen using the bytes in mbuf->data. If the mtype found 
//    in mbuf->data is unknown, mbuf->msglen should be set to 0.
// mbuf->mtype should be returned
uint8_t unpack_type(struct msg_buffer* mbuf){
	if (mbuf->data!=0 && mbuf->datasz!=0&& mbuf->mtype==0xFF){
		uint8_t type = mbuf->data[0];
                mbuf->mtype = type;

		if(type==MSG_WLCM){
        		mbuf->msglen = 4;
		}
		if(type==MSG_HELO){
			mbuf->msglen = 2+mbuf->data[1];
		}
		if(type==MSG_BBYE){
			mbuf->msglen = 2;
		}
		if(type==MSG_CHAT){
			short* mlen = (short*)mbuf->data+2;
                        
			uint16_t mlenb;
			memcpy(&mlenb,&mbuf->data[2],2);
			mlenb = ntohs(mlenb);
			mbuf->msglen = 4 + mbuf->data[1] + mlenb;	
		}
		else{mbuf->msglen = 0;}
	}
	

	return mbuf->mtype;
}

// Return 0 if a username was extracted, otherwise 1
// Should only return 0 for HELO and CHAT messages
// username should point at a byte buffer long enough to hold the maximum
//     length username cstring with its terminating null character
int unpack_username(struct msg_buffer* mbuf, char* username){
	if(mbuf->mtype != MSG_HELO && mbuf->mtype!=MSG_CHAT){
		return 1;
	}
	if(mbuf->mtype == MSG_HELO){
	        
		for(int i = 0; i<mbuf->data[1];i++){
			username[i]= mbuf->data[2+i];
		}		
	        username[mbuf->data[1]] = '\0';

	}
	if(mbuf->mtype==MSG_CHAT){
		for(int i = 0; i<mbuf->data[1];i++){
                        username[i]= mbuf->data[4+i];
                }
		username[mbuf->data[1]] = '\0';
	}
	return 0;
}

// Return 0 if a username and message was extracted, otherwise 1
// Should only return 0 for CHAT messages
// message should point at a byte buffer long enough to hold the maximum
//     length message cstring with its terminating null character
int unpack_message(struct msg_buffer* mbuf, char* message){
	
	if(mbuf->mtype==MSG_CHAT){
		uint16_t mlenb;
                memcpy(&mlenb,&mbuf->data[2],2);
                mlenb = ntohs(mlenb);
                

		memcpy(message,&mbuf->data[4+mbuf->data[1]],mlenb);
		message[mlenb] = '\0';
		return 0;
	}
	return 1;
}

// Return a string containing a string representation of the reason code
// If the reason code is not a known code, return "Unknown Error Code"
// The caller should not need to free the string.
const char const* reason_string(uint8_t reason_code){

	if(reason_code == REASON_NAME_TOO_LONG){
		return "REASON_NAME_TOO_LONG"; 
		

        }
	if(reason_code == REASON_CHAT_TOO_LONG){
		return "REASON_CHAT_TOO_LONG"; 
        	
	}
	if(reason_code == REASON_UNKNOWN_ERROR){
                return "REASON_UNKNOWN_ERROR";

        }
	
	return "Unknown Error Code";
	
}


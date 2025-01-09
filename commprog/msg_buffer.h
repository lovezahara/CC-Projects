#ifndef MSG_BUFFER_H
#define MSG_BUFFER_H
#include <stdint.h>

// Declarations for working with byte buffers for network communications

struct msg_buffer {
    uint8_t  mtype;    // Type id for the message
                       //     should be 0xFF if unset
    uint32_t msglen;  // Number of bytes used in the buffer since the buffer
                       //     size may be larger than the msg length
                       //     should be 0 if unset
    uint32_t datasz;   // Number of bytes allocted for data
                       //     should be 0 if data has yet to be allocated
    uint8_t* data;     // Raw bytes for the wire protocol
                       //     should be 0 if data has yet to be allocated
};

// constants for message types
extern const uint8_t MSG_BBYE; // =0
extern const uint8_t MSG_HELO; // =2;
extern const uint8_t MSG_WLCM; // =3;
extern const uint8_t MSG_CHAT; // =4;

// constants for reason codes
extern const uint8_t REASON_UNKNOWN_ERROR; // =0;
extern const uint8_t REASON_NAME_TOO_LONG; // =16;
extern const uint8_t REASON_CHAT_TOO_LONG; // =17;

// Max username length -- This is a global to help with buffer allocation
//                         The client should set this when the first WLCM
//                             is received.
//                         The server should set this once at the beginning
//                             of execution.
//                        0xFF indicates unset; valid settings must be less
//                             than 255.
extern uint8_t MAX_USERNAME_LEN; // =0xFF;
// Max message length  -- This is a global to help with buffer allocation
//                         The client should set this when the first WLCM
//                             is received.
//                         The server should set this once at the beginning
//                             of execution.
//                        0xFFFF indicates unset; valid settings must be less
//                            than 65535
extern uint16_t MAX_MESSAGE_LEN; // =0xFFFF;

// Initialize msg_buffer memory
// Do not check current values in the msg_buffer and
//    set mtype=0xFF
//    set msglen=0
//    set datasz=0
//    set data=0
void init_msg_buffer(struct msg_buffer* mbuf);

// Free memory associated with a message buffer
// This function should be safe to call on stack and heap allocated
//     msg_buffers. You should be able to use our understanding of 
//     how the stack and heap are placed in memory to determine if
//     mbuf point to space in the heap or stack;
// Always free the memory data points at
// Sometimes free the memory mbuf points at
void free_msg_buffer(struct msg_buffer* mbuf);

// Return 0 if the packing is successful, otherwise 1
// username is a cstring containing the username
// Since the WLCM will not have been received at the time the
//     HELO is packed, reject on names longer than 254 characters
int pack_helo(struct msg_buffer* mbuf, uint8_t* username);

// Return 0 if the packing is successful, otherwise 1
// Should fail if the maximum lengths have been unset
int pack_wlcm(struct msg_buffer* mbuf);

// Return 0 if the packing is successful, otherwise 1
// This should never fail...
int pack_bbye(struct msg_buffer* mbuf, uint8_t reason_code);

// Return 0 if the packing is successful, otherwise non-zero
// username is a cstring containing the username
// message is a cstring contaniing the message
// Should fail if the maximum lengths have been unset
//     returning 1
// Should fail if the username is longer than the max
//     returning REASON_NAME_TOO_LONG
// Should fail if the message is longer than the max
//     returning REASON_CHAT_TOO_LONG
int pack_chat(struct msg_buffer* mbuf, uint8_t* username, uint8_t* message);

// Return the type of message in the buffer
// If mbuf->data and mbuf->datasz are non-zero and 
//    mbuf->mtype is 0xFF, this function must correctly set mbuf->mtype 
//    and mbuf->msglen using the bytes in mbuf->data. If the mtype found 
//    in mbuf->data is unknown, mbuf->msglen should be set to 0.
// mbuf->mtype should be returned
uint8_t unpack_type(struct msg_buffer* mbuf);

// Return 0 if a username was extracted, otherwise 1
// Should only return 0 for HELO and CHAT messages
// username should point at a byte buffer long enough to hold the maximum
//     length username cstring with its terminating null character
int unpack_username(struct msg_buffer* mbuf, char* username);

// Return 0 if a username and message was extracted, otherwise 1
// Should only return 0 for CHAT messages
// message should point at a byte buffer long enough to hold the maximum
//     length message cstring with its terminating null character
int unpack_message(struct msg_buffer* mbuf, char* message);

// Return a string containing a string representation of the reason code
// If the reason code is not a known code, return "Unknown Error Code"
// The caller should not need to free the string.
const char const* reason_string(uint8_t reason_code);

#endif

#ifndef __HEARTBEAT_H__
#define __HEARTBEAT_H__

#define MSG_TYPE1       1
#define MSG_TYPE2       2
#define MSG_HEARTBEAT   3

typedef struct {
    u_int32_t type;
    char data[2000];
} msg_t;

#define IDLE_TIME   60
#define WAIT_TIME   10

#endif  /* __HEARTBEAT_H__ */

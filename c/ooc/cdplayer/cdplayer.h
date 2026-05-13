#ifndef _CDPLAYER_H_
#define _CDPLAYER_H_

#ifdef __cplusplus
extern "C" {
#endif

typedef struct state_t {
    const struct state_t *(*stop)(const struct state_t *self);
    const struct state_t *(*play_or_pause)(const struct state_t *self);
} state_t;

void initialize(void);
void on_stop(void);
void on_play_or_pause(void);

#ifdef __cplusplus
}
#endif

#endif /* _CDPLAYER_H_ */

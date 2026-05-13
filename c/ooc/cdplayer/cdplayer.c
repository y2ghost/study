#include "cdplayer.h"
#include <stddef.h>

static const state_t *current_state_ = NULL;

/* state convert handlers */
static const state_t *_ignore(const state_t *state);
static const state_t *_start_play(const state_t *state);
static const state_t *_stop_play(const state_t *state);
static const state_t *_pause_play(const state_t *state);
static const state_t *_resume_play(const state_t *state);

const state_t IDLE = {
    _ignore,
    _start_play,
};

const state_t PLAY = {
    _stop_play,
    _pause_play,
};

const state_t PAUSE = {
    _stop_play,
    _resume_play,
};

void initialize(void)
{
    current_state_ = &IDLE;
}

void on_stop(void)
{
    current_state_ = current_state_->stop(current_state_);
}

void on_play_or_pause(void)
{
    current_state_ = current_state_->play_or_pause(current_state_);
}

static const state_t *_ignore(const state_t *state) {
    return current_state_;
}

static const state_t *_stop_play(const state_t *state) {
    return &IDLE;
}

static const state_t *_pause_play(const state_t *state) {
    return &PAUSE;
}

static const state_t *_resume_play(const state_t *state) {
    return &PLAY;
}

static const state_t *_start_play(const state_t *state) {
    return &PLAY;
}

const char *get_current_state(void)
{
    const char *str = "unknown";
    if (&PLAY == current_state_) {
        str = "start";
    } else if (&PAUSE == current_state_) {
        str = "pause";
    } else if (&IDLE == current_state_) {
        str = "stop";
    }

    return str;
}

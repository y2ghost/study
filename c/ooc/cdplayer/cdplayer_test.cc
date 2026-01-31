#include "cdplayer.h"
#include <gtest/gtest.h>
#include <stdbool.h>
#include <string.h>

const char *get_current_state(void);

TEST(PlayTest, playOnIdleWillStartPlaying) {
    initialize();
    on_play_or_pause();
    ASSERT_TRUE(0 == strcmp(get_current_state(), "start"));

    on_play_or_pause();
    ASSERT_TRUE(0 == strcmp(get_current_state(), "pause"));

    on_play_or_pause();
    ASSERT_TRUE(0 == strcmp(get_current_state(), "start"));
}

TEST(StopTest, playOnIdleAndStopWillStopPlaying) {
    initialize();
    on_play_or_pause();
    ASSERT_TRUE(0 == strcmp(get_current_state(), "start"));

    on_stop();
    ASSERT_TRUE(0 == strcmp(get_current_state(), "stop"));
}

TEST(StopTest, playOnIdleAndPauseAndStopWillStopPlaying) {
    initialize();
    on_play_or_pause();
    ASSERT_TRUE(0 == strcmp(get_current_state(), "start"));

    on_play_or_pause();
    ASSERT_TRUE(0 == strcmp(get_current_state(), "pause"));

    on_stop();
    ASSERT_TRUE(0 == strcmp(get_current_state(), "stop"));
}

int main(int argc, char **argv) {
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}

#include "validator.h"
#include "validator_view.h"
#include <gtest/gtest.h>
#include <string.h>
#include <stdio.h>

TEST(ValidatorTest, rangeValidator) {
    char buf[32] = {0};
    range_validator_t v = new_range_validator(0, 9);
    print_validator(&v.base, buf, sizeof(buf));
    EXPECT_EQ(0, strcmp("Range(0-9)", buf));
}

TEST(ValidatorTest, previousValidator) {
    char buf[32] = {0};
    previous_validator_t v = new_previous_validator;
    print_validator(&v.base, buf, sizeof(buf));
    EXPECT_EQ(0, strcmp("Previous", buf));
}

int main(int argc, char **argv) {
  ::testing::InitGoogleTest(&argc, argv);
  return RUN_ALL_TESTS();
}

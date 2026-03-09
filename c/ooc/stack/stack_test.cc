#include "stack.h"
#include <gtest/gtest.h>
#include <stdbool.h>

typedef struct test_validator_t {
    validator_t base;
    bool result;
} test_validator_t;

static bool _validate_test(validator_t *v, int val);

static test_validator_t true_validator = {{_validate_test}, true};
static test_validator_t false_validator = {{_validate_test}, false};

static bool _validate_test(validator_t *v, int val)
{
    test_validator_t *tv = (test_validator_t*)v;
    return tv->result;
}

TEST(StackTest, popFromEmptyStackReturnsFalse)
{
    int buf[16] = {0};
    mystack_t stack = new_stack(buf);
    EXPECT_EQ(false, pop(&stack, 0));
}

TEST(StackTest, popReturnsStackTopAndRemoveIt)
{
    int buf[16] = {0};
    mystack_t stack = new_stack(buf);
    EXPECT_EQ(true, push(&stack, 123));

    int rc = 0;
    EXPECT_EQ(true, pop(&stack, &rc));
    EXPECT_EQ(123, rc);
    EXPECT_EQ(false, pop(&stack, &rc));
}

TEST(StackTest, pushToFullStackReturnsFalse)
{
    int buf[16] = {0};
    mystack_t stack = new_stack(buf);

    for (int i=0; i<16; ++i) {
        push(&stack, i);
    }

    int rc = 0;
    EXPECT_EQ(false, push(&stack, 100));
    EXPECT_EQ(true, pop(&stack, &rc));
    EXPECT_EQ(15, rc);
}

TEST(StackTest, pushWithRangeCheck)
{
    int buf[16] = {0};
    range_validator_t validator = new_range_validator(0, 9);
    mystack_t stack = new_stack_with_validator(buf, &validator.base);
    EXPECT_EQ(false, push(&stack, -1));
    EXPECT_EQ(false, push(&stack, 10));
}

TEST(StackTest, pushWithPreviousCheck)
{
    int buf[16] = {0};
    previous_validator_t validator = new_previous_validator;
    mystack_t stack = new_stack_with_validator(buf, &validator.base);
    EXPECT_EQ(true, push(&stack, 3));
    EXPECT_EQ(false, push(&stack, 2));
}

TEST(StackTest, singleChaindValidator)
{
    chained_validator_t v1 = new_chained_validator(&true_validator.base, NULL);
    EXPECT_EQ(true, v1.base.validate(&v1.base, 0));

    chained_validator_t v2 = new_chained_validator(&false_validator.base, NULL);
    EXPECT_EQ(false, v2.base.validate(&v2.base, 0));
}

TEST(StackTest, falseTrueChainedValidator)
{
    chained_validator_t v1 = new_chained_validator(&false_validator.base, NULL);
    chained_validator_t v2 = new_chained_validator(&true_validator.base, &v1.base);
    EXPECT_EQ(false, v2.base.validate(&v2.base, 0));
}

TEST(StackTest, trueFalseChainedValidator)
{
    chained_validator_t v1 = new_chained_validator(&true_validator.base, NULL);
    chained_validator_t v2 = new_chained_validator(&false_validator.base, &v1.base);
    EXPECT_EQ(false, v2.base.validate(&v2.base, 0));
}

TEST(StackTest, trueTrueChainedValidator)
{
    chained_validator_t v1 = new_chained_validator(&true_validator.base, NULL);
    chained_validator_t v2 = new_chained_validator(&true_validator.base, &v1.base);
    EXPECT_EQ(true, v2.base.validate(&v2.base, 0));
}

TEST(StackTest, falseFalseChainedValidator)
{
    chained_validator_t v1 = new_chained_validator(&false_validator.base, NULL);
    chained_validator_t v2 = new_chained_validator(&false_validator.base, &v1.base);
    EXPECT_EQ(false, v2.base.validate(&v2.base, 0));
}

TEST(StackTest, rangePrevChainedValidator)
{
    range_validator_t range = new_range_validator(0, 9);
    previous_validator_t prev = new_previous_validator;
    chained_validator_t v1 = new_chained_validator(&prev.base, NULL);
    chained_validator_t v2 = new_chained_validator(&range.base, &v1.base);

    int buf[16] = {0};
    mystack_t stack = new_stack_with_validator(buf, &v2.base);
    ASSERT_EQ(false, push(&stack, -1));
    ASSERT_EQ(true, push(&stack, 5));
    ASSERT_EQ(false, push(&stack, 4));
    ASSERT_EQ(true, push(&stack, 9));
    ASSERT_EQ(false, push(&stack, 10));
}

int main(int argc, char **argv) {
  ::testing::InitGoogleTest(&argc, argv);
  return RUN_ALL_TESTS();
}

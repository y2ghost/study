#include "int_sorter.h"
#include <gtest/gtest.h>
#include <stdio.h>
#include <stdbool.h>

static bool _write_int(FILE *fp, int val)
{
    return fwrite(&val, sizeof(int), 1, fp) == 1;
}

static int _read_int(FILE *fp)
{
    int val = 0;
    fread(&val, sizeof(int), 1, fp);
    return val;
}

TEST(TemplateTest, normalCase)
{
    char fname[512] = {"/tmp/intsort_test.XXXXXX"};
    mkstemp(fname);

    FILE *fp = fopen(fname, "wb");
    EXPECT_EQ(true, _write_int(fp, 1231));
    EXPECT_EQ(true, _write_int(fp, 1));
    EXPECT_EQ(true, _write_int(fp, 441));
    EXPECT_EQ(0, fclose(fp));
    int_sorter(fname);

    fp = fopen(fname, "rb");
    EXPECT_EQ(1, _read_int(fp));
    EXPECT_EQ(441, _read_int(fp));
    EXPECT_EQ(1231, _read_int(fp));
    EXPECT_EQ(0, fclose(fp));
    unlink(fname);
}

TEST(TemplateTest, emptyFile)
{
    char fname[512] = {"/tmp/intsort_test.XXXXXX"};
    mkstemp(fname);

    FILE *fp = fopen(fname, "wb");
    EXPECT_EQ(0, fclose(fp));
    int_sorter(fname);
    unlink(fname);
}

TEST(TemplateTest, nonExistentFile)
{
    int_sorter("--- non existent file ---");
}

int main(int argc, char **argv) {
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}

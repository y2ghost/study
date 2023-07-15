#pragma once

namespace Utility
{
    inline bool IsEven(int value)
    {
        return (value % 2) == 0;
    }

    inline bool IsEven(long long value)
    {
        return (value % 2) == 0;
    }

    void PrintIsEvenResult(int value);
    void PrintIsEvenResult(long long value);
    void PrintBool(bool value);
}

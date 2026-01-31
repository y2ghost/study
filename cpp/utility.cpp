#include "utility.h"
#include <iostream>
#include <ostream>

using namespace std;
using namespace Utility;

void Utility::PrintIsEvenResult(int value)
{
    wcout << L"The number " << value << L" is " <<
        (IsEven(value) ? L"" : L"not ") << L"even."
        << endl;
}

void Utility::PrintIsEvenResult(long long value)
{
    wcout << L"The number " << value << L" is " <<
        (IsEven(value) ? L"" : L"not ") << L"even."
        << endl;
}

void Utility::PrintBool(bool value)
{
    wcout << L"The value is" <<
        (value ? L"true." : L"false.") << endl;
}

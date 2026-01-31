#include "simpleMath.h"
#include "peekLastItem.h"
#include <iostream>
#include <ostream>
#include <vector>

using namespace std;

int main(int ac, char *av[])
{
    SimpleMath<float> smf;
    wcout << "1.1F + 2.02F = " << smf.Add(1.1F, 2.02F) << "F." << endl;
    vector<const wchar_t*> strs;
    strs.push_back(L"Hello");
    strs.push_back(L"World");
    wcout << L"Last word was '" <<
        PeekLastItem<std::vector<const wchar_t*>,const wchar_t*>(strs) <<
        L"'." << endl;
    return 0;
}

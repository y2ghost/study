#include <iostream>
#include <ostream>
#include <string>

using namespace std;

namespace Places
{
namespace Cities
{
struct City
{
    City(const wchar_t* name)
    {
        Name = wstring(name);
    }
    
    wstring Name;
};
}
}

int main(int argc, char* argv[])
{
    auto nyc = Places::Cities::City(L"New York City");
    
    wcout << L"City name is " << nyc.Name.c_str() << L"." << endl;
    
    return 0;
}

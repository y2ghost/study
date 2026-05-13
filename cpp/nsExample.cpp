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

// 命名空间使用示例
int main(int ac, char *av[])
{
    auto nyc = Places::Cities::City(L"ChangShan");
    wcout << L"City name is " << nyc.Name.c_str() << L"." << endl;
    return 0;
}


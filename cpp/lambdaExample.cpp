#include <iostream>
#include <ostream>

using namespace std;

int main(int ac, char *av[])
{
    // 简单的lambda表达式
    auto lm1 = []()
    {
        wcout << L"No capture, parameterless lambda." << endl;
    };

    lm1();

    auto lm2 = [](int a, int b)
    {
        wcout << a << L" + " << b << " = " << (a + b) << endl;
    };

    lm2(3,4);

    auto lm3 = [](int a, int b) -> int
    {
        wcout << a << L" % " << b << " = ";
        return a % b;
    };

    wcout << lm3(7, 5) << endl;
    int a = 5;
    int b = 6;

    [=]()
    {
        wcout << a << L" + " << b << " = " << (a + b) << endl;
    }();

    [=]() mutable -> void
    {
        wcout << a << L" + " << b << " = " << (a + b) << endl;
        a = 10;
    }();

    wcout << L"The value of a is " << a << L"." << endl;
    [&]()
    {
        wcout << a << L" + " << b << " = " << (a + b) << endl;
        a = 10;
    }();

    wcout << L"The value of a is " << a << L"." << endl;
    [a,&b]()
    {
        b = 12;
        wcout << a << L" + " << b << " = " << (a + b) << endl;
    }();

    [=,&b]()
    {
        b = 15;
        wcout << a << L" + " << b << " = " << (a + b) << endl;
    }();

    [&,a]()
    {
        b = 18;
        wcout << a << L" + " << b << " = " << (a + b) << endl;
    }();

    return 0;
}


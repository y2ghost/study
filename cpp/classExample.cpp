#include "iceCreamSundae.h"
#include "flavor.h"
#include "toppings.h"
#include <iostream>
#include <ostream>

using namespace std;

int CallingMsg(const wchar_t* cls)
{
    wcout << L"Calling " << cls << L" constructor." << endl;
    return 0;
}

int InitializingIntMsg(int value, const wchar_t* mvarName)
{
    wcout << L"Initializing " << mvarName << L"." << endl;
    return value;
}

class A
{
public:
    A(void) :
        value(InitializingIntMsg(5, L"DEFAULT value"))
    {
        wcout << L"DEFAULT Constructing A. value is '" <<
            value << L"'." << endl;
    }

    explicit A(int) :
        value(InitializingIntMsg(20, L"value"))
    {
        wcout << L"Constructing A. value is '" <<
            value << L"'." << endl;
    }

    virtual ~A(void)
    {
        wcout << L"Destroying A." << endl;
    }

private:
    int value;
};

class B : virtual public A
{
public:
    explicit B(int) :
        A(CallingMsg(L"A")),
        a(InitializingIntMsg(5, L"a")),
        b(InitializingIntMsg(2, L"b"))
    {
        wcout << L"Constructing B. a is '" <<
            a << L"' and b is '" << b << L"'." << endl;
    }

    virtual ~B(void)
    {
        wcout << L"Destroying B." << endl;
    }

private:
    int a;
    int b;
};

class C
{
public:
    explicit C(int) :
        c(InitializingIntMsg(0, L"c"))
    {
        wcout << L"Constructing C. c is '" <<
            c << L"'." << endl;
    }

    virtual ~C(void)
    {
        wcout << L"Destroying C." << endl;
    }

private:
    int c;
};

class D
{
public:
    explicit D(int) :
        d(InitializingIntMsg(3, L"d"))
    {
        wcout << L"Constructing D. d is '" <<
            d << L"'." << endl;
    }

    virtual ~D(void)
    {
        wcout << L"Destroying D." << endl;
    }

private:
    int d;
};

// 初始化列表，虚基类在前，普通基类在后，类成员按序最后
class Y : virtual public B, public D, virtual public C
{
public:
    explicit Y(int value) :
        B(CallingMsg(L"B")),
        C(CallingMsg(L"C")),
        D(CallingMsg(L"D")),
        someInt(InitializingIntMsg(value, L"someInt"))
    {
        wcout << L"Constructing Y. someInt is '" <<
            someInt << L"'." << endl;
    }

    virtual ~Y(void)
    {
        wcout << L"Destroying Y." << endl;
    }

    int GetSomeInt(void) { return someInt; }

private:
    int someInt;
};

class Z : public D, virtual public B, public C
{
public:
    explicit Z(int value) :
        A(CallingMsg(L"A")),
        B(CallingMsg(L"B")),
        D(CallingMsg(L"D")),
        C(CallingMsg(L"C")),
        someInt(InitializingIntMsg(value, L"someInt"))
    {
        wcout << L"Constructing Z. someInt is '" <<
            someInt << L"'." << endl;
    }

    virtual ~Z(void)
    {
        wcout << L"Destroying Z." << endl;
    }

    int GetSomeInt(void) { return someInt; }

private:
    int someInt;
};

// 类初始化示例
static void testInitialization()
{
    {
        Y someY(CallingMsg(L"Y"));
        wcout << L"Y::GetSomeInt returns '" <<
            someY.GetSomeInt() << L"'." << endl;
    }

    wcout << endl << "Break between Y and Z." << endl
        << endl;

    {
        Z someZ(CallingMsg(L"Z"));
        wcout << L"Z::GetSomeInt returns '" <<
            someZ.GetSomeInt() << L"'." << endl;
    }
}

using namespace Desserts;

typedef Desserts::Toppings::ToppingsList ToppingsList;

// 构造函数示例
static void testConstructors(void)
{
    const wchar_t* outputPrefixStr = L"Current Dessert: ";
    IceCreamSundae s1 = Flavor::Vanilla;
    wcout << outputPrefixStr << s1.GetSundaeDescription() << endl;
    s1.AddTopping(ToppingsList::HotFudge);
    wcout << outputPrefixStr << s1.GetSundaeDescription() << endl;
    s1.AddTopping(ToppingsList::Cherry);
    wcout << outputPrefixStr << s1.GetSundaeDescription() << endl;
    s1.AddTopping(ToppingsList::CrushedWalnuts);
    wcout << outputPrefixStr << s1.GetSundaeDescription() << endl;
    s1.AddTopping(ToppingsList::WhippedCream);
    wcout << outputPrefixStr << s1.GetSundaeDescription() << endl;
    s1.RemoveTopping(ToppingsList::CrushedWalnuts);
    wcout << outputPrefixStr << s1.GetSundaeDescription() << endl;
    wcout << endl <<
        L"Copy constructing s2 from s1." << endl;
    IceCreamSundae s2(s1);
    wcout << endl <<
        L"Copy assignment to s1 from s2." << endl;
    s1 = s2;
    wcout << endl <<
        L"Move constructing s3 from s1." << endl;
    IceCreamSundae s3(std::move(s1));
    wcout << endl <<
        L"Move assigning to s1 from s2." << endl;
    s1 = std::move(s2);
}

int main(int ac, char *av[])
{
    testInitialization();
    testConstructors();
    return 0;
}

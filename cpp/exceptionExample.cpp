#include <iostream>
#include <ostream>
#include <memory>
#include <exception>
#include <stdexcept>
#include <typeinfo>
#include <algorithm>
#include <cstdlib>
#include "invalidArgumentException.h"

using namespace CppForCsExceptions;
using namespace std;

class ThrowClass
{
public:
    ThrowClass(void)
        : shouldThrow(false)
    {
        wcout << L"Constructing ThrowClass." << endl;
    }

    explicit ThrowClass(bool shouldThrow)
        : shouldThrow(shouldThrow)
    {
        wcout << L"Constructing ThrowClass. shouldThrow = " <<
            (shouldThrow ? L"true." : L"false.") << endl;
        if (shouldThrow)
        {
            throw InvalidArgumentException<const char*>(
                "ThrowClass",
                "ThrowClass(bool shouldThrow)",
                "shouldThrow",
                "true"
                );
        }
    }

    ~ThrowClass(void)
    {
        wcout << L"Destroying ThrowClass." << endl;
    }

    const wchar_t* GetShouldThrow(void) const
    {
        return (shouldThrow ? L"True" : L"False");
    }

private:
    bool shouldThrow;
};

class RegularClass
{
public:
    RegularClass(void)
    {
        wcout << L"Constructing RegularClass." << endl;
    }

    ~RegularClass(void)
    {
        wcout << L"Destroying RegularClass." << endl;
    }
};

class ContainStuffClass
{
public:
    ContainStuffClass(void) :
        regularClass(new RegularClass()),
        throwClass(new ThrowClass())
    {
        wcout << L"Constructing ContainStuffClass." << endl;
    }

    ContainStuffClass(const ContainStuffClass& other) :
        regularClass(new RegularClass(*other.regularClass)),
        throwClass(other.throwClass)
    {
        wcout << L"Copy constructing ContainStuffClass." << endl;
    }

    ~ContainStuffClass(void)
    {
        wcout << L"Destroying ContainStuffClass." << endl;
    }

    const wchar_t* GetString(void) const
    {
        return L"I'm a ContainStuffClass.";
    }

private:

    unique_ptr<RegularClass> regularClass;
    shared_ptr<ThrowClass> throwClass;
};

void TerminateHandler(void)
{
    wcout << L"Terminating due to unhandled exception." << endl;
    abort();
}

// 异常类示例
int main(int ac, char *av[])
{
    set_terminate(&TerminateHandler);
    try
    {
        ContainStuffClass cSC;
        wcout << cSC.GetString() << endl;
        ThrowClass tC(false);
        wcout << L"tC should throw? " << tC.GetShouldThrow() << endl;
        tC = ThrowClass(true);
        wcout << L"tC should throw? " << tC.GetShouldThrow() << endl;
    }
    catch (InvalidArgumentExceptionBase& e)
    {
        wcout << L"Caught '" << typeid(e).name() << L"'." << endl <<
            L"Message: " << e.what() << endl;
    }
    catch (std::exception& e)
    {
        wcout << L"Caught '" << typeid(e).name() << L"'." << endl <<
            L"Message: " << e.what() << endl;
        throw;
    }
    catch (...)
    {
        wcout << L"Caught unknown exception type." << endl;
        throw;
    }

    wcout << L"tC should throw? " <<
        ThrowClass(true).GetShouldThrow() << endl;
    return 0;
}


#include "iceCreamSundae.h"
#include <string>
#include <sstream>
#include <iostream>
#include <ostream>
#include <memory>

using namespace Desserts;
using namespace std;

IceCreamSundae::IceCreamSundae(void) :
    flavor(Flavor::None),
    toppings(Toppings::None),
    description()
{
    wcout << L"Default constructing IceCreamSundae(void)." <<
        endl;
}

IceCreamSundae::IceCreamSundae(Flavor flavor) :
    flavor(flavor),
    toppings(Toppings::None),
    description()
{
    wcout << L"Conversion constructing IceCreamSundae(Flavor)." <<
        endl;
}

IceCreamSundae::IceCreamSundae(Toppings::ToppingsList toppings) :
    flavor(Flavor::None),
    toppings(toppings),
    description()
{
    wcout << L"Parameter constructing IceCreamSundae(\
              Toppings::ToppingsList)." << endl;
}

IceCreamSundae::IceCreamSundae(const IceCreamSundae& other) :
    flavor(other.flavor),
    toppings(other.toppings),
    description()
{
    wcout << L"Copy constructing IceCreamSundae." << endl;
}

IceCreamSundae& IceCreamSundae::operator=(const IceCreamSundae& other)
{
    wcout << L"Copy assigning IceCreamSundae." << endl;

    flavor = other.flavor;
    toppings = other.toppings;
    return *this;
}

IceCreamSundae::IceCreamSundae(IceCreamSundae&& other) :
    flavor(),
    toppings(),
    description()
{
    wcout << L"Move constructing IceCreamSundae." << endl;
    *this = std::move(other);
}

IceCreamSundae& IceCreamSundae::operator=(IceCreamSundae&& other)
{
    wcout << L"Move assigning IceCreamSundae." << endl;

    if (this != &other)
    {
        flavor = std::move(other.flavor);
        toppings = std::move(other.toppings);
        description = std::move(other.description);
        other.flavor = Flavor::None;
        other.toppings = Toppings::None;
        other.description = std::wstring();
    }
    return *this;
}

IceCreamSundae::~IceCreamSundae(void)
{
    wcout << L"Destroying IceCreamSundae." << endl;
}

void IceCreamSundae::AddTopping(Toppings::ToppingsList topping)
{
    this->toppings = this->toppings | topping;
}

void IceCreamSundae::RemoveTopping(Toppings::ToppingsList topping)
{
    this->toppings = this->toppings & ~topping;
}

void IceCreamSundae::ChangeFlavor(Flavor flavor)
{
    this->flavor = flavor;
}

const wchar_t* IceCreamSundae::GetSundaeDescription(void)
{
    wstringstream str;
    str << L"A " << GetFlavorString(flavor) <<
        L" sundae with the following toppings: " << toppings.GetString();
    description = wstring(str.str());
    return description.c_str();
}

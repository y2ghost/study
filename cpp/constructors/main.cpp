#include <iostream>
#include <ostream>
#include "IceCreamSundae.h"
#include "Flavor.h"
#include "Toppings.h"

using namespace Desserts;
using namespace std;

typedef Desserts::Toppings::ToppingsList ToppingsList;

int main(int argc, const char * argv[])
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
    
    return 0;
}


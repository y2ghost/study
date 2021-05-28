#include <iostream>
#include <ostream>
#include "Sandwich.h"

using namespace std;

// 演示接口编程

int main(int argc, char* argv[])
{
    Sandwich s;
    s.AddIngredient(L"Turkey");
    s.AddIngredient(L"Cheddar");
    s.AddIngredient(L"Lettuce");
    s.AddIngredient(L"Tomato");
    s.AddIngredient(L"Mayo");
    s.RemoveIngredient(L"Cheddar");
    s.SetBreadType(L"a Roll");
    
    wcout << s.GetSandwich() << endl;

    return 0;
}

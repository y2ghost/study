#include "sandwich.h"
#include <iostream>
#include <ostream>

using namespace std;

int main(int ac, char *av[])
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

#include <iostream>
#include <ostream>
#include <string>

// 属于有范围的枚举类型
enum class Color
{
    Red,
    Orange,
    Yellow,
    Blue,
    Indigo,
    Violet
};

// 属于无范围的枚举类型，可以自己指定合适的数值类型
enum Flavor : unsigned short int
{
    Vanilla,
    Chocolate,
    Strawberry,
    Mint,
};

int main(int argc, char* argv[])
{
    Flavor f = Vanilla;
    Color c = Color::Orange;
    
    // 无范围的枚举才可以进行如下赋值
    f = Mint;
    
    std::wstring flavor;
    std::wstring color;
    
    switch (c)
    {
        case Color::Red:
            color = L"Red";
            break;
        case Color::Orange:
            color = L"Orange";
            break;
        case Color::Yellow:
            color = L"Yellow";
            break;
        case Color::Blue:
            color = L"Blue";
            break;
        case Color::Indigo:
            color = L"Indigo";
            break;
        case Color::Violet:
            color = L"Violet";
            break;
        default:
            color = L"Unknown";
            break;
    }
    
    switch (f)
    {
        case Vanilla:
            flavor = L"Vanilla";
            break;
        case Chocolate:
            flavor = L"Chocolate";
            break;
        case Strawberry:
            flavor = L"Strawberry";
            break;
        case Mint:
            flavor = L"Mint";
            break;
        default:
            break;
    }
    
    std::wcout << L"Flavor is " << flavor.c_str() << L" (" << f <<
    L"). Color is " << color.c_str() << L" (" <<
    static_cast<int>(c) << L")." << std::endl <<
    L"The size of Flavor is " << sizeof(Flavor) <<
    L"." << std::endl <<
    L"The size of Color is " << sizeof(Color) <<
    L"." << std::endl;
    
    return 0;
}

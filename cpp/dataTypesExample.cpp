#include <iostream>
#include <ostream>
#include <string>
#include <vector>
#include <algorithm>

enum class Color
{
    Red,
    Orange,
    Yellow,
    Blue,
    Indigo,
    Violet
};

// 传统的方式可以指定继承的基础类型
enum Flavor : unsigned short int
{
    Vanilla,
    Chocolate,
    Strawberry,
    Mint,
};

// 枚举用法示例
static void testEnum(void)
{
    Flavor f = Vanilla;
    Color c = Color::Orange;
    // 下面的赋值报错，因为Color存在范围限制
    // c = Orange;

    std::wstring flavor;
    std::wstring color;

    switch (c) {
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

    switch (f) {
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
}

enum class SomeValueDataType
{
    Int = 0,
    Float = 1,
    Double = 2
};

struct SomeData
{
    SomeValueDataType Type;
    union
    {
        int iData;
        float fData;
        double dData;
    } Value;

    SomeData(void)
    {
        SomeData(0);
    }

    SomeData(int i)
    {
        Type = SomeValueDataType::Int;
        Value.iData = i;
    }

    SomeData(float f)
    {
        Type = SomeValueDataType::Float;
        Value.fData = f;
    }

    SomeData(double d)
    {
        Type = SomeValueDataType::Double;
        Value.dData = d;
    }
};


// Union类型用法示例
static void testUnion(void)
{
    SomeData data = SomeData(2.3F);
    std::wcout << L"Size of SomeData::Value is " << sizeof(data.Value) <<
        L" bytes." << std::endl;
    switch (data.Type) {
    case SomeValueDataType::Int:
        std::wcout << L"Int data is " << data.Value.iData << L"."
            << std::endl;
        break;
    case SomeValueDataType::Float:
        std::wcout << L"Float data is " << data.Value.fData << L"F."
            << std::endl;
        break;
    case SomeValueDataType::Double:
        std::wcout << L"Double data is " << data.Value.dData << L"."
            << std::endl;
        break;
    default:
        std::wcout << L"Data type is unknown." << std::endl;
        break;
    }
}

typedef std::vector<int> WidgetIdVector;

static bool ContainsWidgetId(WidgetIdVector idVector, int id)
{
    return (std::end(idVector) != 
        std::find(std::begin(idVector), std::end(idVector), id)
        );
}

// typedef用法示例
static void testTypedef(void)
{
    WidgetIdVector idVector;
    idVector.push_back(5);
    idVector.push_back(8);
    std::wcout << L"Contains 8: " <<
        (ContainsWidgetId(idVector, 8) ? L"true." : L"false.") <<
        std::endl;
}

int main(int ac, char *av[])
{
    testEnum();
    testUnion();
    testTypedef();
    return 0;
}

#include "utility.h"
#include "vehicle.h"
#include "vehicleCondition.h"
#include "iwriteData.h"
#include "consoleWriteData.h"
#include <iostream>
#include <ostream>
#include <string>
#include <iomanip>
#include <typeinfo>

using namespace Utility;

// 演示函数的用法
static void testFunction(void)
{
    int i1 = 3;
    int i2 = 4;
    bool b1 = IsEven(i1);
    PrintBool(b1);
    PrintIsEvenResult(i1);
    PrintIsEvenResult(i2);

    long long ll1 = 6;
    long long ll2 = 7;
    PrintIsEvenResult(ll1);
    PrintIsEvenResult(ll2);
}

using namespace Inventory;
using namespace std;

// 简单类用法示例
static void testClass(void)
{
    auto vehicle = Vehicle(VehicleCondition::Excellent, 325844942.65);
    auto condition = vehicle.GetVehicleCondition();
    wcout << L"The vehicle is in " <<
        GetVehicleConditionString(condition).c_str() <<
        L" condition. Its basis is $" << setw(10) <<
        setprecision(2) << setiosflags(ios::fixed) <<
        vehicle.GetBasis() << L"." << endl;
}

class A
{
public:
    A(void) : someInt(0) { }
    virtual ~A(void) { }
    const wchar_t* Id(void) const { return L"A"; }
    virtual const wchar_t* VirtId(void) const { return L"A"; }
    int GetSomeInt(void) const { return someInt; }
    int someInt;
};

class B1 : virtual public A
{
public:
    B1(void) :
        A(),
        fValue(10.0f)
    {
        someInt = 10;
    }

    virtual ~B1(void) { }

    const wchar_t* Id(void) const { return L"B1"; }

    virtual const wchar_t* VirtId(void) const override
    {
        return L"B1";
    }

    const wchar_t* Conflict(void) const { return L"B1::Conflict()"; }
    float getFvlue() { return fValue; }

private:
    float fValue;
};

class B2 : virtual public A
{
public:
    B2(void) : A() { }
    virtual ~B2(void) { }

    const wchar_t* Id(void) const { return L"B2"; }

    virtual const wchar_t* VirtId(void) const override
    {
        return L"B2";
    }

    const wchar_t* Conflict(void) const { return L"B2::Conflict()"; }

};

class B3 : public A
{
public:
    B3(void) : A() { }
    virtual ~B3(void) { }

    const wchar_t* Id(void) const { return L"B3"; }

    virtual const wchar_t* VirtId(void) const override
    {
        return L"B3";
    }

    const wchar_t* Conflict(void) const { return L"B3::Conflict()"; }

};

class VirtualClass : virtual public B1, virtual public B2
{
public:
    VirtualClass(void) :
        B1(),
        B2(),
        id(L"VirtualClass")
    { }

    virtual ~VirtualClass(void) { }

    const wchar_t* Id(void) const { return id.c_str(); }

    virtual const wchar_t* VirtId(void) const override
    {
        return id.c_str();
    }

private:
    wstring id;
};

class NonVirtualClass : public B1, public B3
{
public:
    NonVirtualClass(void) :
        B1(),
        B3(),
        id(L"NonVirtualClass")
    { }

    virtual ~NonVirtualClass(void) { }

    const wchar_t* Id(void) const { return id.c_str(); }

    virtual const wchar_t* VirtId(void) const override
    {
        return id.c_str();
    }

private:
    wstring id;
};

void DemonstrateNonVirtualInheritance(void)
{
    NonVirtualClass nvC = NonVirtualClass();

    // someInt具有二义性
    // nvC.someInt = 20;

    nvC.B1::someInt = 20;
    nvC.B3::someInt = 20;

    // 因为二义性，下面语句报错
    //A& nvCA = nvC;

    B1& nvCB1 = nvC;
    B3& nvCB3 = nvC;
    A& nvCAfromB1 = nvCB1;
    A& nvCAfromB3 = nvCB3;
    wcout <<
        L"nvCAfromB1::someInt = " << nvCAfromB1.someInt << endl <<
        L"nvCAfromB3::someInt = " << nvCAfromB3.someInt << endl <<
        L"B1::someInt = " << nvCB1.someInt << endl <<
        L"B3::someInt = " << nvCB3.someInt << endl <<
        endl;
    ++nvCB1.someInt;
    nvCB3.someInt += 20;
    wcout <<
        L"B1::someInt = " << nvCB1.someInt << endl <<
        L"B3::someInt = " << nvCB3.someInt << endl <<
        endl;

    // 成员函数存在冲突
    wcout <<
        typeid(nvC).name() << endl <<
        nvC.Id() << endl <<
        nvC.VirtId() << endl <<
        // 指定类名解决成员函数冲突
        nvC.B3::Conflict() << endl <<
        nvC.B1::Conflict() << endl <<
        // 无法执行GetSomeInt
        //nvC.GetSomeInt() << endl <<
        endl <<
        
        typeid(nvCB3).name() << endl <<
        nvCB3.Id() << endl <<
        nvCB3.VirtId() << endl <<
        nvCB3.Conflict() << endl <<
        endl <<

        typeid(nvCB1).name() << endl <<
        nvCB1.Id() << endl <<
        nvCB1.VirtId() << endl <<
        nvCB1.GetSomeInt() << endl <<
        nvCB1.Conflict() << endl <<
        endl;
}

void DemonstrateVirtualInheritance(void)
{
    VirtualClass vC = VirtualClass();

    // 因为虚拟继承，所以只有一个A的拷贝
    vC.someInt = 20;

    A& vCA = vC;
    B1& vCB1 = vC;
    B2& vCB2 = vC;

    wcout <<
        L"B1::someInt = " << vCB1.someInt << endl <<
        L"B3::someInt = " << vCB2.someInt << endl <<
        endl;

    ++vCB1.someInt;
    vCB2.someInt += 20;

    wcout <<
        L"B1::someInt = " << vCB1.someInt << endl <<
        L"B3::someInt = " << vCB2.someInt << endl <<
        endl;

    wcout <<
        typeid(vC).name() << endl <<
        vC.Id() << endl <<
        vC.VirtId() << endl <<
        vC.B2::Id() << endl <<
        vC.B2::VirtId() << endl <<
        vC.B1::Id() << endl <<
        vC.B1::VirtId() << endl <<
        vC.A::Id() << endl <<
        vC.A::VirtId() << endl <<
        vC.B2::Conflict() << endl <<
        vC.B1::Conflict() << endl <<
        vC.GetSomeInt() << endl <<
        endl <<
        
        typeid(vCB2).name() << endl <<
        vCB2.Id() << endl <<
        vCB2.VirtId() << endl <<
        vCB2.Conflict() << endl <<
        endl <<

        typeid(vCB1).name() << endl <<
        vCB1.Id() << endl <<
        vCB1.VirtId() << endl <<
        vCB1.GetSomeInt() << endl <<
        vCB1.Conflict() << endl <<
        endl <<

        typeid(vCA).name() << endl <<
        vCA.Id() << endl <<
        vCA.VirtId() << endl <<
        vCA.GetSomeInt() << endl <<
        endl;
}

// 类的继承示例
static void testInheritanceClass(void)
{
    DemonstrateNonVirtualInheritance();
    DemonstrateVirtualInheritance();
}


// 抽象类示例
static void testWriteData(void)
{
    // 抽象类不能直接赋值，下面语句会报错
    // IWriteData iwd = IWriteData();
    // IWriteData iwd = ConsoleWriteData();

    ConsoleWriteData cwd = ConsoleWriteData();
    IWriteData& r_iwd = cwd;
    IWriteData* p_iwd = &cwd;
    cwd.WriteLine(10);
    r_iwd.WriteLine(14.6);
    p_iwd->WriteLine(L"Hello Abstract World!");
}

int main(int ac, char *av[])
{
    testFunction();
    testClass();
    testInheritanceClass();
    testWriteData();
    return 0;
}


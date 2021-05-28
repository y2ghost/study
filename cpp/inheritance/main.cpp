#include <iostream>
#include <ostream>
#include <string>
#include <typeinfo>

using namespace std;

class A
{
public:
    A(void) : SomeInt(0) { }
    virtual ~A(void) { }
    
    const wchar_t* Id(void) const { return L"A"; }
    
    virtual const wchar_t* VirtId(void) const { return L"A"; }
    
    int GetSomeInt(void) const { return SomeInt; }
    
    int			SomeInt;
};

class B1 : virtual public A
{
public:
    B1(void) :
    A(),
    m_fValue(10.0f)
    {
        SomeInt = 10;
    }
    virtual ~B1(void) { }
    
    const wchar_t* Id(void) const { return L"B1"; }
    
    virtual const wchar_t* VirtId(void) const override
    {
        return L"B1";
    }
    
    const wchar_t* Conflict(void) const { return L"B1::Conflict()"; }
    
private:
    float			m_fValue;
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

class VirtualClass : virtual public B1,	virtual public B2
{
public:
    VirtualClass(void) :
    B1(),
    B2(),
    m_id(L"VirtualClass")
    { }
    
    virtual ~VirtualClass(void) { }
    
    const wchar_t* Id(void) const { return m_id.c_str(); }
    
    virtual const wchar_t* VirtId(void) const override
    {
        return m_id.c_str();
    }
    
private:
    wstring				m_id;
};

// Note: If you were try to inherit from A before inheriting from B1
// and B3 here there would be a Visual C++ compiler error. If you
// tried to inherit from it after B1 and B3 there would still be a
// compiler warning. If you both indirectly and directly inherit
// from a class, it is impossible to get at the direct inheritance
// version of it.
class NonVirtualClass : public B1, public B3
{
public:
    NonVirtualClass(void) :
    B1(),
    B3(),
    m_id(L"NonVirtualClass")
    { }
    
    virtual ~NonVirtualClass(void) { }
    
    const wchar_t* Id(void) const { return m_id.c_str(); }
    
    virtual const wchar_t* VirtId(void) const override
    {
        return m_id.c_str();
    }
    
    //// If we decided we wanted to use B1::Conflict, we could use
    //// a using declaration. In this case we would be saying that
    //// calling NonVirtualClass::Conflict means call B1::Conflict
    //using B1::Conflict;
    
    //// We can also use it to resolve ambiguity between member
    //// data. In this case we would be saying that
    //// NonVirtualClass::SomeInt means B3::SomeInt, such that
    //// the nvC.SomeInt statement in
    //// DemonstrateNonVirtualInheritance would be legal (even
    //// though IntelliSense says otherwise).
    //using B3::SomeInt;
    
private:
    wstring				m_id;
};

void DemonstrateNonVirtualInheritance(void)
{
    NonVirtualClass nvC = NonVirtualClass();
    
    //// SomeInt is ambiguous since there are two copies of A, one
    //// indirectly from B1 and the other indirectly from B3.
    //nvC.SomeInt = 20;
    
    // But you can get to the two copies of SomeInt by specifying which
    // base class's SomeInt you want. Note that if NonVirtualClass also
    // directly inherited from A then this too would be impossible.
    nvC.B1::SomeInt = 20;
    nvC.B3::SomeInt = 20;
    
    //// It is impossible to create a reference to A due to ambiguity.
    //A& nvCA = nvC;
    
    // We can create references to B1 and B3 though.
    B1& nvCB1 = nvC;
    B3& nvCB3 = nvC;
    
    // If we want a reference to some particular A we can now get one.
    A& nvCAfromB1 = nvCB1;
    A& nvCAfromB3 = nvCB3;
    
    // To demonstrate that there are two copies of A's data.
    wcout <<
    L"B1::SomeInt = " << nvCB1.SomeInt << endl <<
    L"B3::SomeInt = " << nvCB3.SomeInt << endl <<
    endl;
    
    ++nvCB1.SomeInt;
    nvCB3.SomeInt += 20;
    
    wcout <<
    L"B1::SomeInt = " << nvCB1.SomeInt << endl <<
    L"B3::SomeInt = " << nvCB3.SomeInt << endl <<
    endl;
    
    // Let's see a final demo of the result. Note that the Conflict
    // member function is also ambiguous because both B1 and B3 have
    // a member function named Conflict with the same signature.
    wcout <<
    typeid(nvC).name() << endl <<
    nvC.Id() << endl <<
    nvC.VirtId() << endl <<
    //// This is ambiguous between B1 and B3
    //nvC.Conflict() << endl <<
    // But we can solve that ambiguity.
    nvC.B3::Conflict() << endl <<
    nvC.B1::Conflict() << endl <<
    //// GetSomeInt is ambiguous too.
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
    vC.SomeInt = 20;
    
    
    A& vCA = vC;
    B1& vCB1 = vC;
    B2& vCB2 = vC;
    
    wcout <<
    L"B1::SomeInt = " << vCB1.SomeInt << endl <<
    L"B3::SomeInt = " << vCB2.SomeInt << endl <<
    endl;
    
    ++vCB1.SomeInt;
    vCB2.SomeInt += 20;
    
    wcout <<
    L"B1::SomeInt = " << vCB1.SomeInt << endl <<
    L"B3::SomeInt = " << vCB2.SomeInt << endl <<
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

int main(int argc, char* argv[])
{
    DemonstrateNonVirtualInheritance();
    DemonstrateVirtualInheritance();
    return 0;
}

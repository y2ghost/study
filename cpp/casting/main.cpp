#include <iostream>
#include <ostream>
#include <typeinfo>

using namespace std;

class A
{
public:
    A(void) : m_value(0) { }
    virtual ~A(void) { }
    
    virtual int GetValue(void) { return m_value; }
    virtual void SetValue(int value) { m_value = value; }
private:
    int m_value;
};

class B : virtual public A
{
public:
    B(void) : A() { }
    virtual ~B(void) { }
    
    virtual int GetValue(void) override { return A::GetValue(); }
    virtual void SetValue(int value) { A::SetValue(-value); }
};

class C
{
public:
    C(void) { }
    ~C(void) { }
};

int main(int argc, char* argv[])
{
    A a1;
    B b1;
    C c1;
    
    A* p_a1 = &a1;
    B* p_b1 = &b1;
    
    // Implicit casting of a B object to a pointer to A.
    A* p_ab1 = &b1;
    A& r_ab1 = b1;
    
    //// static_cast cannot cast through virtual inheritance.
    //B& b1Ref = static_cast<B&>(*p_ab1);
    
    // Which is where dynamic_cast comes in handy.
    B& b1Ref = dynamic_cast<B&>(*p_ab1);
    
    try
    {
        B& b2Ref = dynamic_cast<B&>(*p_a1);
    }
    catch (bad_cast)
    {
        wcout << L"You can't cast to a derived class if it isn't " <<
        L"actually an instance of that derived class or of some " <<
        L"class derived from it." << endl;
    }
    
    A& a1Ref = static_cast<A&>(*p_b1);
    
    A* a1Ptr = dynamic_cast<A*>(p_b1);
    
    A* dcBPtrToAPtr = dynamic_cast<A*>(p_b1);
    
    // Why oh why does it let you do this?
    B* dcAPtrToBPtr = dynamic_cast<B*>(p_a1);
    
    C* p_aPtrToCPtr = nullptr;
    // This will return null, so if you wanted an exception you'll have 
    // to throw one yourself.
    try
    {
        p_aPtrToCPtr = dynamic_cast<C*>(p_a1);
        if (p_aPtrToCPtr == nullptr)
        {
            throw bad_cast();
        }
    }
    catch (std::bad_cast)
    {
        wcout << L"Bad cast!" << endl;
    }
    
    // This will throw a runtime exception.
    try
    {
        C& r_aPtrToCPtr= dynamic_cast<C&>(a1Ref);
    }
    catch (std::bad_cast)
    {
        wcout << L"Bad cast!" << endl;
    }
    
    // Const cast to const. This is ok.
    auto p_a2 = const_cast<const A*>(p_a1);
    //// This is illegal since this is const.
    //p_a2->SetValue(20);
    
    // Const cast to remove const. This is a bad idea.
    auto p_a3 = const_cast<A*>(p_a2);
    // This is legal now because we removed const. 
    p_a3->SetValue(20);
    
    // This is a C style cast. Don't do this.
    A* cCast = (A*)p_b1;
    
    return 0;
}

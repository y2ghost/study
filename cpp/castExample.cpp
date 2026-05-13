#include <iostream>
#include <ostream>
#include <typeinfo>

using namespace std;

class A
{
public:
    A(void) : value(0) { }
    virtual ~A(void) { }

    virtual int GetValue(void) { return value; }
    virtual void SetValue(int value) { this->value = value; }
private:
    int value;
};

class B : virtual public A
{
public:
    B(void) : A() { }
    virtual ~B(void) { }

    virtual int GetValue(void) override { return A::GetValue(); }
    virtual void SetValue(int value) override { A::SetValue(-value); }
};

class C
{
public:
    C(void) { }
    int getScore() { return score; }
    ~C(void) { }
private:
    int score;
};

// 类型转换示例
int main(int ac, char *av[])
{
    A a1;
    B b1;
    C c1;

    A* p_a1 = &a1;
    B* p_b1 = &b1;
    
    // 隐式转换
    A* p_ab1 = &b1;
    A& r_ab1 = b1;
    wcout << r_ab1.GetValue() << endl;

    // 虚基类不能静态转为子类
    // B& b1Ref = static_cast<B&>(*p_ab1);

    // dynamic_cast转换
    B& b1Ref = dynamic_cast<B&>(*p_ab1);
    wcout << b1Ref.GetValue() << endl;

    try
    {
        B& b2Ref = dynamic_cast<B&>(*p_a1);
        wcout << b2Ref.GetValue() << endl;
    }
    catch (bad_cast &ex)
    {
        wcout << L"You can't cast to a derived class if it isn't " <<
            L"actually an instance of that derived class or of some " <<
            L"class derived from it." << ex.what() << endl;
    }

    A& a1Ref = static_cast<A&>(*p_b1);
    wcout << a1Ref.GetValue() << endl;

    A* a1Ptr = dynamic_cast<A*>(p_b1);
    wcout << a1Ptr->GetValue() << endl;

    A* dcBPtrToAPtr = dynamic_cast<A*>(p_b1);
    B* dcAPtrToBPtr = dynamic_cast<B*>(p_a1);
    wcout << (dcBPtrToAPtr == dcAPtrToBPtr) << endl;

    C* p_aPtrToCPtr = nullptr;
    try
    {
        p_aPtrToCPtr = dynamic_cast<C*>(p_a1);
        if (p_aPtrToCPtr == nullptr)
        {
            throw std::bad_cast();
        }
    }
    catch (std::bad_cast &ex)
    {
        wcout << L"Bad cast!" << ex.what() << endl;
    }

    // 运行时会出错
    try
    {
        C& r_aPtrToCPtr = dynamic_cast<C&>(a1Ref);
        wcout << r_aPtrToCPtr.getScore() << endl;
    }
    catch (std::bad_cast &ex)
    {
        wcout << L"Bad cast!" << ex.what() << endl;
    }

    // const转换const没问题
    auto p_a2 = const_cast<const A*>(p_a1);
    // 下面代码不合法
    // p_a2->SetValue(20);

    // 移除const限制，不建议
    auto p_a3 = const_cast<A*>(p_a2);
    p_a3->SetValue(20);

    // C风格的转换，C++不建议使用
    // A* cCast = (A*)p_b1;
    return 0;
}

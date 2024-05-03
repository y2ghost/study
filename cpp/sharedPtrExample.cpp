#include <memory>
#include <iostream>
#include <ostream>

using namespace std;

struct TwoInts
{
    TwoInts(void) : A(), B() { }
    TwoInts(int a, int b) : A(a), B(b) { }

    int A;
    int B;
};

wostream& operator<<(wostream& stream, TwoInts* v)
{
    stream << v->A << L" " << v->B;
    return stream;
}

// 智能指针示例
int main(int ac, char *av[])
{
    // shared_ptr示例
    {
        auto sp1 = shared_ptr<TwoInts>(new TwoInts(10, 20));
        auto sp2 = shared_ptr<TwoInts>(sp1);
        wcout << L"sp1 count is " << sp1.use_count() << L"." << endl <<
            L"sp2 count is " << sp2.use_count() << L"." << endl;
        wcout << L"sp1 value is " << sp1 << L"." << endl <<
            L"sp2 value is " << sp2 << L"." << endl;
    }
    // make_shared示例
    {
        auto sp1 = make_shared<TwoInts>(10, 20);
        auto sp2 = shared_ptr<TwoInts>(sp1);
        wcout << L"sp1 count is " << sp1.use_count() << L"." << endl <<
            L"sp2 count is " << sp2.use_count() << L"." << endl;
        wcout << L"sp1 value is " << sp1 << L"." << endl <<
            L"sp2 value is " << sp2 << L"." << endl;
    }
    
    return 0;
}

#include <iostream>
#include <ostream>
#include <sstream>
#include <thread>
#include <memory>
#include <cstddef>
#include "SomeClass.h"

using namespace std;

struct SomeStruct
{
    int Value;
};

namespace Value
{
thread_local SomeStruct ThreadLocalSomeStruct = {};

static SomeClass g_staticSomeClass = SomeClass(20, L"g_staticSomeClass");
}

void ChangeAndPrintValue(int value)
{
    
    wstringstream wsStr(L"");
    wsStr << L"ChangeAndPrintValue thread id: '" << this_thread::get_id()
    << L"'";
    SomeClass sc(value, wsStr.str().c_str());
    
    wcout << L"Old value is " << Value::ThreadLocalSomeStruct.Value <<
    L". Thread id: '" << this_thread::get_id() << L"'." << endl;
    Value::ThreadLocalSomeStruct.Value = value;
    wcout << L"New value is " << Value::ThreadLocalSomeStruct.Value <<
    L". Thread id: '" << this_thread::get_id() << L"'." << endl;
}

void LocalStatic(int value)
{
    static SomeClass sc(value, L"LocalStatic sc");
    
    
    wcout << L"Local Static sc value: '" << sc.GetValue() <<
    L"'." << endl << endl;
}

int main(int argc, const char * argv[])
{
    
    SomeClass sc1(1, L"_pmain sc1");
    wcout << L"sc1 value: '" << sc1.GetValue() <<
    L"'." << endl << endl;
    {
        
        SomeClass sc2(2, L"_pmain sc2");
        wcout << L"sc2 value: '" << sc2.GetValue() <<
        L"'." << endl << endl;
    }
    
    LocalStatic(1000);
    LocalStatic(5000);
    ChangeAndPrintValue(20);
    auto thr = thread(ChangeAndPrintValue, 40);
    thr.join();
    SomeClass* p_dsc = new SomeClass(1000, L"_pmain p_dsc");
    
    const std::size_t arrIntSize = 5;
    int* p_arrInt = new int[arrIntSize];
    
    for (int i = 0; i < arrIntSize; i++)
    {
        wcout << L"i: '" << i << L"'. p_arrInt[i] = '" <<
        p_arrInt[i] << L"'." << endl;
        p_arrInt[i] = i;
    }
    
    wcout << endl;
    for (int i = 0; i < arrIntSize; i++)
    {
        wcout << L"i: '" << i << L"'. p_arrInt[i] = '" <<
        p_arrInt[i] << L"'." << endl;
    }
    
    delete p_dsc;
    p_dsc = nullptr;
    delete[] p_arrInt;
    p_arrInt = nullptr;
    wcout << L"Ending program." << endl;
    return 0;
}

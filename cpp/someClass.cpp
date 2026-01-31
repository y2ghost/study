#include "someClass.h"
#include <string>
#include <ostream>
#include <iostream>
#include <ios>
#include <iomanip>
#include <thread>
#include <memory>

using namespace std;

SomeClass::SomeClass(int value) :
    value(value),
    stringId(L"(No string id provided.)")
{
    SomeClass* localThis = this;
    auto addr = reinterpret_cast<unsigned long>(localThis);
    wcout << L"Creating SomeClass instance." << endl <<
        L"StringId: " << stringId.c_str() << L"." << endl <<
        L"Address is: '0x" << setw(8) << setfill(L'0') <<
        hex << addr << dec << L"'." << endl <<
        L"Value is '" << value << L"'." << endl <<
        L"Thread id: '" <<
        this_thread::get_id() << L"'." << endl << endl;
}

SomeClass::SomeClass(
    int value,
    const wchar_t* stringId
    ) : value(value),
    stringId(stringId)
{
    SomeClass* localThis = this;
    auto addr = reinterpret_cast<unsigned long>(localThis);
    wcout << L"Creating SomeClass instance." << endl <<
        L"StringId: " << stringId << L"." << endl <<
        L"Address is: '0x" << setw(8) << setfill(L'0') <<
        hex << addr << dec << L"'." << endl <<
        L"Value is '" << value << L"'." << endl <<
        L"Thread id: '" <<
        this_thread::get_id() << L"'." << endl << endl;
}

SomeClass::~SomeClass(void)
{
    value = 0;
    SomeClass* localThis = this;
    auto addr = reinterpret_cast<unsigned long>(localThis);
    wcout << L"Destroying SomeClass instance." << endl <<
        L"StringId: " << stringId.c_str() << L"." << endl <<
        L"Address is: '0x" << setw(8) << setfill(L'0') <<
        hex << addr << dec << L"'." << endl <<
        L"Thread id: '" <<
        this_thread::get_id() << L"'." << endl << endl;
}

unique_ptr<SomeClass> SomeClass::s_someClass =
    unique_ptr<SomeClass>(new SomeClass(10, L"s_someClass"));

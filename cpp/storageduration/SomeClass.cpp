#include "SomeClass.h"
#include <string>
#include <ostream>
#include <iostream>
#include <ios>
#include <iomanip>
#include <thread>
#include <memory>

using namespace std;

SomeClass::SomeClass(int value)
: m_value(value),
  m_stringId(L"(No string id provided.)")
{
    SomeClass* localThis = this;
    auto addr = reinterpret_cast<uintptr_t>(localThis);
    wcout << L"Creating SomeClass instance." << endl <<
    L"StringId: " << m_stringId.c_str() << L"." << endl <<
    L"Address is: '0x" << setw(8) << setfill(L'0') <<
    hex << addr << dec << L"'." << endl <<
    L"Value is '" << m_value << L"'." << endl <<
    L"Thread id: '" <<
    this_thread::get_id() << L"'." << endl << endl;
}

SomeClass::SomeClass(int value, const wchar_t* stringId )
: m_value(value),
  m_stringId(stringId)
{
    SomeClass* localThis = this;
    auto addr = reinterpret_cast<uintptr_t>(localThis);
    wcout << L"Creating SomeClass instance." << endl <<
    L"StringId: " << m_stringId.c_str() << L"." << endl <<
    L"Address is: '0x" << setw(8) << setfill(L'0') <<
    hex << addr << dec << L"'." << endl <<
    L"Value is '" << m_value << L"'." << endl <<
    L"Thread id: '" <<
    this_thread::get_id() << L"'." << endl << endl;
}

SomeClass::~SomeClass(void)
{
    m_value = 0;
    SomeClass* localThis = this;
    auto addr = reinterpret_cast<uintptr_t>(localThis);
    wcout << L"Destroying SomeClass instance." << endl <<
    L"StringId: " << m_stringId.c_str() << L"." << endl <<
    L"Address is: '0x" << setw(8) << setfill(L'0') <<
    hex << addr << dec << L"'." << endl <<
    L"Thread id: '" <<
    this_thread::get_id() << L"'." << endl << endl;
}

unique_ptr<SomeClass> SomeClass::s_someClass =
unique_ptr<SomeClass>(new SomeClass(10, L"s_someClass"));

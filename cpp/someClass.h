#pragma once
#include <string>
#include <memory>

class SomeClass
{
public:
    explicit SomeClass(int value = 0);

    SomeClass(int value, const wchar_t* stringId);

    ~SomeClass(void);

    int GetValue(void) { return value; }

    void SetValue(int value) { this->value = value; }

    static std::unique_ptr<SomeClass> s_someClass;

private:
    int value;
    std::wstring stringId;
};

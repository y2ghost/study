#pragma once

class IWriteData
{
public:
    virtual void Write(const wchar_t* value) = 0;
    virtual void Write(double value) = 0;
    virtual void Write(int value) = 0;

    virtual void WriteLine(void) = 0;
    virtual void WriteLine(const wchar_t* value) = 0;
    virtual void WriteLine(double value) = 0;
    virtual void WriteLine(int value) = 0;
};


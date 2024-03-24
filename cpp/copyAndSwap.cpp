#include <algorithm>
#include <cstddef>
#include <iostream>

class dumb_array
{
public:
    // 默认构造函数
    dumb_array(std::size_t size = 0)
        : mSize(size),
          mArray(mSize ? new int[mSize]() : nullptr)
    {
        std::cout << "构造对象: " << this << std::endl;
    }

    // 拷贝构造函数
    dumb_array(const dumb_array &other)
        : mSize(other.mSize),
          mArray(mSize ? new int[mSize] : nullptr)
    {
        std::cout << this << " 拷贝对象: " << &other << std::endl;
        std::copy(other.mArray, other.mArray + mSize, mArray);
    }

    // 拷贝赋值运算符函数
    dumb_array &operator=(const dumb_array &other)
    {
        std::cout << this << " 赋值对象: " << &other << std::endl;
        dumb_array temp(other);
        swap(*this, temp);
        return *this;
    }

    friend void swap(dumb_array &first, dumb_array &second)
    {
        using std::swap;
        swap(first.mSize, second.mSize);
        swap(first.mArray, second.mArray);
    }

    // 析构函数
    ~dumb_array()
    {
        std::cout << "删除对象: " << this << std::endl;
        delete[] mArray;
    }

private:
    std::size_t mSize;
    int *mArray;
};

int main(void)
{
    dumb_array a(5);
    dumb_array b(a);
    b = a;
    return 0;
}

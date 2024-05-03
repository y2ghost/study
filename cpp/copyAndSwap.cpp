#include <algorithm>
#include <cstddef>
#include <iostream>

class DumBArray
{
public:
    // 默认构造函数
    DumBArray(std::size_t size = 0)
        : mSize(size),
          mArray(mSize ? new int[mSize]() : nullptr)
    {
        std::cout << "构造对象: " << this << std::endl;
    }

    // 拷贝构造函数
    DumBArray(const DumBArray &other)
        : mSize(other.mSize),
          mArray(mSize ? new int[mSize] : nullptr)
    {
        std::cout << this << " 拷贝对象: " << &other << std::endl;
        std::copy(other.mArray, other.mArray + mSize, mArray);
    }

    // 拷贝赋值运算符函数
    DumBArray &operator=(const DumBArray &other)
    {
        std::cout << this << " 赋值对象: " << &other << std::endl;
        DumBArray temp(other);
        swap(*this, temp);
        return *this;
    }

    friend void swap(DumBArray &first, DumBArray &second)
    {
        using std::swap;
        swap(first.mSize, second.mSize);
        swap(first.mArray, second.mArray);
    }

    // 析构函数
    ~DumBArray()
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
    DumBArray a(5);
    DumBArray b(a);
    b = a;
    return 0;
}

#include <utility>
#include <stdio.h>

class SharedCount
{
public:
    SharedCount() noexcept
        : count(1) {}

    void addCount() noexcept
    {
        ++count;
    }

    long reduceCount() noexcept
    {
        return --count;
    }

    long getCount() noexcept
    {
        return count;
    }

private:
    long count;
};

/**
 * 拷贝智能指针不会拷贝对象
 * 1.智能指针的初衷是减少对象拷贝
 * 2.在 C++ 里没有像 Java 的 clone 方法这样的约定
 * 3.并没有通用的方法可以通过基类的指针来构造出一个子类的对象
 */
template <typename T>
class SmartPointer
{
public:
    template <typename U>
    friend class SmartPointer;

    explicit SmartPointer(T *ptr = nullptr) : smartPtr(ptr)
    {
        if (ptr)
        {
            sharedCount = new SharedCount();
        }
    }

    ~SmartPointer()
    {
        if (smartPtr && !sharedCount->reduceCount())
        {
            delete smartPtr;
            delete sharedCount;
        }
    }

    SmartPointer(const SmartPointer &other)
    {
        smartPtr = other.smartPtr;
        if (smartPtr)
        {
            other.sharedCount->addCount();
            sharedCount = other.sharedCount;
        }
    }

    template <typename U>
    SmartPointer(SmartPointer<U> &other) noexcept
    {
        smartPtr = other.smartPtr;
        if (smartPtr)
        {
            other.sharedCount->addCount();
            sharedCount = other.sharedCount;
        }
    }

    /**
     * 根据 C++ 的规则，如果我提供了移动构造函数而没有手动提供拷贝构造函数
     * 那么拷贝构造函数自动被禁用
     * 注意使用了模板的话不会被编译器当作移动构造函数
     */
    template <typename U>
    SmartPointer(SmartPointer<U> &&other) noexcept
    {
        smartPtr = other.smartPtr;
        if (smartPtr)
        {
            sharedCount = other.sharedCount;
            other.smartPtr = nullptr;
        }
    }

    template <typename U>
    SmartPointer(const SmartPointer<U> &other, T *ptr) noexcept
    {
        smartPtr = ptr;
        if (ptr)
        {
            other.sharedCount->addCount();
            sharedCount = other.sharedCount;
        }
    }

    SmartPointer &operator=(SmartPointer other) noexcept
    {
        // 赋值分为拷贝构造和交换两个步骤
        // 一般异常只能发生在拷贝构造，就算异常this对象也不会受到影响
        // 从而保证强异常安全性
        other.swap(*this);
        return *this;
    }

    T *get() const noexcept
    {
        return smartPtr;
    }

    T &operator*() const noexcept
    {
        return *smartPtr;
    }

    T *operator->() const noexcept
    {
        return smartPtr;
    }

    operator bool()
    {
        return smartPtr;
    }

    void swap(SmartPointer &other) noexcept
    {
        using std::swap;
        swap(smartPtr, other.smartPtr);
        swap(sharedCount, other.sharedCount);
    }

    long useCount() const noexcept
    {
        if (smartPtr)
        {
            return sharedCount->getCount();
        }
        else
        {
            return 0;
        }
    }

private:
    T *smartPtr;
    SharedCount *sharedCount;
};

template <typename T>
void swap(SmartPointer<T> &first, SmartPointer<T> &second) noexcept
{
    first.swap(second);
}

template <typename T, typename U>
SmartPointer<T> static_pointer_cast(const SmartPointer<U> &other) noexcept
{
    T *ptr = static_cast<T *>(other.get());
    return SmartPointer<T>(other, ptr);
}

template <typename T, typename U>
SmartPointer<T> reinterpret_pointer_cast(const SmartPointer<U> &other) noexcept
{
    T *ptr = reinterpret_cast<T *>(other.get());
    return SmartPointer<T>(other, ptr);
}

template <typename T, typename U>
SmartPointer<T> const_pointer_cast(const SmartPointer<U> &other) noexcept
{
    T *ptr = const_cast<T *>(other.get());
    return SmartPointer<T>(other, ptr);
}

template <typename T, typename U>
SmartPointer<T> dynamic_pointer_cast(const SmartPointer<U> &other) noexcept
{
    T *ptr = dynamic_cast<T *>(other.get());
    return SmartPointer<T>(other, ptr);
}

class Shape
{
public:
    virtual ~Shape() {}
};

class Circle : public Shape
{
public:
    ~Circle() { puts("~Circle()"); }
};

int main()
{
    SmartPointer<Circle> ptr1(new Circle());
    printf("use count of ptr1 is %ld\n", ptr1.useCount());
    SmartPointer<Shape> ptr2;
    printf("use count of ptr2 was %ld\n", ptr2.useCount());
    ptr2 = ptr1;
    printf("use count of ptr2 is now %ld\n", ptr2.useCount());

    if (ptr1)
    {
        puts("ptr1 is not empty");
    }

    SmartPointer<Circle> ptr3 = dynamic_pointer_cast<Circle>(ptr2);
    printf("use count of ptr3 is %ld\n", ptr3.useCount());
}
#include <stdio.h>
#include <utility>

class Shape
{
public:
    virtual ~Shape() {}
};

class Circle : public Shape
{
public:
    Circle() { puts("Circle()"); }
    ~Circle() { puts("~Circle()"); }
};

class Triangle : public Shape
{
public:
    Triangle() { puts("Triangle()"); }
    ~Triangle() { puts("~Triangle()"); }
};

class Result
{
public:
    Result() { puts("Result()"); }
    ~Result() { puts("~Result()"); }
};

Result process_shape(const Shape &shape1,
                     const Shape &shape2)
{
    puts("process_shape()");
    return Result();
}

int main()
{
    puts("main()");
    Result &&r = process_shape(Circle(), Triangle());
    puts("main() end");
}

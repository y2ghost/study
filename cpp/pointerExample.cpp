#include <memory>
#include <cassert>

using namespace std;

void SetValueToZero(int& value)
{
    value = 0;
}

void SetValueToZero(int* value)
{
    *value = 0;
}

// 指针示例
int main(int ac, char *av[])
{
    int value = 0;
    const int intArrCount = 20;
    int* p_intArr = new int[intArrCount];
    int* const cp_intArr = p_intArr;
    uninitialized_fill_n(cp_intArr, intArrCount, 5);
    *cp_intArr = 0;
    const int* pc_intArr = nullptr;
    pc_intArr = p_intArr;
    value = *pc_intArr;

    const int* const cpc_intArr = p_intArr;
    value = *cpc_intArr;
    *p_intArr = 6;
    SetValueToZero(*p_intArr);

    assert(*p_intArr == 0);
    *p_intArr = 9;
    int& r_first = *p_intArr;
    SetValueToZero(r_first);
    assert(*p_intArr == 0);
    const int& cr_first = *p_intArr;
    value = cr_first;

    int* p_firstElement = &r_first;
    *p_firstElement = 10;
    SetValueToZero(*p_firstElement);
    assert(*p_firstElement == 0);
    SetValueToZero(&r_first);
    *p_intArr = 3;
    SetValueToZero(&(*p_intArr));
    assert(*p_firstElement == 0);

    // 函数指针示例
    void (*FunctionPtrToSVTZ)(int&) = nullptr;
    FunctionPtrToSVTZ = &SetValueToZero;
    *p_intArr = 20;
    FunctionPtrToSVTZ(*p_intArr);
    assert(*p_intArr == 0);
    *p_intArr = 50;
    (*FunctionPtrToSVTZ)(*p_intArr);
    assert(*p_intArr == 0);
    *p_intArr = 0;
    value = *p_intArr;
    delete[] p_intArr;
    p_intArr = nullptr;
    return value;
}

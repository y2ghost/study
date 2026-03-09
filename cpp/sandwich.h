#pragma once
#include <memory>

class SandwichImpl;

// 接口和实现分类，实现类SandwichImpl不曝露细节
class Sandwich
{
public:
    Sandwich(void);
    ~Sandwich(void);

    void AddIngredient(const wchar_t* ingredient);
    void RemoveIngredient(const wchar_t* ingredient);
    void SetBreadType(const wchar_t* breadType);
    const wchar_t* GetSandwich(void);

private:
    std::unique_ptr<SandwichImpl> pImpl;
};

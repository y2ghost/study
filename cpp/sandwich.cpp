#include "sandwich.h"
#include <vector>
#include <string>
#include <algorithm>

using namespace std;

class SandwichImpl
{
public:
    SandwichImpl();
    ~SandwichImpl();

    void AddIngredient(const wchar_t* ingredient);
    void RemoveIngredient(const wchar_t* ingredient);
    void SetBreadType(const wchar_t* breadType);

    const wchar_t* GetSandwich(void);

private:
    vector<wstring> ingredients;
    wstring breadType;
    wstring description;
};

SandwichImpl::SandwichImpl()
{
}

SandwichImpl::~SandwichImpl()
{
}

void SandwichImpl::AddIngredient(const wchar_t* ingredient)
{
    ingredients.emplace_back(ingredient);
}

void SandwichImpl::RemoveIngredient(const wchar_t* ingredient)
{
    auto it = find_if(ingredients.begin(), ingredients.end(), [=] (wstring item) -> bool
    {
        return (item.compare(ingredient) == 0);
    });

    if (it != ingredients.end())
    {
        ingredients.erase(it);
    }
}

void SandwichImpl::SetBreadType(const wchar_t* breadType)
{
    this->breadType = breadType;
}

const wchar_t* SandwichImpl::GetSandwich(void)
{
    description.clear();
    description.append(L"A ");
    for (auto ingredient : ingredients)
    {
        description.append(ingredient);
        description.append(L", ");
    }
    description.erase(description.end() - 2, description.end());
    description.append(L" on ");
    description.append(breadType);
    description.append(L".");

    return description.c_str();
}

Sandwich::Sandwich(void)
    : pImpl(new SandwichImpl())
{
}

Sandwich::~Sandwich(void)
{
}

void Sandwich::AddIngredient(const wchar_t* ingredient)
{
    pImpl->AddIngredient(ingredient);
}

void Sandwich::RemoveIngredient(const wchar_t* ingredient)
{
    pImpl->RemoveIngredient(ingredient);
}

void Sandwich::SetBreadType(const wchar_t* breadType)
{
    pImpl->SetBreadType(breadType);
}

const wchar_t* Sandwich::GetSandwich(void)
{
    return pImpl->GetSandwich();
}

#pragma once
#include "flavor.h"
#include "toppings.h"
#include <string>

namespace Desserts
{
    class IceCreamSundae
    {
    public:
        IceCreamSundae(void);
        
        IceCreamSundae(Flavor flavor);

        explicit IceCreamSundae(Toppings::ToppingsList toppings);

        IceCreamSundae(const IceCreamSundae& other);
        IceCreamSundae& operator=(const IceCreamSundae& other);

        IceCreamSundae(IceCreamSundae&& other);
        IceCreamSundae& operator=(IceCreamSundae&& other);

        ~IceCreamSundae(void);

        void AddTopping(Toppings::ToppingsList topping);

        void RemoveTopping(Toppings::ToppingsList topping);

        void ChangeFlavor(Flavor flavor);

        const wchar_t* GetSundaeDescription(void);

    private:
        Flavor flavor;
        Toppings toppings;
        std::wstring description;
    };
}

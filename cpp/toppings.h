#pragma once
#include <string>
#include <sstream>

namespace Desserts
{
	class Toppings
	{
	public:

		enum ToppingsList : unsigned int
		{
			None =				0x00,
			HotFudge =			0x01,
			RaspberrySyrup =	0x02,
			CrushedWalnuts =	0x04,
			WhippedCream =		0x08,
			Cherry =			0x10
		} toppings;

		Toppings(void) :
			toppings(None),
			toppingsString()
		{
		}

		Toppings(ToppingsList toppings) :
			toppings(toppings),
			toppingsString()
		{
		}

		~Toppings(void)
		{
		}

		const wchar_t* GetString(void)
		{
			if (toppings == None)
			{
				toppingsString = L"None";
				return toppingsString.c_str();
			}
			bool addSpace = false;
			std::wstringstream wstrstream;
			if (toppings & HotFudge)
			{
				if (addSpace)
				{
					wstrstream << L" ";
				}
				wstrstream << L"Hot Fudge";
				addSpace = true;
			}
			if (toppings & RaspberrySyrup)
			{
				if (addSpace)
				{
					wstrstream << L" ";
				}
				wstrstream << L"Raspberry Syrup";
				addSpace = true;
			}
			if (toppings & CrushedWalnuts)
			{
				if (addSpace)
				{
					wstrstream << L" ";
				}
				wstrstream << L"Crushed Walnuts";
				addSpace = true;
			}
			if (toppings & WhippedCream)
			{
				if (addSpace)
				{
					wstrstream << L" ";
				}
				wstrstream << L"Whipped Cream";
				addSpace = true;
			}
			if (toppings & Cherry)
			{
				if (addSpace)
				{
					wstrstream << L" ";
				}
				wstrstream << L"Cherry";
				addSpace = true;
			}
			toppingsString = std::wstring(wstrstream.str());
			return toppingsString.c_str();
		}
	private:
		std::wstring		toppingsString;
	};

	inline Toppings operator&(Toppings a, unsigned int b)
	{
		a.toppings = static_cast<Toppings::ToppingsList>(static_cast<int>(a.toppings) & b);
		return a;
	}

	inline Toppings::ToppingsList operator&(Toppings::ToppingsList a, unsigned int b)
	{
		auto val = static_cast<Toppings::ToppingsList>(static_cast<unsigned int>(a) & b);
		return val;
	}

	inline Toppings::ToppingsList operator|(Toppings::ToppingsList a, Toppings::ToppingsList b)
	{
		return static_cast<Toppings::ToppingsList>(static_cast<int>(a) | static_cast<int>(b));
	}

	inline Toppings operator|(Toppings a, Toppings::ToppingsList b)
	{
		a.toppings = static_cast<Toppings::ToppingsList>(static_cast<int>(a.toppings) | static_cast<int>(b));
		return a;
	}
}
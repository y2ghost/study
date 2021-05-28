#include <iostream>
#include <ostream>
#include <string>
#include <iomanip>
#include "Vehicle.h"
#include "VehicleCondition.h"

using namespace Inventory;
using namespace std;

int main(int argc, char* argv[])
{
    auto vehicle = Vehicle(VehicleCondition::Excellent, 325844942.65);
    auto condition = vehicle.GetVehicleCondition();
    wcout << L"The vehicle is in " <<
        GetVehicleConditionString(condition).c_str() <<
        L" condition. Its basis is $" << setw(10) <<
        setprecision(2) << setiosflags(ios::fixed) <<
        vehicle.GetBasis() << L"." << endl;

    return 0;
}

#include "Vehicle.h"
#include "VehicleCondition.h"

using namespace Inventory;
using namespace std;

Vehicle::Vehicle(VehicleCondition condition, double pricePaid) :
m_condition(condition),
m_basis(pricePaid)
{
}

Vehicle::~Vehicle(void)
{
}

void Vehicle::SetVehicleCondition(VehicleCondition condition)
{
    m_condition = condition;
}

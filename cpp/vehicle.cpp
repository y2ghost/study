#include "vehicle.h"
#include "vehicleCondition.h"

using namespace Inventory;
using namespace std;

Vehicle::Vehicle(VehicleCondition condition, double pricePaid) :
	condition(condition),
	basis(pricePaid)
{
}

Vehicle::~Vehicle(void)
{
}

void Vehicle::SetVehicleCondition(VehicleCondition condition)
{
	this->condition = condition;
}

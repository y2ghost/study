#pragma once

namespace Inventory
{
    enum class VehicleCondition;

    class Vehicle
    {
    public:
        Vehicle(VehicleCondition condition, double pricePaid);

        virtual ~Vehicle(void);

        VehicleCondition GetVehicleCondition(void)
        {
            return condition;
        }

        void SetVehicleCondition(VehicleCondition condition);

        double GetBasis(void) { return basis; }

    private:
        VehicleCondition condition;
        double basis;
    };
}


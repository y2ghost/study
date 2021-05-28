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
        return m_condition;
    }
    
    void SetVehicleCondition(VehicleCondition condition);
    
    double GetBasis(void) { return m_basis; }
    
private:
    VehicleCondition m_condition;
    double m_basis;
};
}

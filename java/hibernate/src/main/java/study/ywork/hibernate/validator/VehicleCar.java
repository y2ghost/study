package study.ywork.hibernate.validator;

import javax.validation.constraints.Max;

public class VehicleCar implements Vehicle {
    @Override
    public void drive(@Max(55) int speedInMph) {
        System.out.println(speedInMph);
    }
}

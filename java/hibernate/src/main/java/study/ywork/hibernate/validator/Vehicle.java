package study.ywork.hibernate.validator;

import javax.validation.constraints.Max;

public interface Vehicle {
    void drive(@Max(75) int speedInMph);
}

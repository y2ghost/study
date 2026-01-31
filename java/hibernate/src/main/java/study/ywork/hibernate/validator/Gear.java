package study.ywork.hibernate.validator;

public class Gear {
    // 扭力
    private final Integer torque;

    public Gear(Integer torque) {
        this.torque = torque;
    }

    public Integer getTorque() {
        return torque;
    }

    public static class AcmeGear extends Gear {
        public AcmeGear() {
            super(60);
        }
    }
}

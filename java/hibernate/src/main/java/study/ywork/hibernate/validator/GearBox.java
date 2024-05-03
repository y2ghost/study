package study.ywork.hibernate.validator;

public class GearBox<T extends Gear> {
    private final T gear;

    public GearBox(T gear) {
        this.gear = gear;
    }

    public Gear getGear() {
        return gear;
    }
}

package study.ywork.hibernate.validator;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Car {
    // 制造商
    @NotNull(message = "不能为null")
    private String manufacturer;

    // 牌照
    @NotNull(message = "个数必须在2和14之间")
    @Size(min = 2, max = 14)
    @CheckCase(CaseMode.UPPER)
    private String licensePlate;

    // 座位数
    @Min(value = 2, message = "不能少于2")
    private int seatCount;

    // 变速箱
    private GearBox<@MinTorque(value = 100, message = "不能少于100") Gear> gearBox;

    public Car(String manufacturer, String licencePlate, int seatCount) {
        this.manufacturer = manufacturer;
        this.licensePlate = licencePlate;
        this.seatCount = seatCount;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public GearBox<Gear> getGearBox() {
        return gearBox;
    }

    public void setGearBox(GearBox<Gear> gearBox) {
        this.gearBox = gearBox;
    }

    @Override
    public String toString() {
        return "Car [manufacturer=" + manufacturer + ", licensePlate=" + licensePlate + ", seatCount=" + seatCount
            + ", gearBox=" + gearBox + "]";
    }
}

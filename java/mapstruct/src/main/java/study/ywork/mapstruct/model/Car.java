package study.ywork.mapstruct.model;

public class Car {
    private int id;
    private String price;
    private String manufacturingDate;
    private String brand;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Car{" + "id=" + id + ", price='" + price + '\'' + ", manufacturingDate='" + manufacturingDate + '\''
            + ", brand='" + brand + '\'' + ", name='" + name + '\'' + '}';
    }
}

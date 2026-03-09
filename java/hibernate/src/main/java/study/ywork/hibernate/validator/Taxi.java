package study.ywork.hibernate.validator;

import java.util.List;

@ValidPassengerCount
public class Taxi {
    private int seatCount;
    private List<Person> passengers;

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public List<Person> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Person> passengers) {
        this.passengers = passengers;
    }

    @Override
    public String toString() {
        return "Taxi [seatCount=" + seatCount + ", passengers=" + passengers + "]";
    }
}

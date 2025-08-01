package study.ywork.jpa.model.associations.onetoone.jointable;

import study.ywork.jpa.constant.CommonConstants;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Shipment {
    @Id
    @GeneratedValue(generator = CommonConstants.ID_GENERATOR)
    protected Long id;

    @NotNull
    protected Date createdOn = new Date();

    @NotNull
    protected ShipmentState shipmentState = ShipmentState.TRANSIT;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ITEM_SHIPMENT",
            joinColumns =
            @JoinColumn(name = "SHIPMENT_ID"),
            inverseJoinColumns =
            @JoinColumn(name = "ITEM_ID",
                    nullable = false,
                    unique = true)
    )
    protected Item auction;

    public Shipment() {
    }

    public Shipment(Item auction) {
        this.auction = auction;
    }

    public Long getId() {
        return id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public ShipmentState getShipmentState() {
        return shipmentState;
    }

    public void setShipmentState(ShipmentState shipmentState) {
        this.shipmentState = shipmentState;
    }

    public Item getAuction() {
        return auction;
    }

    public void setAuction(Item auction) {
        this.auction = auction;
    }
}

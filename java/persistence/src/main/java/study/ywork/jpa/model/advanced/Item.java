package study.ywork.jpa.model.advanced;

import study.ywork.jpa.constant.CommonConstants;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

@Entity
public class Item {
    @Id
    @GeneratedValue(generator = CommonConstants.ID_GENERATOR)
    protected Long id;

    @org.hibernate.annotations.Type(type = "yes_no")
    protected boolean verified = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    protected Date createdOn;

    @NotNull
    @Basic(fetch = FetchType.LAZY)
    protected String description;

    @Basic(fetch = FetchType.LAZY)
    @Column(length = 131072)
    protected byte[] image;

    @Lob
    protected Blob imageBlob;

    @NotNull
    @Enumerated(EnumType.STRING)
    protected AuctionType auctionType = AuctionType.HIGHEST_BID;

    @org.hibernate.annotations.Formula(
            "substr(DESCRIPTION, 1, 12) || '...'"
    )
    protected String shortDescription;

    @org.hibernate.annotations.Formula(
            "(select avg(b.AMOUNT) from BID b where b.ITEM_ID = ID)"
    )
    protected BigDecimal averageBidAmount;

    @Column(name = "IMPERIALWEIGHT")
    @org.hibernate.annotations.ColumnTransformer(
            read = "IMPERIALWEIGHT / 2.20462",
            write = "? * 2.20462"
    )
    protected double metricWeight;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false)
    @org.hibernate.annotations.Generated(
            org.hibernate.annotations.GenerationTime.ALWAYS
    )
    protected Date lastModified;

    @Column(insertable = false)
    @org.hibernate.annotations.ColumnDefault("1.00")
    @org.hibernate.annotations.Generated(
            org.hibernate.annotations.GenerationTime.INSERT
    )
    protected BigDecimal initialPrice;

    @Access(AccessType.PROPERTY)
    @Column(name = "ITEM_NAME")
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = !name.startsWith("AUCTION: ") ? "AUCTION: " + name : name;
    }

    public Long getId() { // Optional but useful
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public BigDecimal getAverageBidAmount() {
        return averageBidAmount;
    }

    public double getMetricWeight() {
        return metricWeight;
    }

    public void setMetricWeight(double metricWeight) {
        this.metricWeight = metricWeight;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public BigDecimal getInitialPrice() {
        return initialPrice;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Blob getImageBlob() {
        return imageBlob;
    }

    public void setImageBlob(Blob imageBlob) {
        this.imageBlob = imageBlob;
    }

    public AuctionType getAuctionType() {
        return auctionType;
    }

    public void setAuctionType(AuctionType auctionType) {
        this.auctionType = auctionType;
    }
}

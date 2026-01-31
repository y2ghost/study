package study.ywork.jpa.model.simple;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Item {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    protected Long id;

    @Version
    protected long version;

    @NotNull
    @Size(
            min = 2,
            max = 255,
            message = "Name is required, maximum 255 characters."
    )
    protected String name;

    @Future
    protected Date auctionEnd;

    @Transient
    protected Set<Bid> bids = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    protected Category category;

    public Long getId() { // Optional but useful
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getAuctionEnd() {
        return auctionEnd;
    }

    public void setAuctionEnd(Date auctionEnd) {
        this.auctionEnd = auctionEnd;
    }

    protected BigDecimal buyNowPrice;

    public BigDecimal getBuyNowPrice() {
        return buyNowPrice;
    }

    public void setBuyNowPrice(BigDecimal buyNowPrice) {
        this.buyNowPrice = buyNowPrice;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void addBid(Bid bid) {
        if (null == bid) {
            throw new NullPointerException("Can't add null Bid");
        }

        if (null != bid.getItem()) {
            throw new IllegalStateException("Bid is already assigned to an Item");
        }

        getBids().add(bid);
        bid.setItem(this);
    }

    public Bid placeBid(Bid currentHighestBid, BigDecimal bidAmount) {
        if (null == currentHighestBid
                || bidAmount.compareTo(currentHighestBid.getAmount()) > 0) {
            return new Bid(bidAmount, this);
        }

        return null;
    }
}

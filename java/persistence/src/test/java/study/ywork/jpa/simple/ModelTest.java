package study.ywork.jpa.simple;

import org.testng.annotations.Test;
import study.ywork.jpa.model.simple.Bid;
import study.ywork.jpa.model.simple.Item;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ModelTest {
    @Test
    public void linkBidAndItem() {
        Item anItem = new Item();
        Bid aBid = new Bid();

        anItem.getBids().add(aBid);
        aBid.setItem(anItem);

        assertEquals(anItem.getBids().size(), 1);
        assertTrue(anItem.getBids().contains(aBid));
        assertEquals(aBid.getItem(), anItem);

        Bid secondBid = new Bid();
        anItem.addBid(secondBid);

        assertEquals(anItem.getBids().size(), 2);
        assertTrue(anItem.getBids().contains(secondBid));
        assertEquals(anItem, secondBid.getItem());
    }

    @Test
    public void validateItem() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Item item = new Item();
        item.setName("Some Item");
        item.setAuctionEnd(new Date());

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertEquals(violations.size(), 1);

        ConstraintViolation<Item> violation = violations.iterator().next();
        String failedPropertyName =
                violation.getPropertyPath().iterator().next().getName();

        assertEquals(failedPropertyName, "auctionEnd");

        if (Locale.getDefault().getLanguage().equals("en")) {
            assertEquals(violation.getMessage(), "must be in the future");
        }
    }
}

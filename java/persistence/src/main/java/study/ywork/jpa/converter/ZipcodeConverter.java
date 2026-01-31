package study.ywork.jpa.converter;

import study.ywork.jpa.model.advanced.converter.GermanZipcode;
import study.ywork.jpa.model.advanced.converter.SwissZipcode;
import study.ywork.jpa.model.advanced.converter.Zipcode;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ZipcodeConverter implements AttributeConverter<Zipcode, String> {
    @Override
    public String convertToDatabaseColumn(Zipcode attribute) {
        return attribute.getValue();
    }

    @Override
    public Zipcode convertToEntityAttribute(String s) {
        if (s.length() == 5) {
            return new GermanZipcode(s);
        } else if (s.length() == 4) {
            return new SwissZipcode(s);
        } else {
            throw new IllegalArgumentException("Unsupported zipcode in database: " + s);
        }
    }
}

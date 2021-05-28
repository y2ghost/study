package study.ywork.hibernate.validator;

import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;

public class GearBoxValueExtractor implements ValueExtractor<GearBox<@ExtractedValue ?>> {
    @Override
    public void extractValues(GearBox<@ExtractedValue ?> originalValue, ValueExtractor.ValueReceiver receiver) {
        receiver.value(null, originalValue.getGear());
    }
}

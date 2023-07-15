package study.ywork.mapstruct;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import study.ywork.mapstruct.enums.OrderType;
import study.ywork.mapstruct.enums.PlacedOrderType;
import study.ywork.mapstruct.mapper.UtilityMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilityMapperTest {
    private UtilityMapper utilityMapper = Mappers.getMapper(UtilityMapper.class);

    @Test
    public void testMapMapping() {
        Map<Long, GregorianCalendar> source = new HashMap<>();
        source.put(1L, new GregorianCalendar(2015, 3, 5));
        Map<String, String> map = utilityMapper.getMap(source);
        assertEquals("05.04.2015", map.get("1"));
    }

    @Test
    public void testGetStream() {
        Stream<Integer> numbers = Arrays.asList(1, 2, 3, 4).stream();
        Stream<String> strings = utilityMapper.getStream(numbers);
        assertEquals(4, strings.count());
    }

    @Test
    public void testGetEnum() {
        PlacedOrderType placedOrderType = utilityMapper.getEnum(OrderType.EXTRA);
        PlacedOrderType placedOrderType1 = utilityMapper.getEnum(OrderType.NORMAL);
        PlacedOrderType placedOrderType2 = utilityMapper.getEnum(OrderType.STANDARD);
        assertEquals(PlacedOrderType.SPECIAL.name(), placedOrderType.name());
        assertEquals(PlacedOrderType.NORMAL.name(), placedOrderType1.name());
        assertEquals(PlacedOrderType.STANDARD.name(), placedOrderType2.name());
    }
}

package study.ywork.mapstruct.mapper;

import java.util.GregorianCalendar;
import java.util.Map;
import java.util.stream.Stream;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;
import study.ywork.mapstruct.enums.OrderType;
import study.ywork.mapstruct.enums.PlacedOrderType;

@Mapper
public interface UtilityMapper {
    @MapMapping(valueDateFormat = "dd.MM.yyyy")
    Map<String, String> getMap(Map<Long, GregorianCalendar> source);

    Stream<String> getStream(Stream<Integer> source);

    @ValueMapping(source = "EXTRA", target = "SPECIAL")
    PlacedOrderType getEnum(OrderType order);
}

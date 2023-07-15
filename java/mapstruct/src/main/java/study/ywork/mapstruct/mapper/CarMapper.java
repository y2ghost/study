package study.ywork.mapstruct.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import study.ywork.mapstruct.dto.CarDTO;
import study.ywork.mapstruct.model.Car;

@Mapper(imports = UUID.class)
public interface CarMapper {
    default String getManufacturingDate(GregorianCalendar manufacturingDate) {
        Date d = manufacturingDate.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(d);
    }

    @Named("toEntity")
    @Mapping(source = "name", target = "name", defaultValue = "test")
    @Mapping(target = "brand", constant = "BMW")
    @Mapping(source = "price", target = "price", numberFormat = "$#.00")
    @Mapping(source = "manufacturingDate", target = "manufacturingDate", dateFormat = "dd.MM.yyyy")
    Car toEntity(CarDTO dto);

    @Mapping(source = "name", target = "name", defaultExpression = "java(UUID.randomUUID().toString())")
    @Mapping(target = "brand", constant = "BMW")
    @Mapping(source = "price", target = "price", numberFormat = "$#.00")
    @Mapping(target = "manufacturingDate", expression = "java(getManufacturingDate(dto.getManufacturingDate()))")
    Car toEntityWithExpression(CarDTO dto);

    List<String> getListOfStrings(List<Integer> listOfIntegers);

    @IterableMapping(qualifiedByName = "toEntity")
    List<Car> getCars(List<CarDTO> carEntities);
}

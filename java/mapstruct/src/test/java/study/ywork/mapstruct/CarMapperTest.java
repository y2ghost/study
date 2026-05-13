package study.ywork.mapstruct;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import study.ywork.mapstruct.dto.CarDTO;
import study.ywork.mapstruct.mapper.CarMapper;
import study.ywork.mapstruct.model.Car;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CarMapperTest {
    private CarMapper mapper = Mappers.getMapper(CarMapper.class);

    @Test
    public void testToEntity() {
        CarDTO dto = new CarDTO();
        dto.setPrice(345000);
        dto.setId(1);
        dto.setManufacturingDate(new GregorianCalendar(2015, 3, 5));
        Car entity = mapper.toEntity(dto);
        assertEquals("$345000.00", entity.getPrice());
        assertEquals(dto.getId(), entity.getId());
        assertEquals("05.04.2015", entity.getManufacturingDate());
        assertEquals("BMW", entity.getBrand());
        assertEquals("test", entity.getName());

        entity = mapper.toEntityWithExpression(dto);
        assertEquals("$345000.00", entity.getPrice());
        assertEquals(dto.getId(), entity.getId());
        assertEquals("05.04.2015", entity.getManufacturingDate());
        assertEquals("BMW", entity.getBrand());
        assertNotNull(entity.getName());
    }

    @Test
    public void testGetListOfStrings() {
        List<Integer> integers = Arrays.asList(1, 2, 3);
        List<String> strings = mapper.getListOfStrings(integers);
        assertEquals(3, strings.size());
    }

    @Test
    public void testGetCars() {
        CarDTO dto1 = new CarDTO();
        dto1.setPrice(445000);
        dto1.setId(2);
        dto1.setManufacturingDate(new GregorianCalendar(2015, 3, 5));

        CarDTO dto2 = new CarDTO();
        dto2.setPrice(345000);
        dto2.setId(1);
        dto2.setManufacturingDate(new GregorianCalendar(2015, 3, 5));

        List<CarDTO> carDtos = Arrays.asList(dto1, dto2);
        List<Car> cars = mapper.getCars(carDtos);
        assertEquals(2, cars.size());
    }

}
